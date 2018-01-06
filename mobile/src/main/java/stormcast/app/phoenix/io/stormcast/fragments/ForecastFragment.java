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

import stormcast.app.phoenix.io.stormcast.R;
import stormcast.app.phoenix.io.stormcast.activities.ToolbarCallbacks;
import stormcast.app.phoenix.io.stormcast.adapters.ForecastsAdapter;
import stormcast.app.phoenix.io.stormcast.adapters.OnItemClickHandler;
import stormcast.app.phoenix.io.stormcast.data.PersistenceContract;
import stormcast.app.phoenix.io.stormcast.databinding.FragmentHomeBinding;
import stormcast.app.phoenix.io.stormcast.loaders.CursorLoaderCallbacks;

import static stormcast.app.phoenix.io.stormcast.Stormcast.LOCATIONS_LOADER_ID;

/**
 * Created by sudharti on 12/31/17.
 */

public class ForecastFragment extends Fragment implements View.OnClickListener, CursorLoaderCallbacks.ContentLoaderCallbacks, OnItemClickHandler {

    private Context mContext;
    private Cursor mCursor;
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
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);

        mAdapter = new ForecastsAdapter(this);
        mLayoutManager = new GridLayoutManager(mContext, 2);

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
        fetchLocations();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add_location:
                mFragmentManager.beginTransaction()
                        .replace(R.id.layout_content, AddLocationFragment.newInstance())
                        .addToBackStack(null)
                        .commit();
                break;
        }
    }

    @Override
    public void onLoadFinished(Loader loader, Cursor cursor) {
        switch (loader.getId()) {
            case LOCATIONS_LOADER_ID:
                if (cursor == null || cursor.getCount() <= 0) {
                    showErrorMessage();
                } else {
                    mCursor = cursor;
                    mAdapter.swapCursor(cursor);
                    showRecyclerView();
                }
                break;
        }
    }

    private void fetchLocations() {
        Bundle args = new Bundle();
        args.putParcelable(CursorLoaderCallbacks.URI_EXTRA, PersistenceContract.LOCATIONS_CONTENT_URI);
        if (mLoaderManager != null) {
            mLoaderManager.restartLoader(LOCATIONS_LOADER_ID, args, mCursorLoaderCallbacks);
        }
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

    @Override
    public void onItemClicked(ViewGroup parent, View view, int position) {

    }
}
