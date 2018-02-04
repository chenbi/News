package com.massmutual.news.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;




public class NewsDatabase extends SQLiteOpenHelper {

    private static final String TAG = "news";

    // Bump this version when there is a change to database table structure
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "news.db";

    private static NewsDatabase sDatabaseInstance;

    //private final Context mContext;

    public static synchronized NewsDatabase getInstance(Context context) {
        if (sDatabaseInstance == null) {
            sDatabaseInstance = new NewsDatabase(context.getApplicationContext());
        }

        return sDatabaseInstance;
    }

    public NewsDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private static String getCreateTableString(Model model) {
        final String[] columns = model.getTableColumns();
        final String[] columnTypes = model.getTableColumnTypes();
        final int numColumns = columns.length;
        StringBuilder b = new StringBuilder("CREATE TABLE " + model.getTableName() + " (");
        for (int i = 0; i < numColumns; i++) {
            b.append(columns[i]);
            b.append(" ");
            b.append(columnTypes[i]);
            if (i < numColumns - 1)
                b.append(", ");
        }
        b.append(");");

        return b.toString();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for (Model model : NewsModel.MODELS) {
            db.execSQL(getCreateTableString(model));
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        for (Model model : NewsModel.MODELS) {
            db.execSQL("DROP TABLE IF EXISTS " + model.getTableName());
        }

        onCreate(db);
    }
}
