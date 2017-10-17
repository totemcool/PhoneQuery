package com.mxj.phonequery.mvp.impl;

import android.content.Context;

/**
 * Created by totem on 2017/10/15.
 */

public class BasePresenter {
    Context mContext;
    public void attach(Context context){
        mContext = context;
    }
    public void onPause(){

    }
    public void onResume(){

    }
    public void onDestory(){
        mContext = null;
    }
}
