package com.example.myapp.ui.main.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.gesture.Gesture;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.example.myapp.BaseActivity;
import com.example.myapp.R;
import com.example.myapp.myView.MyAdapter;
import com.example.myapp.myView.MyLayoutAdapter;
import com.example.myapp.ui.main.dto.ImageDto;
import com.example.myapp.ui.main2.dto.MenuDto;
import com.example.myapp.util.AlertDialogUtil;
import com.example.myapp.util.BitmapUtil;
import com.example.myapp.util.FileUtil;
import com.example.myapp.util.ImageUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SeeActivity extends BaseActivity {

    @BindView(R.id.pic_view)
    RecyclerView picView;

    private MyLayoutAdapter myLayoutAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see);

        List<String> imageDtos = new ArrayList<>();
        File file;
        try{
            file = FileUtil.getAppImagePath(FileUtil.PRIVATE1,true);
        }catch (Exception e){
            Toast.makeText(this, "文件读取失败", Toast.LENGTH_SHORT).show();
            return;
        }
        File[] files = file.listFiles();
        for(File fileSon :files){
            imageDtos.add(fileSon.getPath());
        }

        myLayoutAdapter = new MyLayoutAdapter<String>(imageDtos, R.layout.item_grid_view) {
            @Override
            public void reBindViewHolder(ViewHolder holder, int position, List<String> mData) {
                holder.setImageBitmap(SeeActivity.this,R.id.img_icon, mData.get(position));
            }
        };
        myLayoutAdapter.setOnItemClickListener(new MyLayoutAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                try {
                    //跳转至对应页面
                    Intent intent = new Intent(SeeActivity.this, BigPicActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("url",imageDtos.get(position));
                    intent.putExtras(bundle);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        picView.setLayoutManager(new GridLayoutManager(this, 4));
        picView.setAdapter(myLayoutAdapter);
    }
}
