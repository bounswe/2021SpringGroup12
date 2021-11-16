package com.group12.beabee.views.goals;

import android.os.Bundle;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.group12.beabee.Utils;
import com.group12.beabee.models.GoalDTO;
import com.group12.beabee.models.responses.Entity;
import com.group12.beabee.views.MainStructure.BaseEntityListBottomFragment;
import com.group12.beabee.R;
import com.group12.beabee.views.MainStructure.PageMode;

import java.util.List;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GoalFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GoalFragment extends BaseEntityListBottomFragment {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_description)
    TextView tvDescription;
    private GoalDTO goalDTO;


    public GoalFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Task.
     */
    // TODO: Rename and change types and number of parameters
    public static GoalFragment newInstance(int id) {
        GoalFragment fragment = new GoalFragment();
        Bundle args = new Bundle();
        args.putInt("id", id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void OnEditClicked() {
        OpenNewFragment(GoalEditFragment.newInstance(goalDTO));
    }

    @Override
    protected PageMode GetPageMode() {
        return PageMode.Editable;
    }

    @Override
    protected int GetLayoutId() {
        return R.layout.fragment_goal;
    }

    @Override
    public void onResume() {
        super.onResume();
        service.getGoalDetail(id).enqueue(new Callback<GoalDTO>() {
            @Override
            public void onResponse(Call<GoalDTO> call, Response<GoalDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    OnGoalDTOReceived(response.body());
                } else {
                    Utils.ShowErrorToast(getActivity(), "Something went wrong!");
                    GoBack();
                }
            }

            @Override
            public void onFailure(Call<GoalDTO> call, Throwable t) {
                Utils.ShowErrorToast(getActivity(), "Something went wrong!");
                GoBack();
            }
        });

        service.getSublinksForGoal(id).enqueue(new Callback<List<Entity>>() {
            @Override
            public void onResponse(Call<List<Entity>> call, Response<List<Entity>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    OnEntitiesReceived(response.body());
                } else {
                    Utils.ShowErrorToast(getContext(), "Something went wrong!");
                    GoBack();
                }
            }

            @Override
            public void onFailure(Call<List<Entity>> call, Throwable t) {
                Utils.ShowErrorToast(getContext(), "Something went wrong!");
                GoBack();
            }
        });
    }

    private void OnGoalDTOReceived(GoalDTO data) {
        goalDTO = data;
        tvTitle.setText(data.title);
        tvDescription.setText(data.description);
    }
}