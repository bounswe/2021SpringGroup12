package com.group12.beabee.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.group12.beabee.Utils;
import com.group12.beabee.models.responses.BasicResponse;
import com.group12.beabee.network.BeABeeService;
import com.group12.beabee.network.ServiceAPI;
import com.group12.beabee.views.MainStructure.BaseContainerFragment;
import com.group12.beabee.views.MainStructure.PageMode;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        parentFragment.SetAddBtnListener(view -> OnAddClicked());
    }

    public <T extends BaseInnerFragment> void OpenNewFragment(T fragmentToOpen){
        parentFragment.AddNewFragment(fragmentToOpen);
    }

    protected void CreateLink(int parentId, int childId, Runnable runnable){
        BeABeeService.serviceAPI.linkEntities(parentId, childId).enqueue(new Callback<BasicResponse>() {
            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().messageType.equals("SUCCESS")) {
                    Utils.ShowErrorToast(getContext(), "Entity is successfully linked!");
                    runnable.run();
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

    protected void OnAddClicked(){
    }

    protected void GoBack(){
        parentFragment.RemoveFragmentFromStack();
    }

    protected abstract PageMode GetPageMode();

    protected abstract int GetLayoutId();
}
