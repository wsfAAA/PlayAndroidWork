package com.example.playandroidwork.test;


import com.example.base.mvp.BasePresenter;
import com.example.playandroidwork.test.contract.TestPresenterContract;
import com.example.playandroidwork.test.contract.TestViewContract;

public class TestPresenter extends BasePresenter<TestViewContract, TestModel> implements TestPresenterContract {
    @Override
    public void getTest() {
        getView().setTest(getModel().getTest());
    }

    public String getTestTow() {
        return getModel().getTest();
    }
}
