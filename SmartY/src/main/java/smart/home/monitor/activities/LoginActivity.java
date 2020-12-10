package smart.home.monitor.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import smart.home.monitor.R;
import smart.home.monitor.models.User;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.content.SharedPreferences;


import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.facebook.FacebookSdk;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.gms.common.api.ApiException;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.GoogleAuthProvider;
/**
 * @Team_Name: SMARTY
 */
public class LoginActivity extends AppCompatActivity {
    int RC_SIGN_IN=264;
    String TAG="Google sign in";
    TextView createAcctTV;
    EditText emailET, passwordET;
    Button signInBtn, facebookCustomSignInBtn, googleCustomSignInBtn;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Boolean savelogin;
    CheckBox savelogincheckbox;




    GoogleSignInOptions gso;
    SignInButton google_signIn;
    FirebaseAuth mAuth; //private
    GoogleSignInClient googleSignInClient;


   ProgressBar progressBar2;
   // ImageView googleIcon;

   LoginButton loginButton;
   CallbackManager mCallbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences = getSharedPreferences("loginref",MODE_PRIVATE);
        savelogincheckbox = (CheckBox)findViewById(R.id.checkBox);
        editor=sharedPreferences.edit();



        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        //Initialize Facebok SDK
        FacebookSdk.sdkInitialize(LoginActivity.this);

        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();
         loginButton = findViewById(R.id.login_button);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException error) {
            }
        });

        // initializing buttons to replace original facebook and google btns
        facebookCustomSignInBtn = findViewById(R.id.facebookCustomSignInBtn);
        googleCustomSignInBtn = findViewById(R.id.googleCustomSignInBtn);
        facebookCustomSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view == facebookCustomSignInBtn){
                    loginButton.performClick();
                }
            }
        });


        //Initalize firebase auth
        mAuth = FirebaseAuth.getInstance();

        emailET = (EditText) findViewById(R.id.loginEmail);
        passwordET = (EditText) findViewById(R.id.loginPassword);
        signInBtn = findViewById(R.id.signInBtn);
        createAcctTV = (TextView) findViewById(R.id.createAcctTV);
        google_signIn = findViewById(R.id.google_signIn);

        progressBar2 = findViewById(R.id.progressBar2);

        google_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar2.setVisibility(View.VISIBLE);
                Intent signInIntent = googleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

        googleCustomSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                google_signIn.performClick();
            }
        });

        savelogin=sharedPreferences.getBoolean("savelogin",true);
        if(savelogin==true){
            emailET.setText(sharedPreferences.getString("email",null));
            passwordET.setText(sharedPreferences.getString("password",null));
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
            }
        }
        else{
            mCallbackManager.onActivityResult(requestCode,resultCode, data);
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            User newUser = new User(user.getDisplayName(), user.getEmail());
                            newUser.writeNewUserToDB(); // write to DB
                            progressBar2.setVisibility(View.GONE);
                            Intent intent = new Intent(LoginActivity.this, HomePage.class);
                            startActivity(intent);
                        } else {
                            progressBar2.setVisibility(View.GONE);
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            User newUser = new User(user.getDisplayName(), user.getEmail());
                            newUser.writeNewUserToDB();// write to DB

                            progressBar2.setVisibility(View.GONE); // remove progress bar
                            Intent intent = new Intent(LoginActivity.this, HomePage.class);
                            startActivity(intent);

                        } else {
                            progressBar2.setVisibility(View.GONE);
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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

               progressBar2.setVisibility(View.VISIBLE);

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
                            progressBar2.setVisibility(View.INVISIBLE);
                            startActivity(new Intent(LoginActivity.this, HomePage.class));

                        }
                        else{
                            progressBar2.setVisibility(View.INVISIBLE);
                            showAlertDialogOneOption(R.string.signInFailureMsg, R.string.okString);

                        }
                    }
                });
    }

    public void signInBtn(){
        String email = emailET.getText().toString();
        String password = passwordET.getText().toString();

        if(savelogincheckbox.isChecked()){
            editor.putBoolean("savelogin",true);
            editor.putString("email",email);
            editor.putString("password",password);
            editor.commit();

        }else{

            Toast.makeText(LoginActivity.this, "user not saved",
                    Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.quitAppMsg)
                .setPositiveButton(R.string.yesString, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // TO-DO
                        finish();
                    }
                })
                .setNegativeButton(R.string.noString, null);

        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Process.killProcess(Process.myPid());
    }

    /**
     * Segues into Register activity
     */
    private void segueToRegisterActivity(){
        Intent registerActivity = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(registerActivity);
    }

}