package com.httpmodule.interfaces;


import com.httpmodule.http.okhttp.OkHttpManager;
import com.httpmodule.model.RequestBean;

/**
 * Created by apple on 16/7/11.
 * Http 公开接口
 */
public abstract class HttpManager {
    private static final Object OBJECT = new Object();
    protected static HttpManager instance;


    public static HttpManager getInstance(NetType type) {
        if (instance == null) {
            synchronized (OBJECT) {
                if (instance == null) {
                    if (type == NetType.NET_OKHTTP) {
                        instance = new OkHttpManager();
                    }
                }

            }
        }
        return instance;
    }

    public abstract void request(RequestBean bean, BaseCallBack listener);

    public abstract void cancle(Object tag);

    public enum NetType {

        NET_OKHTTP,

        NET_ASYNC,

    }

}
