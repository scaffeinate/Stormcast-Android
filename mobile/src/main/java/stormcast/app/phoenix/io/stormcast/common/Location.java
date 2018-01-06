package stormcast.app.phoenix.io.stormcast.common;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import org.xml.sax.helpers.LocatorImpl;

import stormcast.app.phoenix.io.stormcast.data.PersistenceContract.LocationEntry;

/**
 * Created by sudharti on 12/31/17.
 */

public class Location implements Parcelable,
        DBMappable<Location>, Validatable {
    public static final int UNIT_AUTO = 0;
    public static final int UNIT_IMPERIAL = 1;
    public static final int UNIT_METRIC = 2;

    private String name, address, backgroundColor, textColor;

    private double latitude = 0, longitude = 0;
    private int id = 0, unit = UNIT_AUTO, position = 0;

    public Location() {
    }

    protected Location(Parcel in) {
        name = in.readString();
        address = in.readString();
        backgroundColor = in.readString();
        textColor = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        id = in.readInt();
        unit = in.readInt();
        position = in.readInt();
    }

    protected Location(LocationBuilder locationBuilder) {
        setId(locationBuilder.id);
        setName(locationBuilder.name);
        setAddress(locationBuilder.address);
        setLatitude(locationBuilder.latitude);
        setLongitude(locationBuilder.longitude);
        setBackgroundColor(locationBuilder.backgroundColor);
        setTextColor(locationBuilder.textColor);
        setUnit(locationBuilder.unit);
        setPosition(locationBuilder.position);
    }

    public static final Creator<Location> CREATOR = new Creator<Location>() {
        @Override
        public Location createFromParcel(Parcel in) {
            return new Location(in);
        }

        @Override
        public Location[] newArray(int size) {
            return new Location[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUnit() {
        return unit;
    }

    public void setUnit(int unit) {
        this.unit = unit;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(address);
        parcel.writeString(backgroundColor);
        parcel.writeString(textColor);
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
        parcel.writeInt(id);
        parcel.writeInt(unit);
        parcel.writeInt(position);
    }

    @Override
    public ContentValues toContentValues() {
        ContentValues cv = new ContentValues();
        if (this.id != 0) {
            cv.put(LocationEntry._ID, this.id);
        }
        cv.put(LocationEntry.NAME, this.name);
        cv.put(LocationEntry.ADDRESS, this.address);
        cv.put(LocationEntry.BG_COLOR, this.backgroundColor);
        cv.put(LocationEntry.TEXT_COLOR, this.textColor);
        cv.put(LocationEntry.LATITUDE, this.latitude);
        cv.put(LocationEntry.LONGITUDE, this.longitude);
        cv.put(LocationEntry.UNIT, this.unit);
        cv.put(LocationEntry.POSITION, this.position);
        return cv;
    }

    @Override
    public boolean isValid() {
        if (TextUtils.isEmpty(this.getName()) || TextUtils.isEmpty(this.getAddress()) ||
                this.getLatitude() == 0 || this.getLongitude() == 0) {
            return false;
        }
        return true;
    }
}
