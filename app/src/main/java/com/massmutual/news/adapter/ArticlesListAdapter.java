package com.massmutual.news.adapter;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.massmutual.news.R;
import com.squareup.picasso.Picasso;


public class ArticlesListAdapter extends CursorAdapter {
    private static final String TAG = "ArticlesListAdapter";


    public static final int FEAT_COLUMN_COVER_IMAGE = 17;
    public static final int FEAT_COLUMN_TITLE = 14;
    public static final int FEAT_COLUMN_AIR_DATE = 16;

    private Context mContext;
    private LayoutInflater mInflater;


    public ArticlesListAdapter(Context context, Cursor cursor, int flags, boolean tablet) {
        super(context, cursor, flags);

        mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View row = mInflater.inflate(R.layout.item_featured_list, null);

        ViewHolder holder = new ViewHolder();

        holder.featuredImage = (ImageView) row.findViewById(R.id.iv_image);
        holder.newsTitle = (TextView) row.findViewById(R.id.news_title);
        holder.date = (TextView) row.findViewById(R.id.publish_date);
        holder.overflow = (ImageButton) row.findViewById(R.id.overflow);


        row.setTag(holder);

        return row;
    }


    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        final ViewHolder holder = (ViewHolder) view.getTag();

        if (!cursor.isClosed() && cursor.getCount() > 0) {
            String imageUrl = cursor.getString(FEAT_COLUMN_COVER_IMAGE);


            Picasso.with(context)
                    .load(imageUrl)
                    .into(holder.featuredImage);
            holder.newsTitle.setText(cursor.getString(FEAT_COLUMN_TITLE));
            holder.date.setText(cursor.getString(FEAT_COLUMN_AIR_DATE));

            final Bundle args = new Bundle();

            args.putInt("cursorPosition", cursor.getPosition());


        }
    }

    public static class ViewHolder {
        public ImageView featuredImage;
        public TextView title;
        public TextView newsTitle;
        public TextView date;
        public ImageButton overflow;
    }

}
