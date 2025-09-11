package com.qomg.webappcatalogs.network;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class NetworkUsage {
    public static void updateNetworkStatus(Context context) {
        boolean isConnected = NetworkUtils.isConnected(context);
        boolean isStable = NetworkUtils.isStable();
        long speed = NetworkUtils.getDownloadSpeed();

        String connectivityStatus = isConnected ? "Connected" : "Disconnected";
        String stabilityStatus = isStable ? "Stable" : "Unstable";
        String speedStatus = "Speed: " + speed + " KB/s";
        System.out.printf("connectivityStatus=%s,stabilityStatus=%s,speedStatus=%s%n", connectivityStatus, stabilityStatus, speedStatus);
    }

    public static void startNetworkMonitor(Context context) {
        Intent serviceIntent = new Intent(context, NetworkMonitorService.class);
        context.startService(serviceIntent);
    }

    public static void requireStoragePermission(Activity activity) {
        if (ContextCompat.checkSelfPermission(activity, WRITE_EXTERNAL_STORAGE) != PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{WRITE_EXTERNAL_STORAGE}, 1);
        }
    }
}
