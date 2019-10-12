package com.example.myapp.ui.main2.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.example.myapp.BaseActivity;
import com.example.myapp.R;
import com.example.myapp.util.AlertDialogUtil;
import com.example.myapp.util.FileUtil;
import com.example.myapp.util.StringUtil;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.io.File;
import java.io.FileOutputStream;

public class CreateZxingActivity extends BaseActivity{

    @BindView(R.id.info)
    EditText infoEt;
    @BindView(R.id.create)
    Button create;
    @BindView(R.id.zxing_image)
    ImageView zxingImage;

    String info = "";

    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_zxing);

        ButterKnife.bind(this);

        zxingImage.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View view) {
                //长按存储图片
                //获取内部存储状态  
                String state = Environment.getExternalStorageState();
                //如果状态不是mounted，无法读写  
                if (!state.equals(Environment.MEDIA_MOUNTED)) {
                    Toast.makeText(CreateZxingActivity.this, "系统异常，无法保存", Toast.LENGTH_SHORT).show();
                    return true;
                }
                AlertDialogUtil.YesOrNo("是否保存到本地", CreateZxingActivity.this, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            File file = FileUtil.createPhotoPath("family", info);
                            FileOutputStream out = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                            //保存图片后发送广播通知更新数据库
                            Uri uri = Uri.fromFile(file);
                            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
                            out.flush();
                            out.close();
                            Toast.makeText(CreateZxingActivity.this, "已保存到手机，请到相册查看", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                });
                return true;
            }
        });
    }

    @OnClick(R.id.create)
    public void onViewClicked() {
        info = infoEt.getText().toString();
        if (StringUtil.isEmpty(info)) {
            Toast.makeText(this, "请输入内容", Toast.LENGTH_SHORT).show();
            zxingImage.setVisibility(View.GONE);
            return;
        }
        bitmap = CodeUtils.createImage(info, 300, 300, BitmapFactory.decodeResource(getResources(), R.mipmap.icon_pig));
        zxingImage.setVisibility(View.VISIBLE);
        zxingImage.setImageBitmap(bitmap);
        infoEt.setText("");
    }


}
