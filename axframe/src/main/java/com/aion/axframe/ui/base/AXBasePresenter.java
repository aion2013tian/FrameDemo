package com.aion.axframe.ui.base;

/**
 * Created by Aion on 16/11/2.
 * MVP模式中---Presenter基类接口
 */

public interface AXBasePresenter<T extends AXBaseView> {

    void attachView(T view);

    void detachView();
}
