package com.group12.beabee.views.goals;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.group12.beabee.R;
import com.group12.beabee.models.User;
import com.group12.beabee.views.BaseInnerFragment;
import com.group12.beabee.views.MainStructure.PageMode;

import java.util.ArrayList;

import butterknife.BindView;

public class MemberListFragment extends BaseInnerFragment implements IOnMemberListClickedListener{
    @BindView(R.id.rv_members)
    RecyclerView rvMembers;
    private MembersAdapter membersAdapter;
    private ArrayList<User> members;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        members = ((ArrayList<User>) getArguments().getSerializable("members"));
        membersAdapter = new MembersAdapter();
        rvMembers.setAdapter(membersAdapter);
        membersAdapter.setData(members);
        membersAdapter.setItemClickListener(this);
    }

    public static MemberListFragment newInstance(ArrayList<User> members) {

        Bundle args = new Bundle();
        args.putSerializable("members",members);
        MemberListFragment fragment = new MemberListFragment();
        fragment.setArguments(args);
        return fragment;
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
        return;
    }
}
