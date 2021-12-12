package com.group12.beabee.views.goals;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.group12.beabee.Utils;
import com.group12.beabee.models.ParentType;
import com.group12.beabee.models.responses.GoalDetail;
import com.group12.beabee.models.responses.SubgoalShort;
import com.group12.beabee.views.MainStructure.BaseEntityLinkableFragment;
import com.group12.beabee.R;
import com.group12.beabee.views.MainStructure.PageMode;
import com.group12.beabee.views.entities.IOnTagClickedListener;
import com.group12.beabee.views.entities.TagCardViewAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Optional;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GoalFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GoalFragment extends BaseEntityLinkableFragment implements IOnSubgoalClickedListener, IOnTagClickedListener {
    @BindView(R.id.tv_title)
    @Nullable
    TextView tvTitle;
    @BindView(R.id.tv_description)
    @Nullable
    TextView tvDescription;
    @BindView(R.id.rv_subgoals)
    @Nullable
    RecyclerView rvSubgoal;
    @BindView(R.id.rv_tags)
    @Nullable
    RecyclerView rvTag;

    private GoalDetail goalDetail;


    private TagCardViewAdapter tagAdapter;
    private SubgoalCardViewAdapter subgoalAdapter;


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
        OpenNewFragment(GoalEditFragment.newInstance(goalDetail));
    }

    @Override
    protected PageMode GetPageMode() {
        return PageMode.Editable;
    }

    @Override
    protected ParentType GetLinkableType() {
        return ParentType.GOAL;
    }

    @Override
    protected String GetPageTitle() {
        return "GOAL";
    }

    @Override
    protected int GetLayout() {
        return R.layout.fragment_goal;
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
        service.getGoalDetail(id).enqueue(new Callback<GoalDetail>() {
            @Override
            public void onResponse(Call<GoalDetail> call, Response<GoalDetail> response) {
                Utils.dismissLoading();
                if (response.isSuccessful() && response.body() != null) {
                    OnGoalDTOReceived(response.body());
                } else {
                    Utils.ShowErrorToast(getActivity(), "Something went wrong!");
                    GoBack();
                }
            }

            @Override
            public void onFailure(Call<GoalDetail> call, Throwable t) {
                Utils.dismissLoading();
                Utils.ShowErrorToast(getActivity(), "Something went wrong!");
                GoBack();
            }
        });
    }

    @OnClick(R.id.addSubgoalButton)
    @Optional
    public void addSubgoalButton(View view) {
        OpenNewFragment(SubgoalCreateFragment.newInstance(goalDetail.id, SubgoalCreateFragment.FROM_GOAL));
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

    private void OnGoalDTOReceived(GoalDetail data) {
        goalDetail = data;
        tvTitle.setText(data.title);
        tvDescription.setText(data.description);
        SetEntityLinks(data.entities);
        SetSubgoals(data.subgoals);
    }
}