package com.bojan.sdk.myokhttp;

import android.os.Handler;
import android.os.Looper;

import com.bojan.sdk.myokhttp.builder.DeleteBuilder;
import com.bojan.sdk.myokhttp.builder.DownloadBuilder;
import com.bojan.sdk.myokhttp.builder.GetBuilder;
import com.bojan.sdk.myokhttp.builder.PatchBuilder;
import com.bojan.sdk.myokhttp.builder.PostBuilder;
import com.bojan.sdk.myokhttp.builder.PutBuilder;
import com.bojan.sdk.myokhttp.builder.UploadBuilder;

import okhttp3.Call;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;

/**
 * MyOkhttp
 * Created by tsy on 16/9/14.
 */
public class MyOkHttp {
    private volatile static MyOkHttp mInstance;
    private OkHttpClient mOkHttpClient;
    public static Handler mHandler = new Handler(Looper.getMainLooper());
    public static String BASE_URL = "http://vonlink.com/";

    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }
    public static String getBaseUrl() {
        return BASE_URL;
    }

    public static void setBaseUrl(String baseUrl) {
        BASE_URL = baseUrl;
    }
    /**
     * construct
     */
    public MyOkHttp() {
        this(null);
    }

    /**
     * construct
     *
     * @param okHttpClient custom okhttpclient
     */
    public MyOkHttp(OkHttpClient okHttpClient) {
        if (mOkHttpClient == null) {
            synchronized (MyOkHttp.class) {
                if (mOkHttpClient == null) {
                    if (okHttpClient == null) {
                        mOkHttpClient = new OkHttpClient();
                    } else {
                        mOkHttpClient = okHttpClient;
                    }
                }
            }
        }
    }

    public static MyOkHttp initClient(OkHttpClient okHttpClient) {
        if (mInstance == null) {
            synchronized (MyOkHttp.class) {
                if (mInstance == null) {
                    mInstance = new MyOkHttp(okHttpClient);
                }
            }
        }
        return mInstance;
    }

    public static MyOkHttp getInstance() {
        return initClient(null);
    }

    public static GetBuilder get() {
        return new GetBuilder(getInstance());
    }

    public static PostBuilder post() {
        return new PostBuilder(getInstance());
    }

    public static PutBuilder put() {
        return new PutBuilder(getInstance());
    }

    public static PatchBuilder patch() {
        return new PatchBuilder(getInstance());
    }

    public static DeleteBuilder delete() {
        return new DeleteBuilder(getInstance());
    }

    public static UploadBuilder upload() {
        return new UploadBuilder(getInstance());
    }

    public static DownloadBuilder download() {
        return new DownloadBuilder(getInstance());
    }

    /**
     * do cacel by tag
     *
     * @param tag tag
     */
    public void cancel(Object tag) {
        Dispatcher dispatcher = mOkHttpClient.dispatcher();
        for (Call call : dispatcher.queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : dispatcher.runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }
}
