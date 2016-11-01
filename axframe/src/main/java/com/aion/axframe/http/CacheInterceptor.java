package com.aion.axframe.http;

import android.content.Context;

import com.aion.axframe.utils.NetworkUtil;
import com.aion.axframe.utils.log.ALog;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Aion on 16/9/26.
 */
public class CacheInterceptor implements Interceptor {

    private Context context;
    private String cacheControlValue;
    private String cacheOnlineControlValue;
    //set cahe times is 3 days
    private static final int maxStale = 60 * 60 * 24 * 3;
    // read from cache for 60 s
    private static final int maxOnlineStale = 60;

    public CacheInterceptor(Context context) {
        this(context, String.format("max-stale=%d", maxStale));
    }

    public CacheInterceptor(Context context, String cacheControlValue) {
        this(context, cacheControlValue, String.format("max-stale=%d", maxOnlineStale));
    }

    public CacheInterceptor(Context context, String cacheControlValue, String cacheOnlineControlValue) {
        this.context = context;
        this. cacheControlValue = cacheControlValue;
        this.cacheOnlineControlValue = cacheOnlineControlValue;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (NetworkUtil.isNetworkAvailable(context)) {
            Response response = chain.proceed(request);
//            String cacheControl = request.cacheControl().toString();
            return response.newBuilder()
                    .removeHeader("Pragma")
                    .removeHeader("Cache-Control")
                    .header("Cache-Control", "public, " + cacheOnlineControlValue)
                    .build();
        } else {
            //TODO 当前无网络提醒
//            ((Activity) context).runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    Toast.makeText(context, "当前无网络! 为你智能加载缓存", Toast.LENGTH_SHORT).show();
//                }
//            });
            ALog.e(" no network load cahe");
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
            Response response = chain.proceed(request);
            return response.newBuilder()
                    .removeHeader("Pragma")
                    .removeHeader("Cache-Control")
                    .header("Cache-Control", "public, only-if-cached, " + cacheControlValue)
                    .build();
        }
    }
}
