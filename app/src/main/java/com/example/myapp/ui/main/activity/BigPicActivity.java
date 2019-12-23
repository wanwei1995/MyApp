package com.example.myapp.ui.main.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.bumptech.glide.Glide;
import com.example.myapp.BaseActivity;
import com.example.myapp.R;
import com.example.myapp.myView.FogView;

import java.util.List;

public class BigPicActivity extends BaseActivity {

    @BindView(R.id.image)
    ImageView image;

    int position;

    List<String> urlList;
    @BindView(R.id.button_previous)
    Button buttonPrevious;
    @BindView(R.id.button_next)
    Button buttonNext;
    @BindView(R.id.rubFog)
    FogView rubFog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_pic);

        position = Integer.valueOf(getIntent().getStringExtra("position"));
        urlList = (List<String>) getIntent().getExtras().getSerializable("list");
        showPic();
    }

    private void showPic() {
        Glide.with(this)
                .load(urlList.get(position))
                .into(image);
    }


    @OnClick({R.id.button_previous, R.id.button_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.button_previous:
                if (position == 0) {
                    return;
                }
                position--;
                showPic();
                break;
            case R.id.button_next:
                if (position == urlList.size() - 1) {
                    return;
                }
                position++;
                showPic();
                break;
        }
    }
}
