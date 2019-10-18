package com.example.myapp.ui.main2.cooking;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapp.BaseFragment;
import com.example.myapp.R;

public class MyFoodFragment extends BaseFragment {


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
       return view;
    }

}
