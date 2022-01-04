package com.group12.beabee.views.feed;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.group12.beabee.BeABeeApplication;
import com.group12.beabee.R;
import com.group12.beabee.Utils;
import com.group12.beabee.models.User;
import com.group12.beabee.models.responses.ActivityStream;
import com.group12.beabee.models.responses.GoalShort;
import com.group12.beabee.views.BaseInnerFragment;
import com.group12.beabee.views.MainStructure.PageMode;
import com.group12.beabee.views.goals.Home4_2Fragment;
import com.group12.beabee.views.goals.MembersAdapter;
import com.group12.beabee.views.goals.PrototypeGoalFragment;
import com.group12.beabee.views.userprofile.UserProfileFragment;
import com.group12.beabee.views.userprofile.UserSearchFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedFragment extends BaseInnerFragment implements IOnFeedClickedListener {

    @BindView(R.id.rv_feed)
    @Nullable
    RecyclerView rvFeed;
    private FeedAdapter feedAdapter;
    private List<ActivityStream> feed;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        feedAdapter = new FeedAdapter();
        rvFeed.setAdapter(feedAdapter);
        feedAdapter.setItemClickListener(this);
    }


    public void OnFeedReceived(List<ActivityStream> feed) {
        feedAdapter.setData(feed);
    }

    @Override
    public void onResume() {
        super.onResume();
        service.getActivityStream(BeABeeApplication.userId).enqueue(new Callback<List<ActivityStream>>() {
            @Override
            public void onResponse(Call<List<ActivityStream>> call, Response<List<ActivityStream>> response) {
                if (response.isSuccessful() && response.body() != null){
                    OnFeedReceived(response.body());
                    feed = response.body();
                }else{
                    Utils.ShowErrorToast(getContext(), "Something went wrong!");
                }
            }

            @Override
            public void onFailure(Call<List<ActivityStream>> call, Throwable t) {
                Utils.ShowErrorToast(getContext(),"Something went wrong!");
            }
        });
    }


    @Override
    protected PageMode GetPageMode() {
        return PageMode.Feed;
    }

    @Override
    protected String GetPageTitle() {
        return "Feed";
    }

    @Override
    protected int GetLayoutId() {
        return R.layout.fragment_feed;
    }

    @Override
    public void OnFeedClicked(int id, int type) {
        switch (type){
            case 0:
                OpenNewFragment(UserProfileFragment.newInstance(id));
                break;
            case 1:
                OpenNewFragment(PrototypeGoalFragment.newInstance(id));
                break;
        }
    }

    @Override
    protected void OnPPClicked() {
        OpenNewFragment(UserProfileFragment.newInstance(BeABeeApplication.userId));
    }

    @Override
    protected void OnSearchClicked(){
        OpenNewFragment((new UserSearchFragment()));
    }

}
