package com.example.myapp.ui.main.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import com.example.myapp.BaseActivity;
import com.example.myapp.R;
import com.example.myapp.glide.GlideLoadEngine;
import com.example.myapp.myView.MyLayoutAdapter;
import com.example.myapp.ui.main2.dto.MenuDto;
import com.example.myapp.util.AlertDialogUtil;
import com.example.myapp.util.FileUtil;
import com.example.myapp.util.Global;
import com.example.myapp.util.StringUtil;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class PrivateActivity extends BaseActivity {

    @BindView(R.id.private_view)
    RecyclerView privateView;

    private Bitmap bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private);

        List<MenuDto> menuDTOs = loadMenus(R.raw.private_menu);
        showMenu(menuDTOs);
    }

    private void showMenu(List<MenuDto> menuDTOs) {

        MyLayoutAdapter myLayoutAdapter = new MyLayoutAdapter<MenuDto>(menuDTOs, R.layout.item_recycler_icon) {
            @Override
            public void reBindViewHolder(ViewHolder holder, int position, List<MenuDto> mData) {
                holder.setText(R.id.txt_icon, mData.get(position).getName());
                holder.setImageResource(R.id.img_icon, getResources().getIdentifier(mData.get(position).getIcon(), "mipmap", getPackageName()));
            }
        };
        myLayoutAdapter.setOnItemClickListener((view, position) -> {
            if (StringUtil.isNotEmpty(menuDTOs.get(position).getCode()) && menuDTOs.get(position).getCode().equals("save_pic")) {
                savePic();
                return;
            }
            try {
                //跳转至对应页面
                Intent intent = new Intent(PrivateActivity.this, Class.forName(getPackageName() + ".ui.main.activity." + menuDTOs.get(position).getActivity()));
                Bundle bundle = new Bundle();
                intent.putExtras(bundle);
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        privateView.setAdapter(myLayoutAdapter);
        privateView.setLayoutManager(new GridLayoutManager(this, 3));

    }


    private void savePic() {
        //打开相册，选择图片并保存
        //进入相册选择图片
        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            requestPermissions(Global.PERMISSTIONS_CAMERA, Global.REQUEST_EXTERNAL_STORAGE);
        } else {

            Matisse.from(this)
                    .choose(MimeType.ofImage())
                    .capture(true)
                    .captureStrategy(new CaptureStrategy(true, "cc.shinichi.bigimageviewpager.fileprovider"))
                    .countable(true)
                    .maxSelectable(30)
                    .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                    .thumbnailScale(0.85f)
                    .imageEngine(new GlideLoadEngine())
                    .theme(com.zhihu.matisse.R.style.Matisse_Zhihu)
                    .showSingleMediaType(true)
                    .originalEnable(true)
                    .forResult(Global.OPEN_ALBUM);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**
         * 选择系统图片并解析
         */
        if (requestCode == Global.OPEN_ALBUM) {
            if (data != null) {

                AlertDialogUtil.YesOrNo("",this,(dialog, which) -> {
                    //长按存储图片
                    //获取内部存储状态  
                    String state = Environment.getExternalStorageState();
                    //如果状态不是mounted，无法读写  
                    if (!state.equals(Environment.MEDIA_MOUNTED)) {
                        Toast.makeText(PrivateActivity.this, "系统异常，无法保存", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    //选择保存目录
                    File file = new File(FileUtil.getAllPrivate());
                    if (!file.exists()) {
                        Toast.makeText(PrivateActivity.this, "请先创建文件夹!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    List<String> folderNames = new ArrayList<>();
                    File[] files = file.listFiles();
                    for (File fileson : files) {
                        if (fileson.isDirectory()) {
                            folderNames.add(fileson.getName());
                        }
                    }

                    ArrayAdapter<Object> adapter = new ArrayAdapter<Object>(PrivateActivity.this,
                            android.R.layout.simple_dropdown_item_1line, folderNames.toArray());

                    new AlertDialog.Builder(PrivateActivity.this).setTitle("请选择文件夹")
                            .setIcon(R.mipmap.icon_folder)
                            .setAdapter(adapter, (dialog1, which1) -> {
                                String folderName = folderNames.get(which1);
                                for (Uri uri : Matisse.obtainResult(data)) {
                                    savePic2(uri, folderName);
                                }
                                Toast.makeText(PrivateActivity.this, "已保存到手机，请到相册查看", Toast.LENGTH_SHORT).show();
                            }).show();
                });
            }
        }
    }

    private void savePic2(Uri uri, String folderName) {
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
        } catch (Exception e) {
            Toast.makeText(PrivateActivity.this, "图片读取失败", Toast.LENGTH_SHORT).show();
            return;
        }
        FileOutputStream out = null;
        try {
            File file = FileUtil.createPhotoPath(FileUtil.PRIVATE1 + "/" + folderName, folderName, true);
            out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.flush();
                out.close();
                //删除图片
                FileUtil.deletePicture(uri, PrivateActivity.this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
