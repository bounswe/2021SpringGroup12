package com.group12.beabee.views.entities;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.group12.beabee.BeABeeApplication;
import com.group12.beabee.R;
import com.group12.beabee.Utils;
import com.group12.beabee.models.responses.BasicResponse;
import com.group12.beabee.models.responses.QuestionDTO;
import com.group12.beabee.views.BaseInnerFragment;
import com.group12.beabee.views.MainStructure.PageMode;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QuestionFragmentEdit#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuestionFragmentEdit extends BaseInnerFragment {

    @BindView(R.id.et_title)
    EditText etTitle;
    @BindView(R.id.et_description)
    EditText etDescription;
    @BindView(R.id.cb_isDone)
    CheckBox cbIsDone;

    private QuestionDTO questionDTO;

    public QuestionFragmentEdit() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment QuestionEdit.
     */
    public static QuestionFragmentEdit newInstance(QuestionDTO questionDTO) {
        QuestionFragmentEdit fragment = new QuestionFragmentEdit();
        Bundle args = new Bundle();
        args.putSerializable("questionDTO", questionDTO);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            questionDTO = (QuestionDTO) getArguments().getSerializable("questionDTO");
        }
        if (questionDTO == null) {
            Utils.ShowErrorToast(getContext(), "Something is wrong!!");
            GoBack();
        }
        etTitle.setText(questionDTO.title);
        etDescription.setText(questionDTO.description);
        cbIsDone.setChecked(questionDTO.isDone);
    }

    @Override
    protected void OnApproveClicked() {
        if (etTitle.getText().toString().length() < 3) {
            Utils.ShowErrorToast(getContext(), "The title should be at least 3 chars length!");
            return;
        }
        if (etDescription.getText().toString().length() < 5) {
            Utils.ShowErrorToast(getContext(), "The description should be at least 5 chars length!");
            return;
        }

        questionDTO.title = etTitle.getText().toString();
        questionDTO.description = etDescription.getText().toString();
        questionDTO.isDone = cbIsDone.isChecked();
        service.updateQuestion(questionDTO.id, questionDTO).enqueue(new Callback<BasicResponse>() {
            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().messageType.equals("SUCCESS")) {
                    Utils.ShowErrorToast(getContext(), "Question is successfully updated!");
                    GoBack();
                } else if (!response.isSuccessful() || response.body() == null) {
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
        return R.layout.fragment_question_edit;
    }
}