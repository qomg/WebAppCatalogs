package com.qomg.webappcatalogs.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NetworkUtils {
    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkCapabilities capabilities = cm.getNetworkCapabilities(cm.getActiveNetwork());
        if (capabilities == null) {
            return false;
        }
        return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET);
    }
    public static boolean isStable() {
        try {
            Process process = Runtime.getRuntime().exec("/system/bin/ping -c 4 8.8.8.8");
            int returnVal = process.waitFor();
            return (returnVal == 0);
        } catch (Exception e) {
            return false;
        }
    }

    // TODO 耗时
    public static long getDownloadSpeed() {
        // 实现下载逻辑并计算速度
        // 这里可以使用OkHttp或其他网络库进行下载
        // get https://httpbin.org/bytes/1024
        OkHttpClient client = new OkHttpClient.Builder().build();
        try {
            long start = System.currentTimeMillis();
            Request request = new Request.Builder().url("https://httpbin.org/bytes/1024").build();
            Response response = client.newCall(request).execute();
            response.close();
            float seconds = (System.currentTimeMillis() - start) / 1000f;
            return (long) (1 / seconds); // KB/s
        } catch (IOException e) {
            return 0;
        }
    }
}
