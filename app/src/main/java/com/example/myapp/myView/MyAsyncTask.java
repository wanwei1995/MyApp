package com.example.myapp.myView;

import android.os.AsyncTask;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MyAsyncTask extends AsyncTask<Integer, Integer, String> {

    private TextView txt;
    private ProgressBar pgbar;

    public MyAsyncTask(TextView txt, ProgressBar pgbar) {
        super();
        this.txt = txt;
        this.pgbar = pgbar;
    }


    @Override
    protected String doInBackground(Integer... params) {

        int value = params[0];
        int i = 0;
        while (i < value) {
            delay();
            publishProgress(i);
            i++;
        }
        return value + params[0].intValue() + "";
    }

    private void delay() {
        try {
            Thread.sleep(100);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPreExecute() {
        txt.setText("开始执行异步线程~");
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        int value = values[0];
        pgbar.setProgress(value);
    }
}
