package com.example.myapp.ui.main2.cooking;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.entity.node.BaseNode;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.myapp.BaseFragment;
import com.example.myapp.R;
import com.example.myapp.datebase.AppDatabase;
import com.example.myapp.datebase.entity.FoodBook;
import com.example.myapp.datebase.entity.MyFoodBook;
import com.example.myapp.ui.main2.adapter.MySectionEntity;
import com.example.myapp.ui.main2.adapter.SectionQuickAdapter;
import com.example.myapp.ui.main2.adapter.node.section.NodeSectionAdapter;
import com.example.myapp.ui.main2.adapter.node.section.entity.ItemNode;
import com.example.myapp.ui.main2.adapter.node.section.entity.RootNode;
import com.example.myapp.ui.main2.dto.SettingDto;
import com.example.myapp.util.CollectionUtils;
import com.example.myapp.util.DateUtils;
import com.example.myapp.util.GlideUtil;
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

    private SectionQuickAdapter mAdapter;

    private List<BaseNode> entityList = new ArrayList<>();


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

        mDisposable.add(AppDatabase.getInstance().myFoodBookDao().findList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(findList -> {
                            //
                            if (CollectionUtils.isEmpty(findList)) {
                                return;
                            }
                            entityList = new ArrayList<>();
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
                //明细
                List<BaseNode> items = new ArrayList<>();
                for (FoodBook foodBook : foodBookList) {
                    items.add(new ItemNode(foodBook.getPicUrl()));
                }
                RootNode entity = new RootNode(items, DateUtils.getFormatDateTime(DateUtils.valueOf(myFoodBook.getCreateTime()), DateUtils.ymd));
                entityList.add(entity);
            }

        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {

                            myFoodList.setLayoutManager(new GridLayoutManager(mContext, 3));
                            final NodeSectionAdapter nodeAdapter = new NodeSectionAdapter(mContext);
                            // 顶部header
                            View view = getLayoutInflater().inflate(R.layout.head_view, myFoodList, false);
                            nodeAdapter.addHeaderView(view);

                            myFoodList.setAdapter(nodeAdapter);

                            nodeAdapter.setNewData(entityList);
                            myFoodList.scheduleLayoutAnimation();

                        },
                        throwable -> {
                            Toast.makeText(mContext, "新增数据失败", Toast.LENGTH_SHORT).show();
                        }
                ));
    }
}
