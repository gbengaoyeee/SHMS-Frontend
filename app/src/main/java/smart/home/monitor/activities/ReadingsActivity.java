package smart.home.monitor.activities;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Map;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import smart.home.monitor.R;
import smart.home.monitor.models.DatabaseObserveHandler;
import smart.home.monitor.models.Device;

public class ReadingsActivity extends AppCompatActivity {
    TextView temperatureReading, gasReading, humidityReading;
    Device selectedDevice;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_readings);

        temperatureReading = findViewById(R.id.temperatureReading);
        gasReading = findViewById(R.id.gasReading);
        humidityReading = findViewById(R.id.humidityReading);

        if(getIntent().getExtras() != null) {
            selectedDevice = (Device) getIntent().getParcelableExtra("device");
            Toast.makeText(this, selectedDevice.device_code, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        selectedDevice.observeDevice(new DatabaseObserveHandler() {
            @Override
            public void onChange(Map<String, String> data) {
                if(data.containsKey("gas")){
                    String gasText = data.get("gas") + " - Normal";
                    gasReading.setText(gasText);
                }
                if(data.containsKey("humidity")){
                    String humidityText = data.get("humidity") + " - Normal";
                    humidityReading.setText(humidityText);
                }
                if(data.containsKey("temperature")){
                    String temperatureText = data.get("temperature") + " - Normal";
                    temperatureReading.setText(temperatureText);
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        System.out.println("ON STOP");
        selectedDevice.stopObserving();
    }
}
