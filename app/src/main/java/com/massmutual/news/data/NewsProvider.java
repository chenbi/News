/*
 * Copyright 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * Modifications:
 * -Imported from iosched com.google.android.apps.iosched.provider.ScheduleProvider
 * https://code.google.com/p/iosched/source/browse/android/src/main/java/com/google/android/apps/iosched/provider/ScheduleProvider.java
 * -Based NewsProvider off of ScheduleProvider
 */

package com.massmutual.news.data;

import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

import com.massmutual.news.data.NewsContract.Clip;
import com.massmutual.news.utils.SelectionBuilder;

public class NewsProvider extends ContentProvider {

    private static final String TAG = "NewsProvider";
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static final int CLIPS = 200;
    private static final int CLIP_ID = 201;


    private static NewsDatabase mDbOpenHelper;

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = NewsContract.AUTHORITY;


        matcher.addURI(authority, Clip.TABLE_NAME, CLIPS);

        matcher.addURI(authority, Clip.TABLE_NAME + "/*", CLIP_ID);


        return matcher;
    }

    @Override
    public boolean onCreate() {
        mDbOpenHelper = NewsDatabase.getInstance(getContext());
        return true;
    }

    private SelectionBuilder buildSimpleSelection(Uri uri) {

        final SelectionBuilder builder = new SelectionBuilder();
        final int match = sUriMatcher.match(uri);
        switch (match) {

            case CLIPS:
            case CLIP_ID:
                return builder.table(Clip.TABLE_NAME);


            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Log.v(TAG, "query(uri=" + uri + ", proj=" + Arrays.toString(projection) + ")");
        final SQLiteDatabase db = mDbOpenHelper.getReadableDatabase();

        final SelectionBuilder builder = buildSimpleSelection(uri);

        final int match = sUriMatcher.match(uri);
        Cursor c;

        c = builder.where(selection, selectionArgs).query(db, projection, sortOrder);

        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public String getType(Uri uri) {
        return "vnd.android.cursor.dir/vnd." + NewsContract.AUTHORITY;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.v(TAG, "insert(uri=" + uri + ", values=" + values.toString() + ")");
        final SQLiteDatabase db = mDbOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {

            case CLIPS:
            case CLIP_ID:
                db.insertOrThrow(Clip.TABLE_NAME, null, values);
                notifyChange(uri);
                return Clip.buildClipUri(values.getAsString(Clip.CLIP_ID));

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        Log.v(TAG, "bulkInsert(uri=" + uri + ", values=" + Arrays.toString(values) + ")");
        final SQLiteDatabase db = mDbOpenHelper.getWritableDatabase();
        int numInserted = 0;
        final String tableName;
        final int match = sUriMatcher.match(uri);
        switch (match) {

            case CLIPS:
            case CLIP_ID:
                tableName = Clip.TABLE_NAME;
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        db.beginTransaction();
        try {
            for (ContentValues v : values) {
                db.insertWithOnConflict(tableName, null, v, SQLiteDatabase.CONFLICT_IGNORE);
//                db.insertOrThrow(tableName, null, v);
            }
            db.setTransactionSuccessful();
            numInserted = values.length;
            notifyChange(uri);
        } finally {
            db.endTransaction();
        }

        return numInserted;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.v(TAG, "delete(uri=" + uri + ")");
        final SQLiteDatabase db = mDbOpenHelper.getWritableDatabase();
        final SelectionBuilder builder = buildSimpleSelection(uri);
        int returnVal = builder.where(selection, selectionArgs).delete(db);
        notifyChange(uri);
        return returnVal;
    }


    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        Log.v(TAG, "update(uri=" + uri + ", values=" + values.toString() + ")");
        final SQLiteDatabase db = mDbOpenHelper.getWritableDatabase();
        final SelectionBuilder builder = buildSimpleSelection(uri);
        int returnVal = builder.where(selection, selectionArgs).update(db, values);
        notifyChange(uri);
        return returnVal;
    }


    @Override
    public ContentProviderResult[] applyBatch(ArrayList<ContentProviderOperation> operations)
            throws OperationApplicationException {
        final SQLiteDatabase db = mDbOpenHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            final int numOperations = operations.size();
            final ContentProviderResult[] results = new ContentProviderResult[numOperations];
            for (int i = 0; i < numOperations; i++) {
                results[i] = operations.get(i).apply(this, results, i);
            }
            db.setTransactionSuccessful();
            return results;
        } finally {
            db.endTransaction();
        }
    }

    private void notifyChange(Uri uri) {
        Log.v(TAG, "notifyChange(" + uri + ")");
        Context context = getContext();
        context.getContentResolver().notifyChange(uri, null);
    }


}
