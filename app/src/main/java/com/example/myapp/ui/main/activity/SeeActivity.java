package com.example.myapp.ui.main.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cc.shinichi.library.ImagePreview;
import cc.shinichi.library.view.listener.OnBigImageClickListener;
import cc.shinichi.library.view.listener.OnBigImageLongClickListener;
import cc.shinichi.library.view.listener.OnBigImagePageChangeListener;
import cc.shinichi.library.view.listener.OnOriginProgressListener;
import com.bumptech.glide.request.RequestOptions;
import com.example.myapp.BaseActivity;
import com.example.myapp.R;
import com.example.myapp.myView.MyLayoutAdapter;
import com.example.myapp.util.AlertDialogUtil;
import com.example.myapp.util.FileUtil;
import com.example.myapp.util.GlideUtil;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SeeActivity extends BaseActivity {

    ImagePreview.LoadStrategy loadStrategy = ImagePreview.LoadStrategy.Default;

    boolean enableClickClose = false;
    boolean enableDragClose = false;
    boolean enableUpDragClose = false;
    boolean showIndicator = false;
    boolean showCloseButton = false;
    boolean showDownButton = false;
    boolean showErrorToast = false;


    private static String TAG = "seeActivity";


    @BindView(R.id.pic_view)
    RecyclerView picView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private MyLayoutAdapter myLayoutAdapter;

    private List<String> imageDtos;

    private int showType = 0;

    private MenuItem item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see);


        String folder = getIntent().getExtras().getString("folder");

        imageDtos = new ArrayList<>();
        File file = new File(folder);

        File[] files = file.listFiles();
        for (File fileSon : files) {
            imageDtos.add(fileSon.getPath());
        }
        show(false);
    }

    private void show(boolean flag) {
        myLayoutAdapter = new MyLayoutAdapter<String>(imageDtos, R.layout.item_grid_view) {
            @Override
            public void reBindViewHolder(ViewHolder holder, int position, List<String> mData) {
                if(flag){
                    holder.setImageBitmap(SeeActivity.this, R.id.img_icon, mData.get(position), GlideUtil.getCrop(R.mipmap.icon_loading_girl).override(260, 260));
                }else{
                    holder.setImageBitmap(SeeActivity.this, R.id.img_icon, R.mipmap.icon_loading_girl, GlideUtil.getCrop(R.mipmap.icon_loading_girl).override(260, 260));
                }
            }
        };
        myLayoutAdapter.setOnItemLongClickListener(new MyLayoutAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                //长按删除
                AlertDialogUtil.YesOrNo("请确定是否删除图片", SeeActivity.this, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //删除图片
                        String path = imageDtos.get(position);
                        FileUtil.deletePicture(path, SeeActivity.this);
                        //刷新列表
                        myLayoutAdapter.remove(position);
                    }
                });
            }
        });

        myLayoutAdapter.setOnItemClickListener(new MyLayoutAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                if (showType == 0) {
                    Intent intent = new Intent(SeeActivity.this, BigPicActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("position", String.valueOf(position));
                    bundle.putSerializable("list", (Serializable) imageDtos);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    return;
                }

                ImagePreview.getInstance()
                        // 上下文，必须是activity，不需要担心内存泄漏，本框架已经处理好
                        .setContext(SeeActivity.this)
                        // 从第几张图片开始，索引从0开始哦~
                        .setIndex(position)

                        //=================================================================================================
                        // 有三种设置数据集合的方式，根据自己的需求进行三选一：
                        // 1：第一步生成的imageInfo List
                        //.setImageInfoList(List<ImageInfo> imageInfos)

                        // 2：直接传url List
                        .setImageList(imageDtos)

                        // 3：只有一张图片的情况，可以直接传入这张图片的url
                        //.setImage(String image)
                        //=================================================================================================

                        // 加载策略，默认为手动模式
                        .setLoadStrategy(loadStrategy)

                        // 保存的文件夹名称，会在SD卡根目录进行文件夹的新建。
                        // (你也可设置嵌套模式，比如："BigImageView/Download"，会在SD卡根目录新建BigImageView文件夹，并在BigImageView文件夹中新建Download文件夹)
                        .setFolderName("BigImageView/Download")

                        // 缩放动画时长，单位ms
                        .setZoomTransitionDuration(300)

                        // 是否显示加载失败的Toast
                        .setShowErrorToast(showErrorToast)

                        // 是否启用点击图片关闭。默认启用
                        .setEnableClickClose(enableClickClose)
                        // 是否启用下拉关闭。默认不启用
                        .setEnableDragClose(enableDragClose)
                        // 是否启用上拉关闭。默认不启用
                        .setEnableUpDragClose(enableUpDragClose)
                        // 是否显示关闭页面按钮，在页面左下角。默认不显示
                        .setShowCloseButton(showCloseButton)
                        // 设置关闭按钮图片资源，可不填，默认为库中自带：R.drawable.ic_action_close
                        .setCloseIconResId(R.drawable.ic_action_close)

                        // 是否显示下载按钮，在页面右下角。默认显示
                        .setShowDownButton(showDownButton)
                        // 设置下载按钮图片资源，可不填，默认为库中自带：R.drawable.icon_download_new
                        .setDownIconResId(R.drawable.icon_download_new)

                        // 设置是否显示顶部的指示器（1/9）默认显示
                        .setShowIndicator(showIndicator)

                        // 设置失败时的占位图，默认为库中自带R.drawable.load_failed，设置为 0 时不显示
                        .setErrorPlaceHolder(R.drawable.load_failed)

                        // 点击回调
                        .setBigImageClickListener(new OnBigImageClickListener() {
                            @Override
                            public void onClick(View view, int position) {
                                // ...
                                Log.d(TAG, "onClick: ");
                            }
                        })
                        // 长按回调
                        .setBigImageLongClickListener(new OnBigImageLongClickListener() {
                            @Override
                            public boolean onLongClick(View view, int position) {
                                // ...
                                Log.d(TAG, "onLongClick: ");
                                return false;
                            }
                        })
                        // 页面切换回调
                        .setBigImagePageChangeListener(new OnBigImagePageChangeListener() {
                            @Override
                            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                                Log.d(TAG, "onPageScrolled: ");
                            }

                            @Override
                            public void onPageSelected(int position) {
                                Log.d(TAG, "onPageSelected: ");
                            }

                            @Override
                            public void onPageScrollStateChanged(int state) {
                                Log.d(TAG, "onPageScrollStateChanged: ");
                            }
                        })

                        //=================================================================================================
                        // 设置查看原图时的百分比样式：库中带有一个样式：ImagePreview.PROGRESS_THEME_CIRCLE_TEXT，使用如下：
                        .setProgressLayoutId(ImagePreview.PROGRESS_THEME_CIRCLE_TEXT, new OnOriginProgressListener() {
                            @Override
                            public void progress(View parentView, int progress) {
                                Log.d(TAG, "progress: " + progress);

                                // 需要找到进度控件并设置百分比，回调中的parentView即传入的布局的根View，可通过parentView找到控件：
                                ProgressBar progressBar = parentView.findViewById(R.id.sh_progress_view);
                                TextView textView = parentView.findViewById(R.id.sh_progress_text);
                                progressBar.setProgress(progress);
                                String progressText = progress + "%";
                                textView.setText(progressText);
                            }

                            @Override
                            public void finish(View parentView) {
                                Log.d(TAG, "finish: ");
                            }
                        })
                        // 开启预览
                        .start();
            }
        });

        picView.setLayoutManager(new GridLayoutManager(this, 4));
        picView.setAdapter(myLayoutAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        item = menu.findItem(R.id.action_cart);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_cart://监听菜单按钮
                switch (showType) {
                    case 1:
                        showType = 0;
                        item.setIcon(R.mipmap.icon_eye);
                        //隐藏
                        show(false);
                        break;
                    case 0:
                        showType = 1;
                        item.setIcon(R.mipmap.icon_eye2);
                        //显示
                        show(true);
                        break;
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
