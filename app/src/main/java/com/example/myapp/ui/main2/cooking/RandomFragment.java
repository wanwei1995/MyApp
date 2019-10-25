package com.example.myapp.ui.main2.cooking;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.myapp.BaseFragment;
import com.example.myapp.R;


public class RandomFragment extends BaseFragment {

    public static RandomFragment newInstance() {
        RandomFragment fragment = new RandomFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_random, container, false);
        butterKnife(view);
        return view;
    }
}
