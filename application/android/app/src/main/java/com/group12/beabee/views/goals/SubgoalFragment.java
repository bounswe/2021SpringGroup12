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

import com.group12.beabee.R;
import com.group12.beabee.Utils;
import com.group12.beabee.models.ParentType;
import com.group12.beabee.models.requests.ExtendDeadline;
import com.group12.beabee.models.responses.BasicResponse;
import com.group12.beabee.models.responses.SubgoalDetail;
import com.group12.beabee.models.responses.SubgoalShort;
import com.group12.beabee.views.MainStructure.BaseEntityLinkableFragment;
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
 * Use the {@link SubgoalFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SubgoalFragment extends BaseEntityLinkableFragment  implements IOnSubgoalClickedListener, IOnTagClickedListener, DatePickerDialog.OnDateSetListener {

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

    private SubgoalDetail subgoalDetail;

    private TagCardViewAdapter tagAdapter;
    private SubgoalCardViewAdapter subgoalAdapter;

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
        service.getSubgoal(id).enqueue(new Callback<SubgoalDetail>() {
            @Override
            public void onResponse(Call<SubgoalDetail> call, Response<SubgoalDetail> response) {
                Utils.dismissLoading();
                if (response.isSuccessful() && response.body() != null) {
                    OnSubgoalReceived(response.body());
                } else {
                    Utils.ShowErrorToast(getContext(), "Something went wrong!");
                    GoBack();
                }
            }

            @Override
            public void onFailure(Call<SubgoalDetail> call, Throwable t) {
                Utils.dismissLoading();
                Utils.ShowErrorToast(getContext(), "Something went wrong!");
                GoBack();
            }
        });
        Utils.showLoading(getChildFragmentManager());
    }

    private void OnSubgoalReceived(SubgoalDetail data) {
        subgoalDetail = data;
        tvTitle.setText(data.title);
        tvDescription.setText(data.description);
        tvDateSelected.setText(data.deadline);
        SetEntityLinks(data.entities);
        SetSubgoals(data.sublinks);
    }

    @Override
    protected void OnEditClicked() {
        super.OnEditClicked();
        OpenNewFragment(SubgoalEditFragment.newInstance(subgoalDetail));
    }

    @Override
    protected PageMode GetPageMode() {
        return PageMode.Editable;
    }

    @Override
    protected ParentType GetLinkableType() {
        return ParentType.SUBGOAL;
    }

    @Override
    protected String GetPageTitle() {
        return "SUBGOAL";
    }

    @Override
    protected int GetLayout() {
        return R.layout.fragment_subgoal;
    }

    @OnClick(R.id.addSubgoalButton)
    @Optional
    public void addSubgoalButton(View view){
        OpenNewFragment(SubgoalCreateFragment.newInstance(subgoalDetail.id, SubgoalCreateFragment.FROM_SUBGOAL));
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
        service.extendSubgoal(id, extendDeadline).enqueue(new Callback<BasicResponse>() {
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
}