package com.example1.cpu_x.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example1.cpu_x.fragments.AboutFragment;
import com.example1.cpu_x.fragments.BatteryFragment;
import com.example1.cpu_x.fragments.CPUFragment;
import com.example1.cpu_x.fragments.DeviceFragment;
import com.example1.cpu_x.fragments.SensorFragment;
import com.example1.cpu_x.fragments.SystemFragment;

public class MyAdapter extends FragmentPagerAdapter {

    public MyAdapter(@NonNull FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new CPUFragment();
            case 1:
                return new DeviceFragment();
            case 2:
                return new SystemFragment();
            case 3:
                return new BatteryFragment();
            case 4:
                return new SensorFragment();
            case 5:
                return new AboutFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 6;
    }
}
