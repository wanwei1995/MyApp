package com.example.myapp.ui.main.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.myapp.BaseActivity;
import com.example.myapp.R;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SocketActivity extends BaseActivity {

    @BindView(R.id.info)
    EditText info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket);
    }


    public void print(View view) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                //获取本机InetAddress的实例：
                StringBuilder stringBuilder = new StringBuilder();
                Message message = new Message();
                try{
                    InetAddress address = InetAddress.getLocalHost();
                    stringBuilder.append("本机名：" + address.getHostName()+"\n");
                    stringBuilder.append("IP地址：" + address.getHostAddress()+"\n");
                    byte[] bytes = address.getAddress();
                    stringBuilder.append("字节数组形式的IP地址：" + Arrays.toString(bytes)+"\n");
                    stringBuilder.append("直接输出InetAddress对象：" + address);
                    message.what = 0;
                    message.obj = stringBuilder.toString();
                    handler.sendMessage(message);
                }catch (Exception e){
                    message.what = 1;
                    message.obj = e.getMessage();
                    handler.sendMessage(message);
                }
            }
        }).start();
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            if(0 == msg.what){
                info.setText((String)msg.obj);
            }else{
                Toast.makeText(SocketActivity.this, (String)msg.obj, Toast.LENGTH_SHORT).show();
            }
        }
    };


}
