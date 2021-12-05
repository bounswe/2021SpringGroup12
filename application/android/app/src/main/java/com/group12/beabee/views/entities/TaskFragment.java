package com.group12.beabee.views.entities;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.group12.beabee.R;
import com.group12.beabee.Utils;
import com.group12.beabee.models.responses.Entity;
import com.group12.beabee.models.responses.TaskDTO;
import com.group12.beabee.views.MainStructure.BaseEntityListBottomFragment;
import com.group12.beabee.views.MainStructure.PageMode;
import com.group12.beabee.views.goals.SubgoalEditFragment;

import java.util.List;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TaskFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TaskFragment extends BaseEntityListBottomFragment {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_description)
    TextView tvDescription;
    @BindView(R.id.cb_isDone)
    CheckBox cbIsDone;
    @BindView(R.id.tv_dateSelected)
    TextView tvDateSelected;
    private TaskDTO taskDTO;


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
        service.getTask(id).enqueue(new Callback<TaskDTO>() {
            @Override
            public void onResponse(Call<TaskDTO> call, Response<TaskDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    OnTaskReceived(response.body());
                } else {
                    Utils.ShowErrorToast(getContext(), "Something went wrong!");
                    GoBack();
                }
            }

            @Override
            public void onFailure(Call<TaskDTO> call, Throwable t) {
                Utils.ShowErrorToast(getContext(), "Something went wrong!");
                GoBack();
            }
        });
        service.getSublinksForEntity(id).enqueue(new Callback<List<Entity>>() {
            @Override
            public void onResponse(Call<List<Entity>> call, Response<List<Entity>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    OnEntitiesReceived(response.body());
                } else {
                    Utils.ShowErrorToast(getContext(), "Something went wrong!");
                    GoBack();
                }
            }

            @Override
            public void onFailure(Call<List<Entity>> call, Throwable t) {
                Utils.ShowErrorToast(getContext(), "Something went wrong!");
                GoBack();
            }
        });
    }

    private void OnTaskReceived(TaskDTO data) {
        taskDTO = data;
        tvTitle.setText(data.title);
        tvDescription.setText(data.description);
        cbIsDone.setChecked(data.isDone);
        tvDateSelected.setText(data.deadline);


    }

    @Override
    protected void OnEditClicked() {
        super.OnEditClicked();
        OpenNewFragment(TaskFragmentEdit.newInstance(taskDTO));
    }

    @Override
    protected PageMode GetPageMode() {
        return PageMode.Editable;
    }

    @Override
    protected int GetLayoutId() {
        return R.layout.fragment_task;
    }
}