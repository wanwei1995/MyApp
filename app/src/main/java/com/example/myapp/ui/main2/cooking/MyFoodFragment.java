package com.example.myapp.ui.main2.cooking;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import com.example.myapp.BaseFragment;
import com.example.myapp.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class MyFoodFragment extends BaseFragment {

    @BindView(R.id.my_food_tabs)
    TabLayout myFoodTabs;

    @BindView(R.id.my_food_fragment_container)
    FrameLayout myFoodFragmentContainer;
    @BindView(R.id.pager)
    ViewPager pager;

    private List<Fragment> fragments = new ArrayList<>();

    private List<String> titles = new ArrayList<>();


    public static MyFoodFragment newInstance() {
        MyFoodFragment fragment = new MyFoodFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_food, container, false);
        butterKnife(view);

        fragments.add(new NewFoodFragment().newInstance());
        fragments.add(new OldFoodFragment().newInstance());
        titles.add("今日美食");
        titles.add("往日美食");

        pager.setAdapter(new FragmentStatePagerAdapter(mContext.getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                super.destroyItem(container, position, object);
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {

                return titles.get(position);
            }
        });

        myFoodTabs.setupWithViewPager(pager);
        return view;
    }
}
