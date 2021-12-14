package com.group12.beabee.views.entities;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.TextView;

import com.group12.beabee.R;
import com.group12.beabee.Utils;
import com.group12.beabee.models.ParentType;
import com.group12.beabee.models.requests.ExtendDeadline;
import com.group12.beabee.models.responses.BasicResponse;
import com.group12.beabee.models.responses.RoutineDetail;
import com.group12.beabee.views.MainStructure.BaseEntityLinkableFragment;
import com.group12.beabee.views.MainStructure.PageMode;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Optional;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RoutineFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RoutineFragment extends BaseEntityLinkableFragment implements DatePickerDialog.OnDateSetListener{

    @BindView(R.id.tv_title)
    @Nullable
    TextView tvTitle;
    @BindView(R.id.tv_description)
    @Nullable
    TextView tvDescription;
    @BindView(R.id.cb_isDone)
    @Nullable
    CheckBox cbIsDone;
    @BindView(R.id.tv_dateSelected)
    @Nullable
    TextView tvDateSelected;
    @BindView(R.id.tv_periodSelected)
    @Nullable
    TextView tvPeriodSelected;
    private RoutineDetail routineDetail;


    public RoutineFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Routine.
     */
    // TODO: Rename and change types and number of parameters
    public static RoutineFragment newInstance(int id) {
        RoutineFragment fragment = new RoutineFragment();
        Bundle args = new Bundle();
        args.putInt("id", id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        Utils.showLoading(getParentFragmentManager());
        service.getRoutine(id).enqueue(new Callback<RoutineDetail>() {
            @Override
            public void onResponse(Call<RoutineDetail> call, Response<RoutineDetail> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Utils.dismissLoading();
                    OnRoutineReceived(response.body());
                } else {
                    Utils.ShowErrorToast(getContext(), "Something went wrong!");
                    GoBack();
                }
            }

            @Override
            public void onFailure(Call<RoutineDetail> call, Throwable t) {
                Utils.dismissLoading();
                Utils.ShowErrorToast(getContext(), "Something went wrong!");
                GoBack();
            }
        });
    }

    private void OnRoutineReceived(RoutineDetail data) {
        routineDetail = data;
        tvTitle.setText(data.title);
        tvDescription.setText(data.description);
        cbIsDone.setChecked(data.isDone);
        SetEntityLinks(data.entities);
        tvDateSelected.setText(data.deadline.get(data.deadline.size()-1));
        tvPeriodSelected.setText(String.valueOf(data.period));
    }

    @Override
    protected void OnEditClicked() {
        super.OnEditClicked();
        OpenNewFragment(RoutineFragmentEdit.newInstance(routineDetail));
    }

    @Override
    protected PageMode GetPageMode() {
        return PageMode.Editable;
    }

    @Override
    protected ParentType GetLinkableType() {
        return ParentType.ENTITY;
    }

    @Override
    protected String GetPageTitle() {
        return "Routine";
    }

    @Override
    protected int GetLayout() {
        return R.layout.fragment_routine;
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
        service.extendEntity(id, extendDeadline).enqueue(new Callback<BasicResponse>() {
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