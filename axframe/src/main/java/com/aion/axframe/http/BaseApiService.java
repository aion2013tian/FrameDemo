package com.aion.axframe.http;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by Aion on 16/9/26.
 */
public interface BaseApiService {

    public static final String BASE_URL = "http://192.168.1.38:8088/";

    @GET("{url}")
    Observable<Object> executeGet(@Path("url") String url, @QueryMap Map<String, String> map);

    @POST("url")
    Observable<BaseResponse> executePost(@Path("url") String url, @QueryMap Map<String, String> map);

    @GET("url")
    Call<? extends BaseResponse> executeGetCall(@Path("url") String url, @QueryMap Map<String, String> map);

    @POST("url")
    Call<? extends BaseResponse> executePostCall(@Path("url") String url, @QueryMap Map<String, String> map);
}
