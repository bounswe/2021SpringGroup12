package com.group12.beabee.views.goals;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.group12.beabee.R;
import com.group12.beabee.views.BaseInnerFragment;
import com.group12.beabee.views.MainPage.PageMode;

import butterknife.BindView;

public class HomeFragment extends BaseInnerFragment {

    @BindView(R.id.no_goal_view)
    View noGoalView;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        noGoalView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenNewFragment(new SampleFragment());
            }
        });
    }

    @Override
    protected PageMode GetPageMode() {
        return PageMode.NoTopBar;
    }

    @Override
    protected int GetLayoutId() {
        return R.layout.fragment_home;
    }
}
