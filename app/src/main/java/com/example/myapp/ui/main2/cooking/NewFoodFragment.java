package com.example.myapp.ui.main2.cooking;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import com.example.myapp.BaseFragment;
import com.example.myapp.R;
import com.example.myapp.datebase.AppDatabase;
import com.example.myapp.datebase.entity.FoodBook;
import com.example.myapp.datebase.entity.MyFoodBook;
import com.example.myapp.myView.MyLayoutAdapter;
import com.example.myapp.util.*;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NewFoodFragment extends BaseFragment {


    @BindView(R.id.food_list)
    RecyclerView foodList;

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

        getData();
        return view;
    }

    private void getData() {

        mDisposable.add(AppDatabase.getInstance().myFoodBookDao().findByTime(DateUtils.addDay(new Date(), -1).getTime()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(myFoodBookDtoList -> {
                            if (CollectionUtils.isEmpty(myFoodBookDtoList)) {
                                //暂无美食
                                return;
                            }
                            showView(myFoodBookDtoList.get(0));

                        },
                        throwable -> {
                            Toast.makeText(mContext, "删除数据失败", Toast.LENGTH_SHORT).show();
                        }
                ));
    }

    private void showView(MyFoodBook myFoodBook){

        mDisposable.add(AppDatabase.getInstance().foodBookDao().findListByIds(ListUtil.getList(myFoodBook.getFoodIdStr()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(foodBookDtos -> {
                            if (CollectionUtils.isEmpty(foodBookDtos)) {
                                //暂无美食
                                return;
                            }
                            MyLayoutAdapter myLayoutAdapter = new MyLayoutAdapter<FoodBook>(foodBookDtos, R.layout.item_layout_foodbook2) {
                                @Override
                                public void reBindViewHolder(ViewHolder holder, int position, List<FoodBook> mData) {
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
                                    for (FoodBook foodBook : foodBookDtos) {
                                        sb.append(foodBook.getId()).append(",");
                                    }
                                    if (CollectionUtils.isEmpty(foodBookDtos)) {
                                        myFoodBook.setFoodIdStr("");
                                    } else {
                                        myFoodBook.setFoodIdStr(sb.substring(0, sb.length() - 1));
                                    }
                                    mDisposable.add(Completable.fromAction(() -> {
                                        AppDatabase.getInstance().myFoodBookDao().update(myFoodBook);
                                    }).subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(() -> {

                                                    },
                                                    throwable -> {
                                                        Toast.makeText(mContext, "更新数据失败", Toast.LENGTH_SHORT).show();
                                                    }
                                            ));
                                });
                            });

                            foodList.setAdapter(myLayoutAdapter);
                            foodList.setLayoutManager(new GridLayoutManager(mContext, 1));

                        },
                        throwable -> {
                            Toast.makeText(mContext, "删除数据失败", Toast.LENGTH_SHORT).show();
                        }
                ));
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Activity.RESULT_FIRST_USER && resultCode == 1) {
            //刷新菜单
            getData();
        }
    }
}
