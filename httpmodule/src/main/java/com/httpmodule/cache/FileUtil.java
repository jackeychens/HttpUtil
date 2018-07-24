package com.httpmodule.cache;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;

import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;
import okio.Sink;

/**
 * Created by apple on 16/7/1.
 */
public class FileUtil {


    public static File createNewFile(String path,String fileName){
        File file = new File(path);
        if (!file.exists()){
            file.mkdir();
        }
        File tempFile = new File(path,fileName);
        if (tempFile.exists()){
            tempFile.delete();
        }
        return tempFile;
    }



    public static String readFile(String path,String fileName){
        File file1 = new File(path,fileName);
        if (file1.exists()){
            return readFile(file1);
        }
       return null;
    }

    public static String readFile(File file){
        try {
            BufferedSource source = Okio.buffer(Okio.source(file));
            String data = source.readString(Charset.defaultCharset());
            return data;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean writeFile(String key,String path,String data){
        File file = createNewFile(path,key);
        return writeFile(file,data);
    }

    public static boolean writeFile(File file,String data){
        try {
            Sink sink = Okio.sink(file);
            BufferedSink bufferedSink = Okio.buffer(sink);
            bufferedSink.writeString(data, Charset.defaultCharset());
            bufferedSink.flush();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }







}
