package com.group12.beabee.views.MainStructure;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.group12.beabee.R;
import com.group12.beabee.Utils;
import com.group12.beabee.models.ParentType;
import com.group12.beabee.models.responses.EntityShort;
import com.group12.beabee.views.BaseInnerFragment;

import java.util.List;

import butterknife.BindView;

public abstract class BaseEntityLinkableFragment extends BaseInnerFragment {


    @BindView(R.id.pager)
    @Nullable
    ViewPager2 viewPager2;
    @BindView(R.id.tab_layout)
    @Nullable
    TabLayout tabLayout;


    protected int id;

    private LinkablesFragment linkablesFragment;
    private FirstPageFragment firstPageFragment;
    private PagerAdapter pagerAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            id = getArguments().getInt("id", -1);
        else
            id = -1;

        if (id == -1) {
            Utils.ShowErrorToast(getContext(), "Something is wrong!!");
            GoBack();
        }
        linkablesFragment = new LinkablesFragment(id, this, GetLinkableType());
        firstPageFragment = new FirstPageFragment(this, GetLayout());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pagerAdapter = new PagerAdapter(this, firstPageFragment, linkablesFragment);
        viewPager2.setAdapter(pagerAdapter);
        viewPager2.setOffscreenPageLimit(3);
        new TabLayoutMediator(tabLayout, viewPager2,
                (tab, position) -> {
                    if (position == 1)
                        tab.setText("Linked Entities");
                    else
                        tab.setText("Info");
                }
        ).attach();
    }

    @Override
    protected abstract PageMode GetPageMode();

    protected abstract ParentType GetLinkableType();

    @Override
    protected abstract String GetPageTitle();

    protected void SetEntityLinks(List<EntityShort> entities) {
        linkablesFragment.OnEntitiesReceived(entities);
    }

    @Override
    protected final int GetLayoutId() {
        return R.layout.fragment_base_entity_linkable;
    }

    @LayoutRes
    protected abstract int GetLayout();

    public void onReady() {

    }
}


class PagerAdapter extends FragmentStateAdapter {

    private final LinkablesFragment linkablesFragment;
    private final FirstPageFragment firstPageFragment;

    public PagerAdapter(Fragment fragment, FirstPageFragment firstPageFragment, LinkablesFragment linkablesFragment) {
        super(fragment);
        this.firstPageFragment = firstPageFragment;
        this.linkablesFragment = linkablesFragment;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return firstPageFragment;
            case 1:
                return linkablesFragment;
        }
        try {
            throw new Exception("something is wrong with the viewpager adapter page count!!");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return linkablesFragment;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
