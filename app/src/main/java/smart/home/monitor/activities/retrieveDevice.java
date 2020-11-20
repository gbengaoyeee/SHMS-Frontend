package smart.home.monitor.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import smart.home.monitor.R;

public class retrieveDevice extends AppCompatActivity {
        TextView x,y;
        Button btn;
        DatabaseReference reff;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_home_devices);

        x=(TextView)findViewById(R.id.codeView);
        y=(TextView)findViewById(R.id.nameView);
        btn=(Button)findViewById(R.id.DeviceInfobtn);

        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                reff = FirebaseDatabase.getInstance().getReference().child("Device").child("1");
                reff.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        String code = dataSnapshot.child("code").getValue().toString();
                        String name = dataSnapshot.child("name").getValue().toString();
                        x.setText(code);
                        y.setText(name);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
    }
}
