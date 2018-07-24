package com.httpmodule.interfaces;

import com.httpmodule.model.RequestBean;

import okhttp3.Response;

/**
 * Created by apple on 16/7/15.
 */
public abstract class OkHttpCallBack<T> implements BaseCallBack<T,Response> {


    @Override
    public void onSuccess(T obj) {

    }

    @Override
    public void onError(Exception e) {

    }

    @Override
    public void onBefore(RequestBean bean) {

    }

    @Override
    public void onAfter() {

    }

    @Override
    public void onUpdateProgress(int progress) {

    }

    @Override
    public T parseResponse(Response response) {
        return parseResult(response);
    }

    /**
     * 解析数据接口
     * @return
     */
    public abstract T parseResult(Response response);

   public static final OkHttpCallBack DEFAULT_CALLBACK = new OkHttpCallBack() {
        @Override
        public Object parseResult(Response response) {
            return null;
        }
    };
}
