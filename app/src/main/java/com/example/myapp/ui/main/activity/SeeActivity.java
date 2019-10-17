package com.example.myapp.ui.main.activity;

import android.content.Intent;
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
import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.myapp.BaseActivity;
import com.example.myapp.R;
import com.example.myapp.myView.MyAdapter;
import com.example.myapp.ui.main.dto.ImageDto;
import com.example.myapp.ui.main2.dto.MenuDto;
import com.example.myapp.util.BitmapUtil;
import com.example.myapp.util.FileUtil;
import com.example.myapp.util.ImageUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SeeActivity extends BaseActivity {

    @BindView(R.id.pic_view)
    GridView picView;

    private BaseAdapter mAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see);

        List<ImageDto> imageDtos = new ArrayList<>();
        File file;
        try{
            file = FileUtil.getAppImagePath(FileUtil.PRIVATE1,true);
        }catch (Exception e){
            Toast.makeText(this, "文件读取失败", Toast.LENGTH_SHORT).show();
            return;
        }
        File[] files = file.listFiles();
        for(File fileSon :files){
            ImageDto imageDto = new ImageDto();
            imageDto.setUrl(fileSon.getPath());
            imageDtos.add(imageDto);
        }
        mAdapter = new MyAdapter<ImageDto>(imageDtos, R.layout.item_grid_view) {

            @Override
            public void bindView(ViewHolder holder, ImageDto obj) {
                if(obj.getBitmap() == null){
                    obj.setBitmap(BitmapUtil.getBitmap(obj.getUrl()));
                }
                holder.setImageBitmap(R.id.img_icon,BitmapUtil.cropBitmap(obj.getBitmap()));
            }
        };

        picView.setAdapter(mAdapter);

        picView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ImageDto imageDto = imageDtos.get(i);
                try {
                    //跳转至对应页面
                    Intent intent = new Intent(SeeActivity.this, BigPicActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("url",imageDto.getUrl());
                    intent.putExtras(bundle);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
