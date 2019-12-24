package com.example.myapp.ui.main2.cooking;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.annotation.NonNull;
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
import com.example.myapp.util.*;
import com.example.myapp.webDav.Result;
import com.example.myapp.webDav.WebDavService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.ufo.dwrefresh.view.DWRefreshLayout;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import java.io.File;
import java.util.*;

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

    private WebDavService webDavService;

    //点菜详情
    private String info = "";

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

        webDavService = new WebDavService(handler);

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
                        //先从云盘上拉取数据与本地对比，找出不一致的数据,将拉取的新数据插入数据库 然后将两边数据合并重新上传数据
                        webDavService.downFoodBookInfo();
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


    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Result result = (Result) msg.obj;
            if (WebDavService.upMyFood_what == msg.what) {
                if (!result.isSuccess()) {
                    ToastUtils.show("数据同步失败!" + result.getMessage());
                    dwRefreshLayout.setRefresh(false);
                }
            } else if (WebDavService.downFoodBook_what == msg.what) {
                //获取下载数据并解析
                if (!result.isSuccess()) {
                    ToastUtils.show("菜谱数据获取失败!" + result.getMessage());
                    dwRefreshLayout.setRefresh(false);
                } else {
                    //解析数据
                    List<FoodBook> foodBookList = JacksonUtil.readValue(result.getMessage(), new TypeReference<List<FoodBook>>() {
                    });
                    //从拉取数据中筛选出新数据
                    List<FoodBook> foodBookListNew = getNewData(foodBooks, foodBookList);
                    //将新数据保存
                    if (CollectionUtils.isNotEmpty(foodBookListNew)) {
                        save(foodBookListNew);
                    }
                    //从拉取数据中筛选出新数据
                    List<FoodBook> foodBookListLocal = getNewData(foodBookList, foodBooks);
                    //如果本地也没有最新的数据则直接返回
                    if(CollectionUtils.isEmpty(foodBookListLocal)){
                        dwRefreshLayout.setRefresh(false);
                        return;
                    }
                    //上传数据
                    upLoad(foodBookListNew, foodBookListLocal);
                    dwRefreshLayout.setRefresh(false);
                }
            } else if (WebDavService.upFoodBook_what == msg.what) {
                if (result.isSuccess()) {
                    ToastUtils.show("同步完成");
                } else {
                    ToastUtils.show("同步失败:" + result.getMessage());
                }
            }
        }
    };

    private void upLoad(List<FoodBook> foodBookListNew, List<FoodBook> foodBookListLocal) {
        //上传数据之前将本地新数据图片同步到webDav上
        for (FoodBook foodBook : foodBookListLocal) {
            webDavService.upFoodBookPic(foodBook.getPicUrl());
        }
        //将新旧数据合并传到网上去
        foodBooks.addAll(foodBookListNew);
        String info = JacksonUtil.toJSon(foodBooks);
        webDavService.upFoodBookInfo(info);
    }

    private void save(List<FoodBook> foodBookListNew) {
        //将新数据的图片从webdav上下载到本地
        for(FoodBook foodBook:foodBookListNew){
            webDavService.downFoodBookPic(foodBook.getPicUrl());
        }

        //将新数据存到数据库
        mDisposable.add(Completable.fromAction(() -> {
            AppDatabase.getInstance().foodBookDao().insertList(foodBookListNew);
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                            //刷新页面:图片下载后再重新刷新页面
                            File file = new File(foodBookListNew.get(foodBookListNew.size()-1).getPicUrl());
                            while(file == null){
                                Thread.sleep(100);
                            }
                            myLayoutAdapter.reLoad(foodBooks);
                            dwRefreshLayout.setRefresh(false);
                        },
                        throwable -> {
                            Toast.makeText(mContext, "数据获取失败", Toast.LENGTH_SHORT).show();
                        }));
    }

    private List<FoodBook> getNewData(List<FoodBook> list1, List<FoodBook> list2) {
        Map<String, Integer> map = new HashMap<>();
        if (CollectionUtils.isNotEmpty(list1)) {
            for (FoodBook foodBook : list1) {
                map.put(foodBook.getId() + foodBook.getName(), 1);
            }
        }
        //将不一致数据筛选出来
        List<FoodBook> foodBookListNew = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(list2)) {
            for (FoodBook foodBook : list2) {
                if (map.get(foodBook.getId() + foodBook.getName()) == null) {
                    foodBookListNew.add(foodBook);
                }
            }
        }
        return foodBookListNew;
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
            mDisposable.add(AppDatabase.getInstance().foodBookDao().findList()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(findList -> {
                                foodBooks = findList;
                                myLayoutAdapter.reLoad(foodBooks);
                            },
                            throwable -> {
                                Toast.makeText(mContext, "数据获取失败", Toast.LENGTH_SHORT).show();
                            }));
        }
    }

    private void getBookData() {
        //获取菜谱数据
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
                    //保存点单记录
                    save(foodBook);
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                            //同步数据
                            webDavService.upMyFoodInfo(info);
                            Toast.makeText(mContext, "美食[" + foodBook.getName() + "]已加入菜单，请继续选择", Toast.LENGTH_SHORT).show();
                        }, throwable -> {
                            if (throwable instanceof BusinessException) {
                                Toast.makeText(mContext, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(mContext, "系统异常,请重试", Toast.LENGTH_SHORT).show();
                            }
                        }));
            }
        });

        goodsList.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        goodsList.setAdapter(myLayoutAdapter);
    }

    private void save(FoodBook foodBook) {
        MyFoodBook myFoodBook = AppDatabase.getInstance().myFoodBookDao().findLastOne();
        //如果上一次点餐已经超过3小时则，则新建菜单进行记录
        if (myFoodBook != null && DateUtils.initDateByDay().getTime() < myFoodBook.getCreateTime()) {
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
            info = myFoodBook.getFoodIdStr();
            //保存数据
            AppDatabase.getInstance().myFoodBookDao().update(myFoodBook);
        } else {
            info = String.valueOf(foodBook.getId());
            //新建一条作为菜单进行记录 保存数据
            AppDatabase.getInstance().myFoodBookDao().insert(new MyFoodBook(new Date().getTime(), String.valueOf(foodBook.getId())));
        }

    }

}
