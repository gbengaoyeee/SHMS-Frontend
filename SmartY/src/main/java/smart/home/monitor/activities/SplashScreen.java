package smart.home.monitor.activities;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {

    FirebaseAuth mAuth;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize firebase auth
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        handleScreenRedirection(currentUser);
    }

    private void handleScreenRedirection(FirebaseUser user){
        if(user == null){
            startActivity(new Intent(this, LoginActivity.class));
        } else {
            startActivity(new Intent(this, HomePage.class));
        }
    }
}
