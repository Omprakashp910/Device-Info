package com.example1.cpu_x.fragments;

import android.graphics.Typeface;
import android.os.Build;
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

public class SystemFragment extends Fragment {

    LinearLayout systemInfoLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_system, container, false);

        // Get reference to the LinearLayout in your layout
        systemInfoLayout = view.findViewById(R.id.system_info_layout);

        // Add key-value pairs for system information
        addKeyValue(systemInfoLayout, "Android Version", Build.VERSION.RELEASE);
        addLineOrSpace();
        addKeyValue(systemInfoLayout, "API Level", String.valueOf(Build.VERSION.SDK_INT));
        addLineOrSpace();
        addKeyValue(systemInfoLayout, "Kernel Architecture", System.getProperty("os.arch"));
        addLineOrSpace();
        addKeyValue(systemInfoLayout, "Build ID", Build.DISPLAY);
        addLineOrSpace();
        addKeyValue(systemInfoLayout, "Runtime Value", System.getProperty("java.vm.name"));
        addLineOrSpace();
        addKeyValue(systemInfoLayout, "Root Access", isRooted() ? "Yes" : "No");
        addLineOrSpace();

        return view;
    }

    // Helper method to add key-value pairs to the LinearLayout
    private void addKeyValue(LinearLayout layout, String key, String value) {
        LinearLayout keyValuePairLayout = new LinearLayout(requireContext());
        keyValuePairLayout.setOrientation(LinearLayout.HORIZONTAL);
        Typeface typeface = ResourcesCompat.getFont(requireContext(),R.font.pmedium);

        TextView keyTextView = new TextView(requireContext());
        keyTextView.setText(key);
        keyTextView.setTextSize(16);
        keyTextView.setTypeface(typeface);
//        keyTextView.setPadding(10,40,0,20);
        keyTextView.setPadding(10,20,0,40);
        keyTextView.setTextColor(getResources().getColor(R.color.blue));
        keyTextView.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        keyTextView.setGravity(Gravity.CENTER_VERTICAL);


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
        valueTextView.setTypeface(typeface3);
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
        systemInfoLayout.addView(lineView);
        systemInfoLayout.setPadding(60,20,60,0);

        // Alternatively, you can add a space:
        /*
        Space spaceView = new Space(requireContext());
        spaceView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 16)); // Adjust the height as needed
        cpuInfoLayout.addView(spaceView);
        */
    }

    // Helper method to check for root access
    private boolean isRooted() {
        try {
            Process process = Runtime.getRuntime().exec("su");
            process.getOutputStream().close();
            int exitCode = process.waitFor();
            return exitCode == 0;
        } catch (Exception e) {
            return false;
        }
    }
}
