package com.httpmodule.cache;




import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 16/7/1.
 */
public  abstract  class CacheBaseDao<T> {
    protected String dirPath;
    public abstract T parseDataToBean(String data);




    private void deleteAll(File dirFile){
        if (dirFile.exists()){
            File[] fileList = dirFile.listFiles();
            for(int i= 0;i<fileList.length;i++){
                if (fileList[i].isFile())
                    fileList[i].delete();
                else {
                    deleteAll(fileList[i]);
                }
            }
        }
    }

    public void deleteAll(){
        File file = new File(dirPath);
        delete(file);
    }

    public List<File> getAllFile(String dirPath){
        File file = new File(dirPath);
        if (file.exists()){
            File[] fileList = file.listFiles();
            List<File> files = new ArrayList<>();
            int lenght = fileList.length;
            for (int i = 0; i< lenght;i++){
                files.add(fileList[i]);
            }
            return files;
        }
        return null;
    }

    public abstract void replace(String key,T t);

    public abstract T getKey(String key);

    public File getFile(String fileName){
        File file = new File(dirPath,fileName);
        if (file.exists()){
            return file;
        }
        return null;
    }


    public  void remove(String key){
        File file = new File(dirPath,key);
        if (file.exists()&&file.isFile()){
            file.delete();
        }
    };
    public abstract List<T> getAllEntity();
    //加密
    public String encrypKey(String key){
        return null;
    }

    //解密
    public String decrypKey(String key){
        return null;
    }

    public void delete(File file){
        if (file!=null){
            if (file.exists())
                file.delete();
        }
    }
}
