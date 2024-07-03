package com.example1.cpu_x;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;

import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example1.cpu_x.ad.AdManager;

import com.example1.cpu_x.adapters.MyAdapter;


import com.facebook.ads.NativeAdLayout;
import com.google.android.material.tabs.TabLayout;


public class MainActivity extends AppCompatActivity {

String[] permissionsList = new String[]{Manifest.permission.ACCESS_NETWORK_STATE,
        Manifest.permission.INTERNET,Manifest.permission.READ_PHONE_STATE,Manifest.permission.MODIFY_PHONE_STATE};

String[] permissionsList13 = new String[]{Manifest.permission.ACCESS_NETWORK_STATE,
        Manifest.permission.INTERNET,Manifest.permission.READ_PHONE_STATE,Manifest.permission.MODIFY_PHONE_STATE};

    TabLayout tabLayout;
    ViewPager simpleViewPager;

    MyAdapter adapter;

    ProgressBar progress_bar_main;
    ImageView refresh;

    String[] tabs = {"CPU", "Device", "System", "Battery", "Sensor", "Internet"};
    private static final int NETWORK_PERMISSION_CODE = 100;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (!checkPermissions(this, permissionsList13)) {
                ActivityCompat.requestPermissions(MainActivity.this, permissionsList13, 222);
            }
        } else {
            if (!checkPermissions(this, permissionsList)) {
                ActivityCompat.requestPermissions(this, permissionsList, 21);
            }
        }










        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        // Set the status bar color to white
        window.setStatusBarColor(Color.WHITE);

        // Set system UI elements to dark color
        View decorView = window.getDecorView();
        int flags = decorView.getSystemUiVisibility();
        flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        decorView.setSystemUiVisibility(flags);




//        checkPermission(Manifest.permission.READ_PHONE_STATE, NETWORK_PERMISSION_CODE);
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//            Toast.makeText(this, "Please Allow the permission...", Toast.LENGTH_SHORT).show();
//            return;
//        }

        LinearLayout adContainer = findViewById(R.id.banner_container);
        if (AdManager.adType.equals("admob")){
            //admob
            AdManager.initAd(MainActivity.this);
            AdManager.loadBannerAd(MainActivity.this, adContainer);
            AdManager.loadInterAd(MainActivity.this);
        } else if (AdManager.adType.equals("max")){
            //MAX + Fb banner Ads
            AdManager.initMAX(MainActivity.this);
            AdManager.maxBanner(MainActivity.this, adContainer);
            AdManager.maxInterstital(MainActivity.this);

        }else{
            AdManager.initFBAds(MainActivity.this);
            AdManager.fbBanner(MainActivity.this,adContainer);
        }

        simpleViewPager = findViewById(R.id.viewpager_1);
        adapter = new MyAdapter(getSupportFragmentManager());
        simpleViewPager.setAdapter(adapter);

        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(simpleViewPager);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(getTabViewUn(i));


        }

        setupTabIcons();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                simpleViewPager.setCurrentItem(tab.getPosition());

                TabLayout.Tab tabs = tabLayout.getTabAt(tab.getPosition());
                tabs.setCustomView(null);
                tabs.setCustomView(getTabView(tab.getPosition()));
                AdManager.adCounter++;
                if (AdManager.adCounter == AdManager.adDisplayCounter) {
                    if (AdManager.adType.equals("admob")) {
                        AdManager.showInterAd(MainActivity.this, null, 0);
                    } else if(AdManager.adType.equals("max")){
                        AdManager.showMaxInterstitial(MainActivity.this, null, 0);
                    }else{
                        AdManager.showFBInterstitial(MainActivity.this, null,0);
                    }
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Handle unselected tab
                TabLayout.Tab tabs = tabLayout.getTabAt(tab.getPosition());
                tabs.setCustomView(null);
                tabs.setCustomView(getTabViewUn(tab.getPosition()));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Handle reselected tab
            }
        });

        progress_bar_main = findViewById(R.id.progress_bar_main);
        refresh = findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                adapter.notifyDataSetChanged();

                progress_bar_main.setVisibility(View.VISIBLE);
                simpleViewPager.setVisibility(View.GONE);

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        progress_bar_main.setVisibility(View.GONE);
                        simpleViewPager.setVisibility(View.VISIBLE);
//                        final MyAdapter adapter = new MyAdapter(getSupportFragmentManager());
//                        simpleViewPager.setAdapter(adapter);
                        AdManager.adCounter = AdManager.adDisplayCounter;
                        if (AdManager.adType.equals("admob")) {
                            AdManager.showInterAd(MainActivity.this, null, 0);
                        } else if (AdManager.adType.equals("max")){
                            AdManager.showMaxInterstitial(MainActivity.this, null, 0);
                        }else {
                            AdManager.showFBInterstitial(MainActivity.this, null, 0);
                        }
                    }
                },1000);
            }
        });
    }


    private void setupTabIcons() {
        View v = LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        ImageView img = v.findViewById(R.id.tab);
        img.setImageResource(R.drawable.custom_refresh_tab);
        TextView txt = v.findViewById(R.id.tab_txt);
        txt.setText(tabs[0]);
        txt.setTextColor(getResources().getColor(R.color.white));
        RelativeLayout.LayoutParams tabp = new RelativeLayout.LayoutParams(
                getResources().getDisplayMetrics().widthPixels * 200 / 1080,
                getResources().getDisplayMetrics().heightPixels * 84 / 1920);

        img.setLayoutParams(tabp);
        TabLayout.Tab tab = tabLayout.getTabAt(0);
        tab.setCustomView(null);
        tab.setCustomView(v);
    }

    public View getTabView(int position) {
        View v = LayoutInflater.from(MainActivity.this).inflate(R.layout.custom_tab, null);
        ImageView img = v.findViewById(R.id.tab);
        img.setImageResource(R.drawable.custom_refresh_tab);
        TextView txt = v.findViewById(R.id.tab_txt);
        txt.setText(tabs[position]);
        txt.setPadding(0,8,0,0);
        txt.setTextColor(getResources().getColor(R.color.white));
        RelativeLayout.LayoutParams tab = new RelativeLayout.LayoutParams(
                getResources().getDisplayMetrics().widthPixels * 200 / 1080,
                getResources().getDisplayMetrics().heightPixels * 84 / 1920);

        img.setLayoutParams(tab);

        return v;
    }


    public View getTabViewUn(int position) {
        View v = LayoutInflater.from(MainActivity.this).inflate(R.layout.custom_tab, null);
        ImageView img = v.findViewById(R.id.tab);
        img.setImageResource(R.drawable.unpress_tab);
        TextView txt = v.findViewById(R.id.tab_txt);
        txt.setText(tabs[position]);
        txt.setTextColor(getResources().getColor(R.color.blue));
        txt.setPadding(0,8,0,0);

        RelativeLayout.LayoutParams tab = new RelativeLayout.LayoutParams(
                getResources().getDisplayMetrics().widthPixels * 200 / 1080,
                getResources().getDisplayMetrics().heightPixels * 84 / 1920);
        img.setLayoutParams(tab);
        return v;
    }




    @Override
    public void onRequestPermissionsResult(int requestCode,
                                       String[] permissions,
                                            int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode,
                permissions,
                grantResults);

        if (requestCode == NETWORK_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this, "Network Permission Granted", Toast.LENGTH_SHORT) .show();
            }
            else {
                Toast.makeText(MainActivity.this, "Network Permission Denied", Toast.LENGTH_SHORT) .show();
            }
        }

    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        exitAlert();
    }

    void exitAlert() {
        final Dialog exitDialog = new Dialog(MainActivity.this);
        exitDialog.setContentView(R.layout.exit_popup_lay);

        exitDialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView noBtn = exitDialog.findViewById(R.id.noBtn);
        TextView yesBtn = exitDialog.findViewById(R.id.yesBtn);


        RelativeLayout containerNative = exitDialog.findViewById(R.id.nativeContainer);
        if (AdManager.adType.equals("admob")) {
            //admob
            AdManager.initAd(this);
            AdManager.loadNativeAd(this, containerNative);
        } else if (AdManager.adType.equals("max")){
            //MAX + Fb banner Ads
            AdManager.initMAX(this);
            AdManager.loadNativeMAX(this, containerNative);
        }else{
            AdManager.initFBAds(this);
            NativeAdLayout nativeAdLayout = exitDialog.findViewById(R.id.native_ad_container);
            AdManager.loadNativeFbAdPopup( nativeAdLayout, this);
        }

        // this is exit popup add
//        if (AdManager.adType.equals("admob")) {
//            //admob
//            RelativeLayout containerNative = exitDialog.findViewById(R.id.container);
//            AdManager.initAd(this);
//            AdManager.loadNativeAdmob(this,containerNative);
//        }else if (AdManager.adType.equals("max")){
//            RelativeLayout containerNative = exitDialog.findViewById(R.id.container);
//            AdManager.initMAX(this);
//            AdManager.loadNativeMAX(this,containerNative);
//        }else{
//            RelativeLayout containerNative = exitDialog.findViewById(R.id.container);
//            AdManager.initFBAds(this);
//            AdManager.loadNativeFbad(this);
//        }

        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                exitDialog.dismiss();
            }
        });

        yesBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                exitDialog.dismiss();
                MainActivity.super.onBackPressed();
            }
        });
        exitDialog.show();
    }

    public static boolean checkPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }





}