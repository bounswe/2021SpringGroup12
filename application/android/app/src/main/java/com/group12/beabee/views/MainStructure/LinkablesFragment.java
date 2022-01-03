package com.group12.beabee.views.MainStructure;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.group12.beabee.BeABeeApplication;
import com.group12.beabee.R;
import com.group12.beabee.Utils;
import com.group12.beabee.models.LinkingType;
import com.group12.beabee.models.responses.EntityShort;
import com.group12.beabee.network.BeABeeService;
import com.group12.beabee.views.dialogs.EntitySelectorDialog;
import com.group12.beabee.views.entities.IOnQuestionClickedListener;
import com.group12.beabee.views.entities.IOnReflectionClickedListener;
import com.group12.beabee.views.entities.IOnRoutineClickedListener;
import com.group12.beabee.views.entities.IOnTaskClickedListener;
import com.group12.beabee.views.entities.QuestionCardViewAdapter;
import com.group12.beabee.views.entities.QuestionFragment;
import com.group12.beabee.views.entities.ReflectionCardViewAdapter;
import com.group12.beabee.views.entities.ReflectionFragment;
import com.group12.beabee.views.entities.RoutineCardViewAdapter;
import com.group12.beabee.views.entities.RoutineFragment;
import com.group12.beabee.views.entities.TaskCardViewAdapter;
import com.group12.beabee.views.entities.TaskFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LinkablesFragment extends Fragment implements IOnTaskClickedListener,
        IOnRoutineClickedListener, IOnQuestionClickedListener, IOnReflectionClickedListener {

    private final LinkingType parentType;
    @BindView(R.id.rv_tasks)
    RecyclerView rvTask;
    @BindView(R.id.rv_questions)
    RecyclerView rvQuestion;
    @BindView(R.id.rv_reflections)
    RecyclerView rvReflection;
    @BindView(R.id.rv_routines)
    RecyclerView rvRoutine;

    private int id;

    private TaskCardViewAdapter tasksAdapter;
    private RoutineCardViewAdapter routinesAdapter;
    private QuestionCardViewAdapter questionsAdapter;
    private ReflectionCardViewAdapter reflectionAdapter;

    private ArrayList<EntityShort> existingTasks;
    private ArrayList<EntityShort> existingRoutines;
    private ArrayList<EntityShort> existingQuestions;
    private ArrayList<EntityShort> existingReflections;

    private BaseEntityLinkableFragment parentFragment;

    public LinkablesFragment(int id, BaseEntityLinkableFragment parentFragment, LinkingType parentType){
        this.id = id;
        this.parentFragment = parentFragment;
        this.parentType = parentType;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_linkables, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (id == -1){
            Utils.ShowErrorToast(getContext(), "Something is wrong with Linkable Id!!");
        }
        tasksAdapter = new TaskCardViewAdapter();
        routinesAdapter = new RoutineCardViewAdapter();
        questionsAdapter = new QuestionCardViewAdapter();
        reflectionAdapter = new ReflectionCardViewAdapter();

        rvTask.setAdapter(tasksAdapter);
        rvRoutine.setAdapter(routinesAdapter);
        rvQuestion.setAdapter(questionsAdapter);
        rvReflection.setAdapter(reflectionAdapter);

        tasksAdapter.setItemClickListener(this);
        routinesAdapter.setItemClickListener(this);
        questionsAdapter.setItemClickListener(this);
        reflectionAdapter.setItemClickListener(this);


        Utils.showLoading(getChildFragmentManager());
        BeABeeService.serviceAPI.getEntitiesOfGoal(BeABeeApplication.currentMainGoal).enqueue(new Callback<List<EntityShort>>() {
            @Override
            public void onResponse(Call<List<EntityShort>> call, Response<List<EntityShort>> response) {
                Utils.dismissLoading();
                if (response.isSuccessful() && response.body() != null) {
                    OnExistingEntitiesReceived(response.body());
                } else {
                    Utils.ShowErrorToast(getContext(), "Couldn't Load User Entities!");
                }
            }

            @Override
            public void onFailure(Call<List<EntityShort>> call, Throwable t) {
                Utils.dismissLoading();
                Utils.ShowErrorToast(getContext(), "Couldn't Load User Entities!");
            }
        });
    }

    public void OnEntitiesReceived(List<EntityShort> entities) {
        List<EntityShort> taskShorts = new ArrayList<>();
        List<EntityShort> routineShorts = new ArrayList<>();
        List<EntityShort> questionShorts = new ArrayList<>();
        List<EntityShort> reflectionShorts = new ArrayList<>();
        for (EntityShort entity : entities) {
            switch (entity.entityType) {
                case "TASK":
                    taskShorts.add(entity);
                    break;
                case "ROUTINE":
                    routineShorts.add(entity);
                    break;
                case "QUESTION":
                    questionShorts.add(entity);
                    break;
                case "REFLECTION":
                    reflectionShorts.add(entity);
                    break;
            }
        }
        tasksAdapter.setData(taskShorts);
        routinesAdapter.setData(routineShorts);
        questionsAdapter.setData(questionShorts);
        reflectionAdapter.setData(reflectionShorts);
    }


    private void OnExistingEntitiesReceived(List<EntityShort> entities) {
        existingTasks = new ArrayList<>();
        existingRoutines = new ArrayList<>();
        existingQuestions = new ArrayList<>();
        existingReflections = new ArrayList<>();
        for (EntityShort entity : entities) {
            switch (entity.entityType) {
                case "TASK":
                    existingTasks.add(entity);
                    break;
                case "ROUTINE":
                    existingRoutines.add(entity);
                    break;
                case "QUESTION":
                    existingQuestions.add(entity);
                    break;
                case "REFLECTION":
                    existingReflections.add(entity);
                    break;
            }
        }
    }

    @Override
    public void OnQuestionClicked(int id) {
        parentFragment.OpenNewFragment(QuestionFragment.newInstance(id));
    }

    @Override
    public void OnReflectionClicked(int id) {
        parentFragment.OpenNewFragment(ReflectionFragment.newInstance(id));
    }

    @Override
    public void OnRoutineClicked(int id) {
        parentFragment.OpenNewFragment(RoutineFragment.newInstance(id));
    }

    @Override
    public void OnTaskClicked(int id) {
        parentFragment.OpenNewFragment(TaskFragment.newInstance(id));
    }

    @OnClick(R.id.addTaskButton)
    public void addTaskButton(View view){
        EntitySelectorDialog dialog = EntitySelectorDialog.newInstance(id, existingTasks, "TASK", parentType);
        dialog.show(getParentFragmentManager(), "dialog");
    }

    @OnClick(R.id.addRoutineButton)
    public void addRoutineButton(View view){
        EntitySelectorDialog dialog = EntitySelectorDialog.newInstance(id, existingRoutines, "ROUTINE", parentType);
        dialog.show(getParentFragmentManager(), "dialog");
    }

    @OnClick(R.id.addReflectionButton)
    public void addReflectionButton(View view){
        EntitySelectorDialog dialog = EntitySelectorDialog.newInstance(id, existingReflections, "REFLECTION", parentType);
        dialog.show(getParentFragmentManager(), "dialog");
    }

    @OnClick(R.id.addQuestionButton)
    public void addQuestionButton(View view){
        EntitySelectorDialog dialog = EntitySelectorDialog.newInstance(id, existingQuestions, "QUESTION", parentType);
        dialog.show(getParentFragmentManager(), "dialog");
    }
}
