package com.httpmodule.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;

/**
 * Created by apple on 16/7/1.
 */
public class JsonUtils {

    private static Gson gson = null;
    private static JsonParser mParser = new JsonParser();

    static {
        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm:ss").create();
    }

    public static <T> T fromJson(String json,Class<T> tClass){
        try {
            return gson.fromJson(json,tClass);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;

    }

    public static <T> String toJson(T t){
        return gson.toJson(t);
    }
}
