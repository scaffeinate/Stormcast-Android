package stormcast.app.phoenix.io.stormcast.fragments;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import stormcast.app.phoenix.io.stormcast.R;
import stormcast.app.phoenix.io.stormcast.activities.ToolbarCallbacks;
import stormcast.app.phoenix.io.stormcast.adapters.LocationsListAdapter;
import stormcast.app.phoenix.io.stormcast.common.local.Location;
import stormcast.app.phoenix.io.stormcast.common.local.LocationBuilder;
import stormcast.app.phoenix.io.stormcast.data.PersistenceContract;
import stormcast.app.phoenix.io.stormcast.databinding.FragmentEditLocationsBinding;
import stormcast.app.phoenix.io.stormcast.loaders.ContentLoader;
import stormcast.app.phoenix.io.stormcast.loaders.BackgroundLoader;
import stormcast.app.phoenix.io.stormcast.views.drag_drop_list.ItemTouchHelperAdapter;
import stormcast.app.phoenix.io.stormcast.views.drag_drop_list.ItemTouchHelperCallback;
import stormcast.app.phoenix.io.stormcast.views.drag_drop_list.OnStartDragListener;

import static android.content.ContentValues.TAG;
import static stormcast.app.phoenix.io.stormcast.Stormcast.LOCATIONS_LOADER_ID;
import static stormcast.app.phoenix.io.stormcast.Stormcast.UPDATE_LOCATIONS_LOADER_ID;

/**
 * Created by sudharti on 1/1/18.
 */

public class EditLocationsFragment extends Fragment implements ItemTouchHelperAdapter, OnStartDragListener, ContentLoader.ContentLoaderCallbacks, View.OnClickListener {
    private Context mContext;
    private FragmentManager mFragmentManager;

    private RecyclerView.LayoutManager mLayoutManager;
    private LocationsListAdapter mAdapter;
    private List<Location> mLocationList;

    private ItemTouchHelper mItemTouchHelper;
    private ItemTouchHelper.Callback mCallback;

    private FragmentEditLocationsBinding mBinding;
    private ToolbarCallbacks mToolbarCallbacks;

    private ContentResolver mContentResolver;
    private LoaderManager mLoaderManager;

    private ContentLoader mContentLoader;

    private int mDeletedPosition = 0;

    public static EditLocationsFragment newInstance() {
        return new EditLocationsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
        mLocationList = new ArrayList<>();
        mContentLoader = new ContentLoader(mContext, this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_locations, container, false);

        mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL);
        mBinding.recyclerViewLocationsList.addItemDecoration(itemDecoration);
        mBinding.recyclerViewLocationsList.setHasFixedSize(true);

        mAdapter = new LocationsListAdapter(this);
        mBinding.recyclerViewLocationsList.setAdapter(mAdapter);
        mBinding.recyclerViewLocationsList.setLayoutManager(mLayoutManager);

        mCallback = new ItemTouchHelperCallback(this);
        mItemTouchHelper = new ItemTouchHelper(mCallback);
        mItemTouchHelper.attachToRecyclerView(mBinding.recyclerViewLocationsList);

        mBinding.btnAddLocation.setOnClickListener(this);

        mFragmentManager = getActivity().getSupportFragmentManager();
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() != null) {
            mToolbarCallbacks = (ToolbarCallbacks) getActivity();
            mToolbarCallbacks.setToolbarTitle(getString(R.string.action_edit_locations));

            if (getActivity().getContentResolver() != null) {
                mContentResolver = getActivity().getContentResolver();
            }

            if (getActivity().getSupportLoaderManager() != null) {
                mLoaderManager = getActivity().getSupportLoaderManager();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mLocationList.clear();
        fetchLocations();
    }

    @Override
    public void onItemMove(final int fromPosition, final int toPosition) {
        BackgroundLoader<Void> backgroundLoader = new BackgroundLoader<Void>(mContext, new BackgroundLoader.Callbacks<Void>() {
            @Override
            public Void doInBackground() {
                if (fromPosition < toPosition) {
                    for (int i = fromPosition; i <= toPosition; i++) {
                        Location location = mLocationList.get(i);
                        location.setPosition(i + 1);
                        saveLocation(location);
                    }
                } else {
                    for (int i = toPosition; i <= fromPosition; i++) {
                        Location location = mLocationList.get(i);
                        location.setPosition(i + 1);
                        saveLocation(location);
                    }
                }
                return null;
            }

            @Override
            public void onTaskCompleted(Loader loader, Void data) {
                Toast.makeText(mContext, "Reorder complete", Toast.LENGTH_SHORT).show();
            }
        });

        Collections.swap(mLocationList, fromPosition, toPosition);
        mAdapter.notifyItemMoved(fromPosition, toPosition);
        mLoaderManager.restartLoader(UPDATE_LOCATIONS_LOADER_ID, null, backgroundLoader);
    }

    @Override
    public void onItemSwipeRight(int position) {
        Location location = mLocationList.get(position);
        if (location != null) {
            this.mDeletedPosition = position;
            deleteLocation(location);
        }
    }

    @Override
    public void onItemSwipeLeft(int position) {
        Location location = mLocationList.get(position);
        mFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_left_enter, R.anim.slide_left_exit, R.anim.slide_right_enter, R.anim.slide_right_exit)
                .replace(R.id.layout_content, AddLocationFragment.newInstance(location))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    @Override
    public void onLoadFinished(Loader loader, Cursor cursor) {
        switch (loader.getId()) {
            case LOCATIONS_LOADER_ID:
                if (cursor == null || cursor.getCount() <= 0) {
                    showErrorMessage();
                } else {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        Location location = LocationBuilder.from(cursor).build();
                        mLocationList.add(location);
                        cursor.moveToNext();
                    }
                    mAdapter.setLocationList(mLocationList);
                    showRecyclerView();
                }
                break;
        }
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


    private void fetchLocations() {
        Bundle args = new Bundle();
        args.putParcelable(ContentLoader.URI_EXTRA, PersistenceContract.LOCATIONS_CONTENT_URI);
        if (mLoaderManager != null) {
            mLoaderManager.restartLoader(LOCATIONS_LOADER_ID, args, mContentLoader);
        }
    }

    private void deleteLocation(Location location) {
        Bundle args = new Bundle();
        Uri uri = PersistenceContract.LOCATIONS_CONTENT_URI.buildUpon()
                .appendPath(String.valueOf(location.getId()))
                .build();
        args.putParcelable(ContentLoader.URI_EXTRA, uri);
        if (mContentResolver != null) {
            try {
                mContentResolver.delete(uri, null, null);
                mLocationList.remove(this.mDeletedPosition);
                mAdapter.notifyItemRemoved(this.mDeletedPosition);
                if (mLocationList.isEmpty()) {
                    showErrorMessage();
                }

                Toast.makeText(mContext, getString(R.string.location_deleted), Toast.LENGTH_SHORT).show();
            } catch (UnsupportedOperationException | SQLiteException e) {
                Log.e(TAG, "Excepion occurred while trying to delete location: " + e.getMessage());
                Toast.makeText(mContext, getString(R.string.failed_to_delete_location), Toast.LENGTH_SHORT).show();
            }
            this.mDeletedPosition = 0;
        }
    }

    private void showRecyclerView() {
        mBinding.recyclerViewLocationsList.setVisibility(View.VISIBLE);
        mBinding.progressBarLoading.setVisibility(View.GONE);
        mBinding.textViewErrorMessage.setVisibility(View.GONE);
    }

    private void showErrorMessage() {
        mBinding.progressBarLoading.setVisibility(View.GONE);
        mBinding.recyclerViewLocationsList.setVisibility(View.GONE);
        mBinding.textViewErrorMessage.setVisibility(View.VISIBLE);
    }


    private void saveLocation(Location location) {
        if (mContentResolver != null && location != null) {
            Uri uri = PersistenceContract.LOCATIONS_CONTENT_URI
                    .buildUpon()
                    .appendPath(String.valueOf(location.getId()))
                    .build();
            try {
                mContentResolver.update(uri, location.toContentValues(), null, null);
            } catch (UnsupportedOperationException | SQLiteException e) {
                Log.e(TAG, "Exception occurred while updating location: " + e.getMessage());
            }
        }
    }
}
