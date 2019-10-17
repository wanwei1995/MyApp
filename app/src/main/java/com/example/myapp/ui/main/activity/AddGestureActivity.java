package com.example.myapp.ui.main.activity;

import android.content.DialogInterface;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.example.myapp.BaseActivity;
import com.example.myapp.R;
import com.example.myapp.myView.MyLayoutAdapter;
import com.example.myapp.util.AlertDialogUtil;
import com.example.myapp.util.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

//一个手势名称可以有多个手势
//添加和删除手势后需要调save方法
public class AddGestureActivity extends BaseActivity {

    @BindView(R.id.gesture)
    GestureOverlayView gesture;
    @BindView(R.id.showAll)
    Button showAll;
    @BindView(R.id.list)
    RecyclerView list;
    @BindView(R.id.image)
    ImageView image;

    private GestureLibrary gestureLibrary;

    private MyLayoutAdapter myLayoutAdapter;

    private List<String> gestureList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_gesture);

        //手势库文件存在时读取，不存在需要新建手势才行
        if (new File(FileUtil.getGesture()).exists()) {
            gestureLibrary = GestureLibraries.fromFile(FileUtil.getGesture());

            if (!gestureLibrary.load()) {
                Toast.makeText(this, "手势库加载失败", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        gesture.setGestureColor(Color.GREEN);
        gesture.setGestureStrokeWidth(5);
        gesture.addOnGesturePerformedListener(new GestureOverlayView.OnGesturePerformedListener() {
            @Override
            public void onGesturePerformed(GestureOverlayView gestureOverlayView, final Gesture gesture) {
                View saveDialog = getLayoutInflater().inflate(R.layout.dialog_save, null, false);
                ImageView img_show = (ImageView) saveDialog.findViewById(R.id.img_show);
                final EditText edit_name = (EditText) saveDialog.findViewById(R.id.edit_name);
                Bitmap bitmap = gesture.toBitmap(128, 128, 10, 0xffff0000);
                image.setImageBitmap(bitmap);
                img_show.setImageBitmap(bitmap);
                new AlertDialog.Builder(AddGestureActivity.this).setView(saveDialog)
                        .setPositiveButton("保存", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                File mStoreFile = null;
                                /*判断mStoreFile是为空。
                                 * 判断手机是否插入SD卡，并且应用程序是否具有访问SD卡的权限
                                 */
                                if (mStoreFile == null && Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                                    mStoreFile = new File(FileUtil.getGesture());
                                }
                                if (!mStoreFile.exists()) {
                                    try {
                                        mStoreFile.createNewFile();
                                        gestureLibrary = GestureLibraries.fromFile(FileUtil.getGesture());
                                    } catch (Exception e) {
                                        Toast.makeText(AddGestureActivity.this, "手势文件创建失败", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                }

                                gestureLibrary.addGesture(edit_name.getText().toString(), gesture);
                                gestureLibrary.save();
                            }
                        }).setNegativeButton("取消", null).show();
            }
        });

        image.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showAll.setVisibility(View.VISIBLE);
                return false;
            }
        });
    }

    @OnClick(R.id.showAll)
    public void onViewClicked() {
        if (!new File(FileUtil.getGesture()).exists()) {
            Toast.makeText(this, "请先添加手势库", Toast.LENGTH_SHORT).show();
            return;
        }
        //显示所有手势
        if (!gestureLibrary.load()) {
            Toast.makeText(this, "手势库加载失败", Toast.LENGTH_SHORT).show();
        }
        list.setLayoutManager(new GridLayoutManager(this, 3));

        //读取手势库
        Set<String> gestureSet = gestureLibrary.getGestureEntries();

        gestureList = new ArrayList(gestureSet);

        myLayoutAdapter = new MyLayoutAdapter<String>(gestureList, R.layout.item_layout_one) {

            @Override
            public void reBindViewHolder(ViewHolder holder, int position, List mData) {
                if (position >= mData.size()) {
                    return;
                }
                holder.setText(R.id.layout_title, (String) mData.get(position));
                ArrayList<Gesture> gestures = gestureLibrary.getGestures((String) mData.get(position));
                Bitmap bitmap = gestures.get(0).toBitmap(128, 128, 10, 0xffff0000);
                holder.setImageBitmap(R.id.layout_image, bitmap);
            }
        };
        myLayoutAdapter.setOnItemClickListener(new MyLayoutAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                AlertDialogUtil.YesOrNo("是否删除手势", AddGestureActivity.this, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //删除手势并刷新layout
                        gestureLibrary.removeEntry(gestureList.get(position));
                        gestureLibrary.save();
                        if (!gestureLibrary.load()) {
                            Toast.makeText(AddGestureActivity.this, "手势库加载失败", Toast.LENGTH_SHORT).show();
                        }
                        myLayoutAdapter.remove(position);
                    }
                });
            }
        });
        list.setAdapter(myLayoutAdapter);
    }
}
