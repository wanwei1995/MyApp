package com.example.myapp.ui.main2.cooking;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import com.example.myapp.BaseFragment;
import com.example.myapp.R;

public class FoodBookFragment extends BaseFragment {

    @BindView(R.id.goods_list)
    RecyclerView goodsList;

    public static FoodBookFragment newInstance() {
        FoodBookFragment fragment = new FoodBookFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_food_book, container, false);
        butterKnife(view);
        return view;
    }

}
