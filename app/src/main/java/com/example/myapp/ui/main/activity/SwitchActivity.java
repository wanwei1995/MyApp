package com.example.myapp.ui.main.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.ToggleButton;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.myapp.BaseActivity;
import com.example.myapp.R;

public class SwitchActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {

    @BindView(R.id.tbtn_open)
    ToggleButton tbtnOpen;
    @BindView(R.id.swh_status)
    Switch swhStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switch);

        tbtnOpen.setOnCheckedChangeListener(this);
        swhStatus.setOnCheckedChangeListener(this);
    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()){
            case R.id.tbtn_open:
                if(compoundButton.isChecked()) Toast.makeText(this,"关闭声音",Toast.LENGTH_SHORT).show();
                else Toast.makeText(this,"打开声音",Toast.LENGTH_SHORT).show();
                break;
            case R.id.swh_status:
                if(compoundButton.isChecked()) Toast.makeText(this,"开关:ON",Toast.LENGTH_SHORT).show();
                else Toast.makeText(this,"开关:OFF",Toast.LENGTH_SHORT).show();
                break;

        }
    }
}
