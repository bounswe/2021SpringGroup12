package com.group12.beabee.views.goals;

import static android.content.Context.CLIPBOARD_SERVICE;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.group12.beabee.BeABeeApplication;
import com.group12.beabee.R;
import com.group12.beabee.Utils;
import com.group12.beabee.models.GoalDTO;
import com.group12.beabee.models.GroupGoalDTO;
import com.group12.beabee.models.User;
import com.group12.beabee.models.responses.BasicResponse;
import com.group12.beabee.models.responses.Entity;
import com.group12.beabee.models.responses.SubgoalDTO;
import com.group12.beabee.views.MainStructure.BaseEntityListBottomFragment;
import com.group12.beabee.views.MainStructure.PageMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GroupGoalFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GroupGoalFragment extends BaseEntityListBottomFragment implements IOnCopyClickedListener{
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_description)
    TextView tvDescription;
    @BindView(R.id.tv_joincode)
    TextView tvtoken;
    private GroupGoalDTO goalDTO;

    public GroupGoalFragment() {
        // Required empty public constructor
    }
    @OnClick(R.id.openpopup)
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
    protected int GetLayoutId() {
        return R.layout.fragment_group_goal;
    }

    @Override
    public void onResume() {
        super.onResume();
        service.getGGDetail(id).enqueue(new Callback<GroupGoalDTO>() {
            @Override
            public void onResponse(Call<GroupGoalDTO> call, Response<GroupGoalDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    OnGroupGoalDTOReceived(response.body());
                    List<Entity> all_entities = new ArrayList<Entity>();
                    all_entities.addAll(response.body().entities);
                    all_entities.addAll(response.body().subgoals);
                    OnEntitiesReceived(all_entities);
                } else {
                    Utils.ShowErrorToast(getActivity(), "Something went wrong!");
                    GoBack();
                }
            }

            @Override
            public void onFailure(Call<GroupGoalDTO> call, Throwable t) {
                Utils.ShowErrorToast(getActivity(), "Something went wrong!");
                GoBack();
            }
        });
    }

    private void OnGroupGoalDTOReceived(GroupGoalDTO data) {
        goalDTO = data;
        tvTitle.setText(data.title);
        tvDescription.setText(data.description);
        tvtoken.setText(data.token);
    }

    @OnClick(R.id.btn_copy)
    public void OnCopyClicked(){
        ClipboardManager clipboardManager = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("simple text", goalDTO.token.toString());
        clipboardManager.setPrimaryClip(clip);
        Utils.ShowErrorToast(getActivity(), "Copied!");
    }

    @OnClick(R.id.btn_delete)
    public void OnDelete(){
        service.deleteGG(id).enqueue(new Callback<GroupGoalDTO>() {
            @Override
            public void onResponse(Call<GroupGoalDTO> call, Response<GroupGoalDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Utils.ShowErrorToast(getActivity(), "Group Goal Deleted successfully");
                } else {
                    Utils.ShowErrorToast(getActivity(), "Something went wrong!");
                }
                GoBack();
            }

            @Override
            public void onFailure(Call<GroupGoalDTO> call, Throwable t) {
                Utils.ShowErrorToast(getActivity(), "Something went wrong!");
                GoBack();
            }
        });
    }
    @OnClick(R.id.addSubgoalButton)
    public void OnAddingSubgoal(){
        /*service.createSubgoalInGG(id).enqueue(new Callback<BasicResponse>() {
            @Override
            public void onResponse(Call<SubgoalDTO> call, Response<SubgoalDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Utils.ShowErrorToast(getActivity(), "Group Goal Deleted successfully");
                } else {
                    Utils.ShowErrorToast(getActivity(), "Something went wrong!");
                }
                GoBack();
            }

            @Override
            public void onFailure(Call<GroupGoalDTO> call, Throwable t) {
                Utils.ShowErrorToast(getActivity(), "Something went wrong!");
                GoBack();
            }
        });*/

    }


    /* @POST("groupgoals/subgoal")
    Call<BasicResponse> createSubgoalInGG(@Body GroupGoalDTO userDTO);*/

}