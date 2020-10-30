package smart.home.monitor.activities;

import androidx.appcompat.app.AppCompatActivity;
import smart.home.monitor.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {
    TextView createAcct;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        createAcct = (TextView) findViewById(R.id.createAcct);
        createAcct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                segueToRegisterActivity();
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