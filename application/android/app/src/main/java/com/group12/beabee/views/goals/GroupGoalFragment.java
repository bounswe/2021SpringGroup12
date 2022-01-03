package com.group12.beabee.views.goals;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.group12.beabee.BeABeeApplication;
import com.group12.beabee.R;
import com.group12.beabee.Utils;
import com.group12.beabee.models.GroupGoalDetail;
import com.group12.beabee.models.ParentType;
import com.group12.beabee.models.User;
import com.group12.beabee.models.responses.BasicResponse;
import com.group12.beabee.models.responses.SubgoalShort;
import com.group12.beabee.views.MainStructure.BaseEntityLinkableFragment;
import com.group12.beabee.views.MainStructure.PageMode;
import com.group12.beabee.views.entities.IOnTagClickedListener;
import com.group12.beabee.views.entities.TagCardViewAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Optional;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GroupGoalFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GroupGoalFragment extends BaseEntityLinkableFragment implements IOnSubgoalClickedListener, IOnTagClickedListener {
    @BindView(R.id.tv_title)
    @Nullable
    TextView tvTitle;
    @BindView(R.id.tv_description)
    @Nullable
    TextView tvDescription;
    @BindView(R.id.tv_dateSelected)
    @Nullable
    TextView tvDateSelected;
    @BindView(R.id.tv_joincode)
    @Nullable
    TextView tvtoken;
    @BindView(R.id.rv_subgoals)
    @Nullable
    RecyclerView rvSubgoal;
    @BindView(R.id.rv_tags)
    @Nullable
    RecyclerView rvTag;
    private GroupGoalDetail goalDTO;
    private TagCardViewAdapter tagAdapter;
    private SubgoalCardViewAdapter subgoalAdapter;


    public GroupGoalFragment() {
        // Required empty public constructor
    }
    @OnClick(R.id.openpopup)
    @Optional
    public void membersClicked(){
        ArrayList<User> membersArray = new ArrayList<User>();
        for (Object object : goalDTO.members) {
            membersArray.add((User) object);
        }
        OpenNewFragment(MemberListFragment.newInstance(membersArray));
    }

    public static GroupGoalFragment newInstance(int id) {
        GroupGoalFragment fragment = new GroupGoalFragment();
        Bundle args = new Bundle();
        args.putInt("id", id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void OnEditClicked() {
        OpenNewFragment(GroupGoalEditFragment.newInstance(goalDTO));
    }

    @Override
    protected PageMode GetPageMode() {
        return PageMode.Editable;
    }

    @Override
    protected ParentType GetLinkableType() {
        return ParentType.GROUPGOAL;
    }

    @Override
    protected String GetPageTitle() {
        return "GROUP GOAL";
    }

    @Override
    protected int GetLayout() {
        return R.layout.fragment_group_goal;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        subgoalAdapter = new SubgoalCardViewAdapter();
        tagAdapter = new TagCardViewAdapter();
        tagAdapter.setItemClickListener(this);
        subgoalAdapter.setItemClickListener(this);
    }

    @Override
    public void onReady() {
        rvTag.setAdapter(tagAdapter);
        rvSubgoal.setAdapter(subgoalAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        Utils.showLoading(getChildFragmentManager());
        service.getGGDetail(id).enqueue(new Callback<GroupGoalDetail>() {
            @Override
            public void onResponse(Call<GroupGoalDetail> call, Response<GroupGoalDetail> response) {
                Utils.dismissLoading();
                if (response.isSuccessful() && response.body() != null) {
                    OnGroupGoalDTOReceived(response.body());
                } else {
                    Utils.ShowErrorToast(getActivity(), "Something went wrong!");
                    GoBack();
                }
            }

            @Override
            public void onFailure(Call<GroupGoalDetail> call, Throwable t) {
                Utils.dismissLoading();
                Utils.ShowErrorToast(getActivity(), "Something went wrong!");
                GoBack();
            }
        });
    }

    @OnClick(R.id.addSubgoalButton)
    @Optional
    public void addSubgoalButton(View view) {
        OpenNewFragment(SubgoalCreateFragment.newInstance(goalDTO.id, SubgoalCreateFragment.FROM_GROUPGOAL));
    }

    @Override
    public void OnSubgoalClicked(int id) {
        OpenNewFragment(SubgoalFragment.newInstance(id));
    }

    @Override
    public void OnTagClicked(int id) {

    }

    private void SetSubgoals(List<SubgoalShort> subgoals) {
        subgoalAdapter.setData(subgoals);
    }

    private void OnGroupGoalDTOReceived(GroupGoalDetail data) {
        goalDTO = data;
        tvTitle.setText(data.title);
        tvDescription.setText(data.description);
        tvDateSelected.setText(data.deadline);
        tvtoken.setText(data.token);
        SetEntityLinks(data.entities);
        SetSubgoals(data.subgoals);
    }
    @OnClick(R.id.btn_copy)
    @Optional
    public void OnCopyClicked(){
        ClipboardManager clipboardManager = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("simple text", goalDTO.token.toString());
        clipboardManager.setPrimaryClip(clip);
        Utils.ShowErrorToast(getActivity(), "Copied!");
    }

    @OnClick(R.id.btn_delete)
    @Optional
    public void OnDelete(){
        Utils.showLoading(getParentFragmentManager());
        service.deleteGG(id).enqueue(new Callback<BasicResponse>() {
            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                Utils.dismissLoading();
                if (response.isSuccessful() && response.body() != null) {
                    Utils.ShowErrorToast(getActivity(), "Group Goal Deleted successfully");
                } else {
                    Utils.ShowErrorToast(getActivity(), "Something went wrong!");
                }
                GoBack();
            }

            @Override
            public void onFailure(Call<BasicResponse> call, Throwable t) {
                Utils.dismissLoading();
                Utils.ShowErrorToast(getActivity(), "Something went wrong!");
                GoBack();
            }
        });
    }

    @OnClick(R.id.btn_leave)
    @Optional
    public void leaveGroup(View view) {
        Utils.showLoading(getParentFragmentManager());
        service.leaveGG(BeABeeApplication.userId,id).enqueue(new Callback<BasicResponse>() {
            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                Utils.dismissLoading();
                if (response.isSuccessful() && response.body() != null) {
                    Utils.ShowErrorToast(getActivity(), "You left the group!");
                } else {
                    Utils.ShowErrorToast(getActivity(), "Something went wrong!");
                }
                GoBack();
            }

            @Override
            public void onFailure(Call<BasicResponse> call, Throwable t) {
                Utils.dismissLoading();
                Utils.ShowErrorToast(getActivity(), "Something went wrong!");
                GoBack();
            }
        });
    }
}