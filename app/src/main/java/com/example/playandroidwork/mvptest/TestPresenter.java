package com.example.playandroidwork.mvptest;


import com.example.base.mvp.BasePresenter;
import com.example.playandroidwork.mvptest.contract.TestContract;

public class TestPresenter extends BasePresenter<TestContract.TestView> implements TestContract.TestPresenter {

    @Override
    public void getTest() {
        mView.text("测试数据1。。。。。");
        mView.text22("bbbbbbbb");
    }

    public String getTestTow() {
        return "测试数据22222.。。。。。。。。";
    }
}
