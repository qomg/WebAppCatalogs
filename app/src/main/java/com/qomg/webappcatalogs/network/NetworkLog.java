package com.qomg.webappcatalogs.network;

import android.os.Environment;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NetworkLog {
    public static void logError(String message) {
        String logMessage = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()) + ": " + message + "\n";
        try {
            FileOutputStream fos = new FileOutputStream(Environment.getExternalStorageDirectory() + "/network_log.txt", true);
            fos.write(logMessage.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
