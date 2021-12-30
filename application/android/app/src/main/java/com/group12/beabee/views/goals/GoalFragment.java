package com.group12.beabee.views.goals;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.group12.beabee.BeABeeApplication;
import com.group12.beabee.OnRateSelectedListener;
import com.group12.beabee.Utils;
import com.group12.beabee.models.ParentType;
import com.group12.beabee.models.requests.ExtendDeadline;
import com.group12.beabee.models.responses.BasicResponse;
import com.group12.beabee.models.responses.GoalDetail;
import com.group12.beabee.models.responses.SubgoalShort;
import com.group12.beabee.views.MainStructure.BaseEntityLinkableFragment;
import com.group12.beabee.R;
import com.group12.beabee.views.MainStructure.PageMode;
import com.group12.beabee.views.entities.DeadlineCalendarFragment;
import com.group12.beabee.views.entities.IOnTagClickedListener;
import com.group12.beabee.views.entities.TagCardViewAdapter;

import java.util.Calendar;
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
public class GoalFragment extends BaseEntityLinkableFragment implements IOnSubgoalClickedListener, IOnTagClickedListener, DatePickerDialog.OnDateSetListener {
    @BindView(R.id.tv_title)
    @Nullable
    TextView tvTitle;
    @BindView(R.id.tv_description)
    @Nullable
    TextView tvDescription;
    @BindView(R.id.tv_dateSelected)
    @Nullable
    TextView tvDateSelected;
    @BindView(R.id.rv_subgoals)
    @Nullable
    RecyclerView rvSubgoal;
    @BindView(R.id.rv_tags)
    @Nullable
    RecyclerView rvTag;
    @BindView(R.id.btn_complete)
    @Nullable
    View btnComplete;
    @BindView(R.id.btn_publish)
    @Nullable
    View btnPublish;
    @BindView(R.id.btn_republish)
    @Nullable
    View btnRepublish;
    @BindView(R.id.btn_unpublish)
    @Nullable
    View btnUnpublish;

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
        RefreshPage();
    }

    private void RefreshPage(){
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

    /*    @DELETE("groupgoals/{user_id}/{groupgoal_id}")
    Call<BasicResponse> leaveGG(@Path("groupgoal_id") int goalId,@Path("user_id") int userId, @Body GroupGoalDTO ggDTO);*/

    @Override
    public void OnSubgoalClicked(int id) {
        OpenNewFragment(SubgoalFragment.newInstance(id));
    }

    @Override
    public void OnTagClicked(int id) {

    }

    @OnClick(R.id.btn_complete)
    @Optional
    public void OnCompleteClicked(){
        Utils.showLoading(getParentFragmentManager());
        service.completeGoal(id).enqueue(new Callback<BasicResponse>() {
            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                Utils.dismissLoading();
                if (response.isSuccessful() && response.body() != null && response.body().messageType.equals("SUCCESS")) {
                    Utils.ShowErrorToast(getContext(), "You have successfully completed!");
                    RefreshPage();
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

    private void SetSubgoals(List<SubgoalShort> subgoals) {
        subgoalAdapter.setData(subgoals);
    }

    private void OnGoalDTOReceived(GoalDetail data) {
        goalDetail = data;
        tvTitle.setText(data.title);
        tvDescription.setText(data.description);
        tvDateSelected.setText(data.deadline);
        if (data.isDone) {
            btnComplete.setVisibility(View.GONE);
        }else{
            btnComplete.setVisibility(View.VISIBLE);
        }
        SetEntityLinks(data.entities);
        SetSubgoals(data.subgoals);
        if (goalDetail.isPublished)
        {
            btnPublish.setVisibility(View.GONE);
            btnRepublish.setVisibility(View.VISIBLE);
            btnUnpublish.setVisibility(View.VISIBLE);

        }else{
            btnPublish.setVisibility(View.VISIBLE);
            btnRepublish.setVisibility(View.GONE);
            btnUnpublish.setVisibility(View.GONE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String dateString = c.toInstant().toString();

        ExtendDeadline extendDeadline = new ExtendDeadline();
        extendDeadline.newDeadline = dateString;
        Utils.showLoading(getParentFragmentManager());
        service.extendGoal(id, extendDeadline).enqueue(new Callback<BasicResponse>() {
            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                Utils.dismissLoading();
                if (response.isSuccessful() && response.body() != null && response.body().messageType.equals("SUCCESS")) {
                    Utils.ShowErrorToast(getContext(), "Deadline extended succesfully!");
                    tvDateSelected.setText(dateString);
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

    @OnClick(R.id.btn_pickDate)
    @Optional
    public void onClick(View view) {

        DialogFragment datePicker = new DeadlineCalendarFragment(this);
        datePicker.show(getActivity().getSupportFragmentManager(), "date picker");
    }
    @OnClick(R.id.btn_publish)
    @Optional
    public void publishGoal(View view) {
        Utils.showLoading(getParentFragmentManager());
        service.publishGoal(id).enqueue(new Callback<BasicResponse>() {
            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                Utils.dismissLoading();
                if (response.isSuccessful() && response.body() != null) {
                    Utils.ShowErrorToast(getActivity(), "You published your goal!");
                } else {
                    Utils.ShowErrorToast(getActivity(), "Something went wrong!");
                }
            }

            @Override
            public void onFailure(Call<BasicResponse> call, Throwable t) {
                Utils.dismissLoading();
                Utils.ShowErrorToast(getActivity(), "Something went wrong!");
            }
        });
    }
    @OnClick(R.id.btn_republish)
    @Optional
    public void republishGoal(View view) {
        Utils.showLoading(getParentFragmentManager());
        service.republishGoal(id).enqueue(new Callback<BasicResponse>() {
            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                Utils.dismissLoading();
                if (response.isSuccessful() && response.body() != null) {
                    Utils.ShowErrorToast(getActivity(), "You republished your goal!");
                } else {
                    Utils.ShowErrorToast(getActivity(), "Something went wrong!");
                }
            }

            @Override
            public void onFailure(Call<BasicResponse> call, Throwable t) {
                Utils.dismissLoading();
                Utils.ShowErrorToast(getActivity(), "Something went wrong!");
            }
        });
    }
    @OnClick(R.id.btn_unpublish)
    @Optional
    public void unpublishGoal(View view) {
        Utils.showLoading(getParentFragmentManager());
        service.unpublishGoal(id).enqueue(new Callback<BasicResponse>() {
            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                Utils.dismissLoading();
                if (response.isSuccessful() && response.body() != null) {
                    Utils.ShowErrorToast(getActivity(), "You unpublished your goal!");
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