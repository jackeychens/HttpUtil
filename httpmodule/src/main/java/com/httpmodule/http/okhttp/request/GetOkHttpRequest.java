package com.httpmodule.http.okhttp.request;

import com.httpmodule.model.RequestBean;
import com.httpmodule.util.HttpUtil;

import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by apple on 16/7/8.
 * 构建一个get请求的 request
 */
public class GetOkHttpRequest extends OkHttpRequest{
    public GetOkHttpRequest(RequestBean request) {
        super(request);
    }

    @Override
    public Request builerRequest(RequestBody requestBody) {
        String url_Str = HttpUtil.createUrlFromParams(request.getUrl(), request.getParams().urlParamsMap);
        if (request.getTag()==null){
            return builder.get().url(url_Str).tag(this).build();
        }
        return builder.get().url(url_Str).tag(request.getTag()).build();
    }

    @Override
    public RequestBody builderRequestBody() {
        return null;
    }
}
