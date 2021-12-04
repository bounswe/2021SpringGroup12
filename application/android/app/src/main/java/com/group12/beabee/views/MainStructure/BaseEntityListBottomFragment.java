package com.group12.beabee.views.MainStructure;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.group12.beabee.BeABeeApplication;
import com.group12.beabee.R;
import com.group12.beabee.Utils;
import com.group12.beabee.models.responses.Entity;
import com.group12.beabee.views.BaseInnerFragment;
import com.group12.beabee.views.dialogs.EntitySelectorDialog;
import com.group12.beabee.views.entities.IOnQuestionClickedListener;
import com.group12.beabee.views.entities.IOnReflectionClickedListener;
import com.group12.beabee.views.entities.IOnRoutineClickedListener;
import com.group12.beabee.views.entities.IOnTagClickedListener;
import com.group12.beabee.views.entities.IOnTaskClickedListener;
import com.group12.beabee.views.entities.QuestionCardViewAdapter;
import com.group12.beabee.views.entities.QuestionFragment;
import com.group12.beabee.views.entities.ReflectionCardViewAdapter;
import com.group12.beabee.views.entities.ReflectionFragment;
import com.group12.beabee.views.entities.RoutineCardViewAdapter;
import com.group12.beabee.views.entities.RoutineFragment;
import com.group12.beabee.views.entities.TagCardViewAdapter;
import com.group12.beabee.views.entities.TaskCardViewAdapter;
import com.group12.beabee.views.entities.TaskFragment;
import com.group12.beabee.views.goals.IOnSubgoalClickedListener;
import com.group12.beabee.views.goals.SubgoalCardViewAdapter;
import com.group12.beabee.views.goals.SubgoalFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class BaseEntityListBottomFragment extends BaseInnerFragment implements IOnTaskClickedListener,
        IOnRoutineClickedListener, IOnQuestionClickedListener, IOnReflectionClickedListener, IOnSubgoalClickedListener, IOnTagClickedListener {

    @BindView(R.id.rv_subgoals)
    protected RecyclerView rvSubgoal;
    @BindView(R.id.rv_tasks)
    protected RecyclerView rvTask;
    @BindView(R.id.rv_questions)
    protected RecyclerView rvQuestion;
    @BindView(R.id.rv_reflections)
    protected RecyclerView rvReflection;
    @BindView(R.id.rv_routines)
    protected RecyclerView rvRoutine;
    @BindView(R.id.rv_tags)
    protected RecyclerView rvTag;


    protected int id;
    protected TaskCardViewAdapter tasksAdapter;
    protected RoutineCardViewAdapter routinesAdapter;
    protected QuestionCardViewAdapter questionsAdapter;
    protected ReflectionCardViewAdapter reflectionAdapter;
    protected TagCardViewAdapter tagAdapter;
    protected SubgoalCardViewAdapter subgoalAdapter;
    private ArrayList<Entity> existingTasks;
    private ArrayList<Entity> existingRoutines;
    private ArrayList<Entity> existingQuestions;
    private ArrayList<Entity> existingReflections;
    private ArrayList<Entity> existingSubgoals;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments()!=null)
            id = getArguments().getInt("id", -1);
        else
            id = -1;

        if (id == -1){
            Utils.ShowErrorToast(getContext(), "Something is wrong!!");
            GoBack();
        }

        tasksAdapter = new TaskCardViewAdapter();
        routinesAdapter = new RoutineCardViewAdapter();
        questionsAdapter = new QuestionCardViewAdapter();
        reflectionAdapter = new ReflectionCardViewAdapter();
        tagAdapter = new TagCardViewAdapter();
        subgoalAdapter = new SubgoalCardViewAdapter();

        rvTask.setAdapter(tasksAdapter);
        rvRoutine.setAdapter(routinesAdapter);
        rvQuestion.setAdapter(questionsAdapter);
        rvReflection.setAdapter(reflectionAdapter);
        rvTag.setAdapter(tagAdapter);
        rvSubgoal.setAdapter(subgoalAdapter);

        tasksAdapter.setItemClickListener(this);
        routinesAdapter.setItemClickListener(this);
        questionsAdapter.setItemClickListener(this);
        reflectionAdapter.setItemClickListener(this);
        tagAdapter.setItemClickListener(this);
        subgoalAdapter.setItemClickListener(this);

        Utils.showLoading(getChildFragmentManager());
        service.getEntitiesOfUser(BeABeeApplication.userId).enqueue(new Callback<List<Entity>>() {
            @Override
            public void onResponse(Call<List<Entity>> call, Response<List<Entity>> response) {
                Utils.dismissLoading();
                if (response.isSuccessful() && response.body() != null) {
                    OnExistingEntitiesReceived(response.body());
                } else {
                    Utils.ShowErrorToast(getContext(), "Something went wrong!");
                    GoBack();
                }
            }

            @Override
            public void onFailure(Call<List<Entity>> call, Throwable t) {
                Utils.dismissLoading();
                Utils.ShowErrorToast(getContext(), "Something went wrong!");
                GoBack();
            }
        });
    }

    protected void OnEntitiesReceived(List<Entity> entities) {
        List<Entity> taskShorts = new ArrayList<>();
        List<Entity> routineShorts = new ArrayList<>();
        List<Entity> questionShorts = new ArrayList<>();
        List<Entity> reflectionShorts = new ArrayList<>();
        List<Entity> subgoalShorts = new ArrayList<>();
        for (Entity entity : entities) {
            switch (entity.entityType) {
                case "SUBGOAL":
                    subgoalShorts.add(entity);
                    break;
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
        subgoalAdapter.setData(subgoalShorts);
    }

    protected void OnExistingEntitiesReceived(List<Entity> entities) {
        existingTasks = new ArrayList<>();
        existingRoutines = new ArrayList<>();
        existingQuestions = new ArrayList<>();
        existingReflections = new ArrayList<>();
        existingSubgoals = new ArrayList<>();
        for (Entity entity : entities) {
            switch (entity.entityType) {
                case "SUBGOAL":
                    existingSubgoals.add(entity);
                    break;
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
        OpenNewFragment(QuestionFragment.newInstance(id));
    }

    @Override
    public void OnReflectionClicked(int id) {
        OpenNewFragment(ReflectionFragment.newInstance(id));
    }

    @Override
    public void OnRoutineClicked(int id) {
        OpenNewFragment(RoutineFragment.newInstance(id));
    }

    @Override
    public void OnTaskClicked(int id) {
        OpenNewFragment(TaskFragment.newInstance(id));
    }

    @Override
    public void OnSubgoalClicked(int id) {
        OpenNewFragment(SubgoalFragment.newInstance(id));
    }

    @Override
    public void OnTagClicked(int id) {

    }

    @OnClick(R.id.addSubgoalButton)
    public void addSubgoalButton(View view){
        EntitySelectorDialog dialog = EntitySelectorDialog.newInstance(id, existingSubgoals, "SUBGOAL");
        dialog.show(getChildFragmentManager(), "dialog");
    }

    @OnClick(R.id.addTaskButton)
    public void addTaskButton(View view){
        EntitySelectorDialog dialog = EntitySelectorDialog.newInstance(id, existingTasks, "TASK");
        dialog.show(getChildFragmentManager(), "dialog");
    }

    @OnClick(R.id.addRoutineButton)
    public void addRoutineButton(View view){
        EntitySelectorDialog dialog = EntitySelectorDialog.newInstance(id, existingRoutines, "ROUTINE");
        dialog.show(getChildFragmentManager(), "dialog");
    }

    @OnClick(R.id.addReflectionButton)
    public void addReflectionButton(View view){
        EntitySelectorDialog dialog = EntitySelectorDialog.newInstance(id, existingReflections, "REFLECTION");
        dialog.show(getChildFragmentManager(), "dialog");
    }

    @OnClick(R.id.addQuestionButton)
    public void addQuestionButton(View view){
        EntitySelectorDialog dialog = EntitySelectorDialog.newInstance(id, existingQuestions, "QUESTION");
        dialog.show(getChildFragmentManager(), "dialog");
    }

    @Override
    protected abstract PageMode GetPageMode() ;

    @Override
    protected abstract int GetLayoutId();
}
