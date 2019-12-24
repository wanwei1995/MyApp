package com.example.myapp.ui.main2;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import com.bumptech.glide.Glide;
import com.example.myapp.BaseActivity;
import com.example.myapp.R;
import com.example.myapp.common.Router;
import com.example.myapp.ui.main2.Fragment.*;
import com.example.myapp.ui.main2.constant.MenuConstant;
import com.example.myapp.util.*;
import com.google.android.material.navigation.NavigationView;
import me.drakeet.floo.StackCallback;
import pub.devrel.easypermissions.EasyPermissions;
import pub.devrel.easypermissions.PermissionRequest;

import static android.widget.Toast.LENGTH_SHORT;

public class Main2Activity extends BaseActivity implements StackCallback, NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private DrawerLayout drawer_layout;
    private NavigationView nav_view;
    private TextView tv_nav_title;
    private FragmentManager mFgManager;

    private Boolean isExit = false;

    private final static int OPEN_ALBUM = 2;

    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        mFgManager = getSupportFragmentManager();

        initView();
        initData();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case OPEN_ALBUM:
                if (data == null) {
                    return;
                }
                String picUrl = RealPathFromUriUtils.getRealPathFromUri(Main2Activity.this, data.getData());
                AppConfig.set(AppConfig.MY_PIC, picUrl);
                //相册返回
                Glide.with(Main2Activity.this).load(picUrl).apply(GlideUtil.getCircle()).into(image);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void initData() {
        mFgManager.beginTransaction().replace(R.id.cly_main_content,
                CallYouFragment.newInstance(), MenuConstant.CALL_YOU).commit();
        toolbar.setTitle(ResUtils.getString(R.string.menu_call_you));
        String version = PackageUtils.packageName();
        if (version != null) {
            String msg = String.format(ResUtils.getString(R.string.menu_zwll_version), version);
            tv_nav_title.setText(msg);
        }
    }

    private void initView() {
        //新导航栏
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer_layout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawer_layout.addDrawerListener(toggle);
        toggle.syncState();


        nav_view = findViewById(R.id.nav_view);
        tv_nav_title = nav_view.getHeaderView(0).findViewById(R.id.tv_nav_title);

        image= nav_view.getHeaderView(0).findViewById(R.id.img_head_icon);

        String picUrl = AppConfig.getMyPic();
        if (StringUtil.isNotEmpty(picUrl)) {
            Glide.with(Main2Activity.this).load(picUrl).apply(GlideUtil.getCircle()).into(image);
        }

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(EasyPermissions.hasPermissions(Main2Activity.this,Manifest.permission.CAMERA)){
                    Intent intentToPickPic = new Intent(Intent.ACTION_PICK, null);
                    // 如果限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型" 所有类型则写 "image/*"
                    intentToPickPic.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                    startActivityForResult(intentToPickPic, OPEN_ALBUM);
                }else{
                    EasyPermissions.requestPermissions(new PermissionRequest.Builder(Main2Activity.this, 1, Manifest.permission.CAMERA,
                            Manifest.permission.VIBRATE)
                            .setRationale("请打开相机权限后再试!")
                            .setPositiveButtonText(R.string.text_affirm)
                            .setNegativeButtonText(R.string.text_button_cancel)
                            .build());
                }
            }
        });

        //侧边菜单点击事件
        nav_view.setItemIconTintList(null);
        nav_view.setNavigationItemSelectedListener(this);
    }

    @Nullable
    @Override
    public String indexKeyForStackTarget() {
        return Router.IndexKey.INDEX_KEY_HOME;
    }

    @Override
    public void onReceivedResult(@Nullable Object result) {
        //重新加载页面数据
        initData();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            //菜单对应页面跳转
            case R.id.nav_call_you:
                if (mFgManager.findFragmentByTag(MenuConstant.CALL_YOU) == null) {
                    mFgManager.beginTransaction().replace(R.id.cly_main_content,
                            CallYouFragment.newInstance(), MenuConstant.CALL_YOU).commit();
                    toolbar.setTitle(ResUtils.getString(R.string.menu_call_you));
                }
                break;
            case R.id.nav_zxing:
                if (mFgManager.findFragmentByTag(MenuConstant.ZXING) == null) {
                    mFgManager.beginTransaction().replace(R.id.cly_main_content,
                            ZxingFragment.newInstance(), MenuConstant.ZXING).commit();
                    toolbar.setTitle(ResUtils.getString(R.string.menu_tools_zxing));
                }
                break;
            case R.id.nav_cookbook:
                if (mFgManager.findFragmentByTag(MenuConstant.COOKING) == null) {
                    mFgManager.beginTransaction().replace(R.id.cly_main_content,
                            CookingFragment.newInstance(), MenuConstant.COOKING).commit();
                    toolbar.setTitle(ResUtils.getString(R.string.menu_eating));
                }
                break;
            case R.id.nav_setting:
                if (mFgManager.findFragmentByTag(MenuConstant.SETTING) == null) {
                    mFgManager.beginTransaction().replace(R.id.cly_main_content,
                            SettingFragment.newInstance(), MenuConstant.SETTING).commit();
                    toolbar.setTitle(ResUtils.getString(R.string.menu_setting));
                }
                break;


            default:
                toolbar.setTitle("施工中...");
                mFgManager.beginTransaction().replace(R.id.cly_main_content,
                        DefaultFragment.newInstance(), "default").commit();
        }
        //关闭右侧菜单栏
        drawer_layout.closeDrawer(GravityCompat.START);
        return false;
    }

    //返回按钮重写

    /**
     * 双击返回键退出
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
                drawer_layout.closeDrawer(GravityCompat.START);
                return true;
            }
            if (isExit) {
                //按两次退出
                finish();
            } else {
                Toast.makeText(this, "再返回一次退出", LENGTH_SHORT).show();
                isExit = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isExit = false;
                    }
                }, 2000);
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
