package com.aion.axframe.http;

import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by Aion on 16/9/26.
 */
public interface BaseApiService {

    public static final String BASE_URL = "http://www.baidu.com/";

    @GET("url")
    Observable<? extends BaseResponse> executeGet(@Path("url") String url, @QueryMap Map<String, String> map);

    @POST("url")
    Observable<? extends BaseResponse> executePost(@Path("url") String url, @QueryMap Map<String, String> map);
}
