package com.httpmodule.http.okhttp.request;


import com.httpmodule.cache.CacheEntity;
import com.httpmodule.cache.CacheManager;
import com.httpmodule.cache.CacheMode;
import com.httpmodule.cache.RequestCacheDao;
import com.httpmodule.http.okhttp.OkHttpManager;
import com.httpmodule.interfaces.HttpManager;
import com.httpmodule.interfaces.OkHttpCallBack;
import com.httpmodule.model.RequestBean;
import com.httpmodule.util.HttpUtil;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by apple on 16/7/8.
 * 负责执行request
 */
public class OkHttpRequestCall {

    private Request request;
    private Call call;
    private OkHttpClient clone;
    private OkHttpRequest baseRequest;
    private CacheManager cacheManager;

    public OkHttpRequestCall(OkHttpRequest baseRequest) {
        this.baseRequest = baseRequest;
        cacheManager = CacheManager.INSTANCE;
    }

    public void execute(OkHttpCallBack callback)
    {
        generateCalls(callback);

        if (callback != null)
        {
            callback.onBefore(baseRequest.getRequest());
        }

        execute(this, callback);
    }

    private  <T> void execute(final OkHttpRequestCall requestCall, OkHttpCallBack callback) {
        if (callback == null)
            callback = OkHttpCallBack.DEFAULT_CALLBACK;
        final OkHttpCallBack finalCallback = callback;

        final String cacheKey = HttpUtil.createCacheKey(HttpUtil.createUrlFromParams(baseRequest.getUrl(),baseRequest.getParams().urlParamsMap));
        final CacheEntity<T> cacheEntity = (CacheEntity<T>) cacheManager.get(cacheKey);
        if (baseRequest.getCacheMode()== CacheMode.ONLY_CACHE){
            if (cacheEntity!=null)
                try {
                    T t = cacheEntity.getData();
                    sendSuccessResultCallback(t, finalCallback);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            return;
        }
        //先从本地获取 再从网络获取
        if (baseRequest.getCacheMode() == CacheMode.CACHE_ELSE_NETWORK){
            if (cacheEntity!=null)
                try {
                    T t = cacheEntity.getData();
                    sendSuccessResultCallback(t, finalCallback);
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
        call.enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                if (baseRequest.getCacheMode()==CacheMode.NETWORK_ELSE_CACHE){
                    try {
                        if (cacheEntity!=null){
                            T t =  cacheEntity.getData();
                            sendSuccessResultCallback(t, finalCallback);
                        }
                    } catch (Exception e0) {
                        sendFailResultCallback(e0, finalCallback);
                    }
                }else{
                    sendFailResultCallback(e, finalCallback);
                }
            }

            @Override
            public void onResponse(final Call call, final Response response) {
                if (response.code() >= 400 && response.code() <= 599) {
                    try {
                        sendFailResultCallback(new RuntimeException(response.body().string()), finalCallback);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return;
                }
                if (response.code() == 200) {
                    try {
                        T o = (T) finalCallback.parseResponse(response);
                        sendSuccessResultCallback(o, finalCallback);
                        handleCache(cacheKey,o);
                    } catch (Exception e) {
                        e.printStackTrace();
                        sendFailResultCallback(e, finalCallback);
                    }
                }
            }
        });
    }

    public <T> void handleCache(String cacheKey,T t){
        CacheEntity cacheEntity = new CacheEntity();
        cacheEntity.setData(t);
        cacheEntity.setKey(cacheKey);
        cacheManager.replace(cacheKey,cacheEntity);
    }


    private  <T> Call generateCalls(OkHttpCallBack<T> callback) {
        request = generateRequests(callback);
        long readTimeOut = baseRequest.readTimeOut;
        long writeTimeOut = baseRequest.writeTimeOut;
        long connTimeOut = baseRequest.connTimeOut;

        OkHttpClient.Builder newBuilder = new OkHttpClient.Builder();

        readTimeOut = readTimeOut > 0 ? readTimeOut : 0;
        writeTimeOut = writeTimeOut > 0 ? writeTimeOut : 0;
        connTimeOut = connTimeOut > 0 ? connTimeOut : 0;

        if (baseRequest.getInterceptors().size() > 0) {
            for (Interceptor interceptor : baseRequest.getInterceptors()) {
                newBuilder.addInterceptor(interceptor);
            }
        }
        clone = newBuilder
                .readTimeout(readTimeOut, TimeUnit.MILLISECONDS)
                .writeTimeout(writeTimeOut, TimeUnit.MILLISECONDS)
                .connectTimeout(connTimeOut, TimeUnit.MILLISECONDS)
                .build();

        call = clone.newCall(request);
        return call;
    }




    public Request generateRequests(OkHttpCallBack callback) {
       return baseRequest.generateRequests(callback);
    }



    public void sendFailResultCallback(final Exception e, final OkHttpCallBack callback) {
        RequestBean requestBean = baseRequest.getRequest();
        RequestCacheDao dao = new RequestCacheDao();
        final String cacheKey = HttpUtil.createCacheKey(HttpUtil.createUrlFromParams(baseRequest.getUrl(),baseRequest.getParams().urlParamsMap));
        dao.replace(cacheKey,requestBean);
        callback.onError(e);
        ((OkHttpManager)HttpManager.getInstance(HttpManager.NetType.NET_OKHTTP)).getDelivery().post(new Runnable() {
            @Override
            public void run() {
                callback.onAfter();
            }
        });
    }

    public void sendSuccessResultCallback(final Object object, final OkHttpCallBack callback) {
        if (baseRequest.getRequest().isResponseOnUi()){
            sendSuccessOnUiThread(object,callback);
        }else{
            sendSuccessOnWorkThread(object,callback );
        }
    }

    public void sendSuccessOnUiThread(final Object object, final OkHttpCallBack callback){
        ((OkHttpManager)HttpManager.getInstance(HttpManager.NetType.NET_OKHTTP)).getDelivery().post(new Runnable() {
            @Override
            public void run() {
                callback.onSuccess(object);
                callback.onAfter();
            }
        });
    }

    public void sendSuccessOnWorkThread(final Object object, final OkHttpCallBack callback){
        callback.onSuccess(object);
        ((OkHttpManager)HttpManager.getInstance(HttpManager.NetType.NET_OKHTTP)).getDelivery().post(new Runnable() {
            @Override
            public void run() {
                callback.onAfter();
            }
        });
    }
}
