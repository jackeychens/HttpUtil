package com.httpmodule.http.okhttp.request;

import com.httpmodule.cache.CacheMode;
import com.httpmodule.interfaces.HttpManager;
import com.httpmodule.interfaces.OkHttpCallBack;
import com.httpmodule.model.HttpParams;
import com.httpmodule.model.RequestBean;
import com.httpmodule.http.okhttp.OkHttpManager;
import com.httpmodule.http.okhttp.intercepter.BaseInterceptor;
import com.httpmodule.http.okhttp.intercepter.RetryIntecept;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by apple on 16/7/8.
 */
public abstract  class OkHttpRequest {
    RequestBean request;
    Request.Builder builder = new Request.Builder();
    protected String url;
    protected Object tag;
    protected HttpParams params = new HttpParams();
    protected CacheMode cacheMode;
    protected List<BaseInterceptor> interceptors = new ArrayList<>();
    protected long readTimeOut;
    protected long writeTimeOut;
    protected long connTimeOut;


    public OkHttpRequest(RequestBean request){
        this.request = request;
        init();
    }


    public void init(){
        if (this.request==null){
            new NullPointerException("RequestBean is null,you must set a RequestBean object in to OkHttpRequest");
        }
        this.url = request.getUrl();
        this.tag = request.getTag()==null?request.getTag():request;
        this.params = request.getParams();
        this.cacheMode = request.getCacheMode();
        this.connTimeOut = request.getConnectTimeOut()>0?request.getConnectTimeOut():0;
        this.writeTimeOut = request.getWriteTimeout()>0?request.getWriteTimeout():0;
        this.readTimeOut = request.getReadTimeOut()>0?request.getReadTimeOut():0;
        if (request.isRetryFlag()&&request.getRetryCount()>0){
            RetryIntecept retryIntecept = new RetryIntecept(request.getRetryCount());
            this.interceptors.add(retryIntecept);
        }
    }



    //对RequestBody进行二次封装
    protected RequestBody wrapRequestBody(RequestBody requestBody, final OkHttpCallBack callback) {
        ProgressRequestBody progressRequestBody = new ProgressRequestBody(requestBody, new ProgressRequestBody.Listener() {
            @Override
            public void onRequestProgress(final long bytesWritten, final long contentLength, final long networkSpeed) {
                ((OkHttpManager) HttpManager.getInstance(HttpManager.NetType.NET_OKHTTP)).getDelivery().post(new Runnable() {
                    @Override
                    public void run() {
                        if (callback != null)
                            callback.onUpdateProgress((int)(bytesWritten * 1.0f / contentLength));
                    }
                });
            }
        });
        return progressRequestBody;
    }

    public Request generateRequests(OkHttpCallBack callback){
        RequestBody requestBody = wrapRequestBody(builderRequestBody(),callback);
        return  builerRequest(requestBody);
    }


    public abstract Request builerRequest(RequestBody requestBody);

    public abstract RequestBody builderRequestBody();


    public OkHttpRequestCall builder(){
        return new OkHttpRequestCall(this);
    }

    public List<BaseInterceptor> getInterceptors() {
        return interceptors;
    }

    public CacheMode getCacheMode() {
        return cacheMode;
    }

    public String getUrl() {
        return url;
    }

    public Object getTag() {
        return tag;
    }

    public HttpParams getParams() {
        return params;
    }


    public RequestBean getRequest() {
        return request;
    }
}
