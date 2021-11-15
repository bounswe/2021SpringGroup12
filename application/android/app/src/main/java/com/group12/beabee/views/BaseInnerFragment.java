package com.group12.beabee.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.group12.beabee.network.BeABeeService;
import com.group12.beabee.network.ServiceAPI;
import com.group12.beabee.views.MainStructure.BaseContainerFragment;
import com.group12.beabee.views.MainStructure.PageMode;

import butterknife.ButterKnife;

public abstract class BaseInnerFragment extends Fragment {

    private BaseContainerFragment parentFragment;
    protected ServiceAPI service;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(GetLayoutId(), container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        service = BeABeeService.serviceAPI;
        parentFragment = ((BaseContainerFragment) getParentFragment());
        parentFragment.SetMode(GetPageMode());
    }

    @Override
    public void onResume() {
        super.onResume();
        parentFragment.SetMode(GetPageMode());
        parentFragment.SetBackBtnListener(view -> OnBackClicked());
        parentFragment.SetCancelBtnListener(view -> OnCancelClicked());
        parentFragment.SetApproveBtnListener(view -> OnApproveClicked());
        parentFragment.SetEditBtnListener(view -> OnEditClicked());
    }

    protected <T extends BaseInnerFragment> void OpenNewFragment(T fragmentToOpen){
        parentFragment.AddNewFragment(fragmentToOpen);
    }

    protected void OnBackClicked(){
        GoBack();
    }

    protected void OnCancelClicked(){
        GoBack();
    }

    protected void OnApproveClicked(){
    }

    protected void OnEditClicked(){
    }

    protected void GoBack(){
        parentFragment.RemoveFragmentFromStack();
    }

    protected abstract PageMode GetPageMode();

    protected abstract int GetLayoutId();
}
