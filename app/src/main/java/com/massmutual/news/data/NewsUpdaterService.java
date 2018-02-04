package com.massmutual.news.data;

import android.content.Intent;
import android.os.Bundle;

import com.massmutual.news.services.NewsIntentService;

import java.io.IOException;



public class NewsUpdaterService extends NewsIntentService {

    private static final String TAG = "NewsUpdaterService";

    public NewsUpdaterService() {
        super("NewsService");
    }

    public NewsUpdaterService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        final Bundle extras = intent.getExtras();
        processPendingUpdate(extras);

        // TODO: check if more updates are in the queue, if not, send completion broadcast

    }

    private void processPendingUpdate(Bundle extras) {
        NewsUdaterHelper updateHelper = new NewsUdaterHelper(this);
        try {
            updateHelper.performRemoteUpdate(extras);
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }
    }
}
