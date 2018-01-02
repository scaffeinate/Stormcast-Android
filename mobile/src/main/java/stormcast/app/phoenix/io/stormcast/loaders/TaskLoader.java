package stormcast.app.phoenix.io.stormcast.loaders;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import stormcast.app.phoenix.io.stormcast.loaders.AsyncTaskLoaderCallbacks.TaskLoaderCallbacks;

/**
 * Created by sudharti on 1/2/18.
 */

public class TaskLoader<T> extends AsyncTaskLoader {

    private TaskLoaderCallbacks mCallbacks = null;

    public TaskLoader(@NonNull Context context, TaskLoaderCallbacks<T> callbacks) {
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
