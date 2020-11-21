package smart.home.monitor.activities;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import smart.home.monitor.R;

public class ReadingsActivity extends AppCompatActivity {
    TextView temperatureReading, gasReading, humidityReading;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_readings);

        temperatureReading = findViewById(R.id.temperatureReading);
        gasReading = findViewById(R.id.gasReading);
        humidityReading = findViewById(R.id.humidityReading);
    }
}
