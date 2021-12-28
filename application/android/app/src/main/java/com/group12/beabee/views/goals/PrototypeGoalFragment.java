package com.group12.beabee.views.goals;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ExpandableListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.group12.beabee.BeABeeApplication;
import com.group12.beabee.R;
import com.group12.beabee.Utils;
import com.group12.beabee.models.ExpandableListItem;
import com.group12.beabee.models.ParentType;
import com.group12.beabee.models.requests.ExtendDeadline;
import com.group12.beabee.models.responses.BasicResponse;
import com.group12.beabee.models.responses.GoalDetail;
import com.group12.beabee.models.responses.SubgoalShort;
import com.group12.beabee.views.MainStructure.BaseEntityLinkableFragment;
import com.group12.beabee.views.MainStructure.PageMode;
import com.group12.beabee.views.customview.ExpandableView;
import com.group12.beabee.views.entities.DeadlineCalendarFragment;
import com.group12.beabee.views.entities.IOnTagClickedListener;
import com.group12.beabee.views.entities.TagCardViewAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Optional;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PrototypeGoalFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PrototypeGoalFragment extends BaseEntityLinkableFragment implements IOnSubgoalClickedListener, IOnTagClickedListener, DatePickerDialog.OnDateSetListener {

    @BindView(R.id.tv_title)
    @Nullable
    TextView tvTitle;
    @BindView(R.id.tv_description)
    @Nullable
    TextView tvDescription;
    @BindView(R.id.rv_tags)
    @Nullable
    RecyclerView rvTag;
    @BindView(R.id.ex_subgoal_list)
    @Nullable
    ExpandableView subgoalList;

    private GoalDetail goalDetail;

    private TagCardViewAdapter tagAdapter;

    public PrototypeGoalFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Task.
     */
    // TODO: Rename and change types and number of parameters
    public static PrototypeGoalFragment newInstance(int id) {
        PrototypeGoalFragment fragment = new PrototypeGoalFragment();
        Bundle args = new Bundle();
        args.putInt("id", id);
        fragment.setArguments(args);
        return fragment;
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
        return "PROTOTYPE GOAL";
    }

    @Override
    protected int GetLayout() {
        return R.layout.fragment_prototype_goal;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tagAdapter = new TagCardViewAdapter();
        tagAdapter.setItemClickListener(this);
    }

    @Override
    public void onReady() {
    }

    @Override
    public void onResume() {
        super.onResume();
        RefreshPage();
    }

    private void RefreshPage() {
        Utils.showLoading(getChildFragmentManager());
        service.getProGoal(id).enqueue(new Callback<GoalDetail>() {
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

    @Override
    public void OnSubgoalClicked(int id) {

        OpenNewFragment(SubgoalPrototypeFragment.newInstance(id));
    }

    @Override
    public void OnTagClicked(int id) {

    }

    private void SetSubgoals(List<SubgoalShort> subgoals) {
        List<ExpandableListItem> dataList = new ArrayList<>();
        for (SubgoalShort subgoal :
                subgoals) {
            dataList.add(new ExpandableListItem(subgoal.title, subgoal.description));
        }
        subgoalList.SetDataList(dataList);
    }

    private void OnGoalDTOReceived(GoalDetail data) {
        goalDetail = data;
        tvTitle.setText(data.title);
        tvDescription.setText(data.description);
        SetSubgoals(data.subgoals);
    }
    @OnClick(R.id.copyGoal)
    @Optional
    public void OnCopyGoal(){
        Utils.showLoading(getParentFragmentManager());
        service.copyGoal(BeABeeApplication.userId,id).enqueue(new Callback<BasicResponse>() {
            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                Utils.dismissLoading();
                if (response.isSuccessful() && response.body() != null) {
                    Utils.ShowErrorToast(getActivity(), "Group Goal Downloaded successfully");
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

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

    }
}