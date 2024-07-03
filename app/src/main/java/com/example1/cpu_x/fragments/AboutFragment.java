package com.example1.cpu_x.fragments;


import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;

import com.example1.cpu_x.MainActivity;
import com.example1.cpu_x.R;
import com.example1.cpu_x.ad.AdManager;
import com.example1.cpu_x.internet_speed.GetSpeedTestHostsHandler;
import com.example1.cpu_x.internet_speed.test.HttpDownloadTest;
import com.example1.cpu_x.internet_speed.test.HttpUploadTest;
import com.example1.cpu_x.internet_speed.test.PingTest;
import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;


public class AboutFragment extends Fragment {

    static int position = 0;
    static int lastPosition = 0;
    GetSpeedTestHostsHandler getSpeedTestHostsHandler = null;

    HashSet<String> tempBlackList;

    @Override
    public void onResume() {
        super.onResume();

        getSpeedTestHostsHandler = new GetSpeedTestHostsHandler();
        getSpeedTestHostsHandler.start();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        final Button startButton = view.findViewById(R.id.startButton);

        final DecimalFormat dec = new DecimalFormat("#.##");
        startButton.setText("Begin Test");

        tempBlackList = new HashSet<>();

        getSpeedTestHostsHandler = new GetSpeedTestHostsHandler();
        getSpeedTestHostsHandler.start();

        startButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startButton.setEnabled(false);

//                isNetworkAvailable(getContext());

                //Restart test icin eger baglanti koparsa
                if (getSpeedTestHostsHandler == null) {
                    getSpeedTestHostsHandler = new GetSpeedTestHostsHandler();
                    getSpeedTestHostsHandler.start();
                }

                new Thread(new Runnable() {
                    RotateAnimation rotate;
                    ImageView barImageView = view.findViewById(R.id.barImageView);
                    TextView pingTextView = view.findViewById(R.id.pingTextView);
                    TextView downloadTextView = view.findViewById(R.id.downloadTextView);
                    TextView uploadTextView = view.findViewById(R.id.uploadTextView);

                    @Override
                    public void run() {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                startButton.setText("Loading...");
                            }
                        });

                        //Get egcodes.speedtest hosts
                        int timeCount = 60; //1min
                        while (!getSpeedTestHostsHandler.isFinished()) {
                            timeCount--;
                            try {
                                Thread.sleep(10);
                            } catch (InterruptedException e) {
                            }
                            if (timeCount <= 0) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
//                                        Toast.makeText(getContext(), "No Connection...", Toast.LENGTH_LONG).show();
                                        if(isNetworkAvailable(getContext())== false){
                                            startButton.setEnabled(true);
                                            startButton.setTextSize(16);
                                            startButton.setText("Begin Test");
                                        }else{
                                            startButton.setEnabled(true);
                                            startButton.setTextSize(16);
                                            startButton.setText("Restart Test");
                                        }

                                    }
                                });
                                getSpeedTestHostsHandler = null;
                                return;
                            }
                        }

                        //Find closest server
                        HashMap<Integer, String> mapKey = getSpeedTestHostsHandler.getMapKey();
                        HashMap<Integer, List<String>> mapValue = getSpeedTestHostsHandler.getMapValue();
                        double selfLat = getSpeedTestHostsHandler.getSelfLat();
                        double selfLon = getSpeedTestHostsHandler.getSelfLon();
                        double tmp = 19349458;
                        double dist = 0.0;
                        int findServerIndex = 0;
                        for (int index : mapKey.keySet()) {
                            if (tempBlackList.contains(mapValue.get(index).get(5))) {
                                continue;
                            }

                            Location source = new Location("Source");
                            source.setLatitude(selfLat);
                            source.setLongitude(selfLon);

                            List<String> ls = mapValue.get(index);
                            Location dest = new Location("Dest");
                            dest.setLatitude(Double.parseDouble(ls.get(0)));
                            dest.setLongitude(Double.parseDouble(ls.get(1)));

                            double distance = source.distanceTo(dest);
                            if (tmp > distance) {
                                tmp = distance;
                                dist = distance;
                                findServerIndex = index;
                            }
                        }
                        String testAddr = mapKey.get(findServerIndex).replace("http://", "https://");
                        final List<String> info = mapValue.get(findServerIndex);
                        final double distance = dist;

                        if (info == null) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    startButton.setTextSize(12);
                                    startButton.setText("There was a problem in getting Host Location. Try again later.");
                                }
                            });
                            return;
                        }

//                        getActivity().runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                startButton.setTextSize(13);
//                                startButton.setText(String.format("Host Location: %s [Distance: %s km]", info.get(2), new DecimalFormat("#.##").format(distance / 1000)));
//                            }
//                        });

//                        Init Ping graphic
                        final LinearLayout chartPing = view.findViewById(R.id.chartPing);
                        XYSeriesRenderer pingRenderer = new XYSeriesRenderer();
                        XYSeriesRenderer.FillOutsideLine pingFill = new XYSeriesRenderer.FillOutsideLine(XYSeriesRenderer.FillOutsideLine.Type.BOUNDS_ALL);
                        pingFill.setColor(Color.parseColor("#4d5a6a"));
                        pingRenderer.addFillOutsideLine(pingFill);
                        pingRenderer.setDisplayChartValues(false);
                        pingRenderer.setShowLegendItem(false);
                        pingRenderer.setColor(Color.parseColor("#4d5a6a"));
                        pingRenderer.setLineWidth(5);
                        final XYMultipleSeriesRenderer multiPingRenderer = new XYMultipleSeriesRenderer();
                        multiPingRenderer.setXLabels(0);
                        multiPingRenderer.setYLabels(0);
                        multiPingRenderer.setZoomEnabled(false);
                        multiPingRenderer.setXAxisColor(Color.parseColor("#647488"));
                        multiPingRenderer.setYAxisColor(Color.parseColor("#2F3C4C"));
                        multiPingRenderer.setPanEnabled(true, true);
                        multiPingRenderer.setZoomButtonsVisible(false);
                        multiPingRenderer.setMarginsColor(Color.argb(0x00, 0xff, 0x00, 0x00));
                        multiPingRenderer.addSeriesRenderer(pingRenderer);

                        //Init Download graphic
                        final LinearLayout chartDownload = view.findViewById(R.id.chartDownload);
                        XYSeriesRenderer downloadRenderer = new XYSeriesRenderer();
                        XYSeriesRenderer.FillOutsideLine downloadFill = new XYSeriesRenderer.FillOutsideLine(XYSeriesRenderer.FillOutsideLine.Type.BOUNDS_ALL);
                        downloadFill.setColor(Color.parseColor("#4d5a6a"));
                        downloadRenderer.addFillOutsideLine(downloadFill);
                        downloadRenderer.setDisplayChartValues(false);
                        downloadRenderer.setColor(Color.parseColor("#4d5a6a"));
                        downloadRenderer.setShowLegendItem(false);
                        downloadRenderer.setLineWidth(5);
                        final XYMultipleSeriesRenderer multiDownloadRenderer = new XYMultipleSeriesRenderer();
                        multiDownloadRenderer.setXLabels(0);
                        multiDownloadRenderer.setYLabels(0);
                        multiDownloadRenderer.setZoomEnabled(false);
                        multiDownloadRenderer.setXAxisColor(Color.parseColor("#647488"));
                        multiDownloadRenderer.setYAxisColor(Color.parseColor("#2F3C4C"));
                        multiDownloadRenderer.setPanEnabled(false, false);
                        multiDownloadRenderer.setZoomButtonsVisible(false);
                        multiDownloadRenderer.setMarginsColor(Color.argb(0x00, 0xff, 0x00, 0x00));
                        multiDownloadRenderer.addSeriesRenderer(downloadRenderer);

                        //Init Upload graphic
                        final LinearLayout chartUpload = view.findViewById(R.id.chartUpload);
                        XYSeriesRenderer uploadRenderer = new XYSeriesRenderer();
                        XYSeriesRenderer.FillOutsideLine uploadFill = new XYSeriesRenderer.FillOutsideLine(XYSeriesRenderer.FillOutsideLine.Type.BOUNDS_ALL);
                        uploadFill.setColor(Color.parseColor("#4d5a6a"));
                        uploadRenderer.addFillOutsideLine(uploadFill);
                        uploadRenderer.setDisplayChartValues(false);
                        uploadRenderer.setColor(Color.parseColor("#4d5a6a"));
                        uploadRenderer.setShowLegendItem(false);
                        uploadRenderer.setLineWidth(5);
                        final XYMultipleSeriesRenderer multiUploadRenderer = new XYMultipleSeriesRenderer();
                        multiUploadRenderer.setXLabels(0);
                        multiUploadRenderer.setYLabels(0);
                        multiUploadRenderer.setZoomEnabled(false);
                        multiUploadRenderer.setXAxisColor(Color.parseColor("#647488"));
                        multiUploadRenderer.setYAxisColor(Color.parseColor("#2F3C4C"));
                        multiUploadRenderer.setPanEnabled(false, false);
                        multiUploadRenderer.setZoomButtonsVisible(false);
                        multiUploadRenderer.setMarginsColor(Color.argb(0x00, 0xff, 0x00, 0x00));
                        multiUploadRenderer.addSeriesRenderer(uploadRenderer);

                        //Reset value, graphics
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pingTextView.setText("0 ms");
                                chartPing.removeAllViews();
                                downloadTextView.setText("0 Mbps");
                                chartDownload.removeAllViews();
                                uploadTextView.setText("0 Mbps");
                                chartUpload.removeAllViews();
                            }
                        });
                        final List<Double> pingRateList = new ArrayList<>();
                        final List<Double> downloadRateList = new ArrayList<>();
                        final List<Double> uploadRateList = new ArrayList<>();
                        Boolean pingTestStarted = false;
                        Boolean pingTestFinished = false;
                        Boolean downloadTestStarted = false;
                        Boolean downloadTestFinished = false;
                        Boolean uploadTestStarted = false;
                        Boolean uploadTestFinished = false;

                        //Init Test
                        final PingTest pingTest = new PingTest(info.get(6).replace(":8080", ""), 3);
                        final HttpDownloadTest downloadTest = new HttpDownloadTest(testAddr.replace(testAddr.split("/")[testAddr.split("/").length - 1], ""));
                        final HttpUploadTest uploadTest = new HttpUploadTest(testAddr);


                        //Tests
                        while (true) {
                            if (!pingTestStarted) {
                                pingTest.start();
                                pingTestStarted = true;
                            }
                            if (pingTestFinished && !downloadTestStarted) {
                                downloadTest.start();
                                downloadTestStarted = true;
                            }
                            if (downloadTestFinished && !uploadTestStarted) {
                                uploadTest.start();
                                uploadTestStarted = true;
                            }


                            //Ping Test
                            if (pingTestFinished) {
                                //Failure
                                if (pingTest.getAvgRtt() == 0) {
                                    System.out.println("Ping error...");
                                } else {
                                    //Success
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            pingTextView.setText(dec.format(pingTest.getAvgRtt()) + " ms");
                                        }
                                    });
                                }
                            } else {
                                pingRateList.add(pingTest.getInstantRtt());

                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        pingTextView.setText(dec.format(pingTest.getInstantRtt()) + " ms");
                                    }
                                });

                                //Update chart
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        // Creating an  XYSeries for Income
                                        XYSeries pingSeries = new XYSeries("");
                                        pingSeries.setTitle("");

                                        int count = 0;
                                        List<Double> tmpLs = new ArrayList<>(pingRateList);
                                        for (Double val : tmpLs) {
                                            pingSeries.add(count++, val);
                                        }

                                        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
                                        dataset.addSeries(pingSeries);

                                        GraphicalView chartView = ChartFactory.getLineChartView(getContext(), dataset, multiPingRenderer);
                                        chartPing.addView(chartView, 0);

                                    }
                                });
                            }


                            //Download Test
                            if (pingTestFinished) {
                                if (downloadTestFinished) {
                                    //Failure
                                    if (downloadTest.getFinalDownloadRate() == 0) {
                                        System.out.println("Download error...");
                                    } else {
                                        //Success
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                downloadTextView.setText(dec.format(downloadTest.getFinalDownloadRate()) + " Mbps");
                                            }
                                        });
                                    }
                                } else {
                                    //Calc position
                                    double downloadRate = downloadTest.getInstantDownloadRate();
                                    downloadRateList.add(downloadRate);
                                    position = getPositionByRate(downloadRate);

                                    getActivity().runOnUiThread(new Runnable() {

                                        @Override
                                        public void run() {
                                            rotate = new RotateAnimation(lastPosition, position, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                                            rotate.setInterpolator(new LinearInterpolator());
                                            rotate.setDuration(100);
                                            barImageView.startAnimation(rotate);
                                            downloadTextView.setText(dec.format(downloadTest.getInstantDownloadRate()) + " Mbps");

                                        }

                                    });
                                    lastPosition = position;

                                    //Update chart
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            // Creating an  XYSeries for Income
                                            XYSeries downloadSeries = new XYSeries("");
                                            downloadSeries.setTitle("");

                                            List<Double> tmpLs = new ArrayList<>(downloadRateList);
                                            int count = 0;
                                            for (Double val : tmpLs) {
                                                downloadSeries.add(count++, val);
                                            }

                                            XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
                                            dataset.addSeries(downloadSeries);

                                            GraphicalView chartView = ChartFactory.getLineChartView(getContext(), dataset, multiDownloadRenderer);
                                            chartDownload.addView(chartView, 0);
                                        }
                                    });

                                }
                            }


                            //Upload Test
                            if (downloadTestFinished) {
                                if (uploadTestFinished) {
                                    //Failure
                                    if (uploadTest.getFinalUploadRate() == 0) {
                                        System.out.println("Upload error...");
                                    } else {
                                        //Success
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                uploadTextView.setText(dec.format(uploadTest.getFinalUploadRate()) + " Mbps");
                                            }
                                        });
                                    }
                                } else {
                                    //Calc position
                                    double uploadRate = uploadTest.getInstantUploadRate();
                                    uploadRateList.add(uploadRate);
                                    position = getPositionByRate(uploadRate);

                                    getActivity().runOnUiThread(new Runnable() {

                                        @Override
                                        public void run() {
                                            rotate = new RotateAnimation(lastPosition, position, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                                            rotate.setInterpolator(new LinearInterpolator());
                                            rotate.setDuration(100);
                                            barImageView.startAnimation(rotate);
                                            uploadTextView.setText(dec.format(uploadTest.getInstantUploadRate()) + " Mbps");
                                        }

                                    });
                                    lastPosition = position;

                                    //Update chart
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            // Creating an  XYSeries for Income
                                            XYSeries uploadSeries = new XYSeries("");
                                            uploadSeries.setTitle("");

                                            int count = 0;
                                            List<Double> tmpLs = new ArrayList<>(uploadRateList);
                                            for (Double val : tmpLs) {
                                                if (count == 0) {
                                                    val = 0.0;
                                                }
                                                uploadSeries.add(count++, val);
                                            }

                                            XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
                                            dataset.addSeries(uploadSeries);

                                            GraphicalView chartView = ChartFactory.getLineChartView(getContext(), dataset, multiUploadRenderer);
                                            chartUpload.addView(chartView, 0);
                                        }
                                    });

                                }
                            }

                            //Test bitti
                            if (pingTestFinished && downloadTestFinished && uploadTest.isFinished()) {
                                break;
                            }

                            if (pingTest.isFinished()) {
                                pingTestFinished = true;
                            }
                            if (downloadTest.isFinished()) {
                                downloadTestFinished = true;
                            }
                            if (uploadTest.isFinished()) {
                                uploadTestFinished = true;
                            }

                            if (pingTestStarted && !pingTestFinished) {
                                try {
                                    Thread.sleep(300);
                                } catch (InterruptedException e) {
                                }
                            } else {
                                try {
                                    Thread.sleep(100);
                                } catch (InterruptedException e) {
                                }
                            }
                        }

                        //Thread bitiminde button yeniden aktif ediliyor
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // adds after the speed tested
                                AdManager.adCounter = AdManager.adDisplayCounter;
                                if (AdManager.adType.equals("admob")) {
                                    AdManager.showInterAd(getActivity(), null, 0);
                                } else if (AdManager.adType.equals("max")){
                                    AdManager.showMaxInterstitial(getActivity(), null, 0);
                                }else{
                                    AdManager.showFBInterstitial(getActivity(), null, 0);
                                }
                                startButton.setEnabled(true);
                                startButton.setTextSize(16);
                                startButton.setText("Restart Test");
                            }
                        });


                    }
                }).start();
            }
        });

        return view;
    }


//    public int getPositionByRate(double rate) {
//        if (rate <= 1) {
//            return (int) (rate * 30);
//
//        } else if (rate <= 10) {
//            return (int) (rate * 6) + 30;
//
//        } else if (rate <= 30) {
//            return (int) ((rate - 5) * 3) + 90; //10
//
//        } else if (rate <= 50) {
//            return (int) ((rate - 20) * 1.5) + 150;
//
//        } else if (rate <= 100) {
//            return (int) ((rate - 35) * 1.2) + 180;
//        }
//
//        return 0;
//    }

    public int getPositionByRate(double rate) {
        if (rate <= 1) {
            return (int) (rate * 30);

        } else if (rate <= 10) {
            return (int) (rate * 6) + 30;

        } else if (rate <= 30) {
            return (int) ((rate - 10) * 3) + 90;

        } else if (rate <= 50) {
            return (int) ((rate - 30) * 1.5) + 150;

        } else if (rate <= 100) {
            return (int) ((rate - 50) * 1.2) + 180;
        }

        return 0;
    }


    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        // For API 29 or above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
            if (capabilities != null) {
                return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR);
            }
            else{
                Toast.makeText(context.getApplicationContext(), "Check your internet connection",Toast.LENGTH_SHORT).show();
            }
        } else {
            // For below API 29
            if (connectivityManager.getActiveNetworkInfo() != null &&
                    connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting()) {
                return true;
            }
            else {
                Toast.makeText(context.getApplicationContext(), "Check your internet connection",Toast.LENGTH_SHORT).show();
            }
        }

        return false;
    }
}

//        tv_downloading_speed = view.findViewById(R.id.tv_downloading_speed);
//        tv_uploading_speed = view.findViewById(R.id.tv_uploading_speed);
//        btn_check_speed = view.findViewById(R.id.btn_check_speed);
//
//        btn_check_speed.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                checkNetworkSpeed();
//            }
//        });


//    private void checkNetworkSpeed() {
//        ConnectivityManager connectivityManager =
//                (ConnectivityManager) requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
//
//        if (connectivityManager != null) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                Network network = connectivityManager.getActiveNetwork();
//                NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(network);
//
//                if (networkCapabilities != null) {
//                    int downloadSpeed = networkCapabilities.getLinkDownstreamBandwidthKbps();
//                    int uploadSpeed = networkCapabilities.getLinkUpstreamBandwidthKbps();
//
//                    displaySpeed(downloadSpeed, uploadSpeed);
//                }
//            } else {
//                // For devices below Android M, you may use a third-party library or use approximation.
//                // Here, we are simply showing a message.
//                tv_downloading_speed.setText("Not supported on this device");
//                tv_uploading_speed.setText("Not supported on this device");
//            }
//        }
//    }

//    private void displaySpeed(int downloadSpeed, int uploadSpeed) {
//        new Handler(Looper.getMainLooper()).post(new Runnable() {
//            @Override
//            public void run() {
//                tv_downloading_speed.setText("Downloading Speed: " + downloadSpeed/1024 + " MB/s");
//                tv_uploading_speed.setText("Uploading Speed: " + uploadSpeed/1024 + " MB/s");
//            }
//        });
//    }

