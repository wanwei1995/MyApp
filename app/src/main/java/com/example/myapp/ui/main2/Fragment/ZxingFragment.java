package com.example.myapp.ui.main2.Fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import butterknife.BindView;
import com.example.myapp.BaseFragment;
import com.example.myapp.R;
import com.example.myapp.myView.MyAdapter;
import com.example.myapp.ui.main2.dto.MenuDto;
import com.example.myapp.util.Global;
import com.example.myapp.util.ImageUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 二维码工具页面
 */
public class ZxingFragment extends BaseFragment{

    @BindView(R.id.grid_tool)
    GridView gridTool;

    private BaseAdapter mAdapter = null;

    //打开相册
    private final static int OPEN_ALBUM = 1;

    public static ZxingFragment newInstance() {
        ZxingFragment fragment = new ZxingFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_zxing, container, false);
        butterKnife(view);

        List<MenuDto> menuDTOs = loadMenus(R.raw.zwing_menu);
        renderMenus(menuDTOs);
        return view;
    }

    private void renderMenus(List<MenuDto> menuDTOs) {
        mAdapter = new MyAdapter<MenuDto>(menuDTOs, R.layout.item_grid_icon) {
            @Override
            public void bindView(ViewHolder holder, MenuDto obj) {
                holder.setImageResource(R.id.img_icon, mContext.getResources().getIdentifier(obj.getIcon(), "mipmap", mContext.getPackageName()));
                holder.setText(R.id.txt_icon, obj.getName());
            }
        };

        gridTool.setAdapter(mAdapter);
        gridTool.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if(i == 1){
                    readZxing();
                    return;
                }
                try {
                    //跳转至对应页面
                    Intent intent = new Intent(mContext, Class.forName(mContext.getPackageName() + ".ui.main2.activity." + menuDTOs.get(i).getActivity()));
                    Bundle bundle = new Bundle();
                    intent.putExtras(bundle);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void readZxing(){
        //进入相册选择图片
        int permission = ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            requestPermissions(Global.PERMISSTIONS_CAMERA, Global.REQUEST_EXTERNAL_STORAGE);
        } else {
            Intent intentToPickPic = new Intent(Intent.ACTION_PICK, null);
            // 如果限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型" 所有类型则写 "image/*"
            intentToPickPic.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            startActivityForResult(intentToPickPic, OPEN_ALBUM);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        /**
         * 选择系统图片并解析
         */
        if (requestCode == OPEN_ALBUM) {
            if (data != null) {
                Uri uri = data.getData();
                try {
                    CodeUtils.analyzeBitmap(ImageUtil.getImageAbsolutePath(mContext, uri), new CodeUtils.AnalyzeCallback() {
                        @Override
                        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
                            Toast.makeText(mContext, "解析结果:" + result, Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onAnalyzeFailed() {
                            Toast.makeText(mContext, "解析二维码失败", Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
