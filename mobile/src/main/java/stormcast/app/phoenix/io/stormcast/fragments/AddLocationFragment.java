package stormcast.app.phoenix.io.stormcast.fragments;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

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
import stormcast.app.phoenix.io.stormcast.Stormcast;
import stormcast.app.phoenix.io.stormcast.common.Location;
import stormcast.app.phoenix.io.stormcast.common.LocationBuilder;
import stormcast.app.phoenix.io.stormcast.databinding.FragmentAddLocationBinding;
import stormcast.app.phoenix.io.stormcast.utils.ColorPickerHelper;
import stormcast.app.phoenix.io.stormcast.views.colorpick.MaterialColorPickDialog;
import stormcast.app.phoenix.io.stormcast.views.tab_pills.TabPills;

import static android.app.Activity.RESULT_OK;

/**
 * Created by sudharti on 12/31/17.
 */

public class AddLocationFragment extends Fragment implements View.OnClickListener, OnMapReadyCallback {
    private static final String TAG = AddLocationFragment.class.toString();

    private static final String LOCATION = "location";
    private static final String FINISH_ACTIVITY_ON_ACTION = "finishActivityOnAction";
    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 100;
    private static final float TILT = 10f;
    private static final float ZOOM = 12f;

    private Context mContext;

    private CameraPosition mCameraPosition;
    private LocationBuilder mLocationBuilder;
    private MaterialColorPickDialog.Builder mBgColorDialogBuilder;
    private MaterialColorPickDialog.Builder mTextColorDialogBuilder;

    private int backgroundColor, textColor;
    private boolean finishActivityOnAction = false;

    private Animation fadeInAnimation;
    private FragmentAddLocationBinding mBinding;

    public static AddLocationFragment newInstance(boolean finishActivityOnAction) {
        return newInstance(null, finishActivityOnAction);
    }

    public static AddLocationFragment newInstance(Location location, boolean finishActivityOnAction) {
        AddLocationFragment addLocationFragment = new AddLocationFragment();

        Bundle args = new Bundle();
        args.putParcelable(LOCATION, location);
        args.putBoolean(FINISH_ACTIVITY_ON_ACTION, finishActivityOnAction);
        addLocationFragment.setArguments(args);

        return addLocationFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();

        fadeInAnimation = AnimationUtils.loadAnimation(mContext, R.anim.fade_in);

        mLocationBuilder = new LocationBuilder((Location) getArguments().getParcelable(LOCATION));
        finishActivityOnAction = getArguments().getBoolean(FINISH_ACTIVITY_ON_ACTION);

        mBgColorDialogBuilder = MaterialColorPickDialog.with(mContext);
        mTextColorDialogBuilder = MaterialColorPickDialog.with(mContext);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_location, container, false);

        mBinding.locationEditText.setOnClickListener(this);
        mBinding.backgroundColorBtn.setOnClickListener(this);
        mBinding.textColorBtn.setOnClickListener(this);

        mBinding.unitsSwitchTabSelector.addTabs(new TabPills.SwitchTab[]{
                new TabPills.SwitchTab<>("Auto", Location.UNIT_AUTO),
                new TabPills.SwitchTab<>("Imperial", Location.UNIT_IMPERIAL),
                new TabPills.SwitchTab<>("Metric", Location.UNIT_METRIC)
        });

        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        backgroundColor = Stormcast.DEFAULT_BACKGROUND_COLOR;
        textColor = Stormcast.DEFAULT_TEXT_COLOR;

        if (savedInstanceState != null) {
            final Location restored = savedInstanceState.getParcelable(LOCATION);

            backgroundColor = Color.parseColor(restored.getBackgroundColor());
            textColor = Color.parseColor(restored.getTextColor());

            mLocationBuilder.setName(restored.getName())
                    .setAddress(restored.getAddress())
                    .setLatitude(restored.getLatitude())
                    .setLongitude(restored.getLongitude())
                    .setBackgroundColor(restored.getBackgroundColor())
                    .setTextColor(restored.getTextColor())
                    .setUnit(restored.getUnit());

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    addMarker(new LatLng(restored.getLatitude(), restored.getLongitude()));
                }
            }, 250);
        }

        //mToolbarCallbacks.setToolbarTitle("Add Location");

        GradientDrawable drawable = (GradientDrawable) mBinding.backgroundColorBtn.getBackground();
        drawable.setColor(backgroundColor);

        drawable = (GradientDrawable) mBinding.textColorBtn.getBackground();
        drawable.setColor(textColor);

        mBinding.locationMapView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mBinding.locationMapView.onCreate(savedInstanceState);
                MapsInitializer.initialize(getActivity().getApplicationContext());
                mBinding.locationMapView.onResume();
                mBinding.locationMapView.startAnimation(fadeInAnimation);
            }
        }, 500);
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
                mLocationBuilder.setUnit((Integer) mBinding.unitsSwitchTabSelector.getSelectedValue());
                return true;
            case android.R.id.home:
                goBack();
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
                ColorPickerHelper.showColorPicker(mBgColorDialogBuilder, null, new ColorPickerHelper.ColorPickerCallback() {
                    @Override
                    public void onColorSelected(String colorHex) {
                        GradientDrawable drawable = (GradientDrawable) mBinding.backgroundColorBtn.getBackground();
                        drawable.setColor(Color.parseColor(colorHex));
                        mLocationBuilder.setBackgroundColor(colorHex);
                    }
                });
                break;
            case R.id.text_color_btn:
                ColorPickerHelper.showColorPicker(mTextColorDialogBuilder, null, new ColorPickerHelper.ColorPickerCallback() {
                    @Override
                    public void onColorSelected(String colorHex) {
                        GradientDrawable drawable = (GradientDrawable) mBinding.textColorBtn.getBackground();
                        drawable.setColor(Color.parseColor(colorHex));
                        mLocationBuilder.setTextColor(colorHex);
                    }
                });
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

            }
        }
        return builder.toString();
    }

    private void goBack() {
        if (finishActivityOnAction) {
            getActivity().finish();
        } else {
            getFragmentManager().popBackStack();
        }
    }
}
