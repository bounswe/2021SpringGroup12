package com.group12.beabee.views.goals;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.group12.beabee.BeABeeApplication;
import com.group12.beabee.R;
import com.group12.beabee.Utils;
import com.group12.beabee.models.GroupGoalDetail;
import com.group12.beabee.models.responses.GroupGoalShort;
import com.group12.beabee.views.BaseInnerFragment;
import com.group12.beabee.views.MainStructure.PageMode;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Optional;
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
        Utils.showLoading(getParentFragmentManager());
        service.getAllGGofUser(BeABeeApplication.userId).enqueue(new Callback<List<GroupGoalShort>>() {
            @Override
            public void onResponse(Call<List<GroupGoalShort>> call, Response<List<GroupGoalShort>> response) {
                Utils.dismissLoading();
                if (response.isSuccessful() && response.body() != null){
                    OnGoalsReceived(response.body());
                }else{
                    Utils.ShowErrorToast(getContext(), "Something went wrong!");
                }
            }

            @Override
            public void onFailure(Call<List<GroupGoalShort>> call, Throwable t) {
                Utils.dismissLoading();
                Utils.ShowErrorToast(getContext(),"Something went wrong!");
            }
        });
    }

    @Override
    protected void OnAddClicked() {
        OpenNewFragment(GroupGoalCreateFragment.newInstance());
    }

    private void OnGoalsReceived(List<GroupGoalShort> goals){
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
        BeABeeApplication.currentGroupGoal = id;
        OpenNewFragment(GroupGoalFragment.newInstance(id));
    }

    @Override
    protected PageMode GetPageMode() {
        return PageMode.List;
    }

    @Override
    protected String GetPageTitle() {
        return "My Group Goals";
    }

    @Override
    protected int GetLayoutId() {
        return R.layout.fragment_home2_groupgoals;
    }


    @OnClick(R.id.btn_tokenpaste)
    @Optional
    public void leaveGroup(View view) {
        OpenNewFragment(JoinTokenFragment.newInstance());
    }
}
