package smart.home.monitor.models;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;

public class Device {
    public String device_code;
    public String device_name;
    private DatabaseReference mDB = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public Device(){
    }

    public Device(String device_code, String device_name){
        this.device_code = device_code;
        this.device_name = device_name;
    }

    public void writeNewDeviceToDB(final DatabaseWriteHandler<Boolean> handler){
        mDB.child("devices/"+ device_code).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists() && (Boolean) snapshot.getValue()){ // Write to database
                    mDB.child("devices/"+device_code).setValue(false);
                    mDB.child("users").child(User.getSha256(mAuth.getCurrentUser().getEmail())).child("devices").child(device_code).setValue(Device.this);
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
}

