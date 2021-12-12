package com.group12.beabee.views.goals;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.group12.beabee.BeABeeApplication;
import com.group12.beabee.R;
import com.group12.beabee.Utils;
import com.group12.beabee.models.requests.Subgoal;
import com.group12.beabee.models.responses.BasicResponse;
import com.group12.beabee.views.BaseInnerFragment;
import com.group12.beabee.views.MainStructure.PageMode;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SubgoalCreateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SubgoalCreateFragment extends BaseInnerFragment {

    @BindView(R.id.et_title)
    EditText etTitle;
    @BindView(R.id.et_description)
    EditText etDescription;

    public static final int FROM_GOAL = 0;
    public static final int FROM_SUBGOAL = 1;
    public static final int FROM_GROUPGOAL = 2;

    private Subgoal subgoal;
    private int parentId;
    private int fromType;

    public SubgoalCreateFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment TaskEdit.
     */
    public static SubgoalCreateFragment newInstance(int parentId, int fromType) {
        SubgoalCreateFragment fragment = new SubgoalCreateFragment();
        Bundle args = new Bundle();
        args.putInt("parentId", parentId);
        args.putInt("fromType", fromType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        parentId = getArguments().getInt("parentId", -1);
        fromType = getArguments().getInt("fromType", -1);
    }

    @Override
    protected void OnApproveClicked() {
        if (etTitle.getText().toString().length() < 3) {
            Utils.ShowErrorToast(getContext(), "The title should be at least 3 chars long!");
            return;
        }
        if (etDescription.getText().toString().length() < 5) {
            Utils.ShowErrorToast(getContext(), "The description should be at least 5 chars long!");
            return;
        }
        subgoal = new Subgoal();
        subgoal.title = etTitle.getText().toString();
        subgoal.description = etDescription.getText().toString();
        subgoal.mainGoalId = -1;
        subgoal.mainGroupGoalId = -1;
        subgoal.parentSubgoalId = -1;
        subgoal.deadline = "";

        Callback<BasicResponse> callback = new Callback<BasicResponse>() {
            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().messageType.equals("SUCCESS")) {
                    Utils.ShowErrorToast(getContext(), "Subgoal is successfully created!");
                    GoBack();
                } else if (!response.isSuccessful() || response.body() == null) {
                    Utils.ShowErrorToast(getContext(), "Something wrong happened please try again later!");
                } else {
                    Utils.ShowErrorToast(getContext(), response.body().message);
                }
            }

            @Override
            public void onFailure(Call<BasicResponse> call, Throwable t) {
                Utils.ShowErrorToast(getContext(), "Something wrong happened please try again later!");
            }
        };

        switch (fromType) {
            case FROM_GOAL:
                subgoal.mainGoalId = parentId;
                service.createSubgoalUnderGoal(subgoal).enqueue(callback);
                break;
            case FROM_SUBGOAL:
                subgoal.parentSubgoalId = parentId;
                service.createSubgoalUnderSubgoal(subgoal).enqueue(callback);
                break;
            case FROM_GROUPGOAL:
                subgoal.mainGroupGoalId = parentId;
                break;
        }

    }

    @Override
    protected PageMode GetPageMode() {
        return PageMode.Edit;
    }

    @Override
    protected String GetPageTitle() {
        return "create subgoal";
    }

    @Override
    protected int GetLayoutId() {
        return R.layout.fragment_subgoal_edit;
    }
}