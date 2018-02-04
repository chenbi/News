package com.massmutual.news.ui;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.massmutual.news.R;
import com.massmutual.news.adapter.ArticlesListAdapter;
import com.massmutual.news.data.NewsContract;
import com.massmutual.news.utils.Constants;


public class ArticlesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = "ArticlesFragment";

    private View mRootView;
    private ArticlesListAdapter mAdapter;

    private Toolbar mToolbar;


    private ListView mFeaturedList;
    private SwipeRefreshLayout mRefreshLayout;
    private int mPageNum = 1;
    private LayoutInflater mInflater;

    private ProgressBar mProgress;

    private boolean mFirstLoad = false;

    public static ArticlesFragment newInstance(int sectionNumber) {
        ArticlesFragment fragment = new ArticlesFragment();

        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFirstLoad = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        mInflater = inflater;
        mRootView = mInflater.inflate(R.layout.fragment_featured, container, false);

        mFeaturedList = (ListView) mRootView.findViewById(R.id.lv_featured);
        mRefreshLayout = (SwipeRefreshLayout) mRootView.findViewById(R.id.refresh_layout);
        mProgress = (ProgressBar) mRootView.findViewById(R.id.pb_featured_progress);

        setupViews();

        return mRootView;
    }

    private void setupViews() {
        mProgress = (ProgressBar) mRootView.findViewById(R.id.pb_featured_progress);

        mRefreshLayout = (SwipeRefreshLayout) mRootView.findViewById(R.id.refresh_layout);
        mRefreshLayout.setProgressViewOffset(false,
                0,
                (int) getActivity().getResources().getDimension(R.dimen.abc_action_bar_default_height_material) + 75);

        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPageNum = 1;

                if (mAdapter != null) {
                    mAdapter.swapCursor(null);
                }

                ((MainActivity) getActivity()).startUpdateService(Constants.PAGE_SIZE_SMALL);
            }
        });

        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);


        mAdapter = new ArticlesListAdapter(getActivity(), null, 0, false);

        mFeaturedList.setAdapter(mAdapter);


        mProgress.setVisibility(View.VISIBLE);
        mFeaturedList.setVisibility(View.INVISIBLE);

        mToolbar = (Toolbar) ((MainActivity) getActivity()).findViewById(R.id.toolbar);
        mToolbar.setTitle("MassMutal News");
        mToolbar.setNavigationIcon(android.R.drawable.ic_lock_lock);  //mass mutual icon
        mFeaturedList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onViewClicked(position);
            }
        });

        ((MainActivity) getActivity()).findViewById(R.id.fab).setVisibility(View.VISIBLE);
        ((MainActivity) getActivity()).findViewById(R.id.tabs).setVisibility(View.VISIBLE);
        mToolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }
        );

    }


    private void makePaginationCall() {
        if (getUserVisibleHint()) {
            float adapterCount = new Float(mAdapter.getCount());
            float pageSize = new Float(Constants.PAGE_SIZE_SMALL);

            if ((adapterCount % pageSize) > 0) {
                mPageNum = (int) Math.ceil(adapterCount / pageSize);
            } else {
                mPageNum = (int) (Math.ceil(adapterCount / pageSize)) + 1;
            }
        }

        int numResults = Constants.PAGE_SIZE_SMALL;


        if (mPageNum > 1) {
            ((MainActivity) getActivity()).startUpdateService(mPageNum);

        }
    }


    @Override
    public void onDestroyView() {
        mAdapter.swapCursor(null);
        mAdapter = null;

        super.onDestroyView();
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
        String selection = null;
        String[] selectionArgs = null;

        String sortOrder = null;

        CursorLoader cursor;

        cursor = new CursorLoader(getActivity(), uri, projection, selection,    selectionArgs, sortOrder);

        return cursor;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mRefreshLayout.setRefreshing(false);


        if (mFirstLoad) {
            mFirstLoad = false;
        } else {

            if (!cursor.isClosed()) {
                if (cursor.getCount() > 2) {
                    cursor.moveToFirst();

                    mAdapter.swapCursor(cursor);
                    mAdapter.notifyDataSetChanged();

                    mFeaturedList.setVisibility(View.VISIBLE);
                    mProgress.setVisibility(View.INVISIBLE);
                }
            }


        }


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (mAdapter != null) {
            mAdapter.swapCursor(null);
        }
    }

    public void onViewClicked(int position) {
        DetailsFragment detailsFragment = DetailsFragment.newInstance(position);


        FragmentTransaction trans = getFragmentManager().beginTransaction();

        trans.replace(R.id.root_frame, detailsFragment);

        trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        trans.addToBackStack(MainActivity.ICON_UP_CARET);

        trans.commit();


    }


    public void onLoadMore() {
        if (mAdapter.getCount() > 0) {
            makePaginationCall();
        }
    }
}
