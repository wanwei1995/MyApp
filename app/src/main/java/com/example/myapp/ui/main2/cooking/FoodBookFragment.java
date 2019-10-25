package com.example.myapp.ui.main2.cooking;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import butterknife.BindView;
import com.example.myapp.BaseFragment;
import com.example.myapp.R;
import com.example.myapp.data.service.FoodGoodService;
import com.example.myapp.data.service.MyFoodGoodService;
import com.example.myapp.myView.MyLayoutAdapter;
import com.example.myapp.ui.main2.dto.FoodBookDto;
import com.example.myapp.ui.main2.dto.MyFoodBookDto;
import com.example.myapp.util.AlertDialogUtil;
import com.example.myapp.util.DateUtils;
import com.example.myapp.util.GlideUtil;
import com.example.myapp.util.StringUtil;
import com.ufo.dwrefresh.view.DWRefreshLayout;

import java.util.Date;
import java.util.List;

//菜单页面
public class FoodBookFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.goods_list)
    RecyclerView goodsList;
    @BindView(R.id.add)
    ImageButton add;

    List<FoodBookDto> foodBookDtos;
    @BindView(R.id.dwRefreshLayout)
    DWRefreshLayout dwRefreshLayout;

    private FoodGoodService foodGoodService;

    private MyFoodGoodService myFoodGoodService;

    private MyLayoutAdapter myLayoutAdapter;

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
        add.setOnClickListener(this);
        foodGoodService = new FoodGoodService(mContext);
        myFoodGoodService = new MyFoodGoodService(mContext);
        //显示菜单
        showFoodBook();

        //下拉刷新事件
        dwRefreshLayout.lockLoadMore(true);
        dwRefreshLayout.setOnRefreshListener(new DWRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //弹框 是否打印
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMessage("请确定再联网的情况下同步");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(mContext, "同步", Toast.LENGTH_SHORT).show();

                        dwRefreshLayout.setRefresh(false);
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dwRefreshLayout.setRefresh(false);
                    }
                });
                builder.show();
            }

            @Override
            public void onLoadMore() {
                //加载更多回调
                Toast.makeText(mContext, "没有更多了", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.add:
                addBook();
                break;
        }
    }

    private void addBook() {
        //添加菜谱
        Intent intent = new Intent(mContext, AddFoodBookActivity.class);
        startActivityForResult(intent, Activity.RESULT_FIRST_USER);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Activity.RESULT_FIRST_USER && resultCode == 1) {
            //刷新菜单
            myLayoutAdapter.reLoad(foodGoodService.findList());
        }
    }

    private void showFoodBook() {
        //获取数据
        foodBookDtos = foodGoodService.findList();

        myLayoutAdapter = new MyLayoutAdapter<FoodBookDto>(foodBookDtos, R.layout.item_layout_foodbook) {
            @Override
            public void reBindViewHolder(ViewHolder holder, int position, List<FoodBookDto> mData) {
                holder.setImageBitmap(mContext, R.id.food_pic, mData.get(position).getPicUrl(), GlideUtil.getCrop(R.mipmap.icon_food_book_default));
                holder.setText(R.id.food_title, mData.get(position).getName());
                holder.setText(R.id.food_introduction, mData.get(position).getIntroduction());
            }
        };

        myLayoutAdapter.setOnItemClickListener(new MyLayoutAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(mContext, FoodBookDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("foodBookDto", foodBookDtos.get(position));
                intent.putExtras(bundle);
                startActivityForResult(intent, Activity.RESULT_FIRST_USER);
            }
        });

        myLayoutAdapter.setOnItemLongClickListener(new MyLayoutAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                FoodBookDto foodBook = foodBookDtos.get(position);
                MyFoodBookDto myFoodBookDto = myFoodGoodService.findLastOne();
                //如果上一次点餐已经超过3小时则，则新建菜单进行记录
                if (myFoodBookDto != null && DateUtils.addDay(new Date(), -1).getTime() < myFoodBookDto.getCreateTime()) {
                    //如果菜品已存在,则提示
                    String[] foodList = myFoodBookDto.getFoodIdStr().split(",");
                    for(int i = 0 ;i<foodList.length;i++){
                        if(foodList[i].equals(String.valueOf(foodBook.getId()))){
                            Toast.makeText(mContext, "菜品已存在，请勿重复选择", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    if(StringUtil.isNotEmpty(myFoodBookDto.getFoodIdStr())){
                        //取最后一条作为菜单记录并更新
                        myFoodBookDto.setFoodIdStr(myFoodBookDto.getFoodIdStr()+","+foodBook.getId());
                    }else {
                        //取最后一条作为菜单记录并更新
                        myFoodBookDto.setFoodIdStr(String.valueOf(foodBook.getId()));
                    }


                    myFoodGoodService.update(myFoodBookDto);
                } else {
                    //新建一条作为菜单进行记录

                    myFoodGoodService.saveHeader(new MyFoodBookDto(new Date().getTime(),String.valueOf(foodBook.getId())));

                }
                //使用缓存存储选择的美食
                Toast.makeText(mContext, "美食已加入餐谱,请慢享", Toast.LENGTH_SHORT).show();
            }
        });


        //goodsList.setLayoutManager(new GridLayoutManager(mContext, 2));
        goodsList.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        goodsList.setAdapter(myLayoutAdapter);
    }

}
