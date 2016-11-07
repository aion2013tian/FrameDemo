package com.aion.axframe.http;

import android.content.Context;
import android.text.TextUtils;

import com.aion.axframe.utils.log.ALog;

import java.io.File;
import java.net.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;

import okhttp3.Cache;
import okhttp3.ConnectionPool;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Aion on 16/10/27.
 */

public class RetrofitClient {

    private static final int DEFAULT_TIMEOUT = 5;
    private static final int DEFAULT_MAXIDLE_CONNECTIONS = 5;
    private static final long  DEFAULT_KEEP_ALIVEDURATION = 8;
    private Observable<ResponseBody> downObservable;
    private Map<String, Observable<ResponseBody>> downMaps = new HashMap<String, Observable<ResponseBody>>(){};
    private Builder builder;


    RetrofitClient(Builder builder) {
        this.builder = builder;
    }

    public Builder getBuilder() {
        return builder;
    }

    /**
     * 创建自定义的请求服务
     */
    public  <T> T create(Class<T> service) {
        if (service == null) {
            throw new RuntimeException("Api service is null!");
        }
        return builder.retrofit.create(service);
    }

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
        observable.compose(schedulersTransformer())
                .subscribe(subscriber);
    }

    /**
     * 运行线程切换管理
     * @return
     */
    Observable.Transformer schedulersTransformer() {
        return new Observable.Transformer() {
            @Override
            public Object call(Object observable) {
                return ((Observable)  observable).subscribeOn(Schedulers.io())//IO线程解析数据
                        .unsubscribeOn(Schedulers.io())//IO线程解除订阅
                        .observeOn(AndroidSchedulers.mainThread());//UI线程返回处理后的数据
            }
        };
    }

    public Subscription baseGet(String url, Map<String, String> parameters, BaseSubscriber<? extends BaseResponse> subscriber) {
        return builder.apiManager.executeGet(url, parameters)
                .compose(schedulersTransformer())
                .subscribe(subscriber);
    }

    public Subscription basePost(String url, Map<String, String> parameters, BaseSubscriber<? extends BaseResponse> subscriber) {
        return builder.apiManager.executePost(url, parameters)
                .compose(schedulersTransformer())
                .subscribe(subscriber);
    }

    public Subscription basePut(String url, Map<String, String> parameters, BaseSubscriber<? extends BaseResponse> subscriber) {
        return builder.apiManager.executePut(url, parameters)
                .compose(schedulersTransformer())
                .subscribe(subscriber);
    }

    public Subscription baseDelete(String url, Map<String, String> parameters, BaseSubscriber<? extends BaseResponse> subscriber) {
        return builder.apiManager.executeDelete(url, parameters)
                .compose(schedulersTransformer())
                .subscribe(subscriber);
    }

    /**
     * upload
     * @param url
     * @param requestBody requestBody
     * @param subscriber subscriber
     * @param <T> T
     * @return
     */
    public <T> T upload(String url, RequestBody requestBody, BaseSubscriber<ResponseBody> subscriber) {
        builder.apiManager.upLoadFile(url, requestBody)
                .compose(schedulersTransformer())
                .subscribe(subscriber);
        return null;
    }


    /**
     * download
     * @param url
     * @param callBack
     */
    public void download(String url, ProgressCallBack callBack) {

        if (downMaps.get(url) == null) {
            downObservable = builder.apiManager.downloadFile(url);
            downMaps.put(url, downObservable);
        } else {
            downObservable = downMaps.get(url);
        }

        if (DownLoadManager.isDownLoading) {
            downObservable.unsubscribeOn(Schedulers.io());
            DownLoadManager.isDownLoading = false;
            DownLoadManager.isCancel = true;
            return;
        }
        DownLoadManager.isDownLoading = true;
        downObservable.compose(schedulersTransformer())
                .subscribe(new DownSubscriber<ResponseBody>(callBack));

    }

    public static class Builder {
        Context mContext;
        OkHttpClient.Builder okhttpBuilder;
        Retrofit.Builder retrofitBuilder;
        Retrofit retrofit;
        String baseUrl, cacheFileName;
        Map<String, String> headers;
        Map<String, String> parameters;
        BaseApiService apiManager;
        okhttp3.Call.Factory callFactory;
        Executor callbackExecutor;
        boolean validateEagerly;
        private Boolean isLog = false;
        private HostnameVerifier hostnameVerifier;
        private NovateCookieManger cookieManager;
        private Cache cache = null;
        private Proxy proxy;
        private SSLSocketFactory sslSocketFactory;
        private ConnectionPool connectionPool;
        private Converter.Factory converterFactory;
        private CallAdapter.Factory callAdapterFactory;
        private int connectTimeout=-1, writeTimeout=-1;
        private TimeUnit connectUnit, writeUnit;
        private CacheInterceptor cacheInterceptor;
        private long cacheMaxSize = 10 * 1024 * 1024;

        public Builder(Context context) {
            // Add the base url first. This prevents overriding its behavior but also
            // ensures correct behavior when using novate that consume all types.
            okhttpBuilder = new OkHttpClient.Builder();
            retrofitBuilder = new Retrofit.Builder();
            this.mContext = context;
        }

        public Builder callFactory(okhttp3.Call.Factory factory) {
            this.callFactory = checkNotNull(factory, "factory == null");
            return this;
        }

        public Builder connectTimeout(int timeout, TimeUnit unit) {
            this.connectTimeout = timeout;
            this.connectUnit = unit;
            return this;
        }

        public Builder writeTimeout(int timeout, TimeUnit unit) {
            this.writeTimeout = timeout;
            this.writeUnit = unit;
            return this;
        }

        public Builder addLog(boolean isLog) {
            this.isLog = isLog;
            return this;
        }

        public Builder proxy(Proxy proxy) {
            this.proxy = proxy;
            return this;
        }

        public Builder connectionPool(ConnectionPool connectionPool) {
            this.connectionPool = checkNotNull(connectionPool, "connectionPool == null");
            return this;
        }

        public Builder baseUrl(String baseUrl) {
            this.baseUrl = checkNotNull(baseUrl, "baseUrl == null");
            return this;
        }

        public Builder addConverterFactory(Converter.Factory factory) {
            this.converterFactory = checkNotNull(factory, "Converter.Factory == null");
            return this;
        }

        public Builder addCallAdapterFactory(CallAdapter.Factory factory) {
            this.callAdapterFactory = checkNotNull(factory, "CallAdapter.Factory == null");
            return this;
        }

        public Builder addHeader(Map<String, String> headers) {
            this.headers = checkNotNull(headers, "header == null");
            return this;
        }

        public Builder addParameters(Map<String, String> parameters) {
            this.parameters = checkNotNull(parameters, "parameters == null");
            return this;
        }

        public Builder callbackExecutor(Executor executor) {
            this.callbackExecutor = checkNotNull(executor, "executor == null");
            return this;
        }

        public Builder validateEagerly(boolean validateEagerly) {
            this.validateEagerly = validateEagerly;
            return this;
        }

        public Builder cookieManager(NovateCookieManger cookie) {
            this.cookieManager = checkNotNull(cookie, "cookieManager == null");
            return this;
        }

        /**
         *
         */
        public Builder addSSLSocketFactory(SSLSocketFactory sslSocketFactory) {
            this.sslSocketFactory = checkNotNull(sslSocketFactory, "sslSocketFactory == null");
            return this;
        }

        public Builder addHostnameVerifier(HostnameVerifier hostnameVerifier) {
            this.hostnameVerifier = checkNotNull(hostnameVerifier, "hostnameVerifier == null");
            return this;
        }

        /**
         * Sets the handler that can accept cookies from incoming HTTP responses and provides cookies to
         * outgoing HTTP requests.
         * <p/>
         * <p>If unset, {@linkplain NovateCookieManger#NO_COOKIES no cookies} will be accepted nor provided.
         */
        public Builder addSSL(String[] hosts, int[] certificates) {
            if (hosts==null || hosts.length<=0) {
                throw new NullPointerException("hosts == null");
            }
            if (certificates==null || certificates.length<=0) {
                throw new NullPointerException("certificates == null");
            }
            addSSLSocketFactory(RetrofitHttpsFactroy.getSSLSocketFactory(mContext, certificates));
            addHostnameVerifier(RetrofitHttpsFactroy.getHostnameVerifier(hosts));
            return this;
        }

        public Builder addNetworkInterceptor(Interceptor interceptor) {
            okhttpBuilder.addNetworkInterceptor(checkNotNull(interceptor, "NetworkInterceptor == null"));
            return this;
        }

        /**
         * setCache
         * @param cache cahe
         * @return  Builder
         */
        public Builder addCache(Cache cache) {
            this.cache = cache;
            return this;
        }

        /**
         * setCache
         * @return  Builder
         */
        public Builder addCache(String cacheFileName) {
            if (!TextUtils.isEmpty(cacheFileName)) {
                this.cacheFileName = cacheFileName;
            }
            try {
                this.cache = new Cache(new File(mContext.getCacheDir(), this.cacheFileName), cacheMaxSize);
            } catch (Exception e) {
                ALog.e("Could not create http cache");
                ALog.e(e);
            }
            return this;
        }

        /**
         * setCache
         * @return  Builder
         */
        public Builder addCache() {
            try {
                this.cache = new Cache(new File(mContext.getCacheDir(), "Http_cache"), cacheMaxSize);
            } catch (Exception e) {
                ALog.e("Could not create http cache");
                ALog.e(e);
            }
            return this;
        }

        public Builder addCacheInterceptor() {
            this.cacheInterceptor = new CacheInterceptor(mContext);
            return this;
        }

        public Builder addCacheInterceptor(int maxStale) {
            this.cacheInterceptor = new CacheInterceptor(mContext, String.format("max-age=%d", maxStale));
            return this;
        }

        public RetrofitClient build() {
            if (TextUtils.isEmpty(baseUrl)) {
                baseUrl = BaseApiService.BASE_URL;
            }

            if (okhttpBuilder == null) {
                throw new IllegalStateException("okhttpBuilder required.");
            }

            if (retrofitBuilder == null) {
                throw new IllegalStateException("retrofitBuilder required.");
            }

            if (writeTimeout != -1) {
                okhttpBuilder.writeTimeout(writeTimeout, writeUnit);
            } else {
                okhttpBuilder.writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
            }

            if (connectTimeout != -1) {
                okhttpBuilder.writeTimeout(connectTimeout, connectUnit);
            } else {
                okhttpBuilder.writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
            }

            if (headers!=null && !headers.isEmpty()) {
                okhttpBuilder.addInterceptor(new BaseInterceptor(headers));
            }

            if (parameters!=null && !parameters.isEmpty()) {
                okhttpBuilder.addInterceptor(new BaseInterceptor(parameters));
            }

            if (isLog) {
                okhttpBuilder.addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS));
            }

            if(sslSocketFactory != null) {
                okhttpBuilder.sslSocketFactory(sslSocketFactory);
            }

            if (hostnameVerifier != null) {
                okhttpBuilder.hostnameVerifier(hostnameVerifier);
            }

            if (cache == null) {
                addCache();
            }
            okhttpBuilder.cache(cache);

            if (cacheInterceptor == null) {
                addCacheInterceptor();
            }
            okhttpBuilder.addInterceptor(cacheInterceptor);
            okhttpBuilder.addNetworkInterceptor(cacheInterceptor);

            /**
             * Sets the connection pool used to recycle HTTP and HTTPS connections.
             *
             * <p>If unset, a new connection pool will be used.
             */
            if (connectionPool == null) {
                connectionPool = new ConnectionPool(DEFAULT_MAXIDLE_CONNECTIONS, DEFAULT_KEEP_ALIVEDURATION, TimeUnit.SECONDS);
            }
            okhttpBuilder.connectionPool(connectionPool);

            /**
             * Sets the HTTP proxy that will be used by connections created by this client. This takes
             * precedence over {@link #proxySelector}, which is only honored when this proxy is null (which
             * it is by default). To disable proxy use completely, call {@code setProxy(Proxy.NO_PROXY)}.
             */
            if (proxy != null) {
                okhttpBuilder.proxy(proxy);
            }

            /**
             * Sets the handler that can accept cookies from incoming HTTP responses and provides cookies to
             * outgoing HTTP requests.
             *
             * <p>If unset, {@link Novate CookieManager#NO_COOKIES no cookies} will be accepted nor provided.
             */
            if (cookieManager == null) {
                cookieManager = new NovateCookieManger(mContext);
            }
            okhttpBuilder.cookieJar(new NovateCookieManger(mContext));

            /**
             * Set a fixed API base URL.
             * @see #baseUrl(HttpUrl)
             */
            retrofitBuilder.baseUrl(baseUrl);

            /** Add converter factory for serialization and deserialization of objects. */
            if (converterFactory == null) {
                converterFactory = GsonConverterFactory.create();
            }
            retrofitBuilder.addConverterFactory(converterFactory);

            /**
             * Add a call adapter factory for supporting service method return types other than {@link
             * Call}.
             */
            if (callAdapterFactory == null) {
                callAdapterFactory = RxJavaCallAdapterFactory.create();
            }
            retrofitBuilder.addCallAdapterFactory(callAdapterFactory);

            /**
             *okhttp3.Call.Factory callFactory = this.callFactory;
             */
            if (callFactory != null) {
                retrofitBuilder.callFactory(callFactory);
            }

            /**
             * set Retrofit client
             */
            retrofitBuilder.client(okhttpBuilder.build());

            /**
             * create Retrofit
             */
            retrofit = retrofitBuilder.build();
            /**
             *create BaseApiService;
             */
            apiManager = retrofit.create(BaseApiService.class);
            return new RetrofitClient(this);
        }
    }

    public static <T> T checkNotNull(T object, String message) {
        if (object == null) {
            throw new NullPointerException(message);
        }
        return object;
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
            DownLoadManager.getInstance(progressCallBack).writeResponseBodyToDisk(builder.mContext, (okhttp3.ResponseBody) responseBody);
        }
    }
}
