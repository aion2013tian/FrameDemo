package com.aion.axframe.http;

import android.content.Context;
import android.widget.Toast;

import com.aion.axframe.common.AXBaseApplication;
import com.aion.axframe.utils.NetworkUtil;

import rx.Subscriber;

/**
 * Created by Aion on 16/9/26.
 */
public abstract class BaseSubscriber<T> extends Subscriber<T> {

    private Context context;

    public BaseSubscriber() {

    }

    public BaseSubscriber(Context context) {
        this.context = context;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onNext(T t) {

    }

    @Override
    public void onStart() {
        super.onStart();
        if (context != null) {
            if (!NetworkUtil.isNetworkAvailable(context)) {
                Toast.makeText(context, "无网络，读取缓存数据", Toast.LENGTH_SHORT).show();
                onCompleted();
            }
        } else {
            if (!NetworkUtil.isNetworkAvailable(AXBaseApplication.getInstance())) {
                Toast.makeText(AXBaseApplication.getInstance(), "无网络，读取缓存数据", Toast.LENGTH_SHORT).show();
                onCompleted();
            }
        }
    }
}
