package com.group12.beabee.views.MainPage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.group12.beabee.R;
import com.group12.beabee.models.requests.SignUpRequest;
import com.group12.beabee.models.responses.SignUpResponse;
import com.group12.beabee.network.BeABeeService;
import com.group12.beabee.network.ServiceAPI;
import com.group12.beabee.views.goals.GoalFragment;
import com.group12.beabee.views.goals.HomeFragment;
import com.group12.beabee.views.goals.subGoalFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.bottom_bar)
    BottomNavigationView bottomNavigationView;
    @BindView(R.id.vp_main)
    ViewPager2 viewPagerMain;

    private ServiceAPI serviceAPI;

    private boolean onSelectLoop = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        serviceAPI = BeABeeService.serviceAPI;

        viewPagerMain.setAdapter(new MainPagerAdapter(this));
        viewPagerMain.setOffscreenPageLimit(5);
        viewPagerMain.registerOnPageChangeCallback(new PagerPageChangeListener());
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationListener());
    }

    static class MainPagerAdapter extends FragmentStateAdapter {

        public MainPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            BaseContainerFragment baseContainerFragment;
            switch (position) {
                case 0:
                    baseContainerFragment = new BaseContainerFragment(new HomeFragment());
                    break;
                case 1:
                    baseContainerFragment = new BaseContainerFragment(new GoalFragment());
                    break;
                case 2:
                    baseContainerFragment = new BaseContainerFragment(new subGoalFragment());
                    break;
                case 3:
                    baseContainerFragment = new BaseContainerFragment(new HomeFragment());
                    break;
                case 4:
                    baseContainerFragment = new BaseContainerFragment(new HomeFragment());
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
                    break;
                case R.id.page_2:
                    viewPagerMain.setCurrentItem(1);
                    break;
                case R.id.page_3:
                    viewPagerMain.setCurrentItem(2);
                    break;
                case R.id.page_4:
                    viewPagerMain.setCurrentItem(3);
                    break;
                case R.id.page_5:
                    viewPagerMain.setCurrentItem(4);
                    break;
                default:
                    return false;
            }
            return true;
        }
    }
}