package smart.home.monitor.activities;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import smart.home.monitor.R;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class HomeDevicesFragment extends Fragment {

    private ListView devicesListView;
    public HomeDevicesFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragView = inflater.inflate(R.layout.fragment_home_devices, container, false);
        devicesListView = fragView.findViewById(R.id.devicesListView);
        ArrayList<String> dataSource = new ArrayList<>();
        dataSource.add("Kitchen Device");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, dataSource);
        devicesListView.setAdapter(dataAdapter);

        // Inflate the layout for this fragment
        return fragView;
    }
}