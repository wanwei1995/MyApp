package com.example.myapp.ui.main2.Fragment;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.example.myapp.R;
import com.example.myapp.util.AppConfig;
import com.example.myapp.util.BitmapUtil;
import com.example.myapp.util.RealPathFromUriUtils;
import com.example.myapp.util.StringUtil;

public class CallYouFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.addImage)
    ImageButton addImage;
    @BindView(R.id.image)
    ImageButton image;
    @BindView(R.id.phone)
    TextView phoneTv;
    private FragmentActivity mContext;

    private final static int REQUEST_CALL_PERMISSION = 1;

    private static final int REQUEST_EXTERNAL_STORAGE = 2;

    private static String[] PERMISSTIONS_CAMERA = {
            Manifest.permission.CAMERA,
            Manifest.permission.VIBRATE
    };

    private final static int OPEN_PHONE = 1;

    private final static int OPEN_ALBUM = 2;


    @BindView(R.id.call_you)
    ImageButton callYou;

    Unbinder unbinder;

    private String lovePhone = "";

    public static CallYouFragment newInstance() {
        CallYouFragment fragment = new CallYouFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_call_you, container, false);
        unbinder = ButterKnife.bind(this, view);
        callYou.setOnClickListener(this);
        addImage.setOnClickListener(this);
        image.setOnClickListener(this);
        lovePhone = AppConfig.getLovePhone();
        phoneTv.setText(lovePhone);

        String picUrl = AppConfig.getLovePic();
        if (StringUtil.isNotEmpty(picUrl)) {
            addImage.setVisibility(View.GONE);
            image.setImageBitmap(BitmapUtil.changePic(picUrl));
        }
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.call_you:
                if (StringUtil.isEmpty(lovePhone)) {
                    //跳转到电话簿,选择联系人
                    Toast.makeText(mContext, "请先选择并绑定联系人!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    //启动的界面,属于哪一种动作.pick,代表的是选择内容,选取某个数据
                    intent.setAction("android.intent.action.PICK");
                    //用于界定,选择的是什么类型的数据(这里需要获取电话号码数据)
                    //如:intent.setType("image/*");intent.setType("video/*");
                    intent.setType("vnd.android.cursor.dir/phone_v2");
                    startActivityForResult(intent, OPEN_PHONE);
                } else {
                    //打电话
                    int permission = ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE);
                    if (permission != PackageManager.PERMISSION_GRANTED) {
                        //fragment 里面 的权限回调与activity里面不一样
                        requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PERMISSION);
                    } else {
                        call();
                    }
                }
                break;
            case R.id.addImage:
            case R.id.image:
                int permission = ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA);
                if (permission != PackageManager.PERMISSION_GRANTED) {
                    // We don't have permission so prompt the user
                    requestPermissions(PERMISSTIONS_CAMERA, REQUEST_EXTERNAL_STORAGE);
                } else {
                    Intent intentToPickPic = new Intent(Intent.ACTION_PICK, null);
                    // 如果限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型" 所有类型则写 "image/*"
                    intentToPickPic.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                    startActivityForResult(intentToPickPic, OPEN_ALBUM);
                }
                break;
        }

    }

    private void call() {
        Intent intentPhone = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + lovePhone));
        intentPhone.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intentPhone);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case OPEN_PHONE:
                //没有点击条目直接点返回
                if (data == null) {
                    return;
                }
                //content://cpom.android.contacts/data/134.具体的联系人的路径
                Uri uri = data.getData();
                //获取内容解析者
                ContentResolver resolver = mContext.getContentResolver();
                //查询数据
                Cursor cursor = resolver.query(uri, new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER}, null, null, null);
                if (cursor.moveToFirst()) {
                    String phone = cursor.getString(0);
                    if (StringUtil.isNotEmpty(phone)) {
                        lovePhone = phone;
                        phoneTv.setText(lovePhone);
                        AppConfig.set(AppConfig.LOVE_PHONE, phone);
                    }
                }
                cursor.close();
                break;

            case OPEN_ALBUM:
                if (data == null) {
                    return;
                }
                String picUrl = RealPathFromUriUtils.getRealPathFromUri(mContext, data.getData());
                AppConfig.set(AppConfig.LOVE_PIC, picUrl);
                //相册返回
                image.setImageBitmap(BitmapUtil.changePic(picUrl));
                addImage.setVisibility(View.GONE);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    /**
     * 检查权限后的回调
     *
     * @param requestCode  请求码
     * @param permissions  权限
     * @param grantResults 结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CALL_PERMISSION: //拨打电话
                if (permissions.length != 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {//失败
                    Toast.makeText(mContext, "请允许拨号权限后再试", Toast.LENGTH_SHORT).show();
                } else {//成功
                    call();
                }
                break;
        }
    }
}
