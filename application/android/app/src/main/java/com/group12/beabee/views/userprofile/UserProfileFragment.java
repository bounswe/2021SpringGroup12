package com.group12.beabee.views.userprofile;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.group12.beabee.BeABeeApplication;
import com.group12.beabee.R;
import com.group12.beabee.Utils;
import com.group12.beabee.models.User;
import com.group12.beabee.models.responses.BasicResponse;
import com.group12.beabee.models.responses.GoalDetail;
import com.group12.beabee.models.responses.UserSearchData;
import com.group12.beabee.views.BaseInnerFragment;
import com.group12.beabee.views.MainStructure.PageMode;
import com.group12.beabee.views.goals.MemberListFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Optional;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserProfileFragment extends BaseInnerFragment {


    @BindView(R.id.tv_userName)
    @Nullable
    TextView tvUsername;
    @BindView(R.id.tv_followersNum)
    @Nullable
    TextView tvFollowersNum;
    @BindView(R.id.tv_followingNum)
    @Nullable
    TextView tvFollowingNum;
    @BindView(R.id.btn_follow)
    @Nullable
    View btnFollow;
    @BindView(R.id.btn_unfollow)
    @Nullable
    View btnUnfollow;

    private UserSearchData userData;
    private static int userId;

    public UserProfileFragment() {
        // Required empty public constructor
    }


    public static UserProfileFragment newInstance(int id) {
        UserProfileFragment fragment = new UserProfileFragment();
        Bundle args = new Bundle();
        args.putInt("id", id);
        userId = id;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    protected PageMode GetPageMode() {
        return PageMode.OnlyBack;
    }

    @Override
    protected String GetPageTitle() {
        return "User Profile";
    }

    @Override
    protected int GetLayoutId() {
        return R.layout.fragment_user_profile;
    }


    @Override
    public void onResume() {
        super.onResume();
        RefreshPage();
    }

    private void RefreshPage(){
        Utils.showLoading(getChildFragmentManager());
        service.getUser(userId).enqueue(new Callback<UserSearchData>() {
            @Override
            public void onResponse(Call<UserSearchData> call, Response<UserSearchData> response) {
                Utils.dismissLoading();
                if (response.isSuccessful() && response.body() != null) {
                    OnUserReceived(response.body());
                } else {
                    Utils.ShowErrorToast(getActivity(), "Something went wrong!");
                    GoBack();
                }
            }

            @Override
            public void onFailure(Call<UserSearchData> call, Throwable t) {
                Utils.dismissLoading();
                Utils.ShowErrorToast(getActivity(), "Something went wrong!");
                GoBack();
            }
        });

        Utils.showLoading(getChildFragmentManager());
        service.getFollowings(BeABeeApplication.userId).enqueue(new Callback<List<UserSearchData>>() {
            @Override
            public void onResponse(Call<List<UserSearchData>> call, Response<List<UserSearchData>> response) {
                Utils.dismissLoading();
                if (response.isSuccessful() && response.body() != null){
                    if(IsFollowing(response.body())){
                        btnFollow.setVisibility((View.INVISIBLE));
                        btnUnfollow.setVisibility((View.VISIBLE));
                    };
                }else{
                    Utils.ShowErrorToast(getContext(), "Not found !");
                }
            }

            @Override
            public void onFailure(Call<List<UserSearchData>> call, Throwable t) {
                Utils.dismissLoading();
                Utils.ShowErrorToast(getContext(),"Something went wrong!");
            }
        });
    }

    public boolean IsFollowing(List<UserSearchData> members) {
        boolean temp = false;
        for (UserSearchData user :
                members) {
            if(user.id == userId){
                temp = true;
            }
        }
        return temp;

    }
    private void OnUserReceived(UserSearchData data) {
        userData = data;
        System.out.println(data.followerCount);
        tvUsername.setText(data.userCredentials.username);
        tvFollowersNum.setText(String.valueOf(data.followerCount));
        tvFollowingNum.setText(String.valueOf(data.followingCount));
        if(userId == BeABeeApplication.userId){
            btnFollow.setVisibility((View.INVISIBLE));
            btnUnfollow.setVisibility((View.INVISIBLE));
        }
    }

    @OnClick(R.id.btn_follow)
    @Optional
    public void OnFollowClicked(){
        Utils.showLoading(getChildFragmentManager());
        service.followUser(BeABeeApplication.userId, userId).enqueue(new Callback<BasicResponse>() {
            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                Utils.dismissLoading();
                if (response.isSuccessful() && response.body() != null && response.body().messageType.equals("SUCCESS")) {
                    Utils.ShowErrorToast(getActivity(), response.body().message);
                    btnFollow.setVisibility((View.INVISIBLE));
                    btnUnfollow.setVisibility((View.VISIBLE));
                    tvFollowersNum.setText(String.valueOf(Integer.parseInt(tvFollowersNum.getText().toString())+1));
                } else {
                    Utils.ShowErrorToast(getActivity(), "Something went wrong!");
                    GoBack();
                }
            }

            @Override
            public void onFailure(Call<BasicResponse> call, Throwable t) {
                Utils.dismissLoading();
                Utils.ShowErrorToast(getActivity(), "Something went wrong!");
                GoBack();
            }
        });
    }

    @OnClick(R.id.btn_unfollow)
    @Optional
    public void OnUnfollowClicked(){
        Utils.showLoading(getChildFragmentManager());
        service.unfollowUser(BeABeeApplication.userId, userId).enqueue(new Callback<BasicResponse>() {
            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                Utils.dismissLoading();
                if (response.isSuccessful() && response.body() != null && response.body().messageType.equals("SUCCESS")) {
                    Utils.ShowErrorToast(getActivity(), response.body().message);
                    btnFollow.setVisibility((View.VISIBLE));
                    btnUnfollow.setVisibility((View.INVISIBLE));
                    tvFollowersNum.setText(String.valueOf(Integer.parseInt(tvFollowersNum.getText().toString())-1));
                } else {
                    Utils.ShowErrorToast(getActivity(), "Something went wrong!");
                    GoBack();
                }
            }

            @Override
            public void onFailure(Call<BasicResponse> call, Throwable t) {
                Utils.dismissLoading();
                Utils.ShowErrorToast(getActivity(), "Something went wrong!");
                GoBack();
            }
        });
    }

    @OnClick(R.id.tv_followers)
    @Optional
    public void OnFollowersClicked(){
        Utils.showLoading(getChildFragmentManager());
        service.getFollowers(userId).enqueue(new Callback<List<UserSearchData>>() {
            @Override
            public void onResponse(Call<List<UserSearchData>> call, Response<List<UserSearchData>> response) {
                Utils.dismissLoading();
                if (response.isSuccessful() && response.body() != null){
                    OpenNewFragment(UserFollowingList.newInstance((ArrayList<UserSearchData>) response.body()));
                }else{
                    Utils.ShowErrorToast(getContext(), "Not found !");
                }
            }

            @Override
            public void onFailure(Call<List<UserSearchData>> call, Throwable t) {
                Utils.dismissLoading();
                Utils.ShowErrorToast(getContext(),"Something went wrong!");
            }
        });

    }

    @OnClick(R.id.tv_following)
    @Optional
    public void OnFollowingsClicked(){
        Utils.showLoading(getChildFragmentManager());
        service.getFollowings(userId).enqueue(new Callback<List<UserSearchData>>() {
            @Override
            public void onResponse(Call<List<UserSearchData>> call, Response<List<UserSearchData>> response) {
                Utils.dismissLoading();
                if (response.isSuccessful() && response.body() != null){
                    OpenNewFragment(UserFollowingList.newInstance((ArrayList<UserSearchData>) response.body()));

                }else{
                    Utils.ShowErrorToast(getContext(), "Not found !");
                }
            }

            @Override
            public void onFailure(Call<List<UserSearchData>> call, Throwable t) {
                Utils.dismissLoading();
                Utils.ShowErrorToast(getContext(),"Something went wrong!");
            }
        });
    }
}