package com.aion.axframe.http;

import com.aion.axframe.common.AXBaseApplication;
import com.aion.axframe.utils.log.ALog;

import java.io.File;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Aion on 16/10/27.
 */

public class RetrofitClient {

    private static volatile RetrofitClient instance = null;
    private static final int DEFAULT_TIMEOUT = 20;
    private BaseApiService apiService;
    public static String baseUrl = BaseApiService.BASE_URL;

    private Retrofit retrofitRx;
    private OkHttpClient.Builder okBuilder;

    public static RetrofitClient getInstance() {
        if (instance == null) {
            synchronized (RetrofitClient.class) {
                if (instance == null) {
                    instance = new RetrofitClient(null);
                }
            }
        }
        return instance;
    }

    public static RetrofitClient getInstance(Map<String, String> headers) {
        if (instance == null) {
            synchronized (RetrofitClient.class) {
                if (instance == null) {
                    instance = new RetrofitClient(headers);
                }
            }
        } else {
            instance.setHeaders(headers);
        }
        return instance;
    }

    private RetrofitClient(Map<String, String> headers) {
        File httpCacheDirectory = new File(AXBaseApplication.getInstance().getCacheDir(), "tamic_cache");
        Cache cache = null;
        try {
            cache = new Cache(httpCacheDirectory, 10 * 1024 * 1024);
        } catch (Exception e) {
            ALog.e("OKHttp", "Could not create http cache", e);
        }
        okBuilder = new OkHttpClient.Builder()
                .addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .cookieJar(new NovateCookieManger(AXBaseApplication.getInstance()))
                .cache(cache)
                .addInterceptor(new CacheInterceptor(AXBaseApplication.getInstance()))
                .addNetworkInterceptor(new CacheInterceptor(AXBaseApplication.getInstance()))
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .connectionPool(new ConnectionPool(8, 15, TimeUnit.SECONDS));// 这里你可以根据自己的机型设置同时连接的个数和时间，我这里8个，和每个保持时间为10s
        setHeaders(headers);

    }

    public void setHeaders(Map<String, String> headers) {
        okBuilder.addInterceptor(new BaseInterceptor(headers));
        this.retrofitRx = new Retrofit.Builder()
                .client(okBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(baseUrl).build();
        this.apiService = create(BaseApiService.class);
    }

    /**
     * 创建自定义的请求服务
     */
    public  <T> T create(Class<T> service) {
        if (service == null) {
            throw new RuntimeException("Api service is null!");
        }
        return this.retrofitRx.create(service);
    }

    Observable.Transformer schedulersTransformer() {
        return new Observable.Transformer() {
            @Override
            public Object call(Object observable) {
                return ((Observable)  observable).subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    public <T> Observable.Transformer transformer() {

        return new Observable.Transformer() {

            @Override
            public Object call(Object observable) {
                return ((Observable) observable).map(new HandleFuc<T>()).onErrorResumeNext(new HttpResponseFunc<T>());
            }
        };
    }

//    public <T> Observable.Transformer transformer() {
//
//        return new Observable.Transformer() {
//
//            @Override
//            public Object call(Object observable) {
//                return ((Observable) observable).map(new HandleFuc()).onErrorResumeNext(new HttpResponseFunc<T>());
//            }
//        };
//    }

    public Subscription baseGet(String url, Map<String, String> parameters, BaseSubscriber<? extends BaseResponse> subscriber) {

        return apiService.executeGet(url, parameters)
                .compose(schedulersTransformer())
                .compose(transformer())
                .subscribe(subscriber);
    }

    public Subscription basePost(String url, Map<String, String> parameters, BaseSubscriber<? extends BaseResponse> subscriber) {
        return apiService.executePost(url, parameters)
                .compose(schedulersTransformer())
                .compose(transformer())
                .subscribe(subscriber);
    }

    private static class HttpResponseFunc<T> implements Func1<Throwable, Observable<T>> {
        @Override
        public Observable<T> call(Throwable t) {
            return Observable.error(ExceptionHandle.handleException(t));
        }
    }

    private class HandleFuc<T> implements Func1<BaseResponse<T>, T> {
        @Override
        public T call(BaseResponse<T> response) {
            if (!response.isOk()) throw new RuntimeException(response.getCode() + "" + response.getMsg() != null ? response.getMsg(): "");
            return response.getData();
        }
    }

//    private class HandleFuc implements Func1<Object, Object> {
//        @Override
//        public Object call(Object response) {
////            if (!response.isOk()) throw new RuntimeException(response.getCode() + "" + response.getMsg() != null ? response.getMsg(): "");
////            return response.getData();
//            return response;
//        }
//    }

    /**
     * /**
     * execute your customer API
     * For example:
     *  MyApiService service =
     *      RetrofitClient.getInstance(MainActivity.this).create(MyApiService.class);
     *
     *  RetrofitClient.getInstance(MainActivity.this)
     *      .execute(service.lgon("name", "password"), subscriber)
     *     * @param subscriber
     */

    public <T> void execute(Observable<T> observable ,BaseSubscriber<T> subscriber) {
        observable.compose(schedulersTransformer()).compose(transformer()).subscribe(subscriber);
    }

    /**
     * DownSubscriber
     * @param <ResponseBody>
     */
    class DownSubscriber<ResponseBody> extends Subscriber<ResponseBody> {
        ProgressCallBack progressCallBack;

        public DownSubscriber(ProgressCallBack progressCallBack) {
            this.progressCallBack = progressCallBack;
        }

        @Override
        public void onStart() {
            super.onStart();
            if (progressCallBack != null) {
                progressCallBack.onStart();
            }
        }

        @Override
        public void onCompleted() {
            if (progressCallBack != null) {
                progressCallBack.onCompleted();
            }
        }

        @Override
        public void onError(Throwable e) {
            if (progressCallBack != null) {
                progressCallBack.onError(e);
            }
        }

        @Override
        public void onNext(ResponseBody responseBody) {
            DownLoadManager.getInstance(progressCallBack).writeResponseBodyToDisk(AXBaseApplication.getInstance(), (okhttp3.ResponseBody) responseBody);

        }
    }
}
