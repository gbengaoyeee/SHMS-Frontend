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
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
/**
 * @Team_Name: SMARTY
 */
public class Device implements Parcelable {
    public String device_code;
    public String device_name;
    public Double temperature, gas, humidity, lat, lat_home, lon, lon_home;
    public Integer reset;
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
        this.temperature = 0.0;
        this.gas = 0.0;
        this.humidity = 0.0;
        this.lat = 0.0;
        this.lat_home = 0.0;
        this.lon = 0.0;
        this.lon_home = 0.0;
        this.reset = 0;
    }

    protected Device(Parcel in) {
        device_code = in.readString();
        device_name = in.readString();
        temperature = in.readDouble();
        gas = in.readDouble();
        humidity = in.readDouble();
        lat = in.readDouble();
        lat_home = in.readDouble();
        lon = in.readDouble();
        lon_home = in.readDouble();
        reset = in.readInt();
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
        final Random rand = new Random();
        final FirebaseUser currUser = mAuth.getCurrentUser();
        this.allDevicesRef = mDB.child("devices/"+ device_code);
        this.deviceReference = mDB.child("users/" + User.getSha256(currUser.getEmail())).child("devices/" + this.device_code);
        allDevicesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists() && (Boolean) snapshot.getValue()){ // Write to database
                    // Sets this device as registered by a user
                    allDevicesRef.child("isAvailable").setValue(false);
                    allDevicesRef.child("owner").setValue(User.getSha256(currUser.getEmail()));

                    // Adds this device in the user's devices list
                    deviceReference.setValue(Device.this);
                    deviceReference.child("gas").setValue(rand.nextInt(100));
                    deviceReference.child("humidity").setValue(20 + rand.nextInt(100));
                    deviceReference.child("temperature").setValue(15 + rand.nextInt(50));
                    deviceReference.child("lat").setValue(43.63438);
                    deviceReference.child("lat_home").setValue(0);
                    deviceReference.child("lon").setValue(-79.54087);
                    deviceReference.child("lon_home").setValue(0);
                    deviceReference.child("reset").setValue(0);
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

    //simple function to alter value of reset
    public void alterReset(int option){
        FirebaseUser currUser = mAuth.getCurrentUser();
        this.allDevicesRef = mDB.child("devices/"+ device_code);
        this.deviceReference = mDB.child("users/" + User.getSha256(currUser.getEmail())).child("devices/" + this.device_code);

        if(option == 1)
            deviceReference.child("reset").setValue(1);
        else
            deviceReference.child("reset").setValue(0);
    }

    //function to set new home lat/lon values
    public void setHomeLocation(){
        FirebaseUser currUser = mAuth.getCurrentUser();
        this.allDevicesRef = mDB.child("devices/"+ device_code);
        this.deviceReference = mDB.child("users/" + User.getSha256(currUser.getEmail())).child("devices/" + this.device_code);

        //set new lat value based on current lat
        deviceReference.child("lat_home").setValue(lat);
        //set new lon value based on current lon
        deviceReference.child("lon_home").setValue(lon);
    }

    public void removeDeviceFromDB(){
        this.deviceReference.removeValue();
        this.allDevicesRef = mDB.child("devices/"+ device_code);
        // Sets this device as free to be used by another user
        allDevicesRef.child("isAvailable").setValue(true);
        allDevicesRef.child("owner").setValue(null);
    }

    public void observeDevice(final DatabaseObserveHandler handler){
        final Map<String, String> deviceReadings = new HashMap<>();
        FirebaseUser currUser = mAuth.getCurrentUser();
        this.deviceReference = mDB.child("users/" + User.getSha256(currUser.getEmail())).child("devices/" + this.device_code);
        this.deviceListener = deviceReference.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        if(snapshot.getKey().equals("gas")){
                            gas = snapshot.getValue(Double.class);
                        }
                        if(snapshot.getKey().equals("humidity")){
                            humidity = snapshot.getValue(Double.class);
                        }
                        if(snapshot.getKey().equals("temperature")){
                            temperature = snapshot.getValue(Double.class);
                        }
                        if(snapshot.getKey().equals("lat")){
                            lat = snapshot.getValue(Double.class);
                        }
                        if(snapshot.getKey().equals("lat_home")){
                            lat_home = snapshot.getValue(Double.class);
                        }
                        if(snapshot.getKey().equals("lon")){
                            lon = snapshot.getValue(Double.class);
                        }
                        if(snapshot.getKey().equals("lon_home")){
                            lon_home = snapshot.getValue(Double.class);
                        }
                        if(snapshot.getKey().equals("reset")){
                            reset = snapshot.getValue(Integer.class);
                        }

                        handler.onChange(Device.this, false);
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        if(snapshot.getKey().equals("gas")){
                            gas = snapshot.getValue(Double.class);
                            boolean gas_danger = false;
                            if(gas > 100) {
                                gas_danger = true;
                            }
                            handler.onChange(Device.this, gas_danger);
                        }
                        if(snapshot.getKey().equals("humidity")){
                            humidity = snapshot.getValue(Double.class);
                            boolean hum_danger = false;
                            if(!(humidity > 20 && humidity < 100)) {
                                hum_danger = true;
                            }
                            handler.onChange(Device.this, hum_danger);
                        }
                        if(snapshot.getKey().equals("temperature")){
                            temperature = snapshot.getValue(Double.class);
                            boolean temp_danger = false;
                            if(!(temperature > 15 && temperature < 50)) {
                                temp_danger = true;
                            }
                            handler.onChange(Device.this, temp_danger);
                        }
                        if(snapshot.getKey().equals("lat")){
                            lat = snapshot.getValue(Double.class);
                            handler.onChange(Device.this, false);
                        }
                        if(snapshot.getKey().equals("lat_home")){
                            lat_home = snapshot.getValue(Double.class);
                            handler.onChange(Device.this, false);
                        }
                        if(snapshot.getKey().equals("lon")){
                            lon = snapshot.getValue(Double.class);
                            handler.onChange(Device.this,false);
                        }
                        if(snapshot.getKey().equals("lon_home")){
                            lon_home = snapshot.getValue(Double.class);
                            handler.onChange(Device.this,false);
                        }
                        if(snapshot.getKey().equals("reset")){
                            reset = snapshot.getValue(Integer.class);
                            handler.onChange(Device.this,false);

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
        parcel.writeDouble(temperature);
        parcel.writeDouble(gas);
        parcel.writeDouble(humidity);
        parcel.writeDouble(lat);
        parcel.writeDouble(lat_home);
        parcel.writeDouble(lon);
        parcel.writeDouble(lon_home);
        parcel.writeInt(reset);
    }
}

