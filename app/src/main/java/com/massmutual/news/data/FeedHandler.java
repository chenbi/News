package com.massmutual.news.data;

import android.content.ContentValues;

import java.io.InputStream;
import java.util.ArrayList;

public abstract class FeedHandler {

    public abstract ArrayList<ContentValues> parse(InputStream in);
}
