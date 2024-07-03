package com.example1.cpu_x.fragments;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.example1.cpu_x.R;

public class DeviceFragment extends Fragment {

    private LinearLayout deviceInfoLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_device, container, false);

        // Create a LinearLayout to hold key-value pairs
        deviceInfoLayout = view.findViewById(R.id.device_info_layout);

        // Add key-value pairs to the layout
//        addKeyValuePair(deviceInfoLayout, "Manufacturer", Build.MANUFACTURER);
        addKeyValuePair(deviceInfoLayout, "Model", Build.MODEL+"\n("+Build.PRODUCT+")");
        addLineOrSpace();
        addKeyValuePair(deviceInfoLayout, "Brand", Build.BRAND);
        addLineOrSpace();
        addKeyValuePair(deviceInfoLayout, "Board", Build.BOARD);
        addLineOrSpace();
        addKeyValuePair(deviceInfoLayout, "Bootloader", Build.BOOTLOADER);
        addLineOrSpace();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        requireActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        addKeyValuePair(deviceInfoLayout, "Screen Resolution", width + " x " + height + " pixels");
        addLineOrSpace();

        addTotalRam(deviceInfoLayout);
        addLineOrSpace();
        addAvailableRam(deviceInfoLayout);
        addLineOrSpace();
        addNetworkType(deviceInfoLayout);

        addLineOrSpace();
        addDarkerLine();
        addKeyValuePair(deviceInfoLayout, "Internal Storage", null);
        addLineOrSpace();

        // Get total and used internal storage
        long totalInternalStorage = getTotalInternalStorage();
        long usedInternalStorage = totalInternalStorage - getAvailableInternalStorage();

        addKeyValuePair(deviceInfoLayout, "# " + "\n#",
                "Total: " + "(" + formatSize(totalInternalStorage) + ")" +
                        "\nUsage: " + "(" + formatSize(usedInternalStorage) + ")");
        addLineOrSpace();
//        addKeyValuePair(deviceInfoLayout, "Android Version", Build.VERSION.RELEASE);


        return view;
    }

    // Helper method to add a key-value pair to the layout
    private void addKeyValuePair(LinearLayout layout, String key, String value) {
        // Create a new LinearLayout for each key-value pair
        LinearLayout keyValuePairLayout = new LinearLayout(requireContext());
        keyValuePairLayout.setOrientation(LinearLayout.HORIZONTAL);


        TextView keyTextView = new TextView(requireContext());
        Typeface typeface = ResourcesCompat.getFont(requireContext(),R.font.pmedium);

        keyTextView.setText(key);
        keyTextView.setTextSize(16);
        keyTextView.setTypeface(typeface);
        keyTextView.setTextColor(getResources().getColor(R.color.blue));
        keyTextView.setPadding(10,40,0,0);
        keyTextView.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));



        TextView colonTextView = new TextView(requireContext());
        Typeface typeface1 = ResourcesCompat.getFont(requireContext(),R.font.pmedium);
        colonTextView.setText(":  ");
        colonTextView.setTextSize(16);
        colonTextView.setTypeface(typeface1);
//        colonTextView.setPadding(40,40,0,20);
        keyTextView.setPadding(10,20,0,40);
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
        deviceInfoLayout.addView(lineView);
        deviceInfoLayout.setPadding(60,20,60,0);

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
        deviceInfoLayout.addView(lineView);
        deviceInfoLayout.setPadding(50,0,0,0);
    }

    private void addTotalRam(LinearLayout layout) {
        ActivityManager activityManager = (ActivityManager) requireContext().getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);

        long totalRamBytes = memoryInfo.totalMem;
        long totalRamMB = totalRamBytes / (1024 * 1024); // Convert bytes to megabytes

        String formattedTotalRam = totalRamMB + " MB";

        addKeyValuePair(layout, "Total RAM", formattedTotalRam);
    }

    private void addAvailableRam(LinearLayout layout) {
        ActivityManager activityManager = (ActivityManager) requireContext().getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);

        long availableRamBytes = memoryInfo.availMem;
        long availableRamMB = availableRamBytes / (1024 * 1024); // Convert bytes to megabytes

        String formattedAvailableRam = availableRamMB + " MB";

        addKeyValuePair(layout, "Available RAM", formattedAvailableRam);
    }


    // Helper method to add a key-value pair for network type
    private void addNetworkType(LinearLayout layout) {
        TelephonyManager telephonyManager = (TelephonyManager) requireContext().getSystemService(Context.TELEPHONY_SERVICE);
//        Log.e("telephonyManager", ""+telephonyManager);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        int networkType = telephonyManager.getNetworkType();
        Log.e("networkType",""+networkType);
        String networkTypeString;

        switch (networkType) {
            case TelephonyManager.NETWORK_TYPE_IWLAN:
                networkTypeString = "GSM";
                break;
            case TelephonyManager.NETWORK_TYPE_NR:
                networkTypeString = "VoLTE";
                break;
            case TelephonyManager.NETWORK_TYPE_GPRS:
                networkTypeString = "GPRS";
                break;
            case TelephonyManager.NETWORK_TYPE_EDGE:
                networkTypeString = "EDGE";
                break;
            case TelephonyManager.NETWORK_TYPE_UMTS:
                networkTypeString = "UMTS";
                break;
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                networkTypeString = "HSDPA";
                break;
            case TelephonyManager.NETWORK_TYPE_HSPA:
                networkTypeString = "HSPA";
                break;
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                networkTypeString = "HSPAP";
                break;
            case TelephonyManager.NETWORK_TYPE_LTE:
                networkTypeString = "LTE";
                break;
            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
            default:
                networkTypeString = "Unknown";
                break;
        }

        addKeyValuePair(layout, "Network Type", networkTypeString);
    }
    // Method to get total internal storage in bytes
    private long getTotalInternalStorage() {
        StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
        long blockSize = statFs.getBlockSizeLong();
        long totalBlocks = statFs.getBlockCountLong();
        return totalBlocks * blockSize;
    }

    // Method to format size in human-readable format (KB, MB, GB, etc.)
    private String formatSize(long size) {
        String suffix = null;

        if (size >= 1024) {
            suffix = "KB";
            size /= 1024;
            if (size >= 1024) {
                suffix = "MB";
                size /= 1024;
                if (size >= 1024) {
                    suffix = "GB";
                    size /= 1024;
                }
            }
        }

        StringBuilder resultBuffer = new StringBuilder(Long.toString(size));

        int commaOffset = resultBuffer.length() - 3;
        while (commaOffset > 0) {
            resultBuffer.insert(commaOffset, ',');
            commaOffset -= 3;
        }

        if (suffix != null) resultBuffer.append(suffix);
        return resultBuffer.toString();
    }
    // Method to get available internal storage in bytes
    private long getAvailableInternalStorage() {
        StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
        long blockSize = statFs.getBlockSizeLong();
        long availableBlocks = statFs.getAvailableBlocksLong();
        return availableBlocks * blockSize;
    }

}
