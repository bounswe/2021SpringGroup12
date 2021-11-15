package com.group12.beabee.views.entities;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import com.group12.beabee.R;
import com.group12.beabee.models.QuestionShort;
import com.group12.beabee.models.ReflectionShort;
import com.group12.beabee.models.RoutineShort;
import com.group12.beabee.models.TaskShort;
import com.group12.beabee.views.BaseInnerFragment;
import com.group12.beabee.views.MainStructure.PageMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TaskFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TaskFragment extends BaseInnerFragment implements IOnTaskClickedListener,
        IOnRoutineClickedListener, IOnQuestionClickedListener, IOnReflectionClickedListener {


    @BindView(R.id.rv_tasks)
    RecyclerView rvTasks;
    @BindView(R.id.rv_routines)
    RecyclerView rvRoutines;
    @BindView(R.id.rv_questions)
    RecyclerView rvQuestions;
    @BindView(R.id.rv_reflections)
    RecyclerView rvReflections;

    public TaskFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Task.
     */
    // TODO: Rename and change types and number of parameters
    public static TaskFragment newInstance() {
        TaskFragment fragment = new TaskFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }





    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TaskCardViewAdapter tasksAdapter = new TaskCardViewAdapter();
        RoutineCardViewAdapter routinesAdapter = new RoutineCardViewAdapter();
        QuestionCardViewAdapter questionsAdapter = new QuestionCardViewAdapter();
        ReflectionCardViewAdapter reflectionsAdapter = new ReflectionCardViewAdapter();


        rvTasks.setAdapter(tasksAdapter);
        rvRoutines.setAdapter(routinesAdapter);
        rvQuestions.setAdapter(questionsAdapter);
        rvReflections.setAdapter(routinesAdapter);

        tasksAdapter.setItemClickListener(this);
        routinesAdapter.setItemClickListener(this);
        questionsAdapter.setItemClickListener(this);
        reflectionsAdapter.setItemClickListener(this);

        //try with mock data
        List<TaskShort> taskShorts = GetTasks();
        List<RoutineShort> routineShorts = GetRoutines();
        List<QuestionShort> questionShorts = GetQuestions();
        List<ReflectionShort> reflectionShorts = GetReflections();


        tasksAdapter.setData(taskShorts);
        routinesAdapter.setData(routineShorts);
        questionsAdapter.setData(questionShorts);
        reflectionsAdapter.setData(reflectionShorts);

    }

    ///MOCK DATA CREATORS
    private List<TaskShort> GetTasks(){
        List<TaskShort> tempList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            TaskShort temp = new TaskShort();
            temp.title = "title"+i;
            temp.description = ""+i+i+i;
            tempList.add(temp);
        }
        return tempList;
    }
    private List<RoutineShort> GetRoutines(){
        List<RoutineShort> tempList2 = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            RoutineShort temp = new RoutineShort();
            temp.title = "title"+i;
            temp.description = ""+i+i+i;
            tempList2.add(temp);
        }
        return tempList2;
    }
    private List<QuestionShort> GetQuestions(){
        List<QuestionShort> tempList3 = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            QuestionShort temp = new QuestionShort();
            temp.title = "title"+i;
            temp.description = ""+i+i+i;
            tempList3.add(temp);
        }
        return tempList3;
    }
    private List<ReflectionShort> GetReflections(){
        List<ReflectionShort> tempList4 = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ReflectionShort temp = new ReflectionShort();
            temp.title = "title"+i;
            temp.description = ""+i+i+i;
            tempList4.add(temp);
        }
        return tempList4;
    }



    @Override
    public void OnTaskClicked(String id) {
        //sendreqquest for taskdata Open
    }

    @Override
    public void OnRoutineClicked(String id) {
        //sendreqquest for routinedata Open
    }

    @Override
    public void OnQuestionClicked(String id) {
        //sendreqquest for questiondata Open
    }

    @Override
    public void OnReflectionClicked(String id) {
        //sendreqquest for reflectiondata Open
    }


    @Override
    protected PageMode GetPageMode() {
        return PageMode.Editable;
    }

    @Override
    protected int GetLayoutId() {
        return R.layout.fragment_task;
    }
}