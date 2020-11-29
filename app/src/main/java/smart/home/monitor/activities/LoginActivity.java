package smart.home.monitor.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import smart.home.monitor.R;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {
    int RC_SIGN_IN=264;
    String TAG="Google sign in";
    TextView createAcctTV;
    EditText emailET, passwordET;
    ImageView googleIcon;
    Button signInBtn;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();


        //Initalize firebase auth
        mAuth = FirebaseAuth.getInstance();

        emailET = findViewById(R.id.loginEmail);
        passwordET = findViewById(R.id.loginPassword);
        signInBtn = findViewById(R.id.signInBtn);
        createAcctTV = (TextView) findViewById(R.id.createAcctTV);
        googleIcon = findViewById(R.id.googleIcon);

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

    private boolean validateForm() {
        if (emailET.getText().toString().isEmpty() ||
                emailET.getText() == null ||
                passwordET.getText().toString().isEmpty() ||
                passwordET.getText() == null
        ) {
            return false;
        }
        return true;
    }

    private void showAlertDialogOneOption(int msg, int option) {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setMessage(msg)
                .setNegativeButton(option, null);

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void signInWithEmail(String email, String password) {
        if (!validateForm()) {
            showAlertDialogOneOption(R.string.emptyCreds, R.string.okString);
            return;
        }
        // showProgressbar() & hideProgressBar() TO-DO

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(LoginActivity.this, HomePage.class));
                        } else {
                            showAlertDialogOneOption(R.string.signInFailureMsg, R.string.okString);
                        }
                    }
                });
    }

    /**
     * Segues into Register activity
     */
    private void segueToRegisterActivity() {
        Intent registerActivity = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(registerActivity);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // [START_EXCLUDE]
                updateUI(null);
                // [END_EXCLUDE]
            }
        }

    }

    private void firebaseAuthWithGoogle(String idToken) {
        // [START_EXCLUDE silent]
        showProgressBar();
        // [END_EXCLUDE]
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Snackbar.make(mBinding.mainLayout, "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // [START_EXCLUDE]
                        hideProgressBar();
                        // [END_EXCLUDE]
                    }
                });
    }

}
