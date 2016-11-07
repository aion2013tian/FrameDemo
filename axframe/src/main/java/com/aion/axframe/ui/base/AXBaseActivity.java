package com.aion.axframe.ui.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.aion.axframe.common.ActivityPageManager;

import me.yokeyword.fragmentation.SupportActivity;

/**
 * Created by Aion on 16/11/2.
 * MVP activity基类
 */

public abstract class AXBaseActivity<T extends AXBasePresenter> extends SupportActivity implements AXBaseView {

    protected T mPresenter;
    protected Activity mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        mContext = this;
        initViewLayout();
        initPresenter();
        if (mPresenter != null)
            mPresenter.attachView(this);
        ActivityPageManager.getInstance().addActivity(this);
        initEventAndData();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    protected void setToolBar(Toolbar toolbar, String title) {
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressedSupport();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
            mPresenter = null;
        }
        ActivityPageManager.getInstance().removeActivity(this);
    }

    @Override
    public void useNightMode(boolean isNight) {
        if (isNight) {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_NO);
        }
        recreate();
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
     */
    protected abstract void initViewLayout();

    /**
     * 填充数据
     */
    protected abstract void initEventAndData();
}
