package com.group12.beabee.views.MainStructure;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.group12.beabee.R;
import com.group12.beabee.Utils;
import com.group12.beabee.models.responses.BasicResponse;
import com.group12.beabee.models.responses.Entity;
import com.group12.beabee.network.BeABeeService;
import com.group12.beabee.views.entities.QuestionCreateFragment;
import com.group12.beabee.views.entities.ReflectionCreateFragment;
import com.group12.beabee.views.entities.RoutineCreateFragment;
import com.group12.beabee.views.entities.TaskCreateFragment;
import com.group12.beabee.views.goals.SubgoalCreateFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EntitySelectorDialog extends DialogFragment {

    @BindView(R.id.spn)
    Spinner spinner;

    private List<Entity> data;
    private int selectedPos = -1;
    private int parentId;
    private String dataType;

    public static EntitySelectorDialog newInstance(int parentId, ArrayList<Entity> data, String dataType) {
        EntitySelectorDialog f = new EntitySelectorDialog();

        Bundle args = new Bundle();
        args.putSerializable("data", data);
        args.putInt("parentId", parentId);
        args.putString("dataType", dataType);
        f.setArguments(args);

        return f;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        data = (List<Entity>) getArguments().getSerializable("data");
        parentId = getArguments().getInt("parentId");
        dataType = getArguments().getString("dataType");
        if( data==null){
            data = new ArrayList<>();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_select_entity, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        List<String> items = new ArrayList<>();
        for (Entity entity : data) {
            items.add(entity.title);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, items);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedPos = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedPos = -1;
            }
        });

    }

    @OnClick(R.id.btn_approve)
    public void onApproveClick(View v) {
        if (selectedPos<0){
            Utils.ShowErrorToast(getContext(), "No ItemSelected!");
            return;
        }
        Utils.ShowErrorToast(getContext(), "Entity is being linked!");

        BeABeeService.serviceAPI.linkEntities(parentId, data.get(selectedPos).id).enqueue(new Callback<BasicResponse>() {
            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().messageType.equals("SUCCESS")) {
                    Utils.ShowErrorToast(getContext(), "Entity is successfully linked!");
                    dismiss();
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

    @OnClick(R.id.btn_dismiss)
    public void onDismissClick(View v) {
        dismiss();
    }

    @OnClick(R.id.btn_add_entity)
    public void onAddClick(View v) {
        switch (dataType){
            case "SUBGOAL":
                dismiss();
                ((BaseEntityListBottomFragment) getParentFragment()).OpenNewFragment(SubgoalCreateFragment.newInstance(parentId));
                break;
            case "TASK":
                dismiss();
                ((BaseEntityListBottomFragment) getParentFragment()).OpenNewFragment(TaskCreateFragment.newInstance(parentId));
                break;
            case "ROUTINE":
                dismiss();
                ((BaseEntityListBottomFragment) getParentFragment()).OpenNewFragment(RoutineCreateFragment.newInstance(parentId));
                break;
            case "QUESTION":
                dismiss();
                ((BaseEntityListBottomFragment) getParentFragment()).OpenNewFragment(QuestionCreateFragment.newInstance(parentId));
                break;
            case "REFLECTION":
                dismiss();
                ((BaseEntityListBottomFragment) getParentFragment()).OpenNewFragment(ReflectionCreateFragment.newInstance(parentId));
                break;
        }
    }
}
