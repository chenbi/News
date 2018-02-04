package com.massmutual.news.data;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;


import com.massmutual.news.utils.Network;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


public class NewsUdaterHelper {


    public static final String KEY_PAGE_NUM = "key_page_num";

    public static final String KEY_SEARCH_QUERY = "key_search_query";


    private static final String TAG = "NewsUdaterHelper";
    private static final boolean PROD = true;
    private static final String DEV_API_URL = "http://api.nytimes.com/svc/topstories/v1/home.json?api-key=40fe73c332235834f11a944d34ac05c9:18:74214315";

    private Context mContext;

    public NewsUdaterHelper(Context context) {
        mContext = context;
    }


    public void performRemoteUpdate(Bundle extras) throws IOException {
        String mediaURL;
        FeedHandler handler;
        InputStream is = null;
        Uri uri;
        String selection = null;
        String[] selectionArgs = null;
        ArrayList<ContentValues> values;

        mediaURL = DEV_API_URL;


        if (extras.getInt(KEY_PAGE_NUM) > 0) {
        mediaURL = mediaURL + "&page=" + extras.getInt(KEY_PAGE_NUM);
        }


        if (extras.getString(KEY_SEARCH_QUERY, "").length() > 0) {
        String query = extras.getString(KEY_SEARCH_QUERY);

        mediaURL = mediaURL + "&q=" + query;

        }

        uri = NewsContract.Clip.buildClipUri("");
        Log.v(TAG, "performUpdate(...)*********************" + uri.toString());
        handler = new NewsHandler.ArticleHandler();

        try {
            Log.v(TAG, "downloading stream from url: " + mediaURL  );
            is = Network.downloadStream(mediaURL);

            values = handler.parse(is);
            if (values != null) {
                final ContentResolver resolver = mContext.getContentResolver();

                int rowsDeleted = resolver.delete(uri, selection, selectionArgs);
                Log.v(TAG, "Deleted " + rowsDeleted + " rows");

                int rowsInserted = resolver.bulkInsert(uri, (ContentValues[]) values.toArray(new ContentValues[0]));
                Log.v(TAG, "Inserted " + rowsInserted + " rows");


            }

        } finally {
            if (is != null) {
                is.close();
            }

        }

    }




}