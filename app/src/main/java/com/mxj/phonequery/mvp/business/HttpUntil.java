package com.mxj.phonequery.mvp.business;


import android.os.Handler;
import android.os.Looper;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import java.util.logging.LogRecord;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by totem on 2017/10/15.
 */

public class HttpUntil {
    private String mUrl;
    private Map<String, String> mParam;
    private HttpResponse mHttpResponse;

    private OkHttpClient client = new OkHttpClient();

    Handler myHandler = new Handler(Looper.getMainLooper());

    public interface HttpResponse {
        void onSuccess(Object object);
        void onFail(String error);
    }

    public HttpUntil(HttpResponse httpResponse){
        mHttpResponse = httpResponse;
    }

    public void sendPostHttp(String url, Map<String, String> param) {
        sendHttp(url, param, true);
    }

    public void sendGetHttp(String url, Map<String, String> param) {
        sendHttp(url, param, false);
    }

    private void sendHttp(String url, Map<String, String> param, boolean isPost) {
        mUrl = url;
        mParam = param;
        run(isPost);
    }

    private void run(boolean isPost){
        Request request = createRequest(isPost);
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if(mHttpResponse != null){
                    myHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mHttpResponse.onFail("请求错误");
                        }
                    });
                }
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if(mHttpResponse != null){
                    myHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if(!response.isSuccessful()){
                                mHttpResponse.onFail("请求失败:code" + response);
                            }else{
                                try {
                                    mHttpResponse.onSuccess(response.body().string());
                                } catch (IOException e) {
                                    mHttpResponse.onFail("请求失败:code" + response);
                                }
                            }
                        }
                    });
                }
            }
        });
    }
    private Request createRequest(boolean isPost){
        Request request;
        if (isPost){
            MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder();
            requestBodyBuilder.setType(MultipartBody.FORM);
            Iterator<Map.Entry<String,String>> iterator = mParam.entrySet().iterator();
            while (iterator.hasNext()){
                Map.Entry<String,String> entry = iterator.next();
                requestBodyBuilder.addFormDataPart(entry.getKey(),entry.getValue());
            }
            request = new okhttp3.Request.Builder().url(mUrl).post(requestBodyBuilder.build()).build();
        }else{
            String urlStr = mUrl + "?" + MapParamToString(mParam);
            request = new Request.Builder().url(urlStr).build();
        }
        return request;
    }

    private String MapParamToString(Map<String,String> param){
        StringBuilder stringBuilder = new StringBuilder();
        Iterator<Map.Entry<String,String>> iterator = mParam.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String,String> entry = iterator.next();
           stringBuilder.append(entry.getKey() + "=" + entry.getValue() + "");
        }
        String str = stringBuilder.toString().substring(0,stringBuilder.length());
        return str;
    }
}
