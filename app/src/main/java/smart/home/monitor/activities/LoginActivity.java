package smart.home.monitor.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import smart.home.monitor.R;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    TextView createAcct;
    EditText mloginEmail, mloginPassword;
    Button msignInBtn;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        fAuth = FirebaseAuth.getInstance();
        mloginEmail = findViewById(R.id.loginEmail);
        mloginPassword = findViewById(R.id.loginPassword);
        msignInBtn = findViewById(R.id.signInBtn);
        createAcct = (TextView) findViewById(R.id.createAcct);


        msignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = mloginEmail.getText().toString().trim();
                String password = mloginPassword.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    mloginEmail.setError("Email is required");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    mloginPassword.setError("Password is required");
                    return;
                }
               /* if(password.length() < 5){
                    mRegisterPassword.setError("Password Must be >= 5");
                    return;
                }*/

             //authenticate user
                fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                      if(task.isSuccessful()){
                          Toast.makeText(LoginActivity.this, "Signed in successfully", Toast.LENGTH_SHORT).show();
                          startActivity(new Intent(getApplicationContext(),HomePage.class));
                      }else{
                          Toast.makeText(LoginActivity.this, "Error occurred "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                      }
                    }
                });



            }
        });
        createAcct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),RegisterActivity.class));
            }
        });
    }

    /**
     * Segues into Register activity
     */
   /* private void segueToRegisterActivity(){
        Intent registerActivity = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(registerActivity);
    }*/

}