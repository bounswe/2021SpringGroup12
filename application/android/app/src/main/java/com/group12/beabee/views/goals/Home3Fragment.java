package com.group12.beabee.views.goals;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.group12.beabee.BeABeeApplication;
import com.group12.beabee.R;
import com.group12.beabee.Utils;
import com.group12.beabee.models.GroupGoalDetail;
import com.group12.beabee.models.responses.Analytics;
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

public class Home3Fragment extends BaseInnerFragment {

    @BindView(R.id.tv_title_best_goal)
    @Nullable
    TextView bestgoal_name;
    @BindView(R.id.tv_des_best)
    @Nullable
    TextView  bestgoal_des;

    @BindView(R.id.tv_title_worst_goal)
    @Nullable
    TextView  worstgoal_name;
    @BindView(R.id.tv_des_worst)
    @Nullable
    TextView  worstgoal_des;

    @BindView(R.id.tv_title_long_goal)
    @Nullable
    TextView  longgoal_name;
    @BindView(R.id.tv_des_long)
    @Nullable
    TextView  longgoal_des;

    @BindView(R.id.tv_title_short_goal)
    @Nullable
    TextView  shortgoal_name;
    @BindView(R.id.tv_des_short)
    @Nullable
    TextView  shortgoal_des;

    @BindView(R.id.numofGoals)
    @Nullable
    TextView  numofGoals;

    @BindView(R.id.numOfCompleted)
    @Nullable
    TextView  numofCompleted;

    @BindView(R.id.avgtime)
    @Nullable
    TextView  avgtime;

    @BindView(R.id.avgRate)
    @Nullable
    TextView  avgRate;

    @Override
    public void onResume() {
        super.onResume();
        Utils.showLoading(getParentFragmentManager());
        service.getUserAnalytics(BeABeeApplication.userId).enqueue(new Callback<Analytics>() {
            @Override
            public void onResponse(Call<Analytics> call, Response<Analytics> response) {
                Utils.dismissLoading();
                if (response.isSuccessful() && response.body() != null){
                    OnAnalyticsReceived(response.body());
                }else{
                    Utils.ShowErrorToast(getContext(), "Something went wrong!");
                }
            }

            @Override
            public void onFailure(Call<Analytics> call, Throwable t) {
                Utils.dismissLoading();
                Utils.ShowErrorToast(getContext(),"Something went wrong!");
            }
        });
    }


    @Override
    protected PageMode GetPageMode() {
        return PageMode.NoTopBar;
    }

    private void OnAnalyticsReceived(Analytics analytics) {
        avgRate.setText(String.format("%d", analytics.averageRating));
        long milisec=analytics.averageCompletionTimeOfGoalsInMiliseconds;
        long minutes = (milisec / 1000) / 60;
        avgtime.setText(String.format("%d",(int)minutes));
        numofCompleted.setText(String.format("%d",analytics.completedGoalCount));
        numofGoals.setText(String.format("%d",analytics.activeGoalCount));
        if(analytics.bestGoal==null){
            bestgoal_name.setText("");
            bestgoal_des.setText("No such evaluated goal");
        }
        else{
            bestgoal_name.setText(analytics.bestGoal.title);
            bestgoal_des.setText(analytics.bestGoal.description);
        }
        if(analytics.worstGoal==null){
            worstgoal_name.setText("");
            worstgoal_des.setText("No such evaluated goal");
        }
        else{
            worstgoal_name.setText(analytics.worstGoal.title);
            worstgoal_des.setText(analytics.worstGoal.description);
        }
        if(analytics.longestGoal==null){
            longgoal_name.setText("");
            longgoal_des.setText("No such evaluated goal");
        }
        else{
            longgoal_name.setText(analytics.longestGoal.title);
            longgoal_des.setText(analytics.longestGoal.description);
        }
        if(analytics.shortestGoal==null){
            shortgoal_name.setText("");
            shortgoal_des.setText("No such evaluated goal");
        }
        else{
            shortgoal_name.setText(analytics.shortestGoal.title);
            shortgoal_des.setText(analytics.shortestGoal.description);
        }

    }

    @Override
    protected String GetPageTitle() {
        return "My Analytics";
    }

    @Override
    protected int GetLayoutId() {
        return R.layout.fragment_home3_analytics;
    }

}
