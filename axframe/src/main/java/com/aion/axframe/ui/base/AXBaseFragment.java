package com.aion.axframe.ui.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by Aion on 16/11/3.
 */

public abstract class AXBaseFragment<T extends AXBasePresenter> extends SupportFragment implements AXBaseView {

    protected T mPresenter;
    protected View rootView;//Fragment布局根view
    protected Activity mActivity;
    protected Context mContext;

    /**
     * 控件初始化是否完成
     */
    private boolean isInited = false;

    @Override
    public void onAttach(Context context) {
        mActivity = (Activity) context;
        mContext = context;
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getLayout() != 0) {
            rootView = inflater.inflate(getLayout(), null);
        } else {
            rootView = super.onCreateView(inflater, container, savedInstanceState);
        }
//        rootView = inflater.inflate(getLayout(), null);
        initViewLayout(rootView);
        initPresenter();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.attachView(this);
//        if (savedInstanceState == null) {
//            if (!isHidden()) {
//                isInited = true;
//                initEventAndData();
//            }
//        } else {
//            if (!isSupportHidden()) {
//                isInited = true;
//                initEventAndData();
//            }
//        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (!isInited) {
                isInited = true;
            } else {
                initEventAndData();
            }
        } else {
            if (!isInited) {
                isInited = true;
            } else {
                onUserInvisible();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
            mPresenter = null;
        }
    }

    @Override
    public void useNightMode(boolean isNight) {

    }

    /**
     * 初始化Presenter实例
     */
    protected abstract void initPresenter();

    /**
     * 布局文件id
     * @return
     */
    protected abstract int getLayout();

    /**
     * 初始化布局控件
     * @param mView
     */
    protected abstract void initViewLayout(View mView);

    /**
     * 用户可见是调用填充数据
     */
    protected abstract void initEventAndData();

    /**
     * 用户不可见时调用
     */
    protected abstract void onUserInvisible();
}
