package stormcast.app.phoenix.io.stormcast.common;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by sudharti on 12/31/17.
 */

public class LocationBuilder {
    protected int id;
    protected String name, address;
    protected double latitude = -1;
    protected double longitude = -1;
    protected String backgroundColor = null, textColor = null;
    protected int unit = Location.UNIT_AUTO, position = 0;

    public LocationBuilder(Location location) {
        setId(location.getId())
                .setName(location.getName())
                .setAddress(location.getAddress())
                .setLatitude(location.getLatitude())
                .setLongitude(location.getLongitude())
                .setBackgroundColor(location.getBackgroundColor())
                .setTextColor(location.getTextColor())
                .setUnit(location.getPosition())
                .setPosition(location.getPosition());
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

    public LocationBuilder setUnit(int unit) {
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
