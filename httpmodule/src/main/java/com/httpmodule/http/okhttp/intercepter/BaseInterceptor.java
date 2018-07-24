package com.httpmodule.http.okhttp.intercepter;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * Created by apple on 16/7/4.
 */
public class BaseInterceptor implements Interceptor {
    public BaseInterceptor() {
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        return chain.proceed(chain.request());
    }
}
