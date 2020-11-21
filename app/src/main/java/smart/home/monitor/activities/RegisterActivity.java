package smart.home.monitor.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import smart.home.monitor.R;

public class RegisterActivity extends AppCompatActivity {

    private EditText nameET, emailET, passwordET;
    private Button registerBtn;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Initalize firebase auth
        mAuth = FirebaseAuth.getInstance();

        nameET = findViewById(R.id.registerName);
        emailET = findViewById(R.id.registerEmail);
        passwordET = findViewById(R.id.registerPassword);
        registerBtn = findViewById(R.id.registerBtn);

        Toolbar toolbar = (Toolbar) findViewById(R.id.registerToolbar);
        setSupportActionBar(toolbar);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount(emailET.getText().toString(), passwordET.getText().toString());
            }
        });
    }

    private void createAccount(String email, String password){
        /* TO-DO
        if (!validateForm()) {
            return;
        } */

        // showProgressbar() & hideProgressBar() TO-DO

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(RegisterActivity.this, "Sign up successful", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(RegisterActivity.this, HomePage.class));
                        }
                        else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                            builder.setMessage(R.string.signUpFailureMsg)
                                    .setNegativeButton(R.string.okString, null);

                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                    }
                });
    }
}
