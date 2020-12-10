package smart.home.monitor.activities;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import smart.home.monitor.R;
import smart.home.monitor.models.DatabaseWriteHandler;
import smart.home.monitor.models.Device;

/**
 * @Team_Name: SMARTY
 */
public class HomeAddDeviceFragment extends Fragment {
    EditText deviceCodeET, deviceNameET;
    Button addDeviceBtn;

    public HomeAddDeviceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragView = inflater.inflate(R.layout.fragment_home_add_device, container, false);
        deviceCodeET = fragView.findViewById(R.id.deviceCodeET);
        deviceNameET = fragView.findViewById(R.id.deviceNameET);
        addDeviceBtn = fragView.findViewById(R.id.addDeviceBtn);
        addDeviceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewDevice(deviceCodeET.getText().toString(), deviceNameET.getText().toString());
            }
        });

        // Inflate the layout for this fragment
        return fragView;
    }

    private void addNewDevice(String deviceRegCode, String deviceName){
        if(!validateForm()){
            showAlertDialogOneOption(R.string.emptyCreds, R.string.okString);
            return;
        }
        Device newDevice = new Device(deviceRegCode, deviceName);
        newDevice.writeNewDeviceToDB(new DatabaseWriteHandler<Boolean>() {
            @Override
            public void onSuccess(boolean success) {
                updateUI(success);
            }

            @Override
            public void onFailure(Exception e) {
                updateUI(false);
            }
        });

    }

    private void updateUI(boolean success){
        if(success){
            Toast.makeText(getContext(), R.string.newDeviceAddedString, Toast.LENGTH_LONG).show();
            deviceNameET.setText("");
            deviceCodeET.setText("");
        }
        else {
            showAlertDialogOneOption(R.string.addingNewDeviceErrorMsg, R.string.okString);
        }

    }

    private void showAlertDialogOneOption(int msg, int option){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(msg)
                .setNegativeButton(option, null);

        AlertDialog alert = builder.create();
        alert.show();
    }

    private boolean validateForm(){
        if(deviceCodeET.getText().toString().isEmpty() ||
                deviceCodeET.getText() == null ||
                deviceNameET.getText().toString().isEmpty()||
                deviceNameET.getText() == null
        ){
            return false;
        }
        return true;
    }
}