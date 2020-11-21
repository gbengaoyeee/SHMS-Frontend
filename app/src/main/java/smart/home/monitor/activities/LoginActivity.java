package smart.home.monitor.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import smart.home.monitor.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    TextView createAcctTV;
    EditText emailET, passwordET;
    Button signInBtn;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Initalize firebase auth
        mAuth = FirebaseAuth.getInstance();

        emailET = findViewById(R.id.loginEmail);
        passwordET = findViewById(R.id.loginPassword);
        signInBtn = findViewById(R.id.signInBtn);
        createAcctTV = (TextView) findViewById(R.id.createAcctTV);

    }

    @Override
    protected void onStart() {
        super.onStart();
        createAcctTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                segueToRegisterActivity();
            }
        });

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInWithEmail(emailET.getText().toString(), passwordET.getText().toString());
            }
        });
    }

    private boolean validateForm(){
        if(emailET.getText().toString().isEmpty() ||
                emailET.getText() == null ||
                passwordET.getText().toString().isEmpty()||
                passwordET.getText() == null
        ){
            return false;
        }
        return true;
    }

    private void showAlertDialogOneOption(int msg, int option){
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setMessage(msg)
                .setNegativeButton(option, null);

        AlertDialog alert = builder.create();
        alert.show();
    }
    private void signInWithEmail(String email, String password){
        if (!validateForm()) {
            showAlertDialogOneOption(R.string.emptyCreds, R.string.okString);
            return;
        }
        // showProgressbar() & hideProgressBar() TO-DO

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            startActivity(new Intent(LoginActivity.this, HomePage.class));
                        }
                        else{
                            showAlertDialogOneOption(R.string.signInFailureMsg, R.string.okString);
                        }
                    }
                });
    }

    /**
     * Segues into Register activity
     */
    private void segueToRegisterActivity(){
        Intent registerActivity = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(registerActivity);
    }

}