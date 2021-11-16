package com.group12.beabee.views.goals;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.group12.beabee.BeABeeApplication;
import com.group12.beabee.R;
import com.group12.beabee.Utils;
import com.group12.beabee.models.GoalDTO;
import com.group12.beabee.models.responses.BasicResponse;
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

        noGoalView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                GoalDTO temp = new GoalDTO();
//                temp.id = 1;
//                temp.title = "title"+1;
//                temp.description = "description"+1;
//                temp.goalType = "GOAL";
//                temp.createdAt = "2021-11-15T18:01:25.047Z";
//                temp.deadLine = "2021-11-15T18:01:25.047Z";
//                service.createGoalOfUser(BeABeeApplication.userId, temp).enqueue(new Callback<BasicResponse>() {
//                    @Override
//                    public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
//                        Utils.ShowErrorToast(getContext(), response.message());
//                    }
//
//                    @Override
//                    public void onFailure(Call<BasicResponse> call, Throwable t) {
//                        Utils.ShowErrorToast(getContext(), "Something went wrong!");
//                    }
//                });
                OpenNewFragment(GoalCreateFragment.newInstance());
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        service.getGoalsOfUser(BeABeeApplication.userId).enqueue(new Callback<List<GoalDTO>>() {
            @Override
            public void onResponse(Call<List<GoalDTO>> call, Response<List<GoalDTO>> response) {
                if (response.isSuccessful() && response.body() != null){
                    OnGoalsReceived(response.body());
                }else{
                    Utils.ShowErrorToast(getContext(), "Something went wrong!");
                }
            }

            @Override
            public void onFailure(Call<List<GoalDTO>> call, Throwable t) {
                Utils.ShowErrorToast(getContext(),"Something went wrong!");
            }
        });
    }

    @Override
    protected void OnAddClicked() {
        OpenNewFragment(GoalCreateFragment.newInstance());
    }

    private void OnGoalsReceived(List<GoalDTO> goals){
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
    protected int GetLayoutId() {
        return R.layout.fragment_home;
    }
}
