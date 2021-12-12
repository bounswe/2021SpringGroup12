package com.group12.beabee.views.goals;

import com.group12.beabee.BeABeeApplication;
import com.group12.beabee.R;
import com.group12.beabee.views.BaseInnerFragment;
import com.group12.beabee.views.MainStructure.PageMode;

public class SampleFragment extends BaseInnerFragment {
    @Override
    protected PageMode GetPageMode() {
        return PageMode.OnlyBack;
    }

    @Override
    protected int GetLayoutId() {
        return R.layout.fragment_sample;
    }
}
