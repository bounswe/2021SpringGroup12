package com.group12.beabee.views.entities;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.group12.beabee.BeABeeApplication;
import com.group12.beabee.R;
import com.group12.beabee.Utils;
import com.group12.beabee.models.LinkingType;
import com.group12.beabee.models.ParentType;
import com.group12.beabee.models.requests.Question;
import com.group12.beabee.models.responses.BasicResponse;
import com.group12.beabee.views.BaseInnerFragment;
import com.group12.beabee.views.MainStructure.MainActivity;
import com.group12.beabee.views.MainStructure.PageMode;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QuestionCreateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuestionCreateFragment extends BaseInnerFragment {

    @BindView(R.id.et_title)
    EditText etTitle;
    @BindView(R.id.et_description)
    EditText etDescription;
    @BindView(R.id.cb_isDone)
    CheckBox cbIsDone;

    private Question question;
    private int parentId;
    private LinkingType parentType;

    public QuestionCreateFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment TaskEdit.
     */
    public static QuestionCreateFragment newInstance(int parentId, LinkingType parentType) {
        QuestionCreateFragment fragment = new QuestionCreateFragment();
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
        parentType = ((LinkingType) getArguments().getSerializable("parentType"));
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
        question = new Question();
        question.title = etTitle.getText().toString();
        question.description = etDescription.getText().toString();
        question.initialParentId = parentId;
        question.initialLinkType = parentType;
        if (getActivity()==null)
            return;
        int currentPage = ((MainActivity) getActivity()).GetCurrentPage();
        if (currentPage==0) {
            question.goalType = ParentType.GOAL;
            question.goalId = BeABeeApplication.currentMainGoal;
        } else if(currentPage==1) {
            question.goalType = ParentType.GROUPGOAL;
            question.goalId = BeABeeApplication.currentGroupGoal;
        }else
            return;
        question.deadline = "";
        Utils.showLoading(getParentFragmentManager());
        service.createQuestion(question).enqueue(new Callback<BasicResponse>() {
            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                Utils.dismissLoading();
                if (response.isSuccessful() && response.body() != null && response.body().messageType.equals("SUCCESS")) {
                    Utils.ShowErrorToast(getContext(), "Question is successfully created!");
//                    if (parentId >=0){
//                        CreateLink(parentId, questionDTO.id, ()->GoBack());
//                    }else
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
        return "Create Question";
    }

    @Override
    protected int GetLayoutId() {
        return R.layout.fragment_question_create;
    }
}