package com.example.myapp.ui.main;

import android.os.Bundle;
import androidx.fragment.app.FragmentManager;
import butterknife.BindView;
import com.example.myapp.BaseActivity;
import com.example.myapp.R;
import com.example.myapp.myView.MyScrollView;
import com.example.myapp.ui.main.Fragment.MainFragment;
import com.example.myapp.ui.main.Fragment.UiFragment;
import com.google.android.material.tabs.TabLayout;
import com.youth.banner.Banner;

public class MainActivity extends BaseActivity {

    public final static String TAG_FIRST = "0";
    public final static String TAG_SECOND = "1";
    public final static String TAG_THIRD = "2";

    @BindView(R.id.tabs)
    TabLayout tabs;

    private FragmentManager mFgManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mFgManager = getSupportFragmentManager();
        initData();
        initTab();
    }

    private void initData() {
        mFgManager.beginTransaction().replace(R.id.fragment_container,
                UiFragment.newInstance()).commit();
    }

    private void initTab() {
        TabLayout.Tab util = tabs.newTab();
        util.setText("UI控件测试");
        //util.setIcon(R.mipmap.icon_other);
        tabs.addTab(util);

        TabLayout.Tab component = tabs.newTab();
        component.setText("POST接口测试");
        //component.setIcon(R.mipmap.icon_main);
        tabs.addTab(component);

        TabLayout.Tab expand = tabs.newTab();
        expand.setText("其他");
        //expand.setIcon(R.mipmap.icon_tool);
        tabs.addTab(expand);

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mFgManager.popBackStack();
                switch (tab.getPosition()) {
                    case 0:
                        mFgManager.beginTransaction().replace(R.id.fragment_container,
                                UiFragment.newInstance(), TAG_FIRST).commit();
                        break;
                    case 1:
                        mFgManager.beginTransaction().replace(R.id.fragment_container,
                                MainFragment.newInstance(), TAG_SECOND).commit();
                        break;
                    case 2:
                        mFgManager.beginTransaction().replace(R.id.fragment_container,
                                MainFragment.newInstance(), TAG_THIRD).commit();
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
