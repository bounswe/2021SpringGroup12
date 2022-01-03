package com.group12.beabee.views.userprofile;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

import com.group12.beabee.BeABeeApplication;
import com.group12.beabee.InputValidator_ihsan;
import com.group12.beabee.R;
import com.group12.beabee.Utils;
import com.group12.beabee.models.User;
import com.group12.beabee.models.responses.GoalDetail;
import com.group12.beabee.models.responses.UserDTO;
import com.group12.beabee.models.responses.UserSearchData;
import com.group12.beabee.views.BaseInnerFragment;
import com.group12.beabee.views.MainStructure.PageMode;
import com.group12.beabee.views.goals.IOnMemberListClickedListener;
import com.group12.beabee.views.goals.MemberListFragment;
import com.group12.beabee.views.goals.MembersAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserSearchFragment extends BaseInnerFragment implements IOnMemberListClickedListener {

    @BindView(R.id.rv_members)
    RecyclerView rvMembers;
    @BindView(R.id.search_view)
    SearchView searchView;
    private MembersAdapter membersAdapter;



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        SetupSearchView();
        membersAdapter = new MembersAdapter();
        rvMembers.setAdapter(membersAdapter);
        membersAdapter.setItemClickListener(this);
    }


    public void OnUsersReceived(List<UserSearchData> members) {

        ArrayList<User> temp = new ArrayList<User>();
        for (UserSearchData user :
                members) {
            User tempUser = new User();
            tempUser.user_id = user.id;
            tempUser.username = user.userCredentials.username;
            tempUser.email = user.userCredentials.email;
            tempUser.password = user.userCredentials.password;

            temp.add(tempUser);
        }
       membersAdapter.setData(temp);

    }


    @Override
    protected PageMode GetPageMode() {
        return PageMode.OnlyBack;
    }

    @Override
    protected String GetPageTitle() {
        return "User Search";
    }

    @Override
    protected int GetLayoutId() {
        return R.layout.fragment_user_search;
    }

    @Override
    public void OnMemberListClicked(int id) {
        OpenNewFragment(UserProfileFragment.newInstance(id));
    }

    //searchview

    private void SetupSearchView(){

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if (!InputValidator_ihsan.IsSearchQueryNonNull(query)  || !InputValidator_ihsan.IsQueryAtLeastOneCharacterLength(query) ) {
                    Utils.ShowErrorToast(getContext(), "Search query should be at least 1 character long!");
                    return false;
                }

                Utils.showLoading(getParentFragmentManager());
                service.getUserSearch(query).enqueue(new Callback<List<UserSearchData>>() {
                    @Override
                    public void onResponse(Call<List<UserSearchData>> call, Response<List<UserSearchData>> response) {
                        Utils.dismissLoading();
                        if (response.isSuccessful() && response.body() != null){
                            OnUsersReceived(response.body());
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
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }
}
