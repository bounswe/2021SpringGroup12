package com.group12.beabee.views.goals;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.group12.beabee.BeABeeApplication;
import com.group12.beabee.R;
import com.group12.beabee.Utils;
import com.group12.beabee.models.ExpandableListItem;
import com.group12.beabee.models.LinkingType;
import com.group12.beabee.models.responses.BasicResponse;
import com.group12.beabee.models.responses.EntityShort;
import com.group12.beabee.models.responses.GoalDetail;
import com.group12.beabee.models.responses.SubgoalShort;
import com.group12.beabee.views.BaseInnerFragment;
import com.group12.beabee.views.MainStructure.BaseEntityLinkableFragment;
import com.group12.beabee.views.MainStructure.PageMode;
import com.group12.beabee.views.customview.ExpandableView;
import com.group12.beabee.views.entities.IOnTagClickedListener;
import com.group12.beabee.views.entities.TagCardViewAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Optional;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PrototypeGoalFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PrototypeGoalFragment extends BaseInnerFragment implements IOnTagClickedListener{

    @BindView(R.id.tv_title)
    @Nullable
    TextView tvTitle;
    @BindView(R.id.tv_description)
    @Nullable
    TextView tvDescription;
    @BindView(R.id.rv_tags)
    @Nullable
    RecyclerView rvTag;
    @BindView(R.id.ex_subgoal_list)
    @Nullable
    ExpandableView subgoalList;
    @BindView(R.id.ex_task_list)
    @Nullable
    ExpandableView taskList;
    @BindView(R.id.ex_routine_list)
    @Nullable
    ExpandableView routineList;
    @BindView(R.id.ex_reflection_list)
    @Nullable
    ExpandableView reflectionList;
    @BindView(R.id.ex_question_list)
    @Nullable
    ExpandableView questionList;

    public static int idPro;

    private GoalDetail goalDetail;

    private TagCardViewAdapter tagAdapter;

    public PrototypeGoalFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Task.
     */
    // TODO: Rename and change types and number of parameters
    public static PrototypeGoalFragment newInstance(int id) {
        PrototypeGoalFragment fragment = new PrototypeGoalFragment();
        Bundle args = new Bundle();
        args.putInt("id", id);
        idPro=id;
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected PageMode GetPageMode() {
        return PageMode.Editable;
    }

    @Override
    protected String GetPageTitle() {
        return "PROTOTYPE GOAL";
    }

    @Override
    protected int GetLayoutId() {
        return R.layout.fragment_prototype_goal;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tagAdapter = new TagCardViewAdapter();
        tagAdapter.setItemClickListener(this);
    }



    @Override
    public void onResume() {
        super.onResume();
        RefreshPage();
    }

    private void RefreshPage() {
        Utils.showLoading(getChildFragmentManager());
        service.getProGoal(idPro).enqueue(new Callback<GoalDetail>() {
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

    @Override
    public void OnTagClicked(int id) {

    }

    private void SetSubgoals(List<SubgoalShort> subgoals) {
        List<ExpandableListItem> dataList = new ArrayList<>();
        for (SubgoalShort subgoal :
                subgoals) {
            dataList.add(new ExpandableListItem(subgoal.title, subgoal.description));
        }
        subgoalList.SetDataList(dataList);
    }

    private void OnGoalDTOReceived(GoalDetail data) {
        goalDetail = data;
        tvTitle.setText(data.title);
        tvDescription.setText(data.description);
        SetSubgoals(data.subgoals);
        SetEntity(data.entities);
    }

    private void SetEntity(List<EntityShort> entities) {
        List<ExpandableListItem> taskShorts = new ArrayList<>();
        List<ExpandableListItem> routineShorts = new ArrayList<>();
        List<ExpandableListItem> questionShorts = new ArrayList<>();
        List<ExpandableListItem> reflectionShorts = new ArrayList<>();
        for (EntityShort entity : entities) {
            switch (entity.entityType) {
                case "TASK":
                    taskShorts.add(new ExpandableListItem(entity.title,entity.description));
                    break;
                case "ROUTINE":
                    routineShorts.add(new ExpandableListItem(entity.title, entity.description));
                    break;
                case "QUESTION":
                    questionShorts.add(new ExpandableListItem(entity.title, entity.description));
                    break;
                case "REFLECTION":
                    reflectionShorts.add(new ExpandableListItem(entity.title, entity.description));
                    break;
            }
        }
        taskList.SetDataList(taskShorts);
        taskList.SetTitle("Tasks");
        routineList.SetDataList(routineShorts);
        routineList.SetTitle("Routines");
        questionList.SetDataList(questionShorts);
        questionList.SetTitle("Questions");
        reflectionList.SetDataList(reflectionShorts);
        reflectionList.SetTitle("Reflections");
    }

    @OnClick(R.id.copyGoal)
    @Optional
    public void OnCopyGoal(){
        Utils.showLoading(getParentFragmentManager());
        service.copyGoal(BeABeeApplication.userId,idPro).enqueue(new Callback<BasicResponse>() {
            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                Utils.dismissLoading();
                if (response.isSuccessful() && response.body() != null) {
                    Utils.ShowErrorToast(getActivity(), "Goal Downloaded successfully");
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
}