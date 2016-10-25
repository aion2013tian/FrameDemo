package com.aion.axframe.http;

/**
 * Created by Aion on 16/9/26.
 */
public abstract class ProgressCallBack {

    public void onStart(){}

    public void onCompleted(){}

    abstract public void onError(Throwable e);

    public void onProgress(long fileSizeDownloaded){}

    abstract public void onSucess(String path, String name, long fileSize);
}
