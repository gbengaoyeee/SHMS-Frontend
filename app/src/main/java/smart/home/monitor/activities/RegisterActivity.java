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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import smart.home.monitor.R;
import smart.home.monitor.models.User;

public class RegisterActivity extends AppCompatActivity {

    private EditText nameET, emailET, passwordET;
    private Button registerBtn;
    FirebaseAuth mAuth;
    private DatabaseReference mDB;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Initalize firebase auth
        mAuth = FirebaseAuth.getInstance();
        //Initialize firebase database
        mDB = FirebaseDatabase.getInstance().getReference();

        nameET = findViewById(R.id.registerName);
        emailET = findViewById(R.id.registerEmail);
        passwordET = findViewById(R.id.registerPassword);
        registerBtn = findViewById(R.id.registerBtn);

        Toolbar toolbar = (Toolbar) findViewById(R.id.registerToolbar);
        setSupportActionBar(toolbar);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount(nameET.getText().toString(), emailET.getText().toString(), passwordET.getText().toString());
            }
        });
    }

    private boolean validateForm(){
        if(emailET.getText().toString().isEmpty() ||
                emailET.getText() == null ||
                passwordET.getText().toString().isEmpty()||
                passwordET.getText() == null ||
                nameET.getText().toString().isEmpty() ||
                nameET.getText() == null
        ){
            return false;
        }
        return true;
    }

    private void showAlertDialogOneOption(int msg, int option){
        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
        builder.setMessage(msg)
                .setNegativeButton(option, null);

        AlertDialog alert = builder.create();
        alert.show();
    }
// firebase login 
    private void writeNewUser(String name, String email){
        User newUser = new User(name, email);
        mDB.child("users").child(User.getSha256(email)).setValue(newUser);
    }

    private void createAccount(final String name, final String email, String password){
        if (!validateForm()) {
            showAlertDialogOneOption(R.string.emptyCreds, R.string.okString);
            return;
        }
        // showProgressbar() & hideProgressBar() TO-DO

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            writeNewUser(name, email);
                            Toast.makeText(RegisterActivity.this, "Sign up successful", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(RegisterActivity.this, HomePage.class));
                        }
                        else {
                            showAlertDialogOneOption(R.string.signUpFailureMsg, R.string.okString);
                        }
                    }
                });
    }
}
