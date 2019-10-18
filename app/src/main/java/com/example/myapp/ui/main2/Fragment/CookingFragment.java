package com.example.myapp.ui.main2.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.fragment.app.FragmentManager;
import butterknife.BindView;
import com.example.myapp.BaseFragment;
import com.example.myapp.R;
import com.example.myapp.myView.MyScrollView;
import com.example.myapp.ui.main.Fragment.MainFragment;
import com.example.myapp.ui.main.Fragment.UiFragment;
import com.example.myapp.ui.main2.cooking.FoodBookFragment;
import com.example.myapp.ui.main2.cooking.MyFoodFragment;
import com.example.myapp.ui.main2.cooking.RandomFragment;
import com.google.android.material.tabs.TabLayout;


public class CookingFragment extends BaseFragment {

    @BindView(R.id.fragment_container)
    FrameLayout fragmentContainer;
    @BindView(R.id.srcoll)
    MyScrollView srcoll;
    @BindView(R.id.tabs)
    TabLayout tabs;

    public final static String TAG_FIRST = "foodBook";
    public final static String TAG_SECOND = "myFood";
    public final static String TAG_THIRD = "random";

    private FragmentManager mFgManager;

    public static CookingFragment newInstance() {
        CookingFragment fragment = new CookingFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cooking, container, false);
        butterKnife(view);
        mFgManager = mContext.getSupportFragmentManager();
        initData();
        initTab();
        return view;
    }

    private void initData() {
        mFgManager.beginTransaction().replace(R.id.fragment_container,
                FoodBookFragment.newInstance()).commit();
    }

    private void initTab() {
        TabLayout.Tab util = tabs.newTab();
        util.setText("菜谱大全");
        util.setIcon(R.mipmap.icon_food_book2);
        tabs.addTab(util);

        TabLayout.Tab component = tabs.newTab();
        component.setText("我的菜");
        component.setIcon(R.mipmap.icon_food);
        tabs.addTab(component);

        TabLayout.Tab expand = tabs.newTab();
        expand.setText("随机推荐");
        expand.setIcon(R.mipmap.icon_random);
        tabs.addTab(expand);

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mFgManager.popBackStack();
                switch (tab.getPosition()) {
                    case 0:
                        mFgManager.beginTransaction().replace(R.id.fragment_container,
                                FoodBookFragment.newInstance(), TAG_FIRST).commit();
                        break;
                    case 1:
                        mFgManager.beginTransaction().replace(R.id.fragment_container,
                                MyFoodFragment.newInstance(), TAG_SECOND).commit();
                        break;
                    case 2:
                        mFgManager.beginTransaction().replace(R.id.fragment_container,
                                RandomFragment.newInstance(), TAG_THIRD).commit();
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
