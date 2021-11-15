package com.group12.beabee.views.goals;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.group12.beabee.R;
import com.group12.beabee.views.BaseInnerFragment;
import com.group12.beabee.views.MainPage.PageMode;
import com.group12.beabee.views.entities.IOnQuestionClickedListener;
import com.group12.beabee.views.entities.IOnReflectionClickedListener;
import com.group12.beabee.views.entities.IOnRoutineClickedListener;
import com.group12.beabee.views.entities.IOnTaskClickedListener;
import com.group12.beabee.views.entities.QuestionCardViewAdapter;
import com.group12.beabee.views.entities.RoutineCardViewAdapter;
import com.group12.beabee.views.entities.TagCardViewAdapter;
import com.group12.beabee.views.entities.TaskCardViewAdapter;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link subGoalFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class subGoalFragment extends BaseInnerFragment implements IOnTaskClickedListener,
        IOnRoutineClickedListener, IOnQuestionClickedListener, IOnReflectionClickedListener,IOnSubgoalClickedListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    @BindView(R.id.rv_subgoals)
    RecyclerView rvSubGoal;
    @BindView(R.id.rv_tasks)
    RecyclerView rvTask;
    @BindView(R.id.rv_questions)
    RecyclerView rvQuestion;
    //@BindView(R.id.rv_reflections)
    //RecyclerView rvReflection;
    @BindView(R.id.rv_routines)
    RecyclerView rvRoutine;
    @BindView(R.id.rv_tags)
    RecyclerView rvTag;



    public subGoalFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Task.
     */
    // TODO: Rename and change types and number of parameters
    public static subGoalFragment newInstance() {
        subGoalFragment fragment = new subGoalFragment();
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
        return R.layout.fragment_subgoal;
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvTask.setAdapter(new TaskCardViewAdapter());
        rvRoutine.setAdapter(new RoutineCardViewAdapter());
        rvQuestion.setAdapter(new QuestionCardViewAdapter());
        //rvReflection.setAdapter(new ReflectionCardViewAdapter());
        rvSubGoal.setAdapter(new SubgoalCardViewAdapter());
        rvTag.setAdapter(new TagCardViewAdapter());
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
}