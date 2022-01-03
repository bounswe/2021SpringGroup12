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

import com.group12.beabee.R;
import com.group12.beabee.Utils;
import com.group12.beabee.models.LinkingType;
import com.group12.beabee.models.responses.SubgoalDetail;
import com.group12.beabee.models.responses.SubgoalShort;
import com.group12.beabee.views.MainStructure.BaseEntityLinkableFragment;
import com.group12.beabee.views.MainStructure.PageMode;
import com.group12.beabee.views.entities.IOnTagClickedListener;
import com.group12.beabee.views.entities.TagCardViewAdapter;

import java.util.List;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SubgoalPrototypeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SubgoalPrototypeFragment extends BaseEntityLinkableFragment  implements IOnSubgoalClickedListener, IOnTagClickedListener, DatePickerDialog.OnDateSetListener {

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
    @BindView(R.id.rating)
    @Nullable
    View ratingView;
    @BindView(R.id.tv_rating)
    @Nullable
    TextView tvRating;
    @BindView(R.id.btn_complete)
    @Nullable
    View btnComplete;

    private SubgoalDetail subgoalDetail;

    private TagCardViewAdapter tagAdapter;
    private SubgoalCardViewAdapter subgoalAdapter;

    public SubgoalPrototypeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Task.
     */
    // TODO: Rename and change types and number of parameters
    public static SubgoalPrototypeFragment newInstance(int id) {
        SubgoalPrototypeFragment fragment = new SubgoalPrototypeFragment();
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
        RefreshPage();
    }

    private void RefreshPage(){
        Utils.showLoading(getChildFragmentManager());
        service.getProSubgoal(id).enqueue(new Callback<SubgoalDetail>() {
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
    }

    private void OnSubgoalReceived(SubgoalDetail data) {
        subgoalDetail = data;
        tvTitle.setText(data.title);
        tvDescription.setText(data.description);
        tvDateSelected.setText(data.deadline);
        if (data.isDone) {
            btnComplete.setVisibility(View.GONE);
            ratingView.setVisibility(View.VISIBLE);
            tvRating.setText(String.valueOf(data.rating));
        }else{
            btnComplete.setVisibility(View.VISIBLE);
            ratingView.setVisibility(View.GONE);
        }
        //SetEntityLinks(data.entities);
        SetSubgoals(data.sublinks);
    }


    @Override
    protected PageMode GetPageMode() {
        return PageMode.Editable;
    }

    @Override
    protected LinkingType GetLinkableType() {
        return LinkingType.SUBGOAL;
    }

    @Override
    protected String GetPageTitle() {
        return "PROTOTYPE SUBGOAL";
    }

    @Override
    protected int GetLayout() {
        return R.layout.fragment_subgoal;
    }

    @Override
    public void OnSubgoalClicked(int id) {
        OpenNewFragment(SubgoalPrototypeFragment.newInstance(id));
    }

    @Override
    public void OnTagClicked(int id) {

    }

    private void SetSubgoals(List<SubgoalShort> subgoals) {
        subgoalAdapter.setData(subgoals);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

    }
}