package com.example.myapp.ui.main.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;
import com.example.myapp.BaseActivity;
import com.example.myapp.R;
import com.example.myapp.myView.MyLayoutAdapter;
import com.example.myapp.myView.RecyclerViewDivider;
import com.example.myapp.myView.SimpleDividerItemDecoration;
import com.example.myapp.ui.main.dto.FolderDto;
import com.example.myapp.util.DateUtils;
import com.example.myapp.util.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

//文件夹
public class FolderActivity extends BaseActivity {

    @BindView(R.id.folder_list)
    RecyclerView folderListRv;
    @BindView(R.id.add)
    ImageButton add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder);

        File file = FileUtil.getAppImagePath(FileUtil.PRIVATE1, true);
        if (!file.exists()) {
            //新建
            FileUtil.createAppFolder(FileUtil.PRIVATE1, true);
        }
        show(file);
    }

    private void show(File file){
        List<FolderDto> folderList = new ArrayList<>();
        FolderDto folderDto;
        //读取文件夹下所有文件
        File[] files = file.listFiles();
        for (File fileSon : files) {
            if (fileSon.isDirectory()) {
                folderDto = new FolderDto();
                folderDto.setDate(fileSon.lastModified());
                folderDto.setName(fileSon.getName());
                folderDto.setTotalSize(fileSon.length());
                folderDto.setTotalNum(fileSon.listFiles().length);
                folderDto.setUrl(fileSon.getAbsolutePath());
                folderList.add(folderDto);
            }
        }

        MyLayoutAdapter myLayoutAdapter = new MyLayoutAdapter<FolderDto>(folderList, R.layout.item_folder) {
            @Override
            public void reBindViewHolder(ViewHolder holder, int position, List<FolderDto> mData) {
                FolderDto folderDto = mData.get(position);
                holder.setText(R.id.layout_title, folderDto.getName());
                holder.setText(R.id.layout_other, folderDto.getTotalNum()+"项"+ " | " + DateUtils.getFormatDateTime(DateUtils.valueOf(folderDto.getDate()),DateUtils.M_D_HM)+ "  " +folderDto.getTotalSize()/(1024*1024));
            }
        };

        myLayoutAdapter.setOnItemClickListener(new MyLayoutAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(View view, int position) {
                //点击读取指定页面
                String folder = folderList.get(position).getUrl();
                Intent intent = new Intent(FolderActivity.this,SeeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("folder",folder);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        //设置布局管理器

        folderListRv.setAdapter(myLayoutAdapter);
        folderListRv.setLayoutManager(layoutManager);
        //设置为垂直布局，这也是默认的
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        //设置分隔线
        folderListRv.addItemDecoration(new SimpleDividerItemDecoration(this));
        //设置增加或删除条目的动画
        folderListRv.setItemAnimator(new DefaultItemAnimator());
    }

    @OnClick(R.id.add)
    public void onViewClicked() {
        //新建文件夹
        //加载对话框资源界面
        final EditText et = new EditText(this);
        //不可换行
        et.setSingleLine();
        new AlertDialog.Builder(this).setTitle("新建文件夹")
                .setIcon(R.mipmap.icon_folder)
                .setView(et)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //新建文件夹
                        FileUtil.createAppFolder(FileUtil.getPrivate() + "/"+et.getText().toString());
                        show(FileUtil.getAppImagePath(FileUtil.PRIVATE1, true));
                    }
                }).setNegativeButton("取消", null).show();
    }
}
