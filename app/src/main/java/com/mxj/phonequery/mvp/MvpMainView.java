package com.mxj.phonequery.mvp;

/**
 * Created by totem on 2017/10/15.
 */

public interface MvpMainView extends MvpLoadingView{
    void showToast(String msg);
    void updateView();
}
