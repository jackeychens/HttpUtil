package com.http.lib;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.httpmodule.cache.CacheMode;
import com.httpmodule.interfaces.HttpManager;
import com.httpmodule.http.okhttp.OkHttpManager;
import com.httpmodule.interfaces.impl.OkStringCallBack;
import com.httpmodule.model.HttpMethod;
import com.httpmodule.model.RequestBean;
import com.httpmodule.util.JsonUtils;


public class MainActivity extends AppCompatActivity {
    TextView showContentView;
    public static final String url = "http://api.k780.com:88/?app=life.time&appkey=10003&sign=b59bc3ef6191eb9f747dd4e83c99f2a4&format=json";
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showContentView = (TextView) findViewById(R.id.showContent);
        final RequestBean bean = new RequestBean();
        bean.setUrl(url).setTag("button1").setMethod(HttpMethod.GET).setCacheMode(CacheMode.DEFAULT_CACHE);
        if (Build.VERSION.SDK_INT > 22) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                //申请WRITE_EXTERNAL_STORAGE权限
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 10);
            }
        }
        //从网络获取
        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showContentView.setText("");
                HttpManager manager = OkHttpManager.getInstance(HttpManager.NetType.NET_OKHTTP);
                manager.request(bean,new OkStringCallBack(){
                    @Override
                    public void onSuccess(String obj) {
                        super.onSuccess(obj);
                        final DataModel model = JsonUtils.fromJson(obj,DataModel.class);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                showContentView.setText(model.getResult().getDatetime_2());
                            }
                        });
                    }
                });

            }
        });

        //从缓存获取
        findViewById(R.id.btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showContentView.setText("");
                RequestBean bean2 = RequestBean.copy(bean);
                bean2.setCacheMode(CacheMode.ONLY_CACHE);
                OkHttpManager.getInstance(HttpManager.NetType.NET_OKHTTP).request(bean2,new OkStringCallBack(){
                    @Override
                    public void onSuccess(String obj) {
                        super.onSuccess(obj);
                        final DataModel model = JsonUtils.fromJson(obj,DataModel.class);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                showContentView.setText(model.getResult().getDatetime_2());
                            }
                        });
                    }
                });

            }
        });
        //先网络 失败 再缓存
        findViewById(R.id.btn3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showContentView.setText("");
            }
        });

        findViewById(R.id.btn4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showContentView.setText("");
            }
        });

        findViewById(R.id.btn6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showContentView.setText("");
            }
        });
        //http://haoma.baidu.com/search?m=13511111111&mobile=13511111111
        findViewById(R.id.btn7).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

//    public void test(){
//        final String path = Environment.getExternalStorageDirectory()+"/testss/";
//        File file = FileUtil.createNewFile(path,"test.txt");
//        Log.v("print"," path :"+file.getAbsolutePath());
//        FileUtil.writeFile(file,"测试测试测试测试傻傻傻傻傻傻傻傻傻傻 是是是是是是是是");
//
//        final FileTest test = new FileTest("复制文件的",file);
//        Log.v("print"," test ==== "+test.toString());
//        final File copyFile = FileUtil.createNewFile(path,"copyFile.txt");
//        String data  = JsonUtils.toJson(test);
//        Log.e("print"," data :"+data);
//        FileUtil.writeFile(copyFile,data);
//
//
//        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String string =  FileUtil.readFile(copyFile);
//                FileTest test1 = JsonUtils.fromJson(string,FileTest.class);
//                String data = FileUtil.readFile(test1.file);
//                File destFile = FileUtil.createNewFile(path,"第三个文件.txt");
//                FileUtil.writeFile(destFile,data);
//            }
//        });
//    }
//
//
//    class FileTest{
//        private String id;
//        private File file;
//
//        public FileTest(String id, File file) {
//            this.id = id;
//            this.file = file;
//        }
//        @Override
//        public String toString() {
//            return "FileTest{" +
//                    "id='" + id + '\'' +
//                    ", file=" + file +
//                    '}';
//        }
//    }
}
