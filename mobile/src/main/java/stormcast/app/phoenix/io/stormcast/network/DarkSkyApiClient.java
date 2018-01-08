package stormcast.app.phoenix.io.stormcast.network;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import stormcast.app.phoenix.io.stormcast.BuildConfig;
import stormcast.app.phoenix.io.stormcast.common.local.Location;
import stormcast.app.phoenix.io.stormcast.common.network._Forecast;

/**
 * Created by sudharti on 8/22/17.
 */

public class DarkSkyApiClient {
    private static final String TAG = DarkSkyApiClient.class.getSimpleName();
    public static final String BASE_URL = "https://api.darksky.net/";
    private static final String API_KEY = BuildConfig.DARK_SKY_API_KEY;
    private static DarkSkyApiClient mDarkSkyApiClient;
    private static Retrofit mRetrofit;
    private static DarkSkyAPI darkSkyApi;

    private DarkSkyApiClient() {
        if (mRetrofit == null) {
            mRetrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        darkSkyApi = mRetrofit.create(DarkSkyAPI.class);
    }

    public static DarkSkyApiClient getInstance() {
        if (mDarkSkyApiClient == null) {
            mDarkSkyApiClient = new DarkSkyApiClient();
        }

        return mDarkSkyApiClient;
    }

    public Call<_Forecast> createForecastRequestFor(Location location) {
        String latLng = new StringBuilder()
                .append(location.getLatitude())
                .append(",")
                .append(location.getLongitude())
                .toString();
        String exclude = "minutely";
        return darkSkyApi.loadForecast(API_KEY, latLng, exclude, location.getUnit());
    }
}
