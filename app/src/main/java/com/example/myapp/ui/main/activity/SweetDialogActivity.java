package com.example.myapp.ui.main.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import cn.pedant.SweetAlert.SweetAlertDialog;
import com.example.myapp.BaseActivity;
import com.example.myapp.R;

public class SweetDialogActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sweet_dialog);

        //
        SweetAlertDialog mySweetDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
                .setTitleText("测试");
        mySweetDialog.show();
        mySweetDialog.setCancelable(true);
        mySweetDialog.setCanceledOnTouchOutside(false);
    }
}
