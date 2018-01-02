package stormcast.app.phoenix.io.stormcast.common;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Created by sudharti on 1/1/18.
 */

public interface DBMappable<T> {

    public ContentValues toContentValues();

    public T fromCursor(Cursor cursor);
}
