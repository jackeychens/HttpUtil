package com.httpmodule.http.okhttp;

import android.os.Handler;
import android.os.Looper;

import com.httpmodule.interfaces.BaseCallBack;
import com.httpmodule.interfaces.HttpManager;
import com.httpmodule.interfaces.OkHttpCallBack;
import com.httpmodule.model.HttpMethod;
import com.httpmodule.model.RequestBean;
import com.httpmodule.http.okhttp.request.GetOkHttpRequest;
import com.httpmodule.http.okhttp.request.OkHttpRequestCall;
import com.httpmodule.http.okhttp.request.PostOkHttpRequest;

import okhttp3.Call;
import okhttp3.OkHttpClient;


/**
 * Created by apple on 16/7/8.
 */
public class OkHttpManager extends HttpManager{

    private Handler mDelivery;


    public OkHttpManager(){
        mDelivery = new Handler(Looper.getMainLooper());
    }

    public Handler getDelivery(){
        return mDelivery;
    }


    private void get(RequestBean request, OkHttpCallBack listener){
        GetOkHttpRequest okHttpRequest = new GetOkHttpRequest(request);
        OkHttpRequestCall requestCall = okHttpRequest.builder();
        requestCall.execute(listener);
    }

    private void post(RequestBean request, OkHttpCallBack listener){
        PostOkHttpRequest postOkHttpRequest = new PostOkHttpRequest(request);
        OkHttpRequestCall requestCall = postOkHttpRequest.builder();
        requestCall.execute(listener);
    }



    private void generalRequest(RequestBean bean,OkHttpCallBack listener){
        HttpMethod method = bean.getMethod();
        if (method == HttpMethod.GET){
            get(bean,listener);
        }else if(method == HttpMethod.POST){
            post(bean,listener);
        }
    }
    public void cancleTask(Object tag){
        OkHttpClient client = new OkHttpClient.Builder().build();
        for (Call call : client.dispatcher().queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : client.dispatcher().runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }
    @Override
    public void request(RequestBean bean, BaseCallBack listener) {
        if (bean==null){
            throw new NullPointerException(" requestbean must not be null");
        }
        if (listener instanceof OkHttpCallBack){
            get(bean,(OkHttpCallBack) listener);
        }
    }

    @Override
    public void cancle(Object tag) {
        cancleTask(tag);
    }
}
