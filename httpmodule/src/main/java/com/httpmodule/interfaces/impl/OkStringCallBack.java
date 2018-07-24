package com.httpmodule.interfaces.impl;

import com.httpmodule.interfaces.OkHttpCallBack;

import java.io.IOException;

import okhttp3.Response;

/**
 * Created by apple on 16/7/15.
 */
public class OkStringCallBack extends OkHttpCallBack<String> {
    @Override
    public String parseResult(Response response) {
        try {
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  null;
    }

}
