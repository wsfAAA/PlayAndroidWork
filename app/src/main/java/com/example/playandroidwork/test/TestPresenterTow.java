package com.example.playandroidwork.test;


import com.example.base.mvp.BasePresenter;

public class TestPresenterTow extends BasePresenter<TestActivity, TestModelTow> {

    public String getTest(){
        return getModel().getTest();
    }
}
