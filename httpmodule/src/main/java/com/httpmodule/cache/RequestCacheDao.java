package com.httpmodule.cache;

import android.os.Environment;

import com.httpmodule.model.RequestBean;
import com.httpmodule.util.JsonUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 16/7/1.
 */
public class RequestCacheDao extends CacheBaseDao<RequestBean> {
    private static final String DEFAULT_PATH = Environment.getExternalStorageDirectory()+"/requestCache/";

    public RequestCacheDao() {
        this.dirPath = DEFAULT_PATH;
    }

    public RequestCacheDao(String path){
        this.dirPath = path;
    }

    @Override
    public RequestBean parseDataToBean(String data) {
        return  JsonUtils.fromJson(data,RequestBean.class);
    }

    @Override
    public void replace(String key,RequestBean request) {
        remove(key);
        File file = FileUtil.createNewFile(dirPath,key);
        FileUtil.writeFile(file,JsonUtils.toJson(request));
    }

    @Override
    public RequestBean getKey(String key) {
        File file = getFile(key);
        String data = FileUtil.readFile(file);
        return JsonUtils.fromJson(data,RequestBean.class);
    }


    @Override
    public List<RequestBean> getAllEntity() {
        List<RequestBean> requestList = new ArrayList<>();
        List<File> fileList = getAllFile(dirPath);
        if (fileList==null)
            return requestList;
        for (File file :fileList) {
            if (file.exists()&&file.isFile()){
                String data = FileUtil.readFile(file);
                RequestBean request = parseDataToBean(data);
                requestList.add(request);
            }
        }
        return requestList;
    }
}
