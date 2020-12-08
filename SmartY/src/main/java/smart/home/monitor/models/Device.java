package smart.home.monitor.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Device implements Parcelable {
    public String device_code;
    public String device_name;
    public long temperature=0, gas=0, humidity=0;
    private DatabaseReference mDB = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference deviceReference;
    private DatabaseReference allDevicesRef;
    private ChildEventListener deviceListener;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public Device(){
    }

    public Device(String device_code, String device_name){
        this.device_code = device_code;
        this.device_name = device_name;
        this.temperature = 0;
        this.gas = 0;
        this.humidity = 0;
    }

    protected Device(Parcel in) {
        device_code = in.readString();
        device_name = in.readString();
        temperature = in.readInt();
        gas = in.readInt();
        humidity = in.readInt();
    }

    public static final Creator<Device> CREATOR = new Creator<Device>() {
        @Override
        public Device createFromParcel(Parcel in) {
            return new Device(in);
        }

        @Override
        public Device[] newArray(int size) {
            return new Device[size];
        }
    };

    public void writeNewDeviceToDB(final DatabaseWriteHandler<Boolean> handler){
        FirebaseUser currUser = mAuth.getCurrentUser();
        this.allDevicesRef = mDB.child("devices/"+ device_code);
        this.deviceReference = mDB.child("users/" + User.getSha256(currUser.getEmail())).child("devices/" + this.device_code);
        allDevicesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists() && (Boolean) snapshot.getValue()){ // Write to database
                    allDevicesRef.setValue(false);
                    deviceReference.setValue(Device.this);
                    handler.onSuccess(true);
                } else {
                    handler.onSuccess(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void removeDeviceFromDB(){
        this.deviceReference.removeValue();
        this.allDevicesRef = mDB.child("devices/"+ device_code);
        allDevicesRef.setValue(true);
    }

    public void observeDevice(final DatabaseObserveHandler handler){
        final Map<String, String> deviceReadings = new HashMap<>();
        FirebaseUser currUser = mAuth.getCurrentUser();
        this.deviceReference = mDB.child("users/" + User.getSha256(currUser.getEmail())).child("devices/" + this.device_code);
        this.deviceListener = deviceReference.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        if(snapshot.getKey().equals("gas")){
                            gas = (Long) snapshot.getValue();
                        }
                        if(snapshot.getKey().equals("humidity")){
                            humidity = (Long) snapshot.getValue();
                        }
                        if(snapshot.getKey().equals("temperature")){
                            temperature = (Long) snapshot.getValue();
                        }

                        handler.onChange(Device.this, false);
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        if(snapshot.getKey().equals("gas")){
                            gas = (Long) snapshot.getValue();
                        }
                        if(snapshot.getKey().equals("humidity")){
                            humidity = (Long) snapshot.getValue();
                        }
                        if(snapshot.getKey().equals("temperature")){
                            temperature = (Long) snapshot.getValue();
                        }

                        boolean gasBool = gas > 100;
                        boolean humidityBool = (humidity < 20 || humidity > 100);
                        boolean temperatureBool = (temperature < 15 || temperature > 50);
                        System.out.println("Gas:"+gasBool);
                        System.out.println("Humid:"+humidityBool);
                        System.out.println("Temp"+temperatureBool);
                        if(gasBool || humidityBool || temperatureBool){
                            handler.onChange(Device.this, true);
                        } else {
                            handler.onChange(Device.this, false);
                        }
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

    public void stopObserving(){
        this.deviceReference.removeEventListener(this.deviceListener);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(device_code);
        parcel.writeString(device_name);
        parcel.writeLong(temperature);
        parcel.writeLong(gas);
        parcel.writeLong(humidity);
    }
}

