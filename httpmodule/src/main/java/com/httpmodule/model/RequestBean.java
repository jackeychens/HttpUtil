package com.httpmodule.model;

import com.httpmodule.cache.CacheMode;

/**
 * Created by Chenzm on 16/6/30.
 */
public class RequestBean {
    public final static long DEFAULT_CONNECTTIMEOUT = 0;
    public final static long DEFAULT_READTIMEOUT = 0;
    public final static long DEFAULT_WRITETIMEOUT = 0;

    private long connectTimeOut;
    private long readTimeOut;
    private long writeTimeout;
    private HttpMethod method = HttpMethod.GET;
    private String url;
    private HttpParams params = new HttpParams();
    private Object tag;
    private boolean retryFlag;
    private int retryCount;
    private String content;
    private PostType postType;

    //返回数据的缓存模式
    private CacheMode cacheMode;

    private boolean responseOnUi = true;


    public RequestBean setConnectTimeOut(long connectTimeOut) {
        this.connectTimeOut = connectTimeOut;
        return this;
    }

    public RequestBean setReadTimeOut(long readTimeOut) {
        this.readTimeOut = readTimeOut;
        return this;
    }

    public RequestBean setWriteTimeout(long writeTimeout) {
        this.writeTimeout = writeTimeout;
        return this;
    }

    public RequestBean setMethod(HttpMethod method) {
        this.method = method;
        return this;
    }

    public RequestBean setUrl(String url) {
        this.url = url;
        return this;
    }

    public RequestBean setParams(HttpParams params) {
        this.params = params;
        return this;
    }

    public RequestBean setTag(Object tag) {
        this.tag = tag;
        return this;
    }

    public RequestBean setRetryFlag(boolean retryFlag) {
        this.retryFlag = retryFlag;
        return this;
    }

    public RequestBean setRetryCount(int retryCount) {
        this.retryCount = retryCount;
        return this;
    }

    public RequestBean setCacheMode(CacheMode cacheMode) {
        this.cacheMode = cacheMode;
        return this;
    }



    public long getConnectTimeOut() {
        return connectTimeOut;
    }

    public long getReadTimeOut() {
        return readTimeOut;
    }

    public long getWriteTimeout() {
        return writeTimeout;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public HttpParams getParams() {
        return params;
    }

    public Object getTag() {
        return tag;
    }

    public boolean isRetryFlag() {
        return retryFlag;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public CacheMode getCacheMode() {
        return cacheMode;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public PostType getPostType() {
        return postType;
    }

    public void setPostType(PostType postType) {
        this.postType = postType;
    }

    public boolean isResponseOnUi() {
        return responseOnUi;
    }

    public void setResponseOnUi(boolean responseOnUi) {
        this.responseOnUi = responseOnUi;
    }

    public static RequestBean copy(RequestBean bean){
        RequestBean bean2 = new RequestBean();
        bean2.setUrl(bean.getUrl());
        bean2.setTag(bean.getTag());
        bean2.setCacheMode(bean.getCacheMode());
        bean2.setConnectTimeOut(bean.getConnectTimeOut());
        bean2.setReadTimeOut(bean.getReadTimeOut());
        bean2.setRetryCount(bean.getRetryCount());
        bean2.setRetryFlag(bean.isRetryFlag());
        bean2.setParams(bean.getParams());
        bean2.setMethod(bean.getMethod());
        bean2.setContent(bean.getContent());
        bean2.setPostType(bean.getPostType());
        bean2.setResponseOnUi(bean.isResponseOnUi());
        return  bean2;
    }

    public enum PostType{
        POST_JSON,
        POST_TXT,
        POST_MUTIFILE,
        POST_FORM,
    }
}
