package com.group12.beabee.views.goals;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.group12.beabee.models.GoalShort;
import com.group12.beabee.models.QuestionShort;
import com.group12.beabee.models.ReflectionShort;
import com.group12.beabee.models.RoutineShort;
import com.group12.beabee.models.SubgoalShort;
import com.group12.beabee.models.TagShort;
import com.group12.beabee.models.TaskShort;
import com.group12.beabee.views.entities.IOnQuestionClickedListener;
import com.group12.beabee.views.entities.IOnReflectionClickedListener;
import com.group12.beabee.views.entities.IOnRoutineClickedListener;
import com.group12.beabee.views.entities.IOnTagClickedListener;
import com.group12.beabee.views.entities.IOnTaskClickedListener;
import com.group12.beabee.views.entities.QuestionCardViewAdapter;
import com.group12.beabee.R;
import com.group12.beabee.views.entities.ReflectionCardViewAdapter;
import com.group12.beabee.views.entities.RoutineCardViewAdapter;
import com.group12.beabee.views.entities.TagCardViewAdapter;
import com.group12.beabee.views.entities.TaskCardViewAdapter;
import com.group12.beabee.views.BaseInnerFragment;
import com.group12.beabee.views.MainStructure.PageMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GoalFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GoalFragment extends BaseInnerFragment implements IOnTaskClickedListener,
        IOnRoutineClickedListener, IOnQuestionClickedListener, IOnReflectionClickedListener,IOnSubgoalClickedListener, IOnTagClickedListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    // TODO: Rename and change types of parameters
    @BindView(R.id.rv_subgoals)
    RecyclerView rvSubgoal;
    @BindView(R.id.rv_tasks)
    RecyclerView rvTask;
    @BindView(R.id.rv_questions)
    RecyclerView rvQuestion;
    @BindView(R.id.rv_reflections)
    RecyclerView rvReflection;
    @BindView(R.id.rv_routines)
    RecyclerView rvRoutine;
    @BindView(R.id.rv_tags)
    RecyclerView rvTag;



    public GoalFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Task.
     */
    // TODO: Rename and change types and number of parameters
    public static GoalFragment newInstance() {
        GoalFragment fragment = new GoalFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    protected PageMode GetPageMode() {
        return PageMode.Editable;
    }

    @Override
    protected int GetLayoutId() {
        return R.layout.fragment_goal;
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TaskCardViewAdapter tasksAdapter = new TaskCardViewAdapter();
        RoutineCardViewAdapter routinesAdapter = new RoutineCardViewAdapter();
        QuestionCardViewAdapter questionsAdapter = new QuestionCardViewAdapter();
        ReflectionCardViewAdapter reflectionAdapter= new ReflectionCardViewAdapter();
        TagCardViewAdapter tagAdapter=new TagCardViewAdapter();
        SubgoalCardViewAdapter subgoalAdapter=new SubgoalCardViewAdapter();

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

        //try with mock data
        List<TaskShort> taskShorts = GetTasks();
        List<RoutineShort> routineShorts = GetRoutines();
        List<QuestionShort> questionShorts = GetQuestions();
        List<TagShort> tagShorts = GetTags();
        List<ReflectionShort> reflectionShorts = GetReflections();
        List<SubgoalShort> subgoalShorts = GetSubgoals();

        tasksAdapter.setData(taskShorts);
        routinesAdapter.setData(routineShorts);
        questionsAdapter.setData(questionShorts);
        reflectionAdapter.setData(reflectionShorts);
        subgoalAdapter.setData(subgoalShorts);
        tagAdapter.setData(tagShorts);

    }
    private List<TaskShort> GetTasks(){
        return new ArrayList<>();
    }
    private List<TagShort> GetTags(){
        return new ArrayList<>();
    }
    private List<ReflectionShort> GetReflections(){
        return new ArrayList<>();
    }
    private List<SubgoalShort> GetSubgoals(){
        return new ArrayList<>();
    }
    private List<RoutineShort> GetRoutines(){return new ArrayList<>(); }
    private List<QuestionShort> GetQuestions(){
        return new ArrayList<>();
    }
    private List<GoalShort> GetGoals(){
        List<GoalShort> tempList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            GoalShort temp = new GoalShort();
            temp.title = "title"+i;
            temp.description = ""+i+i+i;
            tempList.add(temp);
        }
        return tempList;
    }

    @Override
    public void OnQuestionClicked(String id) {

    }

    @Override
    public void OnReflectionClicked(String id) {

    }

    @Override
    public void OnRoutineClicked(String id) {

    }

    @Override
    public void OnTaskClicked(String id) {

    }

    @Override
    public void OnSubgoalClicked(String id) {

    }

    @Override
    public void OnTagClicked(String id) {

    }
}