package com.bojan.sdk.myokhttp.Interceptor;

import com.bojan.sdk.myokhttp.util.LogUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class RetryIntercepter implements Interceptor {
    public int maxRetry;                     //最大重试次数
    private int retryNum = 0;               //假如设置为3次重试的话，则最大可能请求4次（默认1次+3次重试）

    public RetryIntercepter(int maxRetry) {
        this.maxRetry = maxRetry;

    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        LogUtils.i("RetryIntercepter" +"retryNum=" + retryNum);
        Response response = chain.proceed(request);
        while (!response.isSuccessful() && retryNum < maxRetry) {
            retryNum++;
            LogUtils.i("RetryIntercepter" +"retryNum=" + retryNum);
            response = chain.proceed(request);
        }
        return response;
    }

}
