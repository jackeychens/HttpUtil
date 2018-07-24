package com.httpmodule.http.okhttp.request;

import com.httpmodule.model.HttpParams;
import com.httpmodule.model.RequestBean;

import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by apple on 16/7/8.
 */
public class PostOkHttpRequest extends OkHttpRequest {
    public static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json;charset=utf-8");
    public static final MediaType MEDIA_TYPE_TEXT = MediaType.parse("application/text;charset=utf-8");
    String content;
    RequestBean.PostType postType;
    public PostOkHttpRequest(RequestBean request) {
        super(request);
        this.content = request.getContent();
        this.postType = request.getPostType();
    }

    @Override
    public Request builerRequest(RequestBody requestBody) {
        return builder.post(requestBody).url(url).tag(tag).build();
    }

    @Override
    public RequestBody builderRequestBody() {
        if (content!=null && postType!=null &&postType== RequestBean.PostType.POST_JSON ){
            return RequestBody.create(MEDIA_TYPE_JSON,content);
        }
        if (content!=null && postType!=null&&postType== RequestBean.PostType.POST_TXT){
            return RequestBody.create(MEDIA_TYPE_TEXT,content);
        }
        return generateMultipartRequestBody();
    }


    /**
     * 生成类是表单的请求体
     */
    protected RequestBody generateMultipartRequestBody() {
        if (params.fileParamsMap.isEmpty()) {
            //表单提交，没有文件
            FormBody.Builder bodyBuilder = new FormBody.Builder();
            for (String key : params.urlParamsMap.keySet()) {
                List<String> urlValues = params.urlParamsMap.get(key);
                for (String value : urlValues) {
                    bodyBuilder.add(key, value);
                }
            }
            return bodyBuilder.build();
        } else {
            //表单提交，有文件
            MultipartBody.Builder multipartBodybuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
            //拼接键值对
            if (!params.urlParamsMap.isEmpty()) {
                for (Map.Entry<String, List<String>> entry : params.urlParamsMap.entrySet()) {
                    List<String> urlValues = entry.getValue();
                    for (String value : urlValues) {
                        multipartBodybuilder.addFormDataPart(entry.getKey(), value);
                    }
                }
            }
            //拼接文件
            for (Map.Entry<String, List<HttpParams.FileWrapper>> entry : params.fileParamsMap.entrySet()) {
                List<HttpParams.FileWrapper> fileValues = entry.getValue();
                for (HttpParams.FileWrapper fileWrapper : fileValues) {
                    RequestBody fileBody = RequestBody.create(guessMimeType(fileWrapper.fileName), fileWrapper.file);
                    multipartBodybuilder.addFormDataPart(entry.getKey(), fileWrapper.fileName, fileBody);
                }
            }
            return multipartBodybuilder.build();
        }
    }



    private MediaType guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentType = fileNameMap.getContentTypeFor(path);
        if (contentType == null) {
            contentType = "application/octet-stream";
        }
        return MediaType.parse(contentType);
    }

}
