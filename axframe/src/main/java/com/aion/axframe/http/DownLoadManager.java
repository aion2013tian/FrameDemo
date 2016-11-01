package com.aion.axframe.http;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.aion.axframe.utils.log.ALog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;

/**
 * Created by Aion on 16/9/26.
 */
public class DownLoadManager {

    private ProgressCallBack progressCallBack;

    private static final String TAG = "DownLoadManager";

    private static String APK_CONTENTTYPE = "application/vnd.android.package-archive";

    private static String PNG_CONTENTTYPE = "image/png";

    private static String JPG_CONTENTTYPE = "image/jpg";

    private static String fileSuffix="";

    public static boolean isDownLoading = false;

    public static boolean isCancel = false;

    private Handler handler;

    public DownLoadManager(ProgressCallBack progressCallBack) {
        this.progressCallBack = progressCallBack;
    }

    private static DownLoadManager sInstance;

    /**
     *DownLoadManager getInstance
     */
    public static synchronized DownLoadManager getInstance(ProgressCallBack progressCallBack) {
        if (sInstance == null) {
            sInstance = new DownLoadManager(progressCallBack);
        }
        return sInstance;
    }



    public boolean  writeResponseBodyToDisk(Context context, ResponseBody body) {

        ALog.d(TAG, "contentType:>>>>"+ body.contentType().toString());

        String type = body.contentType().toString();

        if (type.equals(APK_CONTENTTYPE)) {

            fileSuffix = ".apk";
        } else if (type.equals(PNG_CONTENTTYPE)) {
            fileSuffix = ".png";
        } else if (type.equals(JPG_CONTENTTYPE)) {
            fileSuffix = ".jpg";
        }

        // 其他同上 自己判断加入

        final String name = System.currentTimeMillis() + fileSuffix;
        final String path = context.getExternalFilesDir(null) + File.separator + name;

        ALog.d(TAG, "path:>>>>"+ path);

        try {
            // todo change the file location/name according to your needs
            File futureStudioIconFile = new File(path);

            if (futureStudioIconFile.exists()) {
                futureStudioIconFile.delete();
            }

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                final long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;
                ALog.d(TAG, "file length: "+ fileSize);
                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                    ALog.d(TAG, "file download: " + fileSizeDownloaded + " of " + fileSize);
                    if (progressCallBack != null) {
                        handler = new Handler(Looper.getMainLooper());
                        final long finalFileSizeDownloaded = fileSizeDownloaded;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                progressCallBack.onProgress(finalFileSizeDownloaded);
                            }
                        });

                    }
                }

                outputStream.flush();
                ALog.d(TAG, "file downloaded: " + fileSizeDownloaded + " of " + fileSize);
                if (progressCallBack != null) {
                    handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressCallBack.onSucess(path, name, fileSize);

                        }
                    });
                    ALog.d(TAG, "file downloaded: " + fileSizeDownloaded + " of " + fileSize);
                }

                return true;
            } catch (IOException e) {
                if (progressCallBack != null) {
                    progressCallBack.onError(e);
                }
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            if (progressCallBack != null) {
                progressCallBack.onError(e);
            }
            return false;
        }
    }
}
