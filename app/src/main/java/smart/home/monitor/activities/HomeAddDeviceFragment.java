package smart.home.monitor.activities;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import smart.home.monitor.R;

public class HomeAddDeviceFragment extends Fragment {

    private androidx.appcompat.widget.Toolbar homeToolBar;
    private ViewPager homeViewPager;
    private TabItem devicesTab, addDeviceTab;
//    private PagerAdapter pagerAdapter;
    private TabLayout homeTabLayout;



    EditText mDeviceCode, mDeviceName;
    Button mAddDeviceBtn;
    DatabaseReference reff;
    Device device;


    public HomeAddDeviceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragView = inflater.inflate(R.layout.fragment_home_add_device, container, false);

        mAddDeviceBtn = mAddDeviceBtn.findViewById(R.id.addDeviceBtn);
        mDeviceCode = mDeviceCode.findViewById(R.id.editText);
        mDeviceName = mDeviceName.findViewById(R.id.editText2);
        device=new Device();
        reff= FirebaseDatabase.getInstance().getReference().child("Device");
        mAddDeviceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int codea=Integer.parseInt(mDeviceCode.getText().toString().trim());

                device.setName(mDeviceName.getText().toString().trim());
                device.setCode(codea);
                reff.push().setValue(device);
               // Toast.makeText(HomeAddDeviceFragment.this, "device added", Toast.LENGTH_LONG).show();

            }
        });

        // Inflate the layout for this fragment
        return fragView;
    }
}

/*
package smart.home.monitor.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import smart.home.monitor.R;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class HomePage extends AppCompatActivity {
    private androidx.appcompat.widget.Toolbar homeToolBar;
    private ViewPager homeViewPager;
    private TabItem devicesTab, addDeviceTab;
    private PagerAdapter pagerAdapter;
    private TabLayout homeTabLayout;

    EditText mDeviceCode, mDeviceName;
    Button mAddDeviceBtn;
    DatabaseReference reff;
    Device device;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_home_add_device);
        //setContentView(R.layout.activity_home_page);

        homeToolBar = findViewById(R.id.homeToolBar);
        homeViewPager = findViewById(R.id.homeViewPager);
        devicesTab = findViewById(R.id.devicesTab);
        addDeviceTab = findViewById(R.id.addDevicesTab);
        homeTabLayout = findViewById(R.id.homeTabLayout);

        mAddDeviceBtn = findViewById(R.id.addDeviceBtn);
        mDeviceCode = findViewById(R.id.editText);
        mDeviceName = findViewById(R.id.editText2);
        device=new Device();
        reff= FirebaseDatabase.getInstance().getReference().child("Device");
        mAddDeviceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int codea=Integer.parseInt(mDeviceCode.getText().toString().trim());

                device.setName(mDeviceName.getText().toString().trim());
                device.setCode(codea);
                reff.push().setValue(device);
                Toast.makeText(HomePage.this, "device added",Toast.LENGTH_LONG).show();

            }
        });
        setSupportActionBar(homeToolBar);

        HomeDevicesFragment DeviceFragment = new HomeDevicesFragment();
        HomeAddDeviceFragment AddDeviceFragment = new HomeAddDeviceFragment();
        pagerAdapter = new PagerAdapter(getSupportFragmentManager(), 0);

        // Adding each tab fragments to pager adapter
        pagerAdapter.addFragment(DeviceFragment);
        pagerAdapter.addFragment(AddDeviceFragment);

        // Setting adapter for view pager
       // homeViewPager.setAdapter(pagerAdapter);

        // handling the selection of a tab
       /* homeTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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
        });*/
 /*   }
}*/
/*
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

 */