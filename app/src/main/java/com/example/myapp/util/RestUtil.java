package com.example.myapp.util;

import android.app.Activity;
import android.widget.Toast;
import com.example.myapp.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

import java.text.SimpleDateFormat;
import java.util.*;

import static android.widget.Toast.LENGTH_SHORT;

public class RestUtil {

    public static final String URL_TEST = "/test";

    //加密秘钥
    private static String secretKey = "d504a4ef-57cb-4097-bdce-d93ccfbd72d1";

    private static final String CONTENT_TYPE_JSON = "application/json";

    private static final String DEFAUL_CHARSET = "UTF-8";

    private static AsyncHttpClient client = new AsyncHttpClient();

    static {
        client.setTimeout(10 * 1000);
        client.setConnectTimeout(10 * 1000);
        client.setResponseTimeout(10 * 1000);
        client.setMaxRetriesAndTimeout(1, 300);//重试次数以及每次之间的休眠时间
    }

    public static void get(String url, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), addAndConver(null), responseHandler);
    }

    public static void post(String url, Map<String, String> params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), addAndConver(params), responseHandler);
    }

    private static RequestParams addAndConver(Map<String, String> params) {
        if (params == null || params.isEmpty()) {
            params = new HashMap<>();
        }
        params.put("androidVersion", Global.getVersionName());
        params.put("requestTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));


        List<Map.Entry<String, String>> mappingList = new ArrayList<>(params.entrySet());
        //通过比较器实现比较排序
        Collections.sort(mappingList, new Comparator<Map.Entry<String, String>>() {
            @Override
            public int compare(Map.Entry<String, String> mapping1, Map.Entry<String, String> mapping2) {
                return mapping1.getKey().toUpperCase().compareTo(mapping2.getKey().toUpperCase());
            }
        });

        StringBuffer signData = new StringBuffer();
        List<NameValuePair> nameValuePairs = new ArrayList<>();

        RequestParams requestParams = new RequestParams();
        for (Map.Entry<String, String> mapping : mappingList) {
            if (StringUtil.isEmpty(mapping.getValue())) {
                continue;
            }
            signData.append(mapping.getValue()).append(secretKey);
            nameValuePairs.add(new BasicNameValuePair(mapping.getKey(), mapping.getValue()));
            requestParams.put(mapping.getKey(), mapping.getValue());
        }
        nameValuePairs.add(new BasicNameValuePair("sign", MD5Util.md5(signData.toString())));
        requestParams.put("sign", MD5Util.md5(signData.toString()));
        return requestParams;
    }

    //请求地址
    private static String getAbsoluteUrl(String relativeUrl) {
       return "http://10.0.1.209:8281/ww/rest/zwll/"+relativeUrl;
    }

    public abstract static class AbstractTextHttpResponseHandler extends TextHttpResponseHandler {
        private Activity activity;

        public AbstractTextHttpResponseHandler(Activity activity) {
            this.activity = activity;
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            Toast.makeText(activity, R.string.network_socket_time_out, LENGTH_SHORT).show();
        }
    }
}
