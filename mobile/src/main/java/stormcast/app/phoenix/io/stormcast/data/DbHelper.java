package stormcast.app.phoenix.io.stormcast.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by sudharti on 12/31/17.
 */

public class DbHelper extends SQLiteOpenHelper {

    private final static String CREATE_LOCATIONS_TABLE_SQL = " CREATE TABLE " + PersistenceContract.LocationEntry.TABLE_NAME + " ( " +
            PersistenceContract.LocationEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + PersistenceContract.LocationEntry.POSITION + " INTEGER, " +
            PersistenceContract.LocationEntry.NAME + " TEXT, " + PersistenceContract.LocationEntry.ADDRESS + " TEXT, " + PersistenceContract.LocationEntry.LATITUDE + " REAL, " +
            PersistenceContract.LocationEntry.LONGITUDE + " REAL, " + PersistenceContract.LocationEntry.BG_COLOR + " VARCHAR, " +
            PersistenceContract.LocationEntry.TEXT_COLOR + " VARCHAR, " + PersistenceContract.LocationEntry.UNIT + " INTEGER)";

    private final static String CREATE_FORECAST_TABLE_SQL = " CREATE TABLE " + PersistenceContract.ForecastEntry.TABLE_NAME +
            " ( " +
            PersistenceContract.ForecastEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            PersistenceContract.ForecastEntry.SUMMARY + " TEXT, " + PersistenceContract.ForecastEntry.ICON + " VARCHAR, " +
            PersistenceContract.ForecastEntry.TIMEZONE + " VARCHAR, " + PersistenceContract.ForecastEntry.UNITS + " VARCHAR, " +
            PersistenceContract.ForecastEntry.TEMPERATURE + " REAL, " + PersistenceContract.ForecastEntry.APPARENT_TEMPERATURE + " REAL, " +
            PersistenceContract.ForecastEntry.MIN_TEMPERATURE + " REAL, " + PersistenceContract.ForecastEntry.MAX_TEMPERATURE + " REAL, " +
            PersistenceContract.ForecastEntry.HUMIDITY + " REAL, " + PersistenceContract.ForecastEntry.WIND_SPEED + " REAL, " +
            PersistenceContract.ForecastEntry.PRESSURE + " REAL, " + PersistenceContract.ForecastEntry.VISIBILITY + " REAL, " +
            PersistenceContract.ForecastEntry.OZONE + " REAL, " + PersistenceContract.ForecastEntry.UV_INDEX + " INTEGER, " +
            PersistenceContract.ForecastEntry.UPDATED_AT + " INTEGER, " + PersistenceContract.ForecastEntry.CURRENT_TIME + " INTEGER, " +
            PersistenceContract.ForecastEntry.DAILY_FORECASTS + " TEXT, " +
            PersistenceContract.ForecastEntry.LOCATION_ID + " INTEGER NOT NULL, " +
            " FOREIGN KEY ( " + PersistenceContract.ForecastEntry.LOCATION_ID + " ) " +
            " REFERENCES " + PersistenceContract.LocationEntry.TABLE_NAME + "(" + PersistenceContract.LocationEntry._ID + ")" +
            " ) ";

    private static DbHelper sDbHelper;

    public static DbHelper getInstance(Context context) {
        if (sDbHelper == null) {
            sDbHelper = new DbHelper(context);
        }

        return sDbHelper;
    }

    private DbHelper(Context context) {
        super(context, PersistenceContract.DATABASE_NAME, null, PersistenceContract.DATABASE_VERSION, null);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_LOCATIONS_TABLE_SQL);
        sqLiteDatabase.execSQL(CREATE_FORECAST_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }
}
