package com.example.myapp.ui.main.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import butterknife.BindView;
import com.example.myapp.BaseFragment;
import com.example.myapp.R;
import com.example.myapp.myView.MyAdapter;
import com.example.myapp.ui.main2.dto.MenuDto;

import java.util.List;


public class UiFragment extends BaseFragment {

    @BindView(R.id.ui_grid_tool)
    GridView uiGridTool;

    private BaseAdapter mAdapter = null;

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
        mAdapter = new MyAdapter<MenuDto>(menuDTOs, R.layout.item_grid_icon) {
            @Override
            public void bindView(ViewHolder holder, MenuDto obj) {
                holder.setImageResource(R.id.img_icon, mContext.getResources().getIdentifier(obj.getIcon(), "mipmap", mContext.getPackageName()));
                holder.setText(R.id.txt_icon, obj.getName());
            }
        };

        uiGridTool.setAdapter(mAdapter);
        uiGridTool.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    //跳转至对应页面
                    Intent intent = new Intent(mContext, Class.forName(mContext.getPackageName() + ".ui.main.activity." + menuDTOs.get(i).getActivity()));
                    Bundle bundle = new Bundle();
                    intent.putExtras(bundle);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
