package com.example.myapp.ui.main.activity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.example.myapp.BaseActivity;
import com.example.myapp.R;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


public class RxJavaActivity extends BaseActivity {

    @BindView(R.id.novel)
    Button bNovel;
    @BindView(R.id.reader1)
    Button bReader1;
    @BindView(R.id.reader2)
    Button bReader2;
    @BindView(R.id.reader3)
    Button bReader3;
    @BindView(R.id.text)
    TextView text;

    //退订是为了防止数据处理异常后继续执行
    private Disposable mDisposable;
    private Disposable mDisposable2;
    private Disposable mDisposable3;

    private Observer<String> reader1;
    private Observer<String> reader2;
    private Observer<String> reader3;


    private Observable novel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_java);

        makeReader();
        //
        mHandler.postDelayed(r, 100);//延时100毫秒
    }

    final Handler mHandler = new Handler();
    Runnable r = new Runnable() {

        @Override
        public void run() {

            text.setText(String.valueOf((Integer.valueOf(text.getText().toString())+1)%4));
            //每隔1s循环执行run方法
            mHandler.postDelayed(this, 500);
        }
    };

    private void makeReader() {
        //创建一个观察者
        reader1 = new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                mDisposable = d;
                Log.e(TAG, "onSubscribe");
            }

            @Override
            public void onNext(String value) {
                switch (value) {
                    case "2":
                        bReader1.setText(value);
                        bReader1.setBackgroundColor(getResources().getColor(R.color.success));
                        break;
                    case "3":
                        bReader1.setText(value);
                        bReader1.setBackgroundColor(getResources().getColor(R.color.gray_color));
                        break;
                    case "0":
                        bReader1.setText(value);
                        bReader1.setBackgroundColor(getResources().getColor(R.color.blue_color));
                        break;
                    case "1":
                        bReader1.setText(value);
                        bReader1.setBackgroundColor(getResources().getColor(R.color.purple));
                        break;
                }

                Log.e(TAG, "onNext:" + value);
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError=" + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.e(TAG, "onComplete()");
            }
        };

        //创建一个观察者
        reader2 = new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                mDisposable2 = d;
                Log.e(TAG, "onSubscribe");
            }

            @Override
            public void onNext(String value) {
                switch (value) {
                    case "0":
                        bReader2.setBackgroundColor(getResources().getColor(R.color.success));
                        break;
                    case "1":
                        bReader2.setBackgroundColor(getResources().getColor(R.color.gray_color));
                        break;
                    case "2":
                        bReader2.setBackgroundColor(getResources().getColor(R.color.blue_color));
                        break;
                    case "3":
                        bReader2.setBackgroundColor(getResources().getColor(R.color.purple));
                        break;
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError=" + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.e(TAG, "onComplete()");
            }
        };

        //创建一个观察者
        reader3 = new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                mDisposable3 = d;
                Log.e(TAG, "onSubscribe");
            }

            @Override
            public void onNext(String value) {
                switch (value) {
                    case "1":
                        bReader3.setBackgroundColor(getResources().getColor(R.color.success));
                        break;
                    case "2":
                        bReader3.setBackgroundColor(getResources().getColor(R.color.gray_color));
                        break;
                    case "3":
                        bReader3.setBackgroundColor(getResources().getColor(R.color.blue_color));
                        break;
                    case "0":
                        bReader3.setBackgroundColor(getResources().getColor(R.color.purple));
                        break;
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError=" + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.e(TAG, "onComplete()");
            }
        };
    }

    @OnClick({R.id.novel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.novel:
                novel = Observable.just(text.getText().toString());
                novel.subscribe(reader1);
                novel.subscribe(reader2);
                novel.subscribe(reader3);
                break;
        }
    }
}
