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
import stormcast.app.phoenix.io.stormcast.data.PersistenceContract.LocationEntry;

/**
 * Created by sudharti on 12/31/17.
 */

public class LocationsProvider extends ContentProvider {

    private static final int LOCATIONS = 100;

    private static final int LOCATIONS_WITH_ID = 101;

    private static final UriMatcher sUriMatcher = builderUriMatcher();

    private static final String PARAM = " ? ";

    private static final String EQUAL_TO = " = ";

    private Context mContext;
    private DbHelper mDbHelper;

    private static UriMatcher builderUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PersistenceContract.LOCATIONS_AUTHORITY, PersistenceContract.LOCATIONS_PATH, LOCATIONS);
        uriMatcher.addURI(PersistenceContract.LOCATIONS_AUTHORITY, PersistenceContract.LOCATIONS_PATH + "/#/", LOCATIONS_WITH_ID);
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
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) throws UnsupportedOperationException {
        final SQLiteDatabase db = mDbHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);
        Cursor cursor = null;
        switch (match) {
            case LOCATIONS:
                cursor = db.query(LocationEntry.TABLE_NAME, null, null,
                        null, null, null, LocationEntry.POSITION);
                break;
            case LOCATIONS_WITH_ID:
                List<String> pathSegments = uri.getPathSegments();
                if (pathSegments != null && pathSegments.size() > 1) {
                    String id = uri.getPathSegments().get(1);
                    cursor = db.query(LocationEntry.TABLE_NAME, projection,
                            LocationEntry._ID + EQUAL_TO + PARAM, new String[]{id},
                            null, null, null);
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
            case LOCATIONS:
                if (contentValues != null) {
                    res = db.insert(LocationEntry.TABLE_NAME, null, contentValues);
                    if (res == -1) {
                        throw new SQLiteException("Failed to insert record: " + uri);
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
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        int res = -1;
        switch (match) {
            case LOCATIONS_WITH_ID:
                List<String> pathSegments = uri.getPathSegments();
                if (pathSegments != null && pathSegments.size() > 1) {
                    String id = uri.getPathSegments().get(1);
                    res = db.delete(LocationEntry.TABLE_NAME,
                            LocationEntry._ID + EQUAL_TO + PARAM, new String[]{id});
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
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String where, @Nullable String[] whereArgs) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        int res = -1;
        switch (match) {
            case LOCATIONS_WITH_ID:
                List<String> pathSegments = uri.getPathSegments();
                if (contentValues != null && pathSegments != null && pathSegments.size() > 1) {
                    String id = uri.getPathSegments().get(1);
                    res = db.update(LocationEntry.TABLE_NAME, contentValues,
                            LocationEntry._ID + EQUAL_TO + PARAM, new String[]{id});
                    if (res <= 0) {
                        throw new SQLiteException("Failed to update record: " + uri);
                    }
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        return res;
    }
}
