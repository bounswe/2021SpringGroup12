package com.group12.beabee.views.goals;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.group12.beabee.BeABeeApplication;
import com.group12.beabee.R;
import com.group12.beabee.Utils;
import com.group12.beabee.models.GroupGoalDTO;
import com.group12.beabee.views.BaseInnerFragment;
import com.group12.beabee.views.MainStructure.PageMode;

import java.util.List;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Home2Fragment extends BaseInnerFragment implements IOnGoalClickedListener{

    @BindView(R.id.no_goal_view)
    View noGoalView;
    @BindView(R.id.rv_goals)
    RecyclerView rvGoals;
    private GroupGoalsAdapter goalsAdapter;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        goalsAdapter = new GroupGoalsAdapter();
        rvGoals.setAdapter(goalsAdapter);
        goalsAdapter.setItemClickListener(this);

        noGoalView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenNewFragment(GroupGoalCreateFragment.newInstance());
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        service.getAllGGofUser(BeABeeApplication.userId).enqueue(new Callback<List<GroupGoalDTO>>() {
            @Override
            public void onResponse(Call<List<GroupGoalDTO>> call, Response<List<GroupGoalDTO>> response) {
                if (response.isSuccessful() && response.body() != null){
                    OnGoalsReceived(response.body());
                }else{
                    Utils.ShowErrorToast(getContext(), "Something went wrong!");
                }
            }

            @Override
            public void onFailure(Call<List<GroupGoalDTO>> call, Throwable t) {
                Utils.ShowErrorToast(getContext(),"Something went wrong!");
            }
        });
    }

    @Override
    protected void OnAddClicked() {
        OpenNewFragment(GroupGoalCreateFragment.newInstance());
    }

    private void OnGoalsReceived(List<GroupGoalDTO> goals){
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
        BeABeeApplication.currentGroupGoal = id;
        OpenNewFragment(GroupGoalFragment.newInstance(id));
    }

    @Override
    protected PageMode GetPageMode() {
        return PageMode.List;
    }

    @Override
    protected int GetLayoutId() {
        return R.layout.fragment_home2_groupgoals;
    }
}
