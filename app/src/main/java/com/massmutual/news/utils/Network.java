package com.massmutual.news.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Map;


public class Network {

    public final static String TAG = "Network";


    /**
     * Checks if device is connected to a network
     * 
     * @param context
     * @return
     */
    public static boolean isConnected(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Given a ConnectivityManager connection type, return whether it is connected
     * 
     * @param context
     * @param connectionType
     * @return
     */
    public static boolean isConnectionType(Context context, int connectionType) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getNetworkInfo(connectionType);
        return (networkInfo != null && networkInfo.isConnected());
    }

    /**
     * Given a string representation of a URL, sets up a connection and gets an input stream.
     * 
     * @param urlString
     * @return
     * @throws SocketTimeoutException
     * @throws IOException
     */
    public static InputStream downloadStream(String urlString) throws SocketTimeoutException, IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(15000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);

        // Starts the query
        conn.connect();

        // Get response code
        int response = conn.getResponseCode();



        return conn.getInputStream();
    }

    public static InputStream downloadStream(String urlString, Map<String, String> headers ) throws SocketTimeoutException, IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(15000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        for(String header:headers.keySet()){
            conn.addRequestProperty(header, headers.get(header));
        }

        // Starts the query
        conn.connect();

        // Get response code
        int response = conn.getResponseCode();



        return conn.getInputStream();
    }

    /**
     * Given a string representation of a URL that redirects, returns the redirect URL
     * 
     * @param urlString
     * @return
     * @throws SocketTimeoutException
     * @throws IOException
     */
    public static String getRedirectUrl(String urlString) throws SocketTimeoutException, IOException {

        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(15000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setInstanceFollowRedirects(false);
        return conn.getHeaderField("Location");
    }

}
