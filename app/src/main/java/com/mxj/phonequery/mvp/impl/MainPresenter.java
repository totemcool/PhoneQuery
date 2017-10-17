package com.mxj.phonequery.mvp.impl;

import android.content.Context;

import com.google.gson.Gson;
import com.mxj.phonequery.model.Phone;
import com.mxj.phonequery.mvp.MvpMainView;
import com.mxj.phonequery.mvp.business.HttpUntil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by totem on 2017/10/15.
 */

public class MainPresenter extends BasePresenter{
    private MvpMainView mMvpMainView;
    private String mUrl = "https://tcc.taobao.com/cc/json/mobile_tel_segment.htm";
    private Phone mPhone;

    public MainPresenter(MvpMainView mvpMainView){
        mMvpMainView = mvpMainView;
    }

    public Phone getPhoneInfo(){
        return mPhone;
    }

    public void checkPhone(String phone){
        if(phone.length() != 11){
            mMvpMainView.showToast("请输入正确的手机号码");
            return;
        }
        mMvpMainView.showLoading();
        //http请求的处理逻辑
        sendHttp(phone);
    }
    private void sendHttp(String phone){
        Map<String,String> map = new HashMap<String,String>();
        map.put("tel",phone);
        HttpUntil httpUntil = new HttpUntil(new HttpUntil.HttpResponse(){

            @Override
            public void onSuccess(Object object) {
                String jsonString = object.toString();
                int index = jsonString.indexOf("{");
                jsonString = jsonString.substring(index,jsonString.length());

                mPhone = parseModelwithOrgJson(jsonString);

                mMvpMainView.hideLoading();
                mMvpMainView.updateView();
            }

            @Override
            public void onFail(String error) {
                mMvpMainView.showToast(error);
                mMvpMainView.hideLoading();
            }
        });
        httpUntil.sendGetHttp(mUrl,map);
    }

    private Phone parseModelwithOrgJson(String json){
        Phone phone = new Phone();
        try {
            JSONObject jsonObject = new JSONObject(json);
            String value = jsonObject.getString("telString");
            phone.setTelString(value);

            value = jsonObject.getString("province");
            phone.setProvince(value);

            value = jsonObject.getString("catName");
            phone.setCatName(value);

            value = jsonObject.getString("carrier");
            phone.setCarrier(value);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  phone;
    }

    private Phone parseModelWithGson(String json){
        Gson gson = new Gson();
        Phone phone = gson.fromJson(json,Phone.class);
        return phone;
    }

    private Phone parseModelWithFastJson(String json){
        Phone phone = com.alibaba.fastjson.JSONObject.parseObject(json,Phone.class);
        return phone;
    }
}
