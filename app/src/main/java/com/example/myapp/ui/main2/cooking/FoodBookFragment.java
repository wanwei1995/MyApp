package com.example.myapp.ui.main2.cooking;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import butterknife.BindView;
import com.example.myapp.BaseFragment;
import com.example.myapp.R;
import com.example.myapp.common.exception.BusinessException;
import com.example.myapp.datebase.AppDatabase;
import com.example.myapp.datebase.entity.FoodBook;
import com.example.myapp.datebase.entity.MyFoodBook;
import com.example.myapp.myView.MyLayoutAdapter;
import com.example.myapp.util.DateUtils;
import com.example.myapp.util.GlideUtil;
import com.example.myapp.util.StringUtil;
import com.ufo.dwrefresh.view.DWRefreshLayout;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import java.util.Date;
import java.util.List;

//菜单页面
public class FoodBookFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.goods_list)
    RecyclerView goodsList;
    @BindView(R.id.add)
    ImageButton add;

    List<FoodBook> foodBooks;
    @BindView(R.id.dwRefreshLayout)
    DWRefreshLayout dwRefreshLayout;

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
        //显示菜单
        getBookData();

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

            mDisposable.add(AppDatabase.getInstance().foodBookDao().findList()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(findList -> {
                                myLayoutAdapter.reLoad(findList);
                            },
                            throwable -> {
                                Toast.makeText(mContext, "数据获取失败", Toast.LENGTH_SHORT).show();
                            }));
            //刷新菜单

        }
    }

    private void getBookData() {

        mDisposable.add(AppDatabase.getInstance().foodBookDao().findList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(findList -> {
                            foodBooks = findList;
                            showView();
                        },
                        throwable -> {
                            Toast.makeText(mContext, "数据获取失败", Toast.LENGTH_SHORT).show();
                        }));
    }

    private void showView() {

        myLayoutAdapter = new MyLayoutAdapter<FoodBook>(foodBooks, R.layout.item_layout_foodbook) {
            @Override
            public void reBindViewHolder(ViewHolder holder, int position, List<FoodBook> mData) {
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
                bundle.putSerializable("foodBookDto", foodBooks.get(position));
                intent.putExtras(bundle);
                startActivityForResult(intent, Activity.RESULT_FIRST_USER);
            }
        });

        myLayoutAdapter.setOnItemLongClickListener(new MyLayoutAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                FoodBook foodBook = foodBooks.get(position);


                mDisposable.add(Completable.fromAction(() -> {
                    MyFoodBook myFoodBook = AppDatabase.getInstance().myFoodBookDao().findLastOne();
                    //如果上一次点餐已经超过3小时则，则新建菜单进行记录
                    if (myFoodBook != null && DateUtils.addDay(new Date(), -1).getTime() < myFoodBook.getCreateTime()) {
                        //如果菜品已存在,则提示
                        String[] foodList = myFoodBook.getFoodIdStr().split(",");
                        for (int i = 0; i < foodList.length; i++) {
                            if (foodList[i].equals(String.valueOf(foodBook.getId()))) {
                               throw new BusinessException("菜品已存在，请勿重新选择!");
                            }
                        }
                        if (StringUtil.isNotEmpty(myFoodBook.getFoodIdStr())) {
                            //取最后一条作为菜单记录并更新
                            myFoodBook.setFoodIdStr(myFoodBook.getFoodIdStr() + "," + foodBook.getId());
                        } else {
                            //取最后一条作为菜单记录并更新
                            myFoodBook.setFoodIdStr(String.valueOf(foodBook.getId()));
                        }

                        //保存数据
                        AppDatabase.getInstance().myFoodBookDao().update(myFoodBook);

                    } else {
                        //新建一条作为菜单进行记录
                        MyFoodBook myFoodBookNew = new MyFoodBook();
                        myFoodBookNew.setFoodIdStr(String.valueOf(foodBook.getId()));
                        myFoodBookNew.setCreateTime(new Date().getTime());
                        //保存数据
                        AppDatabase.getInstance().myFoodBookDao().insert(myFoodBookNew);
                    }

                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                            Toast.makeText(mContext, "美食["+foodBook.getName()+"]已加入菜单，请继续选择", Toast.LENGTH_SHORT).show();
                        }, throwable -> {
                            if (throwable instanceof BusinessException) {
                                Toast.makeText(mContext, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(mContext, "系统异常,请重试", Toast.LENGTH_SHORT).show();
                            }
                        }));
            }
        });


        //goodsList.setLayoutManager(new GridLayoutManager(mContext, 2));
        goodsList.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        goodsList.setAdapter(myLayoutAdapter);
    }

}
