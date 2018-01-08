package stormcast.app.phoenix.io.stormcast.common.local;

import android.database.Cursor;

import com.google.android.gms.maps.model.LatLng;

import stormcast.app.phoenix.io.stormcast.data.PersistenceContract.LocationEntry;

/**
 * Created by sudharti on 12/31/17.
 */

public class LocationBuilder {
    protected int id;
    protected String name, address;
    protected double latitude = 0, longitude = 0;
    protected String backgroundColor = null, textColor = null, unit = Unit.AUTO;
    protected int position = 0;

    public LocationBuilder() {
    }

    public static LocationBuilder from(Location location) {
        LocationBuilder locationBuilder = new LocationBuilder();
        locationBuilder
                .setId(location.getId())
                .setName(location.getName())
                .setAddress(location.getAddress())
                .setLatitude(location.getLatitude())
                .setLongitude(location.getLongitude())
                .setBackgroundColor(location.getBackgroundColor())
                .setTextColor(location.getTextColor())
                .setUnit(location.getUnit())
                .setPosition(location.getPosition());
        return locationBuilder;
    }

    public static LocationBuilder from(Cursor cursor) {
        LocationBuilder locationBuilder = new LocationBuilder();
        locationBuilder
                .setId(cursor.getInt(cursor.getColumnIndex(LocationEntry._ID)))
                .setName(cursor.getString(cursor.getColumnIndex(LocationEntry.NAME)))
                .setAddress(cursor.getString(cursor.getColumnIndex(LocationEntry.ADDRESS)))
                .setLatitude(cursor.getDouble(cursor.getColumnIndex(LocationEntry.LATITUDE)))
                .setLongitude(cursor.getDouble(cursor.getColumnIndex(LocationEntry.LONGITUDE)))
                .setBackgroundColor(cursor.getString(cursor.getColumnIndex(LocationEntry.BG_COLOR)))
                .setTextColor(cursor.getString(cursor.getColumnIndex(LocationEntry.TEXT_COLOR)))
                .setUnit(cursor.getString(cursor.getColumnIndex(LocationEntry.UNIT)))
                .setPosition(cursor.getInt(cursor.getColumnIndex(LocationEntry.POSITION)));
        return locationBuilder;
    }

    public LocationBuilder setId(int id) {
        this.id = id;
        return this;
    }

    public LocationBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public LocationBuilder setAddress(String address) {
        this.address = address;
        return this;
    }

    public LocationBuilder setLatitude(double latitude) {
        this.latitude = latitude;
        return this;
    }

    public LocationBuilder setLongitude(double longitude) {
        this.longitude = longitude;
        return this;
    }

    public LocationBuilder setLatLng(LatLng latLng) {
        this.latitude = latLng.latitude;
        this.longitude = latLng.longitude;
        return this;
    }

    public LocationBuilder setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    public LocationBuilder setTextColor(String textColor) {
        this.textColor = textColor;
        return this;
    }

    public LocationBuilder setUnit(String unit) {
        this.unit = unit;
        return this;
    }

    public LocationBuilder setPosition(int position) {
        this.position = position;
        return this;
    }

    public Location build() {
        return new Location(this);
    }
}
