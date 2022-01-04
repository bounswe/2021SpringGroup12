package com.group12.beabee.views.entities;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.group12.beabee.R;
import com.group12.beabee.Utils;
import com.group12.beabee.models.responses.BasicResponse;
import com.group12.beabee.models.responses.RoutineDetail;
import com.group12.beabee.views.BaseInnerFragment;
import com.group12.beabee.views.MainStructure.PageMode;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RoutineFragmentEdit#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RoutineFragmentEdit extends BaseInnerFragment implements DatePickerDialog.OnDateSetListener{

    @BindView(R.id.et_title)
    EditText etTitle;
    @BindView(R.id.et_description)
    EditText etDescription;
    @BindView(R.id.cb_isDone)
    CheckBox cbIsDone;
    @BindView(R.id.tv_deadline)
    TextView tvDeadline;
    @BindView(R.id.btn_pickDate)
    Button btnPickDate;
    @BindView(R.id.et_pickPeriod)
    EditText etPeriod;
    private RoutineDetail routineDetail;


    public RoutineFragmentEdit() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment RoutineEdit.
     */
    public static RoutineFragmentEdit newInstance(RoutineDetail routineDetail) {
        RoutineFragmentEdit fragment = new RoutineFragmentEdit();
        Bundle args = new Bundle();
        args.putSerializable("routineDTO", routineDetail);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments()!=null){
            routineDetail = (RoutineDetail) getArguments().getSerializable("routineDTO");
        }
        if (routineDetail ==null){
            Utils.ShowErrorToast(getContext(), "Something is wrong!!");
            GoBack();
        }
        etTitle.setText(routineDetail.title);
        etDescription.setText(routineDetail.description);
        cbIsDone.setChecked(routineDetail.isDone);
        tvDeadline.setText(routineDetail.deadline.get(routineDetail.deadline.size()-1));
        etPeriod.setText(String.valueOf(routineDetail.period));
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

        routineDetail.title = etTitle.getText().toString();
        routineDetail.description = etDescription.getText().toString();
        routineDetail.isDone = cbIsDone.isChecked();
        routineDetail.deadline.set(routineDetail.deadline.size()-1, tvDeadline.getText().toString());
        routineDetail.period = Integer.parseInt(etPeriod.getText().toString());
        Utils.showLoading(getParentFragmentManager());
        service.updateRoutine(routineDetail).enqueue(new Callback<BasicResponse>() {
            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                Utils.dismissLoading();
                if (response.isSuccessful() && response.body() != null && response.body().messageType.equals("SUCCESS")) {
                    Utils.ShowErrorToast(getContext(), "Routine is successfully updated!");
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
        return "Edit Routine";
    }

    @Override
    protected int GetLayoutId() {
        return R.layout.fragment_routine_edit;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String dateString = c.toInstant().toString();

        tvDeadline.setText(dateString);


    }

    @OnClick(R.id.btn_pickDate)
    public void onClick(View view) {

        DialogFragment datePicker = new DeadlineCalendarFragment(this);
        datePicker.show(getActivity().getSupportFragmentManager(), "date picker");
    }
}