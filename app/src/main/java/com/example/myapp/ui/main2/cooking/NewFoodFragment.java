package com.example.myapp.ui.main2.cooking;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import com.example.myapp.BaseFragment;
import com.example.myapp.R;
import com.example.myapp.data.service.FoodGoodService;
import com.example.myapp.data.service.MyFoodGoodService;
import com.example.myapp.myView.MyLayoutAdapter;
import com.example.myapp.ui.main2.dto.FoodBookDto;
import com.example.myapp.ui.main2.dto.MyFoodBookDto;
import com.example.myapp.util.AlertDialogUtil;
import com.example.myapp.util.CollectionUtils;
import com.example.myapp.util.DateUtils;
import com.example.myapp.util.GlideUtil;

import java.util.Date;
import java.util.List;

public class NewFoodFragment extends BaseFragment {


    @BindView(R.id.food_list)
    RecyclerView foodList;
    private MyFoodGoodService myFoodGoodService;
    private FoodGoodService foodGoodService;

    public static NewFoodFragment newInstance() {
        NewFoodFragment fragment = new NewFoodFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_food, container, false);
        butterKnife(view);

        myFoodGoodService = new MyFoodGoodService(mContext);
        foodGoodService = new FoodGoodService(mContext);

        showView();
        return view;
    }

    private void showView() {

        List<MyFoodBookDto> myFoodBookDtoList = myFoodGoodService.findByTime(DateUtils.addDay(new Date(), -1).getTime());
        if (CollectionUtils.isEmpty(myFoodBookDtoList)) {
            //暂无美食
            return;
        }
        MyFoodBookDto myFoodBookDto = myFoodBookDtoList.get(0);
        List<FoodBookDto> foodBookDtos = foodGoodService.findListByIds(myFoodBookDto.getFoodIdStr());

        MyLayoutAdapter myLayoutAdapter = new MyLayoutAdapter<FoodBookDto>(foodBookDtos, R.layout.item_layout_foodbook2) {
            @Override
            public void reBindViewHolder(ViewHolder holder, int position, List<FoodBookDto> mData) {
                holder.setImageBitmap(mContext, R.id.food_pic, mData.get(position).getPicUrl(), GlideUtil.getCrop(R.mipmap.icon_food_book_default));
                holder.setText(R.id.food_title, mData.get(position).getName());
                holder.setText(R.id.food_introduction, mData.get(position).getIntroduction());
            }
        };
        myLayoutAdapter.setOnItemClickListener((view, position) -> {
            //查看详情
            Intent intent = new Intent(mContext, FoodBookDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("foodBookDto", foodBookDtos.get(position));
            intent.putExtras(bundle);
            startActivityForResult(intent, Activity.RESULT_FIRST_USER);
        });

        myLayoutAdapter.setOnItemLongClickListener((view, position) -> {
            //长按删除
            AlertDialogUtil.YesOrNo("确定从餐盘中删除美食?", mContext, (dialog, which) -> {
                //删除
                myLayoutAdapter.remove(position);

                //数据库中删除
                StringBuilder sb = new StringBuilder();
                for (FoodBookDto foodBookDto : foodBookDtos) {
                    sb.append(foodBookDto.getId()).append(",");
                }
                if (CollectionUtils.isEmpty(foodBookDtos)) {
                    myFoodBookDto.setFoodIdStr("");
                } else {
                    myFoodBookDto.setFoodIdStr(sb.substring(0, sb.length() - 1));
                }
                myFoodGoodService.update(myFoodBookDto);

            });
        });

        foodList.setAdapter(myLayoutAdapter);
        foodList.setLayoutManager(new GridLayoutManager(mContext, 1));
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Activity.RESULT_FIRST_USER && resultCode == 1) {
            //刷新菜单
            showView();
        }
    }
}
