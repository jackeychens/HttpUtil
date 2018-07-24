package com.httpmodule.http.okhttp.intercepter;

import android.util.Log;

import java.io.IOException;

import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by apple on 16/6/28.
 */
public class RetryIntecept extends BaseInterceptor {
    //重试次数
    public static final int DEFAULT_RETRY_COUNT = 3;
    private int retryCount = 0;
    public RetryIntecept(int count){
        if (count==0)
            retryCount = DEFAULT_RETRY_COUNT;
        this.retryCount = count;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = null;
        boolean responseOK = false;
        int tryCount = 0;
        while (!responseOK && tryCount < retryCount) {
            try {
                response = chain.proceed(request);
                if (response!=null) {
                    responseOK = response.isSuccessful();
                    if (responseOK) {
                        return response;

                    }
                }
            }catch (Exception e){
                Log.e("intercept", "Request is not successful = " + tryCount);
//                e.printStackTrace();
                Log.e("intercept", e.getLocalizedMessage());
            }finally{
                tryCount++;
            }
        }
        return chain.proceed(request);
    }

}
