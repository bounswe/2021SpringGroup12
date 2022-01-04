package com.group12.beabee.views.entities;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.group12.beabee.BeABeeApplication;
import com.group12.beabee.R;
import com.group12.beabee.Utils;
import com.group12.beabee.models.ParentType;
import com.group12.beabee.models.requests.Routine;
import com.group12.beabee.models.requests.Task;
import com.group12.beabee.models.responses.BasicResponse;
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
 * Use the {@link RoutineCreateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RoutineCreateFragment extends BaseInnerFragment implements DatePickerDialog.OnDateSetListener{

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

    private int parentId;
    private ParentType parentType;

    public RoutineCreateFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment TaskEdit.
     */
    public static RoutineCreateFragment newInstance(int parentId, ParentType parentType) {
        RoutineCreateFragment fragment = new RoutineCreateFragment();
        Bundle args = new Bundle();
        args.putInt("parentId", parentId);
        args.putSerializable("parentType", parentType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        parentId = getArguments().getInt("parentId", -1);
        parentType = ((ParentType) getArguments().getSerializable("parentType"));
    }


    @Override
    protected void OnApproveClicked() {
        if (etTitle.getText().toString().length()<3) {
            Utils.ShowErrorToast(getContext(), "The title should be at least 3 chars long!");
            return;
        }
        if (etDescription.getText().toString().length()<5) {
            Utils.ShowErrorToast(getContext(), "The description should be at least 5 chars long!");
            return;
        }
        Routine routine = new Routine();
        routine.deadline = tvDeadline.getText().toString();
        routine.period = Integer.parseInt(etPeriod.getText().toString());
        routine.parentId = parentId;
        routine.parentType = parentType;
        routine.title = etTitle.getText().toString();
        routine.description = etDescription.getText().toString();
        Utils.showLoading(getParentFragmentManager());
        service.createRoutine(routine).enqueue(new Callback<BasicResponse>() {
            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                Utils.dismissLoading();
                if (response.isSuccessful() && response.body() != null && response.body().messageType.equals("SUCCESS")) {
                    Utils.ShowErrorToast(getContext(), "Routine is successfully created!");
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
        return "Create Routine";
    }

    @Override
    protected int GetLayoutId() {
        return R.layout.fragment_routine_create;
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