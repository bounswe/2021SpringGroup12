package com.group12.beabee.views.goals;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.group12.beabee.R;
import com.group12.beabee.Utils;
import com.group12.beabee.models.GroupGoalDetail;
import com.group12.beabee.models.responses.BasicResponse;
import com.group12.beabee.views.BaseInnerFragment;
import com.group12.beabee.views.MainStructure.PageMode;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GroupGoalEditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GroupGoalEditFragment extends BaseInnerFragment {

    @BindView(R.id.et_title)
    EditText etTitle;
    @BindView(R.id.et_description)
    EditText etDescription;

    private GroupGoalDetail goal;

    public GroupGoalEditFragment() {
        // Required empty public constructor
    }
    public static GroupGoalEditFragment newInstance(GroupGoalDetail goalDetail) {
        GroupGoalEditFragment fragment = new GroupGoalEditFragment();
        Bundle args = new Bundle();
        args.putSerializable("goal", goalDetail);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments()!=null){
            goal = (GroupGoalDetail) getArguments().getSerializable("goal");
        }
        if (goal==null){
            Utils.ShowErrorToast(getContext(), "Something is wrong!!");
            GoBack();
        }
        etTitle.setText(goal.title);
        etDescription.setText(goal.description);
    }

    @Override
    protected void OnApproveClicked() {
        if (etTitle.getText().toString().length()<3) {
            Utils.ShowErrorToast(getContext(), "The title should be at least 3 chars length!");
            return;
        }
        if (etDescription.getText().toString().length()<5) {
            Utils.ShowErrorToast(getContext(), "The description should be at least 5 chars length!");
            return;
        }

        goal.title = etTitle.getText().toString();
        goal.description = etDescription.getText().toString();
        Utils.showLoading(getChildFragmentManager());
        service.updateGG(goal).enqueue(new Callback<BasicResponse>() {
            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                Utils.dismissLoading();
                if (response.isSuccessful() && response.body() != null && response.body().messageType.equals("SUCCESS")) {
                    Utils.ShowErrorToast(getContext(), "Goal is successfully updated!");
                    GoBack();
                } else if(!response.isSuccessful() || response.body() == null){
                    Utils.ShowErrorToast(getContext(), "Something wrong happened please try again later!");
                } else {
                    Utils.ShowErrorToast(getContext(), response.body().message);
                }
            }
            @Override
            public void onFailure(Call<BasicResponse> call, Throwable t) {
                Utils.dismissLoading();
                Utils.ShowErrorToast(getContext(), "Something wrong happened please try again later!");
            }
        });
    }

    @Override
    protected PageMode GetPageMode() {
        return PageMode.Edit;
    }

    @Override
    protected String GetPageTitle() {
        return "Edit GOAL";
    }

    @Override
    protected int GetLayoutId() {
        return R.layout.fragment_group_goal_edit;
    }
}