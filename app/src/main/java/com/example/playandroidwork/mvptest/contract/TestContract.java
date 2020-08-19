package com.example.playandroidwork.mvptest.contract;

import com.example.base.mvp.BaseView;

public interface TestContract {

    public interface TestView extends BaseView {
        void text(String data);

        default void text22(String data){}
    }

    public interface TestPresenter {
        void getTest();
    }
}
