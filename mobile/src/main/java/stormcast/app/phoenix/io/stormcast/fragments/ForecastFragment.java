package stormcast.app.phoenix.io.stormcast.fragments;

import android.content.Context;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import stormcast.app.phoenix.io.stormcast.R;
import stormcast.app.phoenix.io.stormcast.activities.ToolbarCallbacks;
import stormcast.app.phoenix.io.stormcast.adapters.ForecastsAdapter;
import stormcast.app.phoenix.io.stormcast.adapters.OnItemClickHandler;
import stormcast.app.phoenix.io.stormcast.common.local.LocationForecast;
import stormcast.app.phoenix.io.stormcast.common.local.LocationForecastBuilder;
import stormcast.app.phoenix.io.stormcast.data.PersistenceContract;
import stormcast.app.phoenix.io.stormcast.databinding.FragmentHomeBinding;
import stormcast.app.phoenix.io.stormcast.loaders.CursorLoaderCallbacks;

import static stormcast.app.phoenix.io.stormcast.Stormcast.LOCATIONS_LOADER_ID;

/**
 * Created by sudharti on 12/31/17.
 */

public class ForecastFragment extends Fragment implements View.OnClickListener, CursorLoaderCallbacks.ContentLoaderCallbacks, OnItemClickHandler {

    private static final String TAG = ForecastFragment.class.getSimpleName();
    private Context mContext;
    private List<LocationForecast> mLocationForecastList;
    private FragmentHomeBinding mBinding;
    private FragmentManager mFragmentManager;
    private RecyclerView.LayoutManager mLayoutManager;

    private ToolbarCallbacks mToolbarCallbacks;
    private CursorLoaderCallbacks mCursorLoaderCallbacks;

    private ForecastsAdapter mAdapter;
    private LoaderManager mLoaderManager;

    public static ForecastFragment newInstance() {
        return new ForecastFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
        mCursorLoaderCallbacks = new CursorLoaderCallbacks(mContext, this);
        mLocationForecastList = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);

        mAdapter = new ForecastsAdapter(mContext, this);
        mLayoutManager = new GridLayoutManager(mContext, 1);

        mBinding.recyclerViewForecasts.setAdapter(mAdapter);
        mBinding.recyclerViewForecasts.setLayoutManager(mLayoutManager);
        mBinding.btnAddLocation.setOnClickListener(this);

        mFragmentManager = getActivity().getSupportFragmentManager();
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() != null) {
            mToolbarCallbacks = (ToolbarCallbacks) getActivity();
            mToolbarCallbacks.setToolbarTitle(getString(R.string.action_forecast));

            if (getActivity().getSupportLoaderManager() != null) {
                mLoaderManager = getActivity().getSupportLoaderManager();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mLocationForecastList.clear();
        fetchLocations();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add_location:
                mFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.slide_left_enter, R.anim.slide_left_exit, R.anim.slide_right_enter, R.anim.slide_right_exit)
                        .replace(R.id.layout_content, AddLocationFragment.newInstance())
                        .addToBackStack(null)
                        .commit();
                break;
        }
    }


    @Override
    public void onItemClicked(ViewGroup parent, View view, int position) {

    }

    @Override
    public void onLoadFinished(Loader loader, final Cursor cursor) {
        switch (loader.getId()) {
            case LOCATIONS_LOADER_ID:
                if (cursor == null || cursor.getCount() <= 0) {
                    showErrorMessage();
                } else {
                    for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                        mLocationForecastList.add(LocationForecastBuilder.from(cursor).build());
                    }
                    mAdapter.setLocationForecastList(mLocationForecastList);
                    showRecyclerView();
                    loadFromNetwork();
                }
                break;
        }
    }


    private void fetchLocations() {
        Bundle args = new Bundle();
        args.putParcelable(CursorLoaderCallbacks.URI_EXTRA, PersistenceContract.LOCATIONS_FORECAST_CONTENT_URI);
        if (mLoaderManager != null) {
            mLoaderManager.restartLoader(LOCATIONS_LOADER_ID, args, mCursorLoaderCallbacks);
        }
    }

    private void loadFromNetwork() {
        /*AsyncTaskLoaderCallbacks<Void> asyncTaskLoaderCallbacks = new AsyncTaskLoaderCallbacks<>(mContext, new TaskLoaderCallbacks() {
            @Override
            public Object doInBackground() {
                Log.i(ForecastFragment.class.getSimpleName(), "Loader started");
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    Location location = LocationBuilder.from(cursor).build();
                    cursor.moveToNext();
                    Response<Forecast> response = DarkSkyApiClient.getInstance().loadForecast(location);
                    Log.i(TAG, location.getName() + " " + response.body().getDaily().getSummary());
                }
                return null;
            }

            @Override
            public void onTaskCompleted(Loader loader, Object data) {

            }
        });

        mLoaderManager.restartLoader(UPDATE_LOCATIONS_LOADER_ID, null, asyncTaskLoaderCallbacks);*/
    }

    private void showRecyclerView() {
        mBinding.recyclerViewForecasts.setVisibility(View.VISIBLE);
        mBinding.progressBarLoading.setVisibility(View.GONE);
        mBinding.textViewErrorMessage.setVisibility(View.GONE);
    }

    private void showErrorMessage() {
        mBinding.progressBarLoading.setVisibility(View.GONE);
        mBinding.recyclerViewForecasts.setVisibility(View.GONE);
        mBinding.textViewErrorMessage.setVisibility(View.VISIBLE);
    }
}
