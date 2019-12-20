package com.example.myapp.ui.main2.cooking;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import com.example.myapp.BaseFragment;
import com.example.myapp.R;
import com.example.myapp.common.exception.BusinessException;
import com.example.myapp.datebase.AppDatabase;
import com.example.myapp.datebase.entity.FoodBook;
import com.example.myapp.datebase.entity.MyFoodBook;
import com.example.myapp.myView.MyLayoutAdapter;
import com.example.myapp.ui.main.activity.WebDavActivity;
import com.example.myapp.util.*;
import com.example.myapp.webDav.WebDavService;
import com.thegrizzlylabs.sardineandroid.Sardine;
import com.thegrizzlylabs.sardineandroid.impl.OkHttpSardine;
import com.ufo.dwrefresh.view.DWRefreshLayout;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;


//菜单超过一天自动新建一份菜单
public class NewFoodFragment extends BaseFragment {


    @BindView(R.id.food_list)
    RecyclerView foodList;
    @BindView(R.id.dwRefreshLayout)
    DWRefreshLayout dwRefreshLayout;

    private List<MyFoodBook> myFoodBookDtoList;

    private WebDavService webDavService;

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

        webDavService = new WebDavService(handler);

        getData();

        //下拉刷新事件
        dwRefreshLayout.lockLoadMore(true);
        dwRefreshLayout.setOnRefreshListener(new DWRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //下载点单数据
                webDavService.downMyFoodInfo();
            }

            @Override
            public void onLoadMore() {
                //加载更多回调
                ToastUtils.show("没有更多了");
            }
        });
        return view;
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            String info = (String) msg.obj;
            if (0 == msg.what) {

                if (info.equals("0")) {
                    Toast.makeText(mContext, "暂无点单数据", Toast.LENGTH_SHORT).show();
                    dwRefreshLayout.setRefresh(false);
                } else {
                    if (info.equals(myFoodBookDtoList.get(0).getFoodIdStr())) {
                        //如果获取数据相同 则无需刷新
                        dwRefreshLayout.setRefresh(false);
                        ToastUtils.show("已刷新");
                    }
                    mDisposable.add(Completable.fromAction(() -> {
                        //保存数据
                        save(info);
                    }).subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(() -> {
                                //刷新页面
                                getData();
                                dwRefreshLayout.setRefresh(false);
                                ToastUtils.show("已刷新");
                            }, throwable -> {
                                if (throwable instanceof BusinessException) {
                                    ToastUtils.show(throwable.getMessage());
                                } else {
                                    ToastUtils.show("系统异常,请重试");
                                }
                                dwRefreshLayout.setRefresh(false);
                            }));
                }
            } else {
                ToastUtils.show("数据加载失败!");
                dwRefreshLayout.setRefresh(false);
            }
        }
    };

    private void save(String info) {
        //获取点菜id
        //如果没有最新的菜单，则新建一个并将数据插入
        if (CollectionUtils.isEmpty(myFoodBookDtoList) || DateUtils.addDay(new Date(), -1).getTime() > myFoodBookDtoList.get(0).getCreateTime()) {
            //新建菜单,保存数据
            AppDatabase.getInstance().myFoodBookDao().insert(new MyFoodBook(new Date().getTime(), info));
        } else {
            //更新菜单
            MyFoodBook myFoodBookOld = myFoodBookDtoList.get(0);
            if (StringUtil.isNotEmpty(myFoodBookOld.getFoodIdStr())) {
                myFoodBookOld.setFoodIdStr(myFoodBookOld.getFoodIdStr() + "," + info);
            } else {
                myFoodBookOld.setFoodIdStr(info);
            }
            AppDatabase.getInstance().myFoodBookDao().update(myFoodBookOld);
        }
    }


    private void getData() {

        mDisposable.add(AppDatabase.getInstance().myFoodBookDao().findByTime(DateUtils.addDay(new Date(), -1).getTime()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> {
                            myFoodBookDtoList = list;
                            if (CollectionUtils.isEmpty(myFoodBookDtoList)) {
                                //暂无美食
                                return;
                            }
                            showView(myFoodBookDtoList.get(0));

                        },
                        throwable -> {
                            ToastUtils.show("查找数据失败");
                        }
                ));
    }

    private void showView(MyFoodBook myFoodBook) {

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
                                                        ToastUtils.show("更新数据失败");
                                                    }
                                            ));
                                });
                            });

                            foodList.setAdapter(myLayoutAdapter);
                            foodList.setLayoutManager(new GridLayoutManager(mContext, 1));

                        },
                        throwable -> {
                            ToastUtils.show("删除数据失败");
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
