package smart.home.monitor.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import smart.home.monitor.R;

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toolbar;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class HomePage extends AppCompatActivity {
    private androidx.appcompat.widget.Toolbar homeToolBar;
    private ViewPager homeViewPager;
    private TabItem devicesTab, addDeviceTab;
    private PagerAdapter pagerAdapter;
    private TabLayout homeTabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        homeToolBar = findViewById(R.id.homeToolBar);
        homeViewPager = findViewById(R.id.homeViewPager);
        devicesTab = findViewById(R.id.devicesTab);
        addDeviceTab = findViewById(R.id.addDevicesTab);
        homeTabLayout = findViewById(R.id.homeTabLayout);

        setSupportActionBar(homeToolBar);

        HomeDevicesFragment DeviceFragment = new HomeDevicesFragment();
        HomeAddDeviceFragment AddDeviceFragment = new HomeAddDeviceFragment();
        pagerAdapter = new PagerAdapter(getSupportFragmentManager(), 0);

        // Adding each tab fragments to pager adapter
        pagerAdapter.addFragment(DeviceFragment);
        pagerAdapter.addFragment(AddDeviceFragment);

        // Setting adapter for view pager
        homeViewPager.setAdapter(pagerAdapter);

        // handling the selection of a tab
        homeTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                homeViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.logoutMsg)
                .setPositiveButton(R.string.yesString, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // TO-DO
                        FirebaseAuth.getInstance().signOut();
                        finish();
                    }
                })
                .setNegativeButton(R.string.noString, null);

        AlertDialog alert = builder.create();
        alert.show();
    }
}

class PagerAdapter extends FragmentPagerAdapter{
    private List<Fragment> fragments = new ArrayList<>();
    public PagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    public void addFragment(Fragment fragment){
        fragments.add(fragment);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}