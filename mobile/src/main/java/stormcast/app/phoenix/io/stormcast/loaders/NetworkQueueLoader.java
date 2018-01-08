package stormcast.app.phoenix.io.stormcast.loaders;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import java.util.List;

/**
 * Created by sudharti on 1/7/18.
 */

public class NetworkQueueLoaderCallbacks<T> implements LoaderManager.LoaderCallbacks<List<T>> {

    public NetworkQueueLoaderCallbacks() {

    }

    @Override
    public Loader<List<T>> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<List<T>> loader, List<T> data) {

    }

    @Override
    public void onLoaderReset(Loader<List<T>> loader) {

    }

    public interface Callbacks<T> {

    }
}
