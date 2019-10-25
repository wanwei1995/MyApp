package com.example.myapp.ui.main2;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import com.example.myapp.R;
import com.example.myapp.ui.main2.Fragment.CallYouFragment;
import com.example.myapp.ui.main2.Fragment.CookingFragment;
import com.example.myapp.ui.main2.Fragment.DefaultFragment;
import com.example.myapp.ui.main2.Fragment.ZxingFragment;
import com.example.myapp.ui.main2.constant.MenuConstant;
import com.example.myapp.util.Global;
import com.example.myapp.util.PackageUtils;
import com.example.myapp.util.ResUtils;
import com.google.android.material.navigation.NavigationView;

import static android.widget.Toast.LENGTH_SHORT;

public class Main2Activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private DrawerLayout drawer_layout;
    private NavigationView nav_view;
    private TextView tv_nav_title;
    private FragmentManager mFgManager;

    private Boolean isExit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        mFgManager = getSupportFragmentManager();
        initView();
        initData();
    }

    private void initData() {
        mFgManager.beginTransaction().replace(R.id.cly_main_content,
                CallYouFragment.newInstance(), MenuConstant.CALL_YOU).commit();
        toolbar.setTitle(ResUtils.getString(R.string.menu_call_you));
        String version = PackageUtils.packageName();
        if(version != null) {
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
        //侧边菜单点击事件
        nav_view.setItemIconTintList(null);
        nav_view.setNavigationItemSelectedListener(this);
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
            if(drawer_layout.isDrawerOpen(GravityCompat.START)){
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