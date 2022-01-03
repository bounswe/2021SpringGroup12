package com.group12.beabee.views.userprofile;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

import com.group12.beabee.InputValidator_ihsan;
import com.group12.beabee.R;
import com.group12.beabee.Utils;
import com.group12.beabee.models.User;
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

public class UserFollowingList extends BaseInnerFragment implements IOnMemberListClickedListener {

    @BindView(R.id.rv_members)
    RecyclerView rvMembers;
    private MembersAdapter membersAdapter;
    private List<UserSearchData> members;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        members = ((ArrayList<UserSearchData>) getArguments().getSerializable("members"));
        membersAdapter = new MembersAdapter();
        rvMembers.setAdapter(membersAdapter);
        OnUsersReceived(members);
        membersAdapter.setItemClickListener(this);
    }

    public static UserFollowingList newInstance(ArrayList<UserSearchData> members) {

        Bundle args = new Bundle();
        args.putSerializable("members",members);
        UserFollowingList fragment = new UserFollowingList();
        fragment.setArguments(args);
        return fragment;
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
        return null;
    }

    @Override
    protected int GetLayoutId() {
        return R.layout.fragment_members;
    }

    @Override
    public void OnMemberListClicked(int id) {
        OpenNewFragment(UserProfileFragment.newInstance(id));
    }


}
