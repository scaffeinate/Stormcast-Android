package stormcast.app.phoenix.io.stormcast.loaders;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;

/**
 * Created by sudharti on 1/2/18.
 */
public class BackgroundLoader<T> implements LoaderManager.LoaderCallbacks<T> {

    private Context mContext;
    private Callbacks mCallbacks;

    public BackgroundLoader(Context context, Callbacks callbacks) {
        this.mContext = context;
        this.mCallbacks = callbacks;
    }

    @Override
    public Loader<T> onCreateLoader(int id, Bundle bundle) {
        return new CustomTaskLoader<>(mContext);
    }

    @Override
    public void onLoadFinished(Loader<T> loader, T t) {
        if (mCallbacks != null) {
            mCallbacks.onTaskCompleted(loader, t);
        }
    }

    @Override
    public void onLoaderReset(Loader<T> loader) {
        // Do Nothing
    }

    public interface Callbacks<T> {
        T doInBackground();

        void onTaskCompleted(Loader loader, T data);
    }

    class CustomTaskLoader<T> extends AsyncTaskLoader<T> {

        public CustomTaskLoader(@NonNull Context context) {
            super(context);
        }

        @Override
        protected void onStartLoading() {
            super.onStartLoading();
            forceLoad();
        }

        @Nullable
        @Override
        public T loadInBackground() {
            return (T) mCallbacks.doInBackground();
        }
    }
}
