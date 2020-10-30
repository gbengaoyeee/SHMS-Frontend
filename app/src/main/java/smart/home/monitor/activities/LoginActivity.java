package smart.home.monitor.activities;

import androidx.appcompat.app.AppCompatActivity;
import smart.home.monitor.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity {
    private Button button2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        button2 = (Button) findViewById(R.id.loginButton);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openHomePage();
            }
        });
    }

    public void openHomePage(){
        Intent intent = new Intent(this, HomePage.class);
        startActivity(intent);
    }

}