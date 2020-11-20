
package smart.home.monitor.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import smart.home.monitor.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class HomePage extends AppCompatActivity {
    private androidx.appcompat.widget.Toolbar homeToolBar;
    private ViewPager homeViewPager;
    private TabItem devicesTab, addDeviceTab;
  //  private PagerAdapter pagerAdapter;
    private TabLayout homeTabLayout;

    EditText mDeviceCode, mDeviceName;
    Button mAddDeviceBtn;
    Button mAddDeviceBtn2;
    DatabaseReference reff;
    Device device;

    FirebaseAuth fAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setContentView(R.layout.activity_home_page);
        setContentView(R.layout.fragment_home_add_device);
        homeToolBar = findViewById(R.id.homeToolBar);
        homeViewPager = findViewById(R.id.homeViewPager);
        devicesTab = findViewById(R.id.devicesTab);
        addDeviceTab = findViewById(R.id.addDevicesTab);
        homeTabLayout = findViewById(R.id.homeTabLayout);

        mAddDeviceBtn2 = findViewById(R.id.addDeviceBtn2);
        mAddDeviceBtn = findViewById(R.id.addDeviceBtn);
        mDeviceCode = findViewById(R.id.editText);
        mDeviceName = findViewById(R.id.editText2);
        device=new Device();
        reff= FirebaseDatabase.getInstance().getReference().child("Device");

        fAuth = FirebaseAuth.getInstance();



        mAddDeviceBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),retrieveDevice.class));
                }
               // openretrieveDevice();


        });

       mAddDeviceBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                int codea=Integer.parseInt(mDeviceCode.getText().toString().trim());

                device.setName(mDeviceName.getText().toString().trim());
                device.setCode(codea);
                reff.push().setValue(device);
                Toast.makeText(HomePage.this, "device added",Toast.LENGTH_LONG).show();


                /* fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                                                       @Override
                                                                                       public void onComplete(@NonNull Task<AuthResult> task) {
                                                                                           if(task.isSuccessful()){
                                                                                               Toast.makeText(RegisterActivity.this, "Created User", Toast.LENGTH_SHORT).show();
                                                                                               startActivity(new Intent(getApplicationContext(),HomePage.class));
                                                                                           }else{
                                                                                               Toast.makeText(RegisterActivity.this, "Error occurred "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                                                           }
                                                                                       }
        //show device                                                                           }*/

              //  fAuth.add

            }

       });


   /*     setSupportActionBar(homeToolBar);

        HomeDevicesFragment DeviceFragment = new HomeDevicesFragment();
        HomeAddDeviceFragment AddDeviceFragment = new HomeAddDeviceFragment();
        pagerAdapter = new PagerAdapter(getSupportFragmentManager(), 0);

        // Adding each tab fragments to pager adapter
        pagerAdapter.addFragment(DeviceFragment);
        pagerAdapter.addFragment(AddDeviceFragment);

        // Setting adapter for view pager
      //  homeViewPager.setAdapter(pagerAdapter);

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



    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
        finish();
    }
   /* public void openretrieveDevice(){
        Intent intent = new Intent(this, retrieveDevice.class);
        startActivity(intent);
    }*/


}
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

