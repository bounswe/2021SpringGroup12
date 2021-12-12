package com.group12.beabee.views.MainStructure;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.group12.beabee.R;
import com.group12.beabee.network.BeABeeService;
import com.group12.beabee.network.ServiceAPI;
import com.group12.beabee.views.goals.GoalFragment;
import com.group12.beabee.views.goals.HomeFragment;
import com.group12.beabee.views.goals.SubgoalFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.bottom_bar)
    BottomNavigationView bottomNavigationView;
    @BindView(R.id.vp_main)
    ViewPager2 viewPagerMain;

    private ServiceAPI serviceAPI;

    private boolean onSelectLoop = false;
    private int currentPage;
    private BaseContainerFragment[] containers = new BaseContainerFragment[5];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        serviceAPI = BeABeeService.serviceAPI;

        viewPagerMain.setAdapter(new MainPagerAdapter(this));
        viewPagerMain.setOffscreenPageLimit(5);
        viewPagerMain.setUserInputEnabled(false);
        viewPagerMain.registerOnPageChangeCallback(new PagerPageChangeListener());
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationListener());
    }

    @Override
    public void onBackPressed() {
        if (containers[currentPage].OnBackPressed())
            return;

        super.onBackPressed();
    }

    static class MainPagerAdapter extends FragmentStateAdapter {

        private final MainActivity fragmentActivity;

        public MainPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
            this.fragmentActivity = ((MainActivity) fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            BaseContainerFragment baseContainerFragment;
            switch (position) {
                case 0:
                    fragmentActivity.containers[0] = new BaseContainerFragment(new HomeFragment());
                    baseContainerFragment = fragmentActivity.containers[0];
                    break;
                case 1:
                    fragmentActivity.containers[1] = new BaseContainerFragment(new HomeFragment());
                    baseContainerFragment = fragmentActivity.containers[1];
                    break;
                case 2:
                    fragmentActivity.containers[2] = new BaseContainerFragment(new HomeFragment());
                    baseContainerFragment = fragmentActivity.containers[2];
                    break;
                case 3:
                    fragmentActivity.containers[3] = new BaseContainerFragment(new HomeFragment());
                    baseContainerFragment = fragmentActivity.containers[3];
                    break;
                case 4:
                    fragmentActivity.containers[4] = new BaseContainerFragment(new HomeFragment());
                    baseContainerFragment = fragmentActivity.containers[4];
                    break;
                default:
                    baseContainerFragment = new BaseContainerFragment(new HomeFragment());
            }
            return baseContainerFragment;
        }

        @Override
        public int getItemCount() {
            return 5;
        }
    }

    class PagerPageChangeListener extends ViewPager2.OnPageChangeCallback {
        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);
            if (onSelectLoop) {
                onSelectLoop = false;
                return;
            }
            onSelectLoop = true;

            int itemId;
            switch (position) {
                case 0:
                    itemId = R.id.home;
                    break;
                case 1:
                    itemId = R.id.page_2;
                    break;
                case 2:
                    itemId = R.id.page_3;
                    break;
                case 3:
                    itemId = R.id.page_4;
                    break;
                case 4:
                    itemId = R.id.page_5;
                    break;
                default:
                    itemId = R.id.home;
            }
            bottomNavigationView.setSelectedItemId(itemId);
        }
    }

    class BottomNavigationListener implements BottomNavigationView.OnNavigationItemSelectedListener {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            if (onSelectLoop) {
                onSelectLoop = false;
                return true;
            }
            onSelectLoop = true;

            switch (item.getItemId()) {
                case R.id.home:
                    viewPagerMain.setCurrentItem(0);
                    currentPage = 0;
                    break;
                case R.id.page_2:
                    viewPagerMain.setCurrentItem(1);
                    currentPage = 1;
                    break;
                case R.id.page_3:
                    viewPagerMain.setCurrentItem(2);
                    currentPage = 2;
                    break;
                case R.id.page_4:
                    viewPagerMain.setCurrentItem(3);
                    currentPage = 3;
                    break;
                case R.id.page_5:
                    viewPagerMain.setCurrentItem(4);
                    currentPage = 4;
                    break;
                default:
                    return false;
            }
            return true;
        }
    }
}