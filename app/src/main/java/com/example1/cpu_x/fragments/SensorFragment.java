package com.example1.cpu_x.fragments;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorManager;
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

import java.util.Arrays;
import java.util.List;

public class SensorFragment extends Fragment {

    private LinearLayout sensorInfoLayout;

    SensorManager sensorManager;

    private final List<Integer> interestedSensorTypes = Arrays.asList(
            Sensor.TYPE_ACCELEROMETER,
            Sensor.TYPE_LINEAR_ACCELERATION,
            Sensor.TYPE_GYROSCOPE,
            Sensor.TYPE_ORIENTATION,
            Sensor.TYPE_PRESSURE,
            Sensor.TYPE_PROXIMITY,
            Sensor.TYPE_ROTATION_VECTOR,
            Sensor.TYPE_TEMPERATURE,
            Sensor.TYPE_GRAVITY,
            Sensor.TYPE_MAGNETIC_FIELD
    );
            // Add other sensor types as needed

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sensor, container, false);

        sensorInfoLayout = view.findViewById(R.id.sensor_info_layout);

        displaySensorInformation();

        return view;
    }

    private void displaySensorInformation() {
        PackageManager packageManager = requireContext().getPackageManager();
        sensorInfoLayout.removeAllViews();
        // Check for Bluetooth support
        boolean isBluetoothSupported = packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH);

        addKeyValueRow(sensorInfoLayout,"Bluetooth", isBluetoothSupported ? "Supported" : "Not Supported");
        addLineOrSpace();

        // Check for WiFi support
        boolean isWifiSupported = packageManager.hasSystemFeature(PackageManager.FEATURE_WIFI);
        addKeyValueRow(sensorInfoLayout,"WiFi", isWifiSupported ? "Supported" : "Not Supported");
        addLineOrSpace();

        // Check for GPS support
        boolean isGpsSupported = packageManager.hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS);
        addKeyValueRow(sensorInfoLayout,"GPS", isGpsSupported ? "Supported" : "Not Supported");
        addLineOrSpace();

        boolean isLiveWallpaperSupported = packageManager.hasSystemFeature(PackageManager.FEATURE_LIVE_WALLPAPER);
        addKeyValueRow(sensorInfoLayout,"Live Wallpapers", isLiveWallpaperSupported ? "Supported" : "Not Supported");
        addLineOrSpace();

        boolean isMicrophoneSupported = packageManager.hasSystemFeature(PackageManager.FEATURE_MICROPHONE);
        addKeyValueRow(sensorInfoLayout,"Microphone", isMicrophoneSupported ?"Supported" : "Not Supported");
        addLineOrSpace();
        addDarkerLine();

        boolean isAccelerometerSupported = packageManager.hasSystemFeature(PackageManager.FEATURE_SENSOR_ACCELEROMETER);
        addKeyValueRow(sensorInfoLayout,"Accelerometer", isAccelerometerSupported ?"Supported" : "Not Supported");
        addLineOrSpace();

        boolean isBarometerSupported = packageManager.hasSystemFeature(PackageManager.FEATURE_SENSOR_BAROMETER);
        addKeyValueRow(sensorInfoLayout,"Barometer", isBarometerSupported ?"Supported" : "Not Supported");
        addLineOrSpace();

        boolean isCompassSupported = packageManager.hasSystemFeature(PackageManager.FEATURE_SENSOR_COMPASS);
        addKeyValueRow(sensorInfoLayout,"Compass", isCompassSupported ?"Supported" : "Not Supported");
        addLineOrSpace();

        boolean isGyroscopeSupported = packageManager.hasSystemFeature(PackageManager.FEATURE_SENSOR_GYROSCOPE);
        addKeyValueRow(sensorInfoLayout,"Gyroscope", isGyroscopeSupported ?"Supported" : "Not Supported");
        addLineOrSpace();

        boolean isLightSupported = packageManager.hasSystemFeature(PackageManager.FEATURE_SENSOR_LIGHT);
        addKeyValueRow(sensorInfoLayout,"Light", isLightSupported ?"Supported" : "Not Supported");
        addLineOrSpace();

        // Check for Magnetic Field sensor support
        sensorManager = (SensorManager) requireContext().getSystemService(Context.SENSOR_SERVICE);

        Sensor magneticFieldSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        boolean isMagneticFieldSensorSupported = magneticFieldSensor != null;
        addKeyValueRow(sensorInfoLayout,"Magnetic Field", isMagneticFieldSensorSupported ? "Supported" : "Not Supported");
        addLineOrSpace();

        // Check for Linear Acceleration sensor support
        Sensor linearAccelerationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        boolean isLinearAccelerationSensorSupported = linearAccelerationSensor != null;
        addKeyValueRow(sensorInfoLayout,"Linear Acceleration", isLinearAccelerationSensorSupported ? "Supported" : "Not Supported");
        addLineOrSpace();

        // Check for Orientation sensor support
        Sensor orientationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        boolean isOrientationSensorSupported = orientationSensor != null;
        addKeyValueRow(sensorInfoLayout,"Orientation", isOrientationSensorSupported ? "Supported" : "Not Supported");
        addLineOrSpace();

        Sensor pressureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        boolean isPressureSupported = pressureSensor != null;
        addKeyValueRow(sensorInfoLayout,"Pressure", isPressureSupported ? "Supported" : "Not Supported");
        addLineOrSpace();

        Sensor proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        boolean isProximitySupported = proximitySensor != null;
        addKeyValueRow(sensorInfoLayout,"Proximity", isProximitySupported ? "Supported" : "Not Supported");
        addLineOrSpace();

        boolean isNFCSupported = packageManager.hasSystemFeature(PackageManager.FEATURE_NFC);
        addKeyValueRow(sensorInfoLayout,"NFC", isNFCSupported ?"Supported" : "Not Supported");
        addLineOrSpace();

        Sensor rotationVectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        boolean isRotationVectorSupported = rotationVectorSensor != null;
        addKeyValueRow(sensorInfoLayout,"Rotation Vector", isRotationVectorSupported ? "Supported" : "Not Supported");
        addLineOrSpace();

        Sensor temperatureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_TEMPERATURE);
        boolean isTemperatureSupported = temperatureSensor != null;
        addKeyValueRow(sensorInfoLayout,"Temperature", isTemperatureSupported ? "Supported" : "Not Supported");
        addLineOrSpace();

        Sensor gravitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        boolean isGravitySupported = gravitySensor != null;
        addKeyValueRow(sensorInfoLayout,"Gravity", isGravitySupported ? "Supported" : "Not Supported");
        addLineOrSpace();

        // Add other sensor types as needed
//        for (Integer sensorType : interestedSensorTypes) {
//            SensorManager sensorManager = (SensorManager) requireContext().getSystemService(Context.SENSOR_SERVICE);
//            List<Sensor> sensors = sensorManager.getSensorList(sensorType);
//
//            if (!sensors.isEmpty()) {
//                Sensor sensor = sensors.get(0); // Assuming there's only one sensor of each type
//                String sensorName = sensor.getName();
//                addKeyValueRow(sensorInfoLayout,sensorName, "Supported");
//            }
//        }
    }


    private void addKeyValueRow(LinearLayout layout, String key, String value) {
        // Create a new LinearLayout for each key-value pair
        LinearLayout keyValuePairLayout = new LinearLayout(requireContext());
        keyValuePairLayout.setOrientation(LinearLayout.HORIZONTAL);

        TextView keyTextView = new TextView(requireContext());
        Typeface typeface = ResourcesCompat.getFont(requireContext(),R.font.pmedium);
        keyTextView.setTypeface(typeface);
        keyTextView.setText(key);
        keyTextView.setTextSize(16);
//        keyTextView.setPadding(10,40,0,20);
        keyTextView.setPadding(10,20,0,40);
        keyTextView.setTextColor(getResources().getColor(R.color.blue));
        keyTextView.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));


        TextView colonTextView = new TextView(requireContext());
        Typeface typeface3 = ResourcesCompat.getFont(requireContext(),R.font.pmedium);
        colonTextView.setTypeface(typeface3);
        colonTextView.setText(":  ");
        colonTextView.setTextSize(16);
        colonTextView.setPadding(40,40,0,0);
        colonTextView.setTextColor(getResources().getColor(R.color.blue));
        colonTextView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        colonTextView.setGravity(Gravity.CENTER);

        TextView valueTextView = new TextView(requireContext());
        Typeface typeface1 = ResourcesCompat.getFont(requireContext(),R.font.pmedium);
        valueTextView.setTypeface(typeface1);
        valueTextView.setText(value);
        valueTextView.setTextSize(16);
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
        sensorInfoLayout.addView(lineView);
        sensorInfoLayout.setPadding(60,20,60,0);

        // Alternatively, you can add a space:
        /*
        Space spaceView = new Space(requireContext());
        spaceView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 16)); // Adjust the height as needed
        cpuInfoLayout.addView(spaceView);
        */
    }
    private void addDarkerLine() {
        // Add a darker line after each key-value pair
        View lineView = new View(requireContext());
        lineView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 2));
        lineView.setBackgroundColor(getResources().getColor(R.color.blue)); // Change color as needed
        sensorInfoLayout.addView(lineView);
    }
}
