package com.example1.cpu_x.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.BatteryManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.example1.cpu_x.R;

public class BatteryFragment extends Fragment {

    private TextView keyTextView1, valueTextView1;

    private LinearLayout batteryInfoLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_battery, container, false);
        batteryInfoLayout = view.findViewById(R.id.battery_info_layout);
        // Find TextView elements in the layout
        keyTextView1 = view.findViewById(R.id.keyTextView1);
        valueTextView1 = view.findViewById(R.id.valueTextView1);

        // Update battery information
        updateBatteryInfo();

        // Register a BroadcastReceiver to listen for battery updates
        getActivity().registerReceiver(batteryReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Unregister the BroadcastReceiver when the fragment is destroyed
        getActivity().unregisterReceiver(batteryReceiver);
    }

    // BroadcastReceiver to listen for battery updates
    private BroadcastReceiver batteryReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateBatteryInfo();
        }
    };

    // Method to update battery information
    private void updateBatteryInfo() {
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = getActivity().registerReceiver(null, ifilter);

        // Get battery information from the intent
        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        String technology = batteryStatus.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY);
        int plugged = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        String pluggedString = getPluggedString(plugged);

        int health = batteryStatus.getIntExtra(BatteryManager.EXTRA_HEALTH, -1);
        String healthString = getHealthString(health);

        int statusInt = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        String status = getStatusString(statusInt);

        int voltage = batteryStatus.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1);
        int temperature = batteryStatus.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1);

        // Update TextView elements with battery information
        keyTextView1.setText("Battery Level:");
        valueTextView1.setText((level) + "%\n" +
                "Technology: " + technology + "\n" +
                "Plugged: " + pluggedString + "\n" +
                "Health: " + healthString + "\n" +
                "Status: " + status + "\n" +
                "Voltage: " + voltage + " mV\n" +
                "Temperature: " + temperature / 10.0 + " °C");

        batteryInfoLayout.removeAllViews(); // Clear existing views

        // Inflate the layout for each key-value pair and add it to the LinearLayout
        addKeyValuePair(batteryInfoLayout,"Battery Level", level + "%");
        addLineOrSpace();
        addKeyValuePair(batteryInfoLayout,"Technology", technology);
        addLineOrSpace();
        addKeyValuePair(batteryInfoLayout,"Plugged", pluggedString);
        addLineOrSpace();
        addKeyValuePair(batteryInfoLayout,"Health", healthString);
        addLineOrSpace();
        addKeyValuePair(batteryInfoLayout,"Status", status);
        addLineOrSpace();
        addKeyValuePair(batteryInfoLayout,"Voltage", voltage + " mV");
        addLineOrSpace();
        addKeyValuePair(batteryInfoLayout,"Temperature", temperature / 10.0 + " °C");
        addLineOrSpace();
    }

    // Helper method to add key-value pairs to the LinearLayout
    private void addKeyValuePair(LinearLayout layout, String key, String value) {
        LinearLayout keyValuePairLayout = new LinearLayout(requireContext());
        keyValuePairLayout.setOrientation(LinearLayout.HORIZONTAL);

        TextView keyTextView = new TextView(requireContext());
        Typeface typeface = ResourcesCompat.getFont(requireContext(),R.font.pmedium);
        keyTextView.setText(key);
        keyTextView.setTextSize(16);
//        keyTextView.setPadding(10,40,0,20);
        keyTextView.setPadding(10,20,0,40);
        keyTextView.setTypeface(typeface);
        keyTextView.setTextColor(getResources().getColor(R.color.blue));
        keyTextView.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));


        TextView colonTextView = new TextView(requireContext());
        Typeface typeface1 = ResourcesCompat.getFont(requireContext(),R.font.pmedium);
        colonTextView.setText(":  ");
        colonTextView.setTextSize(16);
        colonTextView.setTypeface(typeface1);
        colonTextView.setPadding(40,40,0,0);
        colonTextView.setTextColor(getResources().getColor(R.color.blue));
        colonTextView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        colonTextView.setGravity(Gravity.CENTER);

        TextView valueTextView = new TextView(requireContext());
        Typeface typeface3 = ResourcesCompat.getFont(requireContext(),R.font.pmedium);
        valueTextView.setText(value);
        valueTextView.setTextSize(16);
        valueTextView.setTypeface(typeface3);
        valueTextView.setPadding(70,40,0,0);
        valueTextView.setTextColor(getResources().getColor(R.color.blue));
        valueTextView.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));



        // Add TextViews to the key-value pair layout
        keyValuePairLayout.addView(keyTextView);
        keyValuePairLayout.addView(colonTextView);
        keyValuePairLayout.addView(valueTextView);

        // Add key-value pair layout to the main layout
        layout.addView(keyValuePairLayout);
    }

    private void addLineOrSpace() {
        // Add a line or space after each key-value pair
        View lineView = new View(requireContext());
        lineView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1));
        lineView.setBackgroundColor(getResources().getColor(R.color.blue)); // Change color as needed
        batteryInfoLayout.addView(lineView);
        batteryInfoLayout.setPadding(60,20,60,0);

        // Alternatively, you can add a space:
        /*
        Space spaceView = new Space(requireContext());
        spaceView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 16)); // Adjust the height as needed
        cpuInfoLayout.addView(spaceView);
        */
    }

    // Helper method to convert plugged status to a human-readable string
    private String getPluggedString(int plugged) {
        switch (plugged) {
            case BatteryManager.BATTERY_PLUGGED_AC:
                return "Plugged AC";
            case BatteryManager.BATTERY_PLUGGED_USB:
                return "Plugged USB";
            case BatteryManager.BATTERY_PLUGGED_WIRELESS:
                return "Plugged Wireless";
            default:
                return "Not Plugged";
        }
    }

    // Helper method to convert health status to a human-readable string
    private String getHealthString(int health) {
        switch (health) {
            case BatteryManager.BATTERY_HEALTH_GOOD:
                return "Good";
            case BatteryManager.BATTERY_HEALTH_DEAD:
                return "Dead";
            case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
                return "Over Voltage";
            case BatteryManager.BATTERY_HEALTH_OVERHEAT:
                return "Overheat";
            case BatteryManager.BATTERY_HEALTH_UNKNOWN:
            default:
                return "Unknown";
        }
    }
    // Helper method to convert status to a human-readable string
    private String getStatusString(int status) {
        switch (status) {
            case BatteryManager.BATTERY_STATUS_CHARGING:
                return "Charging";
            case BatteryManager.BATTERY_STATUS_DISCHARGING:
                return "Discharging";
            case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                return "Not Charging";
            case BatteryManager.BATTERY_STATUS_FULL:
                return "Full";
            default:
                return "Unknown";
        }
    }
}
