package com.example.myapp.ui.main.Fragment;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import butterknife.BindView;
import com.example.myapp.BaseFragment;
import com.example.myapp.R;
import com.example.myapp.myView.MyAdapter;
import com.example.myapp.ui.GestureActivity;
import com.example.myapp.ui.main2.activity.CreateZxingActivity;
import com.example.myapp.ui.main2.dto.MenuDto;
import com.example.myapp.util.*;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;


public class UiFragment extends BaseFragment {

    @BindView(R.id.ui_grid_tool)
    GridView uiGridTool;

    private Bitmap bitmap;

    private BaseAdapter mAdapter = null;

    public static UiFragment newInstance() {
        UiFragment fragment = new UiFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_ui, container, false);
        butterKnife(view);

        List<MenuDto> menuDTOs = loadMenus(R.raw.ui_menu);
        showMenu(menuDTOs);
        return view;
    }

    private void showMenu(List<MenuDto> menuDTOs) {
        //显示页面
        mAdapter = new MyAdapter<MenuDto>(menuDTOs, R.layout.item_grid_icon) {
            @Override
            public void bindView(ViewHolder holder, MenuDto obj) {
                holder.setImageResource(R.id.img_icon, mContext.getResources().getIdentifier(obj.getIcon(), "mipmap", mContext.getPackageName()));
                holder.setText(R.id.txt_icon, obj.getName());
            }
        };

        uiGridTool.setAdapter(mAdapter);
        uiGridTool.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(StringUtil.isNotEmpty(menuDTOs.get(i).getLock()) &&  menuDTOs.get(i).getLock().equals("1")){
                    //上锁
                    Intent intent = new Intent(mContext, GestureActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("activity",mContext.getPackageName() + ".ui.main.activity." + menuDTOs.get(i).getActivity());
                    bundle.putString("code",menuDTOs.get(i).getCode());
                    intent.putExtras(bundle);
                    startActivity(intent);
                    return;
                }



                if (StringUtil.isNotEmpty(menuDTOs.get(i).getCode()) && menuDTOs.get(i).getCode().equals("save_pic")) {
                    savePic();
                    return;
                }
                try {
                    //跳转至对应页面
                    Intent intent = new Intent(mContext, Class.forName(mContext.getPackageName() + ".ui.main.activity." + menuDTOs.get(i).getActivity()));
                    Bundle bundle = new Bundle();
                    intent.putExtras(bundle);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void savePic() {
        //打开相册，选择图片并保存
        //进入相册选择图片
        int permission = ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            requestPermissions(Global.PERMISSTIONS_CAMERA, Global.REQUEST_EXTERNAL_STORAGE);
        } else {
            Intent intentToPickPic = new Intent(Intent.ACTION_PICK, null);
            // 如果限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型" 所有类型则写 "image/*"
            intentToPickPic.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            startActivityForResult(intentToPickPic, Global.OPEN_ALBUM);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        /**
         * 选择系统图片并解析
         */
        if (requestCode == Global.OPEN_ALBUM) {
            if (data != null) {
                try {
                     bitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), data.getData());
                }catch (Exception e){
                    Toast.makeText(mContext, "图片读取失败", Toast.LENGTH_SHORT).show();
                }
                //长按存储图片
                //获取内部存储状态  
                String state = Environment.getExternalStorageState();
                //如果状态不是mounted，无法读写  
                if (!state.equals(Environment.MEDIA_MOUNTED)) {
                    Toast.makeText(mContext, "系统异常，无法保存", Toast.LENGTH_SHORT).show();
                    return;
                }
                AlertDialogUtil.YesOrNo("是否保存?", mContext, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FileOutputStream out = null;
                        try {
                            File file = FileUtil.createPhotoPath(FileUtil.PRIVATE1, "",true);
                            out = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

                            Toast.makeText(mContext, "已保存到手机，请到相册查看", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                out.flush();
                                out.close();
                                //删除图片
                                FileUtil.deletePicture(data.getData(),mContext);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }


                    }
                });
        }
        }
    }
}
