package com.group12.beabee.views.MainStructure;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import butterknife.ButterKnife;

public class FirstPageFragment extends Fragment {

    private final BaseEntityLinkableFragment parent;
    private final int layoutId;

    public FirstPageFragment(BaseEntityLinkableFragment parent, @LayoutRes int layoutId) {
        super();
        this.parent = parent;
        this.layoutId = layoutId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(layoutId, container, false);
        ButterKnife.bind(parent, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        parent.onReady();
    }
}
