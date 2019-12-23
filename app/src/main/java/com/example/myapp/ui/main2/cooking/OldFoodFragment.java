package com.example.myapp.ui.main2.cooking;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import com.example.myapp.BaseFragment;
import com.example.myapp.R;
import com.example.myapp.datebase.AppDatabase;
import com.example.myapp.datebase.entity.FoodBook;
import com.example.myapp.datebase.entity.MyFoodBook;
import com.example.myapp.ui.main2.Fragment.setting.SettingSectionEntity;
import com.example.myapp.ui.main2.adapter.OldFoodAdapter;
import com.example.myapp.ui.main2.adapter.OldFoodSectionEntity;
import com.example.myapp.util.CollectionUtils;
import com.example.myapp.util.DateUtils;
import com.example.myapp.util.ListUtil;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import java.util.ArrayList;
import java.util.List;


//历史点单记录
public class OldFoodFragment extends BaseFragment {


    @BindView(R.id.my_food_list)
    RecyclerView myFoodList;

    private OldFoodAdapter mAdapter;

    private List<OldFoodSectionEntity> entityList;


    public static OldFoodFragment newInstance() {
        OldFoodFragment fragment = new OldFoodFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_old_food, container, false);

        butterKnife(view);

        mAdapter = new OldFoodAdapter(null);

        entityList = new ArrayList<>();

        mDisposable.add(AppDatabase.getInstance().myFoodBookDao().findList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(findList -> {
                            //
                            if (CollectionUtils.isEmpty(findList)) {
                                return;
                            }
                            getItem(findList);
                        },
                        throwable -> {
                            Toast.makeText(mContext, "数据获取失败", Toast.LENGTH_SHORT).show();
                        }));
        return view;
    }

    private void getItem(List<MyFoodBook> list) {
        mDisposable.add(Completable.fromAction(() -> {
            //遍历数据，并塞值
            for (MyFoodBook myFoodBook : list) {
                List<FoodBook> foodBookList = AppDatabase.getInstance().foodBookDao().findListByIdsSimple(ListUtil.getList(myFoodBook.getFoodIdStr()));
                if (CollectionUtils.isEmpty(foodBookList)) {
                    continue;
                }
                //头
                entityList.add(new OldFoodSectionEntity(DateUtils.getFormatDateTime(DateUtils.valueOf(myFoodBook.getCreateTime()), DateUtils.ymd)));
                //明细
                for (FoodBook foodBook : foodBookList) {
                    entityList.add(new OldFoodSectionEntity(new OldFoodSectionEntity.Item(foodBook)));
                }
            }

        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                            mAdapter.setNewData(entityList);
                            myFoodList.setAdapter(mAdapter);
                            myFoodList.setLayoutManager(new LinearLayoutManager(mContext));
                        },
                        throwable -> {
                            Toast.makeText(mContext, "新增数据失败", Toast.LENGTH_SHORT).show();
                        }
                ));
    }
}
