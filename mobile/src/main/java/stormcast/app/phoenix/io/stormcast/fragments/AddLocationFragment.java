package stormcast.app.phoenix.io.stormcast.fragments;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import stormcast.app.phoenix.io.stormcast.R;
import stormcast.app.phoenix.io.stormcast.activities.ToolbarCallbacks;
import stormcast.app.phoenix.io.stormcast.common.local.Location;
import stormcast.app.phoenix.io.stormcast.common.local.LocationBuilder;
import stormcast.app.phoenix.io.stormcast.data.PersistenceContract;
import stormcast.app.phoenix.io.stormcast.databinding.FragmentAddLocationBinding;
import stormcast.app.phoenix.io.stormcast.utils.FormatUtils;
import stormcast.app.phoenix.io.stormcast.views.colorpick.MaterialColorPickDialog;
import stormcast.app.phoenix.io.stormcast.views.tab_pills.TabPillsSelector;
import stormcast.app.phoenix.io.stormcast.views.tab_pills.TabPillsSelector.TabPill;

import static android.app.Activity.RESULT_OK;

/**
 * Created by sudharti on 12/31/17.
 */

public class AddLocationFragment extends Fragment implements View.OnClickListener, OnMapReadyCallback {
    private static final String TAG = AddLocationFragment.class.toString();

    private static final String LOCATION = "location";

    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 100;
    private static final float TILT = 10f;
    private static final float ZOOM = 12f;

    private Context mContext;

    private CameraPosition mCameraPosition;
    private LocationBuilder mLocationBuilder;

    private int selectedUnit, backgroundColor, textColor;
    private ToolbarCallbacks mToolbarCallbacks;

    private Animation fadeInAnimation;
    private FragmentAddLocationBinding mBinding;
    private TabPill[] mTabPills;

    private MaterialColorPickDialog.Builder mBgColorBuilder, mTextColorBuilder;

    public static AddLocationFragment newInstance() {
        return newInstance(null);
    }

    public static AddLocationFragment newInstance(Location location) {
        AddLocationFragment addLocationFragment = new AddLocationFragment();

        Bundle args = new Bundle();
        args.putParcelable(LOCATION, location);
        addLocationFragment.setArguments(args);

        return addLocationFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
        fadeInAnimation = AnimationUtils.loadAnimation(mContext, R.anim.fade_in);
        mTabPills = new TabPill[]{
                new TabPill<>(getString(R.string.auto), Location.UNIT_AUTO),
                new TabPill<>(getString(R.string.imperial), Location.UNIT_IMPERIAL),
                new TabPill<>(getString(R.string.metric), Location.UNIT_METRIC)
        };
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_location, container, false);

        mBinding.locationEditText.setOnClickListener(this);
        mBinding.backgroundColorBtn.setOnClickListener(this);
        mBinding.textColorBtn.setOnClickListener(this);

        mBgColorBuilder = MaterialColorPickDialog.with(mContext).build();
        mTextColorBuilder = MaterialColorPickDialog.with(mContext).build();

        mBgColorBuilder.setOnColorPickedListener(new MaterialColorPickDialog.OnColorPickedListener() {
            @Override
            public void onClick(String colorHex) {
                GradientDrawable drawable = (GradientDrawable) mBinding.backgroundColorBtn.getBackground();
                drawable.setColor(Color.parseColor(colorHex));
                mLocationBuilder.setBackgroundColor(colorHex);
            }
        });

        mTextColorBuilder.setOnColorPickedListener(new MaterialColorPickDialog.OnColorPickedListener() {
            @Override
            public void onClick(String colorHex) {
                GradientDrawable drawable = (GradientDrawable) mBinding.textColorBtn.getBackground();
                drawable.setColor(Color.parseColor(colorHex));
                mLocationBuilder.setTextColor(colorHex);
            }
        });

        mBinding.unitsSwitchTabSelector.addTabs(mTabPills);
        mBinding.unitsSwitchTabSelector.setOnTabClickListener(new TabPillsSelector.OnTabClickListener() {
            @Override
            public void onTabClick(int index) {
                mLocationBuilder.setUnit((Integer) mBinding.unitsSwitchTabSelector.getValueAt(index));
            }
        });

        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mToolbarCallbacks = (ToolbarCallbacks) getActivity();
        mToolbarCallbacks.setToolbarTitle(getString(R.string.add_location));

        selectedUnit = Location.UNIT_AUTO;
        backgroundColor = ContextCompat.getColor(mContext, R.color.colorPrimary);
        textColor = ContextCompat.getColor(mContext, R.color.textColorLight);

        final Location location = (Location) ((savedInstanceState != null) ? savedInstanceState.getParcelable(LOCATION) :
                getArguments().getParcelable(LOCATION));

        if (location != null) {
            mLocationBuilder = LocationBuilder.from(location);
            backgroundColor = Color.parseColor(location.getBackgroundColor());
            textColor = Color.parseColor(location.getTextColor());
            selectedUnit = location.getUnit();
            mBinding.locationEditText.setText(location.getAddress());
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    addMarker(new LatLng(location.getLatitude(), location.getLongitude()));
                }
            });
        } else {
            mLocationBuilder = new LocationBuilder();
            mLocationBuilder.setBackgroundColor(FormatUtils.convertColorToHex(backgroundColor))
                    .setTextColor(FormatUtils.convertColorToHex(textColor))
                    .setUnit(selectedUnit);
        }

        GradientDrawable drawable = (GradientDrawable) mBinding.backgroundColorBtn.getBackground();
        drawable.setColor(backgroundColor);

        drawable = (GradientDrawable) mBinding.textColorBtn.getBackground();
        drawable.setColor(textColor);

        for (int i = 0; i < mTabPills.length; i++) {
            if (((Integer) mTabPills[i].getValue()).intValue() == selectedUnit) {
                mBinding.unitsSwitchTabSelector.setSelectedIndex(i);
                break;
            }
        }

        mBinding.locationMapView.post(new Runnable() {
            @Override
            public void run() {
                mBinding.locationMapView.onCreate(savedInstanceState);
                MapsInitializer.initialize(getActivity().getApplicationContext());
                mBinding.locationMapView.onResume();
                mBinding.locationMapView.startAnimation(fadeInAnimation);
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.add_location_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_location_menu_item:
                Location location = mLocationBuilder.build();
                if (location.isValid()) {
                    saveLocation(location);
                } else {
                    Toast.makeText(mContext, getString(R.string.invalid_location), Toast.LENGTH_SHORT).show();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(mContext, data);
                String name = place.getName().toString();
                String address = decodeAddress(place);
                mBinding.locationEditText.setText(address);
                mBinding.locationEditText.setCursorVisible(false);
                mBinding.locationEditText.clearFocus();
                mLocationBuilder.setName(name)
                        .setAddress(address)
                        .setLatLng(place.getLatLng());
                addMarker(place.getLatLng());
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.location_edit_text:
                try {
                    AutocompleteFilter filter = new AutocompleteFilter
                            .Builder()
                            .setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES)
                            .build();
                    Intent intent = new PlaceAutocomplete
                            .IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                            .setFilter(filter)
                            .build(getActivity());
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.background_color_btn:
                mBgColorBuilder.show();
                break;
            case R.id.text_color_btn:
                mTextColorBuilder.show();
                break;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mBinding.locationMapView.setVisibility(View.VISIBLE);
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(mCameraPosition));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(LOCATION, mLocationBuilder.build());
    }

    private void addMarker(LatLng latLng) {
        mCameraPosition = CameraPosition.builder()
                .target(latLng)
                .tilt(TILT)
                .zoom(ZOOM)
                .build();
        mBinding.locationMapView.getMapAsync(AddLocationFragment.this);
    }

    private String decodeAddress(Place place) {
        LatLng latLng = place.getLatLng();
        StringBuilder builder = new StringBuilder();
        builder.append(place.getName());
        if (latLng != null) {
            Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
            try {
                List<Address> addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                String adminArea;
                if (addressList.size() > 0 && (adminArea = addressList.get(0).getAdminArea()) != null
                        && !adminArea.isEmpty()) {
                    builder.append(",").append(adminArea);
                }
            } catch (IOException e) {
                Log.e(TAG, " Exception occurred while trying to decode address: " + e.getMessage());
            }
        }
        return builder.toString();
    }

    private void saveLocation(Location location) {
        if (getActivity() != null && getActivity().getContentResolver() != null) {
            try {
                if(location.getId() == 0) {
                    Uri uri = PersistenceContract.LOCATIONS_CONTENT_URI
                            .buildUpon()
                            .build();
                    getActivity().getContentResolver().insert(uri, location.toContentValues());
                } else {
                    Uri uri = PersistenceContract.LOCATIONS_CONTENT_URI
                            .buildUpon()
                            .appendPath(String.valueOf(location.getId()))
                            .build();
                    getActivity().getContentResolver().update(uri, location.toContentValues(), null, null);
                }
                Toast.makeText(mContext, getString(R.string.location_saved), Toast.LENGTH_SHORT).show();
                goBack();
            } catch (UnsupportedOperationException | SQLiteException e) {
                Toast.makeText(mContext, getString(R.string.failed_to_insert_location), Toast.LENGTH_SHORT).show();
                Log.e(TAG, " Exception occurred while inserting location: " + e.getMessage());
            }
        }
    }

    private void goBack() {
        getFragmentManager().popBackStack();
    }
}
