package com.example1.cpu_x.fragments;

import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.example1.cpu_x.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class CPUFragment extends Fragment {

    private LinearLayout cpuInfoLayout;
    private ProcessBuilder processBuilder;
    private String Holder = "";
    private String[] DATA = {"/system/bin/cat", "/proc/cpuinfo"};
    private InputStream inputStream;
    private Process process;
    private byte[] byteArray = new byte[1024]; // Initialize byteArray here

    private boolean processorInfoAdded = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cpu, container, false);

        // Get reference to the LinearLayout in your layout
        cpuInfoLayout = view.findViewById(R.id.cpu_linearLayout);

        // Fetch and display CPU information

        fetchAndDisplayCpuInfo();

        return view;
    }

    private void fetchAndDisplayCpuInfo() {

        try {
            processBuilder = new ProcessBuilder(DATA);
            process = processBuilder.start();
            inputStream = process.getInputStream();

            while (inputStream.read(byteArray) != -1) {
                Holder = Holder + new String(byteArray);

            }

            inputStream.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        cpuInfoLayout.removeAllViews();
        // Format the Holder string
        List<Pair<String, String>> cpuInfoList = formatCpuInfo(Holder);
//        addDarkerLine();
        addKeyValue("Architecture", Build.SUPPORTED_ABIS[0]);
        addLineOrSpace();
        addKeyValue("CPU Cores", String.valueOf(Runtime.getRuntime().availableProcessors())+" cores");
        addLineOrSpace();
        // Add key-value pairs for CPU information
        for (Pair<String, String> pair : cpuInfoList) {

            addKeyValue(pair.first, pair.second);
            // Add a line or space after each key-value pair
            addLineOrSpace();
        }


    }

    private List<Pair<String, String>> formatCpuInfo(String cpuInfo) {
        // Split the original string into lines
        Holder = "";
        String[] lines = cpuInfo.split("\\r?\\n");

        // Create a List to store key-value pairs
        List<Pair<String, String>> cpuInfoList = new ArrayList<>();
        // Iterate through each line and format key-value pairs with a colon

        for (String line : lines) {
            if (line.contains(":")) {
                String[] parts = line.split(":", 2);
                String key = parts[0].trim();
                String value = parts[1].trim();
                cpuInfoList.add(new Pair<>(key, value));

            }
        }

        return cpuInfoList;
    }

    private void addKeyValue(String key, String value) {

        // Skip adding the key-value pair if it's "processor" with value "4" and it has already been added
        if (key.equalsIgnoreCase("processor") && value.equals("4") && processorInfoAdded) {
            return;
        }
        if (key.equalsIgnoreCase("BogoMIPS") && value.equals("3") && processorInfoAdded){
            return;
        }
        // If it's not the "processor" with value "4" or it hasn't been added yet, proceed to add it
        if (key.equalsIgnoreCase("processor") && value.equals("4")) {
            processorInfoAdded = true; // Mark that the processor information with value "4" has been added
        }
        // Add a darker line before each key-value pair representing processor information
        addDarkerLineIfProcessorInfo(key);

        LinearLayout keyValuePairLayout = new LinearLayout(requireContext());
        keyValuePairLayout.setOrientation(LinearLayout.HORIZONTAL);

        TextView keyTextView = new TextView(requireContext());
        Typeface typeface = ResourcesCompat.getFont(requireContext(),R.font.pmedium);

        keyTextView.setText(key);
        keyTextView.setTypeface(typeface);

        keyTextView.setTextSize(16);
        keyTextView.setTextColor(getResources().getColor(R.color.blue));
        keyTextView.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        keyTextView.setGravity(Gravity.CENTER_VERTICAL);
        keyTextView.setPadding(10,20,0,40);
        // Set margin at the start (left)


        TextView colonTextView = new TextView(requireContext());
        Typeface typeface1 = ResourcesCompat.getFont(requireContext(),R.font.pmedium);
        colonTextView.setText(":  ");
        colonTextView.setTextSize(16);
        colonTextView.setPadding(40,40,0,0);
        colonTextView.setTypeface(typeface1);
        colonTextView.setTextColor(getResources().getColor(R.color.blue));
        colonTextView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        colonTextView.setGravity(Gravity.CENTER);



        TextView valueTextView = new TextView(requireContext());
        Typeface typeface3 = ResourcesCompat.getFont(requireContext(),R.font.pmedium);
        valueTextView.setText(value);

        valueTextView.setTextSize(16);
        valueTextView.setTypeface(typeface3);
        valueTextView.setPadding(70,40,0,0);
//        valueTextView.setTextColor(Color.BLACK);
        valueTextView.setTextColor(getResources().getColor(R.color.blue));
        valueTextView.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));



        // Add TextViews to the key-value pair layout
        keyValuePairLayout.addView(keyTextView);
        keyValuePairLayout.addView(colonTextView);
        keyValuePairLayout.addView(valueTextView);


        // Add key-value pair layout to the main layout
        cpuInfoLayout.addView(keyValuePairLayout);

    }

    private void addLineOrSpace() {
        // Add a line or space after each key-value pair
        View lineView = new View(requireContext());
        lineView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1));
        lineView.setBackgroundColor(getResources().getColor(R.color.blue)); // Change color as needed
        cpuInfoLayout.addView(lineView);
        cpuInfoLayout.setPadding(60,20,60,10);

        // Alternatively, you can add a space:
        /*
        Space spaceView = new Space(requireContext());
        spaceView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 16)); // Adjust the height as needed
        cpuInfoLayout.addView(spaceView);
        */
    }


    private void addDarkerLineIfProcessorInfo(String key) {
        // Check if the key contains "processor" or any other condition specific to your use case
        if (key.toLowerCase().contains("processor")) {
            // Add a darker line after each key-value pair representing processor information
            View lineView = new View(requireContext());
            lineView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 3));
            lineView.setBackgroundColor(getResources().getColor(R.color.blue)); // Change color as needed
            cpuInfoLayout.addView(lineView);
        }
    }

}

