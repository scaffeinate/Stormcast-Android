package stormcast.app.phoenix.io.stormcast.loaders;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

/**
 * Created by sudharti on 1/7/18.
 */

public class CustomTaskLoader<T> extends AsyncTaskLoader<T> {

    private BackgroundLoader.Callbacks<T> mCallbacks;

    public CustomTaskLoader(@NonNull Context context, BackgroundLoader.Callbacks<T> callbacks) {
        super(context);
        this.mCallbacks = callbacks;
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
