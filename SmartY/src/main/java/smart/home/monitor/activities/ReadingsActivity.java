package smart.home.monitor.activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Map;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import smart.home.monitor.R;
import smart.home.monitor.models.DatabaseObserveHandler;
import smart.home.monitor.models.Device;
/**
 * @Team_Name: SMARTY
 */
public class ReadingsActivity extends AppCompatActivity {
    TextView temperatureReading, gasReading, humidityReading, resetReading, longitudeReading, latitudeReading;
    Button resetButton;
    Device selectedDevice;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_readings);

        temperatureReading = findViewById(R.id.temperatureReading);
        gasReading = findViewById(R.id.gasReading);
        humidityReading = findViewById(R.id.humidityReading);
        resetReading = findViewById(R.id.resetReading);
        longitudeReading = findViewById(R.id.longitudeReading);
        latitudeReading = findViewById(R.id.latitudeReading);
        resetButton = (Button) findViewById(R.id.resetButton);

        //onclick listener for reset button. clicking will set "reset" to 1 for 3 seconds
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //set reset to 1
                selectedDevice.alterReset(1);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //set reset to 0
                        selectedDevice.alterReset(0);
                    }
                }, 3000);//after 3 seconds, run above
            }
        });

        if(getIntent().getExtras() != null) {
            selectedDevice = (Device) getIntent().getParcelableExtra("device");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        selectedDevice.observeDevice(new DatabaseObserveHandler() {
            @Override
            public void onChange(Device device, boolean danger) {
                String gasText = device.gas + " - Normal";
                String humidityText = device.humidity + " - Normal";
                String temperatureText = device.temperature + " - Normal";
                String latText = "Latitude: " + device.lat;//latitude
                String lonText = "Longitude: " + device.lon;//longitude
                String resetText = "Reset status: " + device.reset; //reset status
                gasReading.setText(gasText);
                humidityReading.setText(humidityText);
                temperatureReading.setText(temperatureText);
                resetReading.setText(resetText);
                latitudeReading.setText(latText);
                longitudeReading.setText(lonText);

                // removing "normal" from string if danger
                if(danger){
                    Toast.makeText(getApplicationContext(),"Danger!",Toast.LENGTH_LONG).show();
                    gasText = device.gas.toString();
                    humidityText = device.humidity.toString();
                    temperatureText = device.temperature.toString();
                    gasReading.setText(gasText);
                    humidityReading.setText(humidityText);
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
