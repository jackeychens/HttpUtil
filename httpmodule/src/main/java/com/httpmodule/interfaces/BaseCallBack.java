package com.httpmodule.interfaces;

import com.httpmodule.model.RequestBean;

/**
 * Created by apple on 16/7/15.
 */
public interface BaseCallBack<T,K> {

    void onSuccess(T obj);


    void onError(Exception e);


    void onBefore(RequestBean bean);


    void onAfter();

    void onUpdateProgress(int progress);

    T parseResponse(K obj);
}
