package com.general.mediaplayer.TwoInOne;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import com.general.mediaplayer.TwoInOne.DroidPHP.ServerService;
import com.general.mediaplayer.TwoInOne.DroidPHP.ServerUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

//tethering
import android.net.wifi.*;

public class ScanMediaActivity extends BaseActivity {

    // related with HTTP server
    final static int PROJECT_CODE = 143;
    final static int DEV_CODE = PROJECT_CODE + 1;
    final static int ABOUT_US_CODE = DEV_CODE + 1;
    final static int SETTING_CODE = ABOUT_US_CODE + 1;
    final static int SHELL_CODE = SETTING_CODE + 1;
    public static HashMap<String, String> server;
    private SharedPreferences prefs;
    private Context mContext;

    private static boolean isStarted = false;

    // for tethering
    private WifiManager mWifiManager;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        final ImageButton btnStart = (ImageButton) findViewById(R.id.btnStart);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScanMediaActivity.this, PriceActivity.class);
                startActivity(intent);
                overridePendingTransition(TransformManager.GetContinueInAnim(), TransformManager.GetContinueOutAnim());
                finish();
            }
        });

        // set resolution to Resolution Manager

        int width = 0, height = 0;
        DisplayMetrics metrics = new DisplayMetrics();
        Display display = getWindowManager().getDefaultDisplay();
        Method mGetRawH = null, mGetRawW = null;

        try {
            // For JellyBean 4.2 (API 17) and onward
            /*if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
                display.getRealMetrics(metrics);

                width = metrics.widthPixels;
                height = metrics.heightPixels;
            } else { */
                mGetRawH = Display.class.getMethod("getRawHeight");
                mGetRawW = Display.class.getMethod("getRawWidth");

                try {
                    width = (Integer) mGetRawW.invoke(display);
                    height = (Integer) mGetRawH.invoke(display);
                } catch (IllegalArgumentException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            //}
        } catch (NoSuchMethodException e3) {
            e3.printStackTrace();
        }

        if (width != 0 || height != 0)
        {
            if (width > height)
                ResolutionSet._instance.setResolution(width, height);
            else
                ResolutionSet._instance.setResolution(height, width);
        }
        else
        {
            WindowManager winManager = (WindowManager) getSystemService(WINDOW_SERVICE);
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int nWidth = metrics.widthPixels, nHeight = metrics.heightPixels;
            if (nWidth > nHeight)
                ResolutionSet._instance.setResolution(nWidth, nHeight);
            else
                ResolutionSet._instance.setResolution(nHeight, nWidth);
        }

        // set default data
        _appPrefs.setSelectedPrice(CommonData.getDefaultSelectedPrice());
        _appPrefs.setSelectedBasic(CommonData.getDefaultSelectedBasic());
        _appPrefs.setSelectedAdvanced(CommonData.getDefaultSelectedAdvanced());

        ResolutionSet._instance.iterateChild(findViewById(R.id.layoutStart));

        // related with HTTP server

        if (CommonData.START_SERVER == 1)
        {
            mContext = ScanMediaActivity.this;

            ServerUtils.setContext(mContext);
            prefs = PreferenceManager.getDefaultSharedPreferences(this);

            if (ScanMediaActivity.isStarted == false)
            {
                ServerUtils.StrictModePermitAll();
                ServerUtils.setHttpDocsUri(prefs.getString("k_docs_dir", "htdocs"));
                ServerUtils.setServerPort(prefs.getString("k_server_port", "8080"));

                ScanMediaActivity.isStarted = true;
            }
        }

        if (CommonData.START_TETHERING == 1)
        {
            this.mWifiManager = ((WifiManager)getSystemService("wifi"));
            int i = mWifiManager.getWifiState();
            //if (i == 2 || i == 3)
            {
                mWifiManager.setWifiEnabled(true);
            }
            Method[] wifiMethods = mWifiManager.getClass().getDeclaredMethods();
            for(Method method: wifiMethods)
            {
                String name = method.getName();
                if (name.equals("setWifiApEnabled"))
                {
                    try {
                        method.invoke(mWifiManager, null, true);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                }
            }
            ;

            ConnectivityManager localConnectivityManager = (ConnectivityManager)getSystemService("connectivity");
            //String[] mUsbRegexs = localConnectivityManager.;
            //Preference mCreateNetwork = findPreference("wifi_ap_ssid_and_security");
            String[] available = null;
            Method[] wMethods = localConnectivityManager.getClass().getDeclaredMethods();
            for(Method method: wMethods) {
                String name = method.getName();
                if (name.equals("setUsbTethering"))
                {
                    Object ret = null;
                    try {
                        ret = method.invoke(localConnectivityManager, true);

                    } catch (IllegalAccessException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                }       /*
                else
                    if (name.equals("tether")) {
                        try {
                            method.invoke(localConnectivityManager, "usb0");
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        }
                    }  */

            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        if (CommonData.START_SERVER == 1)
        {
            try{
                if (false == ServerUtils.checkIfInstalled()) {
                    new InstallerAsync(this).execute();
                } else {
                    if (isServerRunning() == false) {
                        programInstalled();
                    }
                }
            }catch (IOException e) {
                //
            }
        }


    }

    final protected boolean isServerRunning() throws IOException {
        InputStream is;
        java.io.BufferedReader bf;
        boolean isRunning = false;
        try {
            is = Runtime.getRuntime().exec("ps").getInputStream();
            bf = new java.io.BufferedReader(new java.io.InputStreamReader(is));

            String r;
            while ((r = bf.readLine()) != null) {
                if (r.contains("lighttpd")) {
                    isRunning = true;
                    break;
                }
            }
            is.close();
            bf.close();

        } catch (IOException e) {
            e.printStackTrace();

        }
        return isRunning;
    }

    public void programInstalled() {
        // start service

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ServerUtils.runServer();
                String msg = "Unable to Start Server";
                try {
                    if (isServerRunning()) {
                        msg = "Server successfully Started";
                        _appPrefs.setServerStarted(true);
                    }
                } catch (IOException e) {

                }
                android.widget.Toast.makeText(mContext, msg,
                        android.widget.Toast.LENGTH_LONG).show();

                Intent i = new Intent(mContext, ServerService.class);

                i.putExtra(ServerService.EXTRA_PORT,
                        prefs.getString("k_server_port", "8080"));

                startService(i);
                _appPrefs.setServerStarted(true);
            }
        });
    }

    private class InstallerAsync extends
            android.os.AsyncTask<Void, String, Void> {

        ScanMediaActivity parentActivity;
        String loc;

        InstallerAsync(ScanMediaActivity parent) {
            parentActivity = parent;
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            loc = ServerUtils.getAppDirectory() + "/";
            try {

                dirChecker("");
                ZipInputStream zin = new ZipInputStream(getAssets().open(
                        "data.zip"));
                ZipEntry ze = null;

                while ((ze = zin.getNextEntry()) != null) {

                    if (ze.isDirectory()) {
                        dirChecker(ze.getName());
                    } else {
                        FileOutputStream fout = new FileOutputStream(loc
                                + ze.getName());

                        publishProgress("Extracting : " + ze.getName());

                        byte[] buffer = new byte[4096 * 10];
                        int length = 0;
                        while ((length = zin.read(buffer)) != -1) {

                            fout.write(buffer, 0, length);

                        }

                        zin.closeEntry();
                        fout.close();
                    }

                }
                publishProgress("ok");

                zin.close();

            } catch (java.lang.Exception e) {
                publishProgress("error");
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);

            String text = "Error";
            //tv_install_exec.setVisibility(1);
            if (values[0] == "error")
                text = getString(R.string.bin_error);
            if (values[0] == "ok")
                text = getString(R.string.bin_installed);
            else
                text = values[0];

            //tv_install_exec.setText(text);
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            parentActivity.programInstalled();
        }

        private void dirChecker(String dir) {
            File f = new File(loc + dir);

            if (!f.isDirectory()) {
                f.mkdirs();
            }
        }

    }
}
