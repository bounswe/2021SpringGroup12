package com.group12.beabee.views.entities;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.widget.CheckBox;
import android.widget.TextView;

import com.group12.beabee.R;
import com.group12.beabee.Utils;
import com.group12.beabee.models.responses.Entity;
import com.group12.beabee.models.responses.RoutineDTO;
import com.group12.beabee.views.MainStructure.BaseEntityListBottomFragment;
import com.group12.beabee.views.MainStructure.PageMode;

import java.util.List;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RoutineFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RoutineFragment extends BaseEntityListBottomFragment {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_description)
    TextView tvDescription;
    @BindView(R.id.cb_isDone)
    CheckBox cbIsDone;
    private RoutineDTO routineDTO;


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
        service.getRoutine(id).enqueue(new Callback<RoutineDTO>() { //ISTEK ATMA
            @Override
            public void onResponse(Call<RoutineDTO> call, Response<RoutineDTO> response) { //attığın isteğin tütü r.DTO
                if (response.isSuccessful() && response.body() != null) {
                    OnRoutineReceived(response.body());
                } else {
                    Utils.ShowErrorToast(getContext(), "Something went wrong!");
                    GoBack();
                }
            }

            @Override
            public void onFailure(Call<RoutineDTO> call, Throwable t) {
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

    private void OnRoutineReceived(RoutineDTO data) {
        routineDTO = data;
        tvTitle.setText(data.title);
        tvDescription.setText(data.description);
        cbIsDone.setChecked(data.isDone);
    }

    @Override
    protected void OnEditClicked() {
        super.OnEditClicked();
        OpenNewFragment(RoutineFragmentEdit.newInstance(routineDTO));
    }

    @Override
    protected PageMode GetPageMode() {
        return PageMode.Editable;
    }

    @Override
    protected int GetLayoutId() {
        return R.layout.fragment_routine;
    }
}