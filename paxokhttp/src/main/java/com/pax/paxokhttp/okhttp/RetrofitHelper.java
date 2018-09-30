package com.pax.paxokhttp.okhttp;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author ligq
 * @date 2018/9/30
 */

public class RetrofitHelper {
    public static <T> T createApi(Class<T> clazz, String url) {
        return new Retrofit.Builder()
                .baseUrl(url)
                .client(OkHttpUtils.getOkHttpClient(AppUtils.getApp(), OkHttpUtils.DEFAULT_TIMEOUT))
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(clazz);
    }
}
