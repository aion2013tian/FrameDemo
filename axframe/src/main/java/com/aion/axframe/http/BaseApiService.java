package com.aion.axframe.http;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by Aion on 16/9/26.
 */
public interface BaseApiService {

    public static final String BASE_URL = "http://192.168.1.38:8088/";

    @GET("{url}")
    Observable<BaseResponse> executeGet(@Path("url") String url, @QueryMap Map<String, String> map);

    @POST("{url}")
    Observable<BaseResponse> executePost(@Path("url") String url, @QueryMap Map<String, String> map);

    @DELETE("{url}")
    Observable<BaseResponse> executeDelete(@Path("url") String url, @QueryMap Map<String, String> maps);

    @PUT("{url}")
    Observable<BaseResponse> executePut(@Path("url") String url, @QueryMap Map<String, String> maps);

    @Multipart
    @POST("{url}")
    Observable<ResponseBody> upLoadFile(@Path("url") String url, @Part("image\"; filename=\"image.jpg") RequestBody requestBody);

    @Multipart
    @POST("{url}")
    Observable<ResponseBody> uploadFiles(@Path("url") String url, @Part("filename") String description, @PartMap() Map<String, RequestBody> maps);

    @Streaming
    @GET
    Observable<ResponseBody> downloadFile(@Url String fileUrl);
}
