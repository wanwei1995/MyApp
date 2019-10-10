package com.example.myapp.ui.main.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.example.myapp.R;
import com.example.myapp.util.RestUtil;
import com.loopj.android.http.TextHttpResponseHandler;
import cz.msebera.android.httpclient.Header;

import java.util.HashMap;
import java.util.Map;

public class MainFragment extends Fragment implements View.OnClickListener{

    @BindView(R.id.button1)
    Button button1;
    @BindView(R.id.resp_msg)
    EditText respMsg;

    Unbinder unbinder;

    public MainFragment() {
        // Required empty public constructor
    }

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        unbinder = ButterKnife.bind(this, view);
        button1.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button1:
                Map<String, String> params = new HashMap<>();
                params.put("code","20191010");
                params.put("name","wanwei");

                RestUtil.post(RestUtil.URL_TEST, params, new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        button1.setBackgroundColor(getResources().getColor(R.color.error));
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        button1.setBackgroundColor(getResources().getColor(R.color.success));
                        respMsg.setText(responseString);
                    }
                });
                break;
        }
    }
}
