package com.httpmodule.cache;

import android.os.Environment;


import com.httpmodule.util.JsonUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 16/7/1.
 */
public class CacheDao<T> extends CacheBaseDao<CacheEntity<T>> {
    private static final String path = "/net_cache/";

    public CacheDao(){
        this.dirPath = Environment.getExternalStorageDirectory()+path;
    }


    public CacheDao(String path){
        this.dirPath = path;
    }
    @Override
    public CacheEntity parseDataToBean(String data) {
        CacheEntity<T> cacheEntity = JsonUtils.fromJson(data,CacheEntity.class);
        return cacheEntity;
    }


    @Override
    public void replace(String key,CacheEntity t) {
        String fileName = t.getKey();
        remove(fileName);
        File file = FileUtil.createNewFile(dirPath,fileName);
        String data = JsonUtils.toJson(t);
        FileUtil.writeFile(file,data);
    }

    @Override
    public CacheEntity getKey(String key) {
        String data = FileUtil.readFile(dirPath,key);
        CacheEntity entity = parseDataToBean(data);
        return entity;
    }



    @Override
    public List<CacheEntity<T>> getAllEntity() {
        List<File> fileList = getAllFile(dirPath);
        if (fileList==null)
            return null;
        List<CacheEntity<T>> entityList = new ArrayList<>();
        for (File file :fileList) {
            if (file.exists()&&file.isFile()){
                String data = FileUtil.readFile(file);
                CacheEntity entity = parseDataToBean(data);
                entityList.add(entity);
            }
        }
        return entityList;
    }
}
