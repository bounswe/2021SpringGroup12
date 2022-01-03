package com.group12.beabee.views.goals;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

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
import butterknife.OnClick;
import butterknife.Optional;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Home4_2Fragment extends BaseInnerFragment implements IOnGoalClickedListener{


    @BindView(R.id.rv_goals)
    RecyclerView rvGoals;
    private GoalsPrototypeAdapter goalsAdapter;
    @BindView(R.id.search_bar)
    @Nullable
    EditText searchedWord;
    List<GoalShort> goals;
    List<GoalShort> goals1;
    @BindView(R.id.no_goal_view)
    View noGoalView;
    private boolean searchOneDone;
    private boolean searchTwoDone;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        goalsAdapter = new GoalsPrototypeAdapter();
        rvGoals.setAdapter(goalsAdapter);
        goalsAdapter.setItemClickListener(this);
    }

    private List<GoalShort> detailToShort(List<GoalDetail> goals){
        List<GoalShort>newGoals = new ArrayList<GoalShort>();
        for (int i=0;i<goals.size();i++){
            GoalShort goal =new GoalShort();
            goal.id=goals.get(i).id;
            goal.description=goals.get(i).description;
            goal.title=goals.get(i).title;
            goal.username=goals.get(i).username;
            newGoals.add(goal);
        }
        return newGoals;
    }

    private void OnGoalsReceived1(List<GoalShort> goals){
        searchOneDone=true;
        updateDataStatus();
    }
    private void OnGoalsReceived2(List<GoalShort> goals1){
        searchTwoDone=true;
        updateDataStatus();
    }
    private void updateDataStatus(){
        if(!searchTwoDone||!searchOneDone){
            rvGoals.setVisibility(View.GONE);
            noGoalView.setVisibility(View.VISIBLE);
            return;
        }
        if ((goals1==null || goals1.size()==0)&&(goals==null || goals.size()==0))
        {
            rvGoals.setVisibility(View.GONE);
            noGoalView.setVisibility(View.VISIBLE);
        }else{
            rvGoals.setVisibility(View.VISIBLE);
            noGoalView.setVisibility(View.GONE);
            goalsAdapter.setData2(goals1);
            goalsAdapter.setData1(goals);
        }
    }

    @Override
    public void OnGoalClicked(int id) {
        BeABeeApplication.currentPrototypeGoal = id;
        OpenNewFragment(PrototypeGoalFragment.newInstance(id));
    }

    @Override
    protected PageMode GetPageMode() {
        return PageMode.OnlyBack;
    }

    @Override
    protected String GetPageTitle() {
        return "Search Market";
    }

    @Override
    protected int GetLayoutId() {
        return R.layout.fragment_home4_marketplace_search;
    }

    @OnClick(R.id.search_icon)
    @Optional
    public void OnSearch(){
        searchOneDone=false;
        searchTwoDone=false;
        Utils.showLoading(getParentFragmentManager());
        String word=searchedWord.getText().toString();
        service.getProGoalSearch(word).enqueue(new Callback<List<GoalDetail>>() {
            @Override
            public void onResponse(Call<List<GoalDetail>> call, Response<List<GoalDetail>> response) {
                Utils.dismissLoading();
                if (response.isSuccessful() && response.body() != null) {
                    goals=detailToShort(response.body());
                    OnGoalsReceived1(goals);
                } else {
                    Utils.ShowErrorToast(getActivity(), "Something went wrong!");
                }

            }

            @Override
            public void onFailure(Call<List<GoalDetail>> call, Throwable t) {
                Utils.dismissLoading();
                Utils.ShowErrorToast(getActivity(), "Something went wrong!");

            }
        });
        Utils.showLoading(getParentFragmentManager());
        service.getProGoalTagSearch(word).enqueue(new Callback<List<GoalDetail>>() {
            @Override
            public void onResponse(Call<List<GoalDetail>> call, Response<List<GoalDetail>> response) {
                Utils.dismissLoading();
                if (response.isSuccessful() && response.body() != null) {
                    goals1=detailToShort(response.body());
                    OnGoalsReceived2(goals1);
                } else {
                    Utils.ShowErrorToast(getActivity(), "Something went wrong!");
                }

            }

            @Override
            public void onFailure(Call<List<GoalDetail>> call, Throwable t) {
                Utils.dismissLoading();
                Utils.ShowErrorToast(getActivity(), "Something went wrong!");

            }
        });

    }
}
