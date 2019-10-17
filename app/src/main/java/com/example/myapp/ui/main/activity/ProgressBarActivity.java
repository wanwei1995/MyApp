package com.example.myapp.ui.main.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.example.myapp.BaseActivity;
import com.example.myapp.R;
import com.example.myapp.myView.CirclePgBar;
import com.example.myapp.util.StringUtil;

import static android.widget.Toast.LENGTH_SHORT;

public class ProgressBarActivity extends BaseActivity {

    @BindView(R.id.pg_1)
    CirclePgBar pg1;
    @BindView(R.id.change)
    EditText change;
    @BindView(R.id.change2)
    EditText change2;
    @BindView(R.id.changeButton)
    Button changeButton;
    @BindView(R.id.changeButton2)
    Button changeButton2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_bar);
        ButterKnife.bind(this);
        pg1.setmTargetProgress(50);
    }

    @OnClick({R.id.changeButton, R.id.changeButton2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.changeButton:

                String value = change.getText().toString().trim();
                if (StringUtil.isEmpty(value)) {
                    Toast.makeText(getApplicationContext(), R.string.error_enter_empty, LENGTH_SHORT).show();
                    return;
                }

                pg1.reDraw(Integer.valueOf(value));
                change.setText("");
                break;
            case R.id.changeButton2:
                String value2 = change2.getText().toString().trim();
                if (StringUtil.isEmpty(value2)) {
                    Toast.makeText(getApplicationContext(), R.string.error_enter_empty, LENGTH_SHORT).show();
                    return;
                }

                pg1.changeDraw(Integer.valueOf(value2));
                change2.setText("");
                break;
        }
    }
}
