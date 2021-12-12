package com.group12.beabee.views.entities;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.group12.beabee.R;
import com.group12.beabee.Utils;
import com.group12.beabee.models.ParentType;
import com.group12.beabee.models.responses.TaskDetail;
import com.group12.beabee.views.MainStructure.BaseEntityLinkableFragment;
import com.group12.beabee.views.MainStructure.PageMode;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TaskFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TaskFragment extends BaseEntityLinkableFragment {

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
    private TaskDetail taskDetail;


    public TaskFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Task.
     */
    public static TaskFragment newInstance(int id) {
        TaskFragment fragment = new TaskFragment();
        Bundle args = new Bundle();
        args.putInt("id", id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        Utils.showLoading(getParentFragmentManager());
        service.getTask(id).enqueue(new Callback<TaskDetail>() {
            @Override
            public void onResponse(Call<TaskDetail> call, Response<TaskDetail> response) {
                Utils.dismissLoading();
                if (response.isSuccessful() && response.body() != null) {
                    OnTaskReceived(response.body());
                } else {
                    Utils.ShowErrorToast(getContext(), "Something went wrong!");
                    GoBack();
                }
            }

            @Override
            public void onFailure(Call<TaskDetail> call, Throwable t) {
                Utils.dismissLoading();
                Utils.ShowErrorToast(getContext(), "Something went wrong!");
                GoBack();
            }
        });
    }

    private void OnTaskReceived(TaskDetail data) {
        taskDetail = data;
        tvTitle.setText(data.title);
        tvDescription.setText(data.description);
        cbIsDone.setChecked(data.isDone);
        tvDateSelected.setText(data.deadline);
        SetEntityLinks(data.entities);
    }

    @Override
    protected void OnEditClicked() {
        super.OnEditClicked();
        OpenNewFragment(TaskFragmentEdit.newInstance(taskDetail));
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
        return "Task";
    }

    @Override
    protected int GetLayout() {
        return R.layout.fragment_task;
    }
}