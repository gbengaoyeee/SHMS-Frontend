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
    public Double temperature, gas, humidity;
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
    }

    protected Device(Parcel in) {
        device_code = in.readString();
        device_name = in.readString();
        temperature = in.readDouble();
        gas = in.readDouble();
        humidity = in.readDouble();
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
                            gas = snapshot.getValue(Double.class);
                        }
                        if(snapshot.getKey().equals("humidity")){
                            humidity = snapshot.getValue(Double.class);
                        }
                        if(snapshot.getKey().equals("temperature")){
                            temperature = snapshot.getValue(Double.class);
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
    }
}

