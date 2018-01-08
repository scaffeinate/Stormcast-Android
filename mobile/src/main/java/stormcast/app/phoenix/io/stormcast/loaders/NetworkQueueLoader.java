package stormcast.app.phoenix.io.stormcast.loaders;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import retrofit2.Call;
import retrofit2.Response;
import stormcast.app.phoenix.io.stormcast.network.DarkSkyApiClient;

/**
 * Created by sudharti on 1/7/18.
 */

public class NetworkQueueLoader<T> implements LoaderManager.LoaderCallbacks<List<T>> {

    private static final String TAG = NetworkQueueLoader.class.getSimpleName();
    private Context mContext;
    private Callbacks<T> mCallbacks;
    private Queue<Call<T>> mCallQueue;
    private DarkSkyApiClient mApiClient = DarkSkyApiClient.getInstance();

    public NetworkQueueLoader(Context context, List<Call<T>> callQueue, Callbacks<T> callbacks) {
        this.mContext = context;
        this.mCallbacks = callbacks;
        this.mCallQueue = new LinkedList<>();
        for (int i = 0; i < callQueue.size(); i++) {
            this.mCallQueue.offer(callQueue.get(i));
        }
    }

    @Override
    public Loader<List<T>> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<List<T>>(mContext) {
            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                forceLoad();
            }

            @Nullable
            @Override
            public List<T> loadInBackground() {
                List<T> resultList = new ArrayList<>();
                while (!mCallQueue.isEmpty()) {
                    Call<T> call = mCallQueue.poll();
                    try {
                        Response<T> response = call.execute();
                        if (response != null && response.code() == 200) {
                            resultList.add(response.body());
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "Exception occurred while loading forecast: " + e.getMessage());
                    }
                }
                return resultList;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<List<T>> loader, List<T> data) {
        if (mCallbacks != null) {
            mCallbacks.onTaskCompleted(loader, data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<T>> loader) {

    }

    public interface Callbacks<T> {
        void onTaskCompleted(Loader loader, List<T> data);
    }
}
