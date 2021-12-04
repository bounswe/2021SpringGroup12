package com.group12.beabee.views.MainStructure;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;

import com.group12.beabee.R;
import com.group12.beabee.views.BaseInnerFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BaseContainerFragment extends Fragment {

    @BindView(R.id.topbar)
    View topbar;
    @BindView(R.id.fragment_cont)
    FragmentContainerView fragmentContainerView;
    @BindView(R.id.btn_back)
    ImageButton btnBack;
    @BindView(R.id.btn_cancel)
    ImageButton btnCancel;
    @BindView(R.id.btn_edit)
    ImageButton btnEdit;
    @BindView(R.id.btn_approve)
    ImageButton btnApprove;
    @BindView(R.id.btn_add)
    ImageButton btnAdd;

    private FragmentManager fragmentManager;
    private BaseInnerFragment initialFragment;

    public BaseContainerFragment(BaseInnerFragment fragment) {
        initialFragment = fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_container, container, false);
        ButterKnife.bind(this, view);
        AddNewFragment(initialFragment);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void SetBackBtnListener(View.OnClickListener listener) {
        btnBack.setOnClickListener(listener);
    }

    public void SetCancelBtnListener(View.OnClickListener listener) {
        btnCancel.setOnClickListener(listener);
    }

    public void SetApproveBtnListener(View.OnClickListener listener) {
        btnApprove.setOnClickListener(listener);
    }

    public void SetEditBtnListener(View.OnClickListener listener) {
        btnEdit.setOnClickListener(listener);
    }

    public void SetAddBtnListener(View.OnClickListener listener) {
        btnAdd.setOnClickListener(listener);
    }

    public void AddNewFragment(BaseInnerFragment fragment) {
        fragmentManager = getChildFragmentManager();
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_left, R.anim.slide_right)
                .replace(R.id.fragment_cont, fragment)
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .commit();
    }

    public void RemoveFragmentFromStack() {
        fragmentManager.popBackStack();
    }

    public void SetMode(PageMode pageMode) {
        switch (pageMode) {
            case OnlyBack:
                ModeOnlyBack();
                break;
            case Editable:
                ModeEditable();
                break;
            case Edit:
                ModeEdit();
                break;
            case NoTopBar:
                ModeNoTopBar();
                break;
            case List:
                ModeList();
                break;
            case ListWithBack:
                ModeListWithBack();
                break;
        }
    }

    private void ModeEdit() {
        topbar.setVisibility(View.VISIBLE);
        btnBack.setVisibility(View.GONE);
        btnCancel.setVisibility(View.VISIBLE);
        btnApprove.setVisibility(View.VISIBLE);
        btnEdit.setVisibility(View.GONE);
        btnAdd.setVisibility(View.GONE);
    }

    private void ModeEditable() {
        topbar.setVisibility(View.VISIBLE);
        btnBack.setVisibility(View.VISIBLE);
        btnCancel.setVisibility(View.GONE);
        btnApprove.setVisibility(View.GONE);
        btnEdit.setVisibility(View.VISIBLE);
        btnAdd.setVisibility(View.GONE);
    }

    private void ModeOnlyBack() {
        topbar.setVisibility(View.VISIBLE);
        btnBack.setVisibility(View.VISIBLE);
        btnCancel.setVisibility(View.GONE);
        btnApprove.setVisibility(View.GONE);
        btnEdit.setVisibility(View.GONE);
        btnAdd.setVisibility(View.GONE);
    }

    private void ModeList() {
        topbar.setVisibility(View.VISIBLE);
        btnBack.setVisibility(View.GONE);
        btnCancel.setVisibility(View.GONE);
        btnApprove.setVisibility(View.GONE);
        btnEdit.setVisibility(View.GONE);
        btnAdd.setVisibility(View.VISIBLE);
    }

    private void ModeListWithBack() {
        topbar.setVisibility(View.VISIBLE);
        btnBack.setVisibility(View.VISIBLE);
        btnCancel.setVisibility(View.GONE);
        btnApprove.setVisibility(View.GONE);
        btnEdit.setVisibility(View.GONE);
        btnAdd.setVisibility(View.VISIBLE);
    }

    private void ModeNoTopBar() {
        topbar.setVisibility(View.GONE);
    }

}
