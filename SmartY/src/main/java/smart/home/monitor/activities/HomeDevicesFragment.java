package smart.home.monitor.activities;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import smart.home.monitor.R;
import smart.home.monitor.models.DatabaseObserveHandler;
import smart.home.monitor.models.Device;
import smart.home.monitor.models.User;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HomeDevicesFragment extends Fragment {

    private ListView devicesListView;
    private ArrayList<String> dataSource = new ArrayList<>();
    private ArrayList<Device> devicesList = new ArrayList<>();
    private ArrayAdapter<String> dataAdapter;
    private DatabaseReference mDB;
    private FirebaseAuth mAuth;

    public HomeDevicesFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragView = inflater.inflate(R.layout.fragment_home_devices, container, false);
        mDB = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        devicesListView = fragView.findViewById(R.id.devicesListView);

        dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, dataSource);
        devicesListView.setAdapter(dataAdapter);

        //Setting event handler for each item on list
        handleListViewClicks();

        // Inflate the layout for this fragment
        return fragView;
    }

    @Override
    public void onStart() {
        super.onStart();
        observeAndUpdateListView();
    }

    public void observeAndUpdateListView(){
        DatabaseReference devicesRef = mDB.child("users/"+ User.getSha256(mAuth.getCurrentUser().getEmail())).child("devices");
        devicesRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String deviceCode = snapshot.child("device_code").getValue(String.class);
                String deviceName = snapshot.child("device_name").getValue(String.class);
                Device d = new Device(deviceCode, deviceName);
                devicesList.add(d);
                setupForNotification(d);
                dataSource.add(deviceName);
                dataAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                dataAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setupForNotification(Device d){
        d.observeDevice(new DatabaseObserveHandler() {
            @Override
            public void onChange(Device device, boolean danger) {
                if(danger){
                    String message = "There is a problem in "+device.device_name;

                    Uri notifSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext())
                            .setSound(notifSound)
                            .setSmallIcon(R.drawable.com_facebook_button_send_icon_white)
                            .setContentTitle("SHMS")
                            .setContentText(message).setAutoCancel(true);
                    NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(
                            Context.NOTIFICATION_SERVICE
                    );
                    Intent notifIntent = new Intent(getContext(), HomePage.class);
                    PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 0, notifIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setContentIntent(pendingIntent);
                    notificationManager.notify(0, builder.build());
                }
            }
        });
    }

    private void handleListViewClicks(){
        devicesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent readingsActivity = new Intent(getContext(), ReadingsActivity.class);
                readingsActivity.putExtra("device", devicesList.get(i));
                startActivity(readingsActivity);
            }
        });
    }
}