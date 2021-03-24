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
    TextView temperatureReading, gasReading, humidityReading, resetReading,
            longitudeReading, latitudeReading, longitudeHomeReading, latitudeHomeReading;
    Button resetButton, setHomeLocation;
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
        longitudeHomeReading = findViewById(R.id.longitudeHome);
        latitudeReading = findViewById(R.id.latitudeReading);
        latitudeHomeReading = findViewById(R.id.latitudeHome);
        resetButton = (Button) findViewById(R.id.resetButton);
        setHomeLocation = (Button) findViewById(R.id.setHomeLocationButton);

        //onclick listener for reset button. clicking will set "reset" to 1 for three seconds
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

        //onclick listener for set home location button. clicking will set new lat/lon values for home position
        setHomeLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //call to set new home location based on where user currently is
                selectedDevice.setHomeLocation();
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
                String latHomeText = "Home Lat: " + device.lat_home;//latitude
                String lonText = "Longitude: " + device.lon;//longitude
                String lonHomeText = "Home Lon: " + device.lon_home;//longitude
                String resetText = "Reset status: " + device.reset; //reset status
                gasReading.setText(gasText);
                humidityReading.setText(humidityText);
                temperatureReading.setText(temperatureText);
                resetReading.setText(resetText);
                latitudeReading.setText(latText);
                latitudeHomeReading.setText(latHomeText);
                longitudeReading.setText(lonText);
                longitudeHomeReading.setText(lonHomeText);

                //pass home and current GPS locations for location status update
                locationStatus(device.lat, device.lat_home, device.lon, device.lon_home);

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

    //set status of location after finding distance between home and current GPS locations
    protected void locationStatus(double lat1, double lat2, double lon1, double lon2){
        double distance;
        TextView distanceDisplay, locationStatus;

        distanceDisplay = findViewById(R.id.distance);
        locationStatus = findViewById(R.id.locationStatus);

        //get distance in meters
        distance = distance(lat1, lat2, lon1, lon2, 0.0,0.0);

        //display distance string from calculated distance
        String distanceString = String.format("Distance(m): %.2f", distance);
        distanceDisplay.setText(distanceString);

        if(distance <= 50)
            locationStatus.setText("At home");
        else
            locationStatus.setText("Away from home");
    }

    /**
     * Calculate distance between two points in latitude and longitude taking
     * into account height difference. If you are not interested in height
     * difference pass 0.0. Uses Haversine method as its base.
     *
     * lat1, lon1 Start point lat2, lon2 End point el1 Start altitude in meters
     * el2 End altitude in meters
     * @returns Distance in Meters
     */
    public static double distance(double lat1, double lat2, double lon1,
                                  double lon2, double el1, double el2) {

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        double height = el1 - el2;

        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        return Math.sqrt(distance);
    }

    @Override
    protected void onStop() {
        super.onStop();
        System.out.println("ON STOP");
        selectedDevice.stopObserving();
    }
}
