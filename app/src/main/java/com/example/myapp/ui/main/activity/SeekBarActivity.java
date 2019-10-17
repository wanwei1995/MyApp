package com.example.myapp.ui.main.activity;

import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.myapp.R;

public class SeekBarActivity extends AppCompatActivity {

    @BindView(R.id.seekBar)
    SeekBar seekBar;
    @BindView(R.id.txt_cur)
    TextView txtCur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seek_bar);
        ButterKnife.bind(this);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                txtCur.setText("当前进度值:" + progress + "  / 100 ");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //Toast.makeText(SeekBarActivity.this, "触碰SeekBar", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //Toast.makeText(SeekBarActivity.this, "放开SeekBar", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
