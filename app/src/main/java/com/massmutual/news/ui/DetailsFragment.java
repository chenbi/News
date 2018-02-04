package com.massmutual.news.ui;


import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.massmutual.news.R;
import com.massmutual.news.data.NewsContract;
import com.squareup.picasso.Picasso;


public class DetailsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final String TAG = "DetailsFragment";

    private View mRootView;
    private TextView mShowTitleView;
    private TextView mShowNameView;
    private TextView mShowAirDateView;
    private TextView mShowDescriptionView;
    private TextView mMoreFromShow;
    private TextView mShowDurationView;
    private ImageView mNewsImageView;
    private ImageButton mFab;

    private int mNewHeight;

    private String mNewsDesc;

    private Toolbar mToolbar;

    Bundle mArgs;

    private static int item_position;
    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static DetailsFragment newInstance(int args) {
        DetailsFragment fragment = new DetailsFragment();
        item_position = args;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_details, container, false);
        mArgs = getArguments();
        init();
        return mRootView;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (mNewsImageView != null) {
            mNewsImageView.destroyDrawingCache();
        }
    }

    private void init() {
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);

        mShowTitleView = (TextView) mRootView.findViewById(R.id.tv_content_title);
        mShowAirDateView = (TextView) mRootView.findViewById(R.id.tv_content_date);
        mShowDescriptionView = (TextView) mRootView.findViewById(R.id.tv_content_description);

        mNewsImageView = (ImageView) mRootView.findViewById(R.id.iv_show_cover_image);

        mNewHeight = (int) Math.ceil(dm.widthPixels * 9 / 16);

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, mNewHeight);
        layoutParams.addRule(RelativeLayout.BELOW, R.id.toolbar);
        mNewsImageView.setLayoutParams(layoutParams);

        mToolbar = (Toolbar) ((MainActivity) getActivity()).findViewById(R.id.toolbar);
        mToolbar.setNavigationIcon(android.R.drawable.ic_menu_revert);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        getActivity().findViewById(R.id.fab).setVisibility(View.INVISIBLE);
        getActivity().findViewById(R.id.tabs).setVisibility(View.INVISIBLE);


    }


    private void setDetailsInformation(Cursor cursor) {

        cursor.moveToFirst();

        String imageUrl = cursor.getString(19);

        ((MainActivity) getActivity()).getSupportActionBar().setTitle(cursor.getString(14));

        mShowTitleView.setText(cursor.getString(14));
        mShowAirDateView.setText(cursor.getString(16));

        mNewsDesc =cursor.getString(15);


        mShowDescriptionView.setText(mNewsDesc);

        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);

        Uri imageUri = Uri.parse(imageUrl);
        Picasso.with(getContext())
                .load(imageUri)
                .into(mNewsImageView);




    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        getLoaderManager().initLoader(0, getArguments(), this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = NewsContract.Clip.buildClipUri("");
        String[] projection = NewsContract.Clip.PROJ;
        String selection = "_id=?";
        String[] selectionArgs = {""+(item_position+1)};

        String sortOrder = null;

        CursorLoader cursor;

        cursor = new CursorLoader(getActivity(), uri, projection, selection,  selectionArgs, sortOrder);

        return cursor;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        setDetailsInformation(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}

