package com.group12.beabee.views.goals;

import android.os.Bundle;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.group12.beabee.R;
import com.group12.beabee.Utils;
import com.group12.beabee.models.responses.Entity;
import com.group12.beabee.models.responses.SubgoalDTO;
import com.group12.beabee.views.MainStructure.BaseEntityListBottomFragment;
import com.group12.beabee.views.MainStructure.PageMode;

import java.util.List;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SubgoalFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SubgoalFragment extends BaseEntityListBottomFragment {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_description)
    TextView tvDescription;
    private SubgoalDTO subgoalDTO;

    public SubgoalFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Task.
     */
    // TODO: Rename and change types and number of parameters
    public static SubgoalFragment newInstance(int id) {
        SubgoalFragment fragment = new SubgoalFragment();
        Bundle args = new Bundle();
        args.putInt("id", id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        Utils.showLoading(getChildFragmentManager());
        service.getSubgoal(id).enqueue(new Callback<SubgoalDTO>() {
            @Override
            public void onResponse(Call<SubgoalDTO> call, Response<SubgoalDTO> response) {
                Utils.dismissLoading();
                if (response.isSuccessful() && response.body() != null) {
                    OnSubgoalReceived(response.body());
                } else {
                    Utils.ShowErrorToast(getContext(), "Something went wrong!");
                    GoBack();
                }
            }

            @Override
            public void onFailure(Call<SubgoalDTO> call, Throwable t) {
                Utils.dismissLoading();
                Utils.ShowErrorToast(getContext(), "Something went wrong!");
                GoBack();
            }
        });
        Utils.showLoading(getChildFragmentManager());
        service.getSublinksForEntity(id).enqueue(new Callback<List<Entity>>() {
            @Override
            public void onResponse(Call<List<Entity>> call, Response<List<Entity>> response) {
                Utils.dismissLoading();
                if (response.isSuccessful() && response.body() != null) {
                    OnEntitiesReceived(response.body());
                } else {
                    Utils.ShowErrorToast(getContext(), "Something went wrong!");
                    GoBack();
                }
            }

            @Override
            public void onFailure(Call<List<Entity>> call, Throwable t) {
                Utils.dismissLoading();
                Utils.ShowErrorToast(getContext(), "Something went wrong!");
                GoBack();
            }
        });
    }

    private void OnSubgoalReceived(SubgoalDTO data) {
        subgoalDTO = data;
        tvTitle.setText(data.title);
        tvDescription.setText(data.description);
    }

    @Override
    protected void OnEditClicked() {
        super.OnEditClicked();
        OpenNewFragment(SubgoalEditFragment.newInstance(subgoalDTO));
    }

    @Override
    protected PageMode GetPageMode() {
        return PageMode.Editable;
    }

    @Override
    protected int GetLayoutId() {
        return R.layout.fragment_subgoal;
    }
}