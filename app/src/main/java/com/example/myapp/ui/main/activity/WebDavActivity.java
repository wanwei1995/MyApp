package com.example.myapp.ui.main.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.myapp.BaseActivity;
import com.example.myapp.R;
import com.example.myapp.util.ToastUtils;
import com.thegrizzlylabs.sardineandroid.DavResource;
import com.thegrizzlylabs.sardineandroid.Sardine;
import com.thegrizzlylabs.sardineandroid.impl.OkHttpSardine;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

//文件服务器测试页面
public class WebDavActivity extends BaseActivity {

    @BindView(R.id.test)
    TextView test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_dav);
        new Thread(new Runnable() {
            @Override
            public void run() {
                //webdav连接服务器测试
                Sardine sardine= new OkHttpSardine();
                sardine.setCredentials("1254837494@qq.com","aacqkt2pjkdqwgwg");

                try {
                    //如果目录不存在,则创建目录
                    if (!sardine.exists("https://dav.jianguoyun.com/dav/myApp/food/")){
                        sardine.createDirectory("https://dav.jianguoyun.com/dav/myApp/food/");
                    }
                    //
                    String str = "1,2,3,4";
                    byte[] bytes = str.getBytes();
                    sardine.put("https://dav.jianguoyun.com/dav/myApp/food.txt", bytes);
                    handler.sendEmptyMessage(0);

                    //
                    InputStream is = sardine.get("https://dav.jianguoyun.com/dav/myApp/food.txt");

                    ByteArrayOutputStream result = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = is.read(buffer)) != -1) {
                        result.write(buffer, 0, length);
                    }
                    String str1 = result.toString(StandardCharsets.UTF_8.name());
                    System.out.println(str1);

                }catch (Exception e){
                    System.out.println(e);
                    handler.sendEmptyMessage(1);

                }
            }
        }).start();
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            if(0 == msg.what){




            }else{
                Toast.makeText(WebDavActivity.this, (String)msg.obj, Toast.LENGTH_SHORT).show();
            }
        }
    };


}
