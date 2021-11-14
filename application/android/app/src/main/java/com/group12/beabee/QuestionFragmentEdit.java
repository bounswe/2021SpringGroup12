package com.group12.beabee;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.group12.beabee.views.BaseInnerFragment;
import com.group12.beabee.views.MainPage.PageMode;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QuestionFragmentEdit#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuestionFragmentEdit extends BaseInnerFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public QuestionFragmentEdit() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment QuestionEdit.
     */
    // TODO: Rename and change types and number of parameters
    public static QuestionFragmentEdit newInstance() {
        QuestionFragmentEdit fragment = new QuestionFragmentEdit();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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