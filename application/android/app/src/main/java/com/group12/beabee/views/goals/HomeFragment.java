package com.group12.beabee.views.goals;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.group12.beabee.BeABeeApplication;
import com.group12.beabee.R;
import com.group12.beabee.Utils;
import com.group12.beabee.models.responses.GoalShort;
import com.group12.beabee.views.BaseInnerFragment;
import com.group12.beabee.views.MainStructure.PageMode;

import java.util.List;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends BaseInnerFragment implements IOnGoalClickedListener{

    @BindView(R.id.no_goal_view)
    View noGoalView;
    @BindView(R.id.rv_goals)
    RecyclerView rvGoals;
    private GoalsAdapter goalsAdapter;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        goalsAdapter = new GoalsAdapter();
        rvGoals.setAdapter(goalsAdapter);
        goalsAdapter.setItemClickListener(this);

        noGoalView.setOnClickListener(v -> OpenNewFragment(GoalCreateFragment.newInstance()));
    }

    @Override
    public void onResume() {
        super.onResume();
        service.getGoalsOfUser(BeABeeApplication.userId).enqueue(new Callback<List<GoalShort>>() {
            @Override
            public void onResponse(Call<List<GoalShort>> call, Response<List<GoalShort>> response) {
                if (response.isSuccessful() && response.body() != null){
                    OnGoalsReceived(response.body());
                }else{
                    Utils.ShowErrorToast(getContext(), "Something went wrong!");
                }
            }

            @Override
            public void onFailure(Call<List<GoalShort>> call, Throwable t) {
                Utils.ShowErrorToast(getContext(),"Something went wrong!");
            }
        });
    }

    @Override
    protected void OnAddClicked() {
        OpenNewFragment(GoalCreateFragment.newInstance());
    }

    private void OnGoalsReceived(List<GoalShort> goals){
        if (goals==null || goals.size()==0)
        {
            rvGoals.setVisibility(View.GONE);
            noGoalView.setVisibility(View.VISIBLE);
        }else{
            rvGoals.setVisibility(View.VISIBLE);
            noGoalView.setVisibility(View.GONE);
            goalsAdapter.setData(goals);
        }
    }

    @Override
    public void OnGoalClicked(int id) {
        //sendreqquest for goaldata Open
        BeABeeApplication.currentMainGoal = id;
        OpenNewFragment(GoalFragment.newInstance(id));
    }

    @Override
    protected PageMode GetPageMode() {
        return PageMode.List;
    }

    @Override
    protected String GetPageTitle() {
        return "My Goals";
    }

    @Override
    protected int GetLayoutId() {
        return R.layout.fragment_home;
    }
}
