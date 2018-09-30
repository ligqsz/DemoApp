package com.pax.demoapp.ui.model;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @author ligq
 * @date 2018/9/30
 */

public interface WeatherApi {
    @GET("index")
    Observable<ResponseBody> doGet(@Query("cityname") String cityname, @Query("dtype") String dtype,
                                   @Query("format") String format, @Query("key") String key);

    @GET("index")
    Observable<WeatherResponse> doGetWeather(@Query("cityname") String cityname, @Query("dtype") String dtype,
                                             @Query("format") String format, @Query("key") String key);

    @POST("index")
    Observable<ResponseBody> doPost(@Header("key") String key, @Body WeatherRequest request);
}
