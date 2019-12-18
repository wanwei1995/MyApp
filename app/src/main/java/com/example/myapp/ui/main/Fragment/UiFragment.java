package com.example.myapp.ui.main.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import butterknife.BindView;
import com.example.myapp.BaseFragment;
import com.example.myapp.R;
import com.example.myapp.myView.MyLayoutAdapter;
import com.example.myapp.ui.main2.dto.MenuDto;

import java.util.List;


public class UiFragment extends BaseFragment {

    @BindView(R.id.ui_grid_tool)
    RecyclerView uiGridTool;

    private MyLayoutAdapter myLayoutAdapter;

    public static UiFragment newInstance() {
        UiFragment fragment = new UiFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_ui, container, false);
        butterKnife(view);

        List<MenuDto> menuDTOs = loadMenus(R.raw.ui_menu);
        showMenu(menuDTOs);
        return view;
    }

    private void showMenu(List<MenuDto> menuDTOs) {
        //显示页面
        myLayoutAdapter = new MyLayoutAdapter<MenuDto>(menuDTOs, R.layout.item_grid_icon) {
            @Override
            public void reBindViewHolder(ViewHolder holder, int position, List<MenuDto> mData) {
                holder.setImageResource(R.id.img_icon, mContext.getResources().getIdentifier(mData.get(position).getIcon(), "mipmap", mContext.getPackageName()));
                holder.setText(R.id.txt_icon, mData.get(position).getName());
            }
        };

        myLayoutAdapter.setOnItemClickListener(new MyLayoutAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                try {
                    //跳转至对应页面
                    Intent intent = new Intent(mContext, Class.forName(mContext.getPackageName() + ".ui.main.activity." + menuDTOs.get(position).getActivity()));
                    Bundle bundle = new Bundle();
                    intent.putExtras(bundle);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        uiGridTool.setLayoutManager(new StaggeredGridLayoutManager(4,StaggeredGridLayoutManager.VERTICAL));
        uiGridTool.setAdapter(myLayoutAdapter);
    }
}
