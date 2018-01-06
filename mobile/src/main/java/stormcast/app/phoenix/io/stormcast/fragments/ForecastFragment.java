package stormcast.app.phoenix.io.stormcast.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import stormcast.app.phoenix.io.stormcast.R;
import stormcast.app.phoenix.io.stormcast.activities.ToolbarCallbacks;
import stormcast.app.phoenix.io.stormcast.databinding.FragmentHomeBinding;
import stormcast.app.phoenix.io.stormcast.data.PersistenceContract;
import stormcast.app.phoenix.io.stormcast.loaders.CursorLoaderCallbacks;

import static stormcast.app.phoenix.io.stormcast.Stormcast.LOCATIONS_LOADER_ID;

/**
 * Created by sudharti on 12/31/17.
 */

public class ForecastFragment extends Fragment implements View.OnClickListener, CursorLoaderCallbacks.ContentLoaderCallbacks {

    private Cursor mCursor;
    private FragmentHomeBinding mBinding;
    private FragmentManager mFragmentManager;

    private ToolbarCallbacks mToolbarCallbacks;
    private CursorLoaderCallbacks mCursorLoaderCallbacks;
    
    private ForecastsAdapter mAdapter;

    public static ForecastFragment newInstance() {
        return new ForecastFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCursorLoaderCallbacks = new CursorLoaderCallbacks(mContext, this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);

        mFragmentManager = getActivity().getSupportFragmentManager();

        mBinding.btnAddLocation.setOnClickListener(this);

        mAdapter = new ForecastsAdapter(this);

        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mToolbarCallbacks = (ToolbarCallbacks) getActivity();
        mToolbarCallbacks.setToolbarTitle(getString(R.string.action_forecast));
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
}
