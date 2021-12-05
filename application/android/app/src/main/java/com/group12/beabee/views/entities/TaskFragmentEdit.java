package com.group12.beabee.views.entities;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.DialogFragment;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.group12.beabee.BeABeeApplication;
import com.group12.beabee.R;
import com.group12.beabee.Utils;
import com.group12.beabee.models.responses.BasicResponse;
import com.group12.beabee.models.responses.SubgoalDTO;
import com.group12.beabee.models.responses.TaskDTO;
import com.group12.beabee.views.BaseInnerFragment;
import com.group12.beabee.views.MainStructure.PageMode;

import java.text.DateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TaskFragmentEdit#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TaskFragmentEdit extends BaseInnerFragment implements DatePickerDialog.OnDateSetListener {

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

    private TaskDTO taskDTO;

    public TaskFragmentEdit() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment TaskEdit.
     */
    public static TaskFragmentEdit newInstance(TaskDTO taskDTO) {
        TaskFragmentEdit fragment = new TaskFragmentEdit();
        Bundle args = new Bundle();
        args.putSerializable("task", taskDTO);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments()!=null){
            taskDTO = (TaskDTO) getArguments().getSerializable("task");
        }
        if (taskDTO==null){
            Utils.ShowErrorToast(getContext(), "Something is wrong!!");
            GoBack();
        }
        etTitle.setText(taskDTO.title);
        etDescription.setText(taskDTO.description);
        cbIsDone.setChecked(taskDTO.isDone);
        tvDeadline.setText(taskDTO.deadline);
        //Button button = (Button) findViewById(R.id.btn_pickDate);
//        btnPickDate.setOnClickListener(new View.OnClickListener() {
//            @OnClick
//            public void onClick(View v) {
//
//                //Toast.makeText(this, "Click event works.", Toast.LENGTH_SHORT).show();
//                DialogFragment datePicker = new DeadlineCalendarFragment(this);
//                datePicker.show(getChildFragmentManager(), "date picker");
//            }
//        });
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

        taskDTO.title = etTitle.getText().toString();
        taskDTO.description = etDescription.getText().toString();
        taskDTO.isDone = cbIsDone.isChecked();
        taskDTO.deadline = tvDeadline.getText().toString();
        service.updateTask(taskDTO.id, taskDTO).enqueue(new Callback<BasicResponse>() {
            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().messageType.equals("SUCCESS")) {
                    Utils.ShowErrorToast(getContext(), "Task is successfully updated!");
                    GoBack();
                } else if(!response.isSuccessful() || response.body() == null){
                    Utils.ShowErrorToast(getContext(), "Something wrong happened please try again later!");
                } else {
                    Utils.ShowErrorToast(getContext(), response.body().message);
                }
            }
            @Override
            public void onFailure(Call<BasicResponse> call, Throwable t) {
                Utils.ShowErrorToast(getContext(), "Something wrong happened please try again later!");
            }
        });
    }


    @Override
    protected PageMode GetPageMode() {
        return PageMode.Edit;
    }

    @Override
    protected int GetLayoutId() {
        return R.layout.fragment_task_edit;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String dateString = c.toInstant().toString();
//
//
        tvDeadline.setText(dateString);
        //String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());


       // tvDeadline.setText(currentDateString);

    }

    @OnClick(R.id.btn_pickDate)
    public void onClick(View view) {
//        Calendar c = Calendar.getInstance();
//        int year = c.get(Calendar.YEAR);
//        int month = c.get(Calendar.MONTH);
//        int day = c.get(Calendar.DAY_OF_MONTH);

        //DialogFragment datePicker = new DatePickerDialog(getContext(), (DatePickerDialog.OnDateSetListener) getContext(), year, month, day);
        DialogFragment datePicker = new DeadlineCalendarFragment(this);
        datePicker.show(getActivity().getSupportFragmentManager(), "date picker");
    }

}