package com.example.myapp.ui.main.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.myapp.R;
import com.example.myapp.myView.MyAsyncTask;

public class AsyncActivity extends AppCompatActivity {

    @BindView(R.id.txttitle)
    TextView txttitle;
    @BindView(R.id.pgbar)
    SeekBar pgbar;
    @BindView(R.id.btnupdate)
    Button btnupdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_async);
        ButterKnife.bind(this);

        btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyAsyncTask myTask = new MyAsyncTask(txttitle,pgbar);
                myTask.execute(10);
                myTask = new MyAsyncTask(txttitle,pgbar);
                myTask.execute(20);
                myTask = new MyAsyncTask(txttitle,pgbar);
                myTask.execute(30);
            }
        });
    }
}
