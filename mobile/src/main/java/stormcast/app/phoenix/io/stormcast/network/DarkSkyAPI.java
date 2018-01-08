package stormcast.app.phoenix.io.stormcast.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import stormcast.app.phoenix.io.stormcast.common.network.Forecast;

/**
 * Created by sudharti on 8/22/17.
 */

public interface DarkSkyAPI {
    @GET("forecast/{apiKey}/{latLng}")
    Call<Forecast> loadForecast(@Path("apiKey") String apiKey, @Path("latLng") String latLng,
                                @Query("exclude") String exclude, @Query("units") String units);
}
