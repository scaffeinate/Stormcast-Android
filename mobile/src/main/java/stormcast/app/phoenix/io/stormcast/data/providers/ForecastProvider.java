package stormcast.app.phoenix.io.stormcast.data.providers;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import stormcast.app.phoenix.io.stormcast.data.DbHelper;
import stormcast.app.phoenix.io.stormcast.data.PersistenceContract;
import stormcast.app.phoenix.io.stormcast.data.PersistenceContract.ForecastEntry;

/**
 * Created by sudharti on 12/31/17.
 */

public class ForecastProvider extends ContentProvider {

    private static final int FORECAST_WITH_LOCATION_ID = 200;

    private static final UriMatcher sUriMatcher = builderUriMatcher();

    private static final String PARAM = " ? ";

    private static final String EQUAL_TO = " = ";

    private Context mContext;
    private DbHelper mDbHelper;

    private static UriMatcher builderUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PersistenceContract.FORECAST_AUTHORITY, PersistenceContract.FORECAST_PATH + "/#/", FORECAST_WITH_LOCATION_ID);
        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        mContext = getContext();
        mDbHelper = DbHelper.getInstance(mContext);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection,
                        @Nullable String where, @Nullable String[] whereArgs, @Nullable String sortOrder)
            throws UnsupportedOperationException {
        final SQLiteDatabase db = mDbHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);
        Cursor cursor = null;
        switch (match) {
            case FORECAST_WITH_LOCATION_ID:
                List<String> pathSegments = uri.getPathSegments();
                if (pathSegments != null && pathSegments.size() > 1) {
                    String id = uri.getPathSegments().get(1);
                    cursor = db.query(ForecastEntry.TABLE_NAME, projection, ForecastEntry.LOCATION_ID + EQUAL_TO + PARAM, new String[]{id},
                            null, null, sortOrder);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }

        mContext.getContentResolver().notifyChange(uri, null);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues)
            throws SQLiteException, UnsupportedOperationException {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        long res = -1;
        switch (match) {
            case FORECAST_WITH_LOCATION_ID:
                if (contentValues != null) {
                    List<String> pathSegments = uri.getPathSegments();
                    if (pathSegments != null && pathSegments.size() > 1) {
                        String id = uri.getPathSegments().get(1);
                        int numRows = db.update(ForecastEntry.TABLE_NAME, contentValues,
                                ForecastEntry.LOCATION_ID + EQUAL_TO + PARAM, new String[]{id});
                        if (numRows <= 0) {
                            res = db.insert(PersistenceContract.ForecastEntry.TABLE_NAME, null, contentValues);

                            if (res == -1) {
                                throw new SQLiteException("Failed to insert record: " + uri);
                            }
                        }
                    }
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        mContext.getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, res);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings)
            throws SQLiteException, UnsupportedOperationException {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        int res = -1;
        switch (match) {
            case FORECAST_WITH_LOCATION_ID:
                List<String> pathSegments = uri.getPathSegments();
                if (pathSegments != null && pathSegments.size() > 1) {
                    String id = uri.getPathSegments().get(1);
                    res = db.delete(ForecastEntry.TABLE_NAME,
                            ForecastEntry._ID + EQUAL_TO + PARAM, new String[]{id});
                    if (res == -1) {
                        throw new SQLiteException("Failed to delete record: " + uri);
                    }
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        return res;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
