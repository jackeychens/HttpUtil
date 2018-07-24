package com.httpmodule.cache;


/**
 * Created by apple on 16/6/29.
 */
public class CacheEntity<T> {
    private String key;
    private T data;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }


    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }


    @Override
    public String toString() {
        return "CacheEntity{" +
                ", key='" + key + '\'' +
                ", data=" + data +
                '}';
    }

}
