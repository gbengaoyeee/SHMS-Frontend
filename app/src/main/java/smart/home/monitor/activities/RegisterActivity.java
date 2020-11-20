/*
package smart.home.monitor.activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import smart.home.monitor.R;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolbar = (Toolbar) findViewById(R.id.registerToolbar);
        setSupportActionBar(toolbar);
    }
}
*/

package smart.home.monitor.activities;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import smart.home.monitor.R;

public class RegisterActivity extends AppCompatActivity {

    public static final String TAG = "TAG";
    EditText mRegisterName, mRegisterEmail, mRegisterPassword;
    Button mRegisterBtn;
    TextView msignBtn1;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolbar = (Toolbar) findViewById(R.id.registerToolbar);
        setSupportActionBar(toolbar);

        mRegisterName = findViewById(R.id.registerName);
        mRegisterEmail = findViewById(R.id.registerEmail);
        mRegisterPassword = findViewById(R.id.registerPassword);
        mRegisterBtn = findViewById(R.id.registerBtn);
        msignBtn1 = findViewById(R.id.signBtn1);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();



       /* if(fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),HomePage.class));
            finish();
        }*/


        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = mRegisterEmail.getText().toString().trim();
                String password = mRegisterPassword.getText().toString().trim();
                final String Name = mRegisterName.getText().toString();

                if(TextUtils.isEmpty(email)){
                    mRegisterEmail.setError("Email is required");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    mRegisterPassword.setError("Password is required");
                    return;
                }
               /* if(password.length() < 5){
                    mRegisterPassword.setError("Password Must be >= 5");
                    return;
                }*/
                //registering user
                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                                                               @Override
                                                                                               public void onComplete(@NonNull Task<AuthResult> task) {
                                                                                                   if(task.isSuccessful()){
                                                                                                       Toast.makeText(RegisterActivity.this, "Created User", Toast.LENGTH_SHORT).show();
                                                                                                       userID = fAuth.getCurrentUser().getUid();
                                                                                                       DocumentReference documentReference = fStore.collection("users").document(userID);
                                                                                                       Map<String,Object> user = new HashMap<>();
                                                                                                       user.put("Name",Name);
                                                                                                       user.put("email",email);
                                                                                                       // MORE USER DATA TO BE DISPLAYED
                                                                                                       documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                           @Override
                                                                                                           public void onSuccess(Void aVoid) {
                                                                                                               Log.d(TAG, "onSuccess: Profile of user is created for "+userID);

                                                                                                           }
                                                                                                       });
                                                                                                       startActivity(new Intent(getApplicationContext(),HomePage.class));
                                                                                                   }else{
                                                                                                       Toast.makeText(RegisterActivity.this, "Error occurred "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                                                                   }
                                                                                               }
                                                                                           }


                );
            }
        });


        msignBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            }
        });

    }
}
