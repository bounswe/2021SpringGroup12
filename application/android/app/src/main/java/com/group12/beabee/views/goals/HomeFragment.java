package com.group12.beabee.views.goals;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.group12.beabee.R;
import com.group12.beabee.models.GoalShort;
import com.group12.beabee.views.BaseInnerFragment;
import com.group12.beabee.views.MainPage.PageMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class HomeFragment extends BaseInnerFragment implements IOnGoalClickedListener{

    @BindView(R.id.no_goal_view)
    View noGoalView;
    @BindView(R.id.rv_goals)
    RecyclerView rvGoals;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        GoalsAdapter goalsAdapter = new GoalsAdapter();
        rvGoals.setAdapter(goalsAdapter);
        goalsAdapter.setItemClickListener(this);


        List<GoalShort> goalShorts = GetGoals();
        if (goalShorts==null || goalShorts.size()==0)
        {
            rvGoals.setVisibility(View.GONE);
            noGoalView.setVisibility(View.VISIBLE);
        }else{
            rvGoals.setVisibility(View.VISIBLE);
            noGoalView.setVisibility(View.GONE);
            goalsAdapter.setData(goalShorts);
        }

        noGoalView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenNewFragment(new SampleFragment());
            }
        });
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
    public void OnGoalClicked(String id) {
        //sendreqquest for goaldata Open
    }

    @Override
    protected PageMode GetPageMode() {
        return PageMode.NoTopBar;
    }

    @Override
    protected int GetLayoutId() {
        return R.layout.fragment_home;
    }
}
