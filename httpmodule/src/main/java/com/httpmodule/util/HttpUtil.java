package com.httpmodule.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * Created by apple on 16/6/30.
 */
public class HttpUtil {

    /** 将传递进来的参数拼接成 url */
    public static String createUrlFromParams(String url, Map<String, List<String>> params) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(url);
            if (url.indexOf('&') > 0 || url.indexOf('?') > 0) sb.append("&");
            else sb.append("?");
            for (Map.Entry<String, List<String>> urlParams : params.entrySet()) {
                List<String> urlValues = urlParams.getValue();
                for (String value : urlValues) {
                    String urlValue = URLEncoder.encode(value, "UTF-8");
                    sb.append(urlParams.getKey()).append("=").append(urlValue).append("&");
                }
            }
            sb.deleteCharAt(sb.length() - 1);
            return sb.toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return url;
    }


    public static String createCacheKey(String url){
        try {
            return URLEncoder.encode(url,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
