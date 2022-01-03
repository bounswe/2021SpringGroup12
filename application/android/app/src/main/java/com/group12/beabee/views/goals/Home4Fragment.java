package com.group12.beabee.views.goals;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.group12.beabee.BeABeeApplication;
import com.group12.beabee.R;
import com.group12.beabee.Utils;
import com.group12.beabee.models.responses.GoalDetail;
import com.group12.beabee.models.responses.GoalShort;
import com.group12.beabee.views.BaseInnerFragment;
import com.group12.beabee.views.MainStructure.PageMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Home4Fragment extends BaseInnerFragment implements IOnGoalClickedListener{


    @BindView(R.id.rv_goals)
    RecyclerView rvGoals;
    private GoalsPrototypeAdapter1 goalsAdapter;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        goalsAdapter = new GoalsPrototypeAdapter1();
        rvGoals.setAdapter(goalsAdapter);
        goalsAdapter.setItemClickListener(this);

    }

    @Override
    public void onResume() {
        super.onResume();
            service.getMarketPlaceData().enqueue(new Callback<List<GoalDetail>>() {
            @Override
            public void onResponse(Call<List<GoalDetail>> call, Response<List<GoalDetail>> response) {
                if (response.isSuccessful() && response.body() != null){
                    OnGoalsReceived(detailToShort(response.body()));
                }else{
                    Utils.ShowErrorToast(getContext(), "Something went wrong!");
                }
            }

            @Override
            public void onFailure(Call<List<GoalDetail>> call, Throwable t) {
                Utils.ShowErrorToast(getContext(),"Something went wrong!");
            }
        });
    }

    private List<GoalShort> detailToShort(List<GoalDetail> goals){
        List<GoalShort>newGoals = new ArrayList<GoalShort>();
        for (int i=0;i<goals.size();i++){
            GoalShort goal =new GoalShort();
            goal.id=goals.get(i).id;
            goal.description=goals.get(i).description;
            goal.title=goals.get(i).title;
            goal.username=goals.get(i).username;
            goal.download_count=goals.get(i).download_count;
            newGoals.add(goal);
        }
        return newGoals;
    }

    private void OnGoalsReceived(List<GoalShort> goals){
        rvGoals.setVisibility(View.VISIBLE);
        goalsAdapter.setData(goals);

    }

    @Override
    public void OnGoalClicked(int id) {
        BeABeeApplication.currentPrototypeGoal = id;
        OpenNewFragment(PrototypeGoalFragment.newInstance(id));
    }

    @Override
    protected PageMode GetPageMode() {
        return PageMode.OnlySearch;
    }

    @Override
    protected String GetPageTitle() {
        return "Market Place";
    }

    @Override
    protected int GetLayoutId() {
        return R.layout.fragment_home4_main_marketplace;
    }

    @Override
    protected void OnSearchClicked() {
        OpenNewFragment((new Home4_2Fragment()));
    }
}
