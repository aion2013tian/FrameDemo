package com.aion.axframe.ui.base;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Aion on 16/11/2.
 * 基于Rx的Presenter封装,控制订阅的生命周期
 * 若要控制订阅的生命周期，需要继承该类
 */

public class RxAXBasePresenter<T extends AXBaseView> implements AXBasePresenter<T> {

    protected T mView;
    private CompositeSubscription mCompositeSubscription;

    private void unSubscribe() {
        if (mCompositeSubscription != null) {
            mCompositeSubscription.unsubscribe();
        }
    }

    protected void addSubscrebe(Subscription subscription) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(subscription);
    }

    @Override
    public void attachView(T view) {
        this.mView = view;
    }

    @Override
    public void detachView() {
        this.mView = null;
        unSubscribe();
    }
}
