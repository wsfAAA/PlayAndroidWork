package com.example.base.mvp;

/**
 * BasePresenter 基类
 */
public class BasePresenter<V extends BaseView> {
    protected V mView;

    public void attach(V view) {
        this.mView = view;
    }

    public void detach() {
        this.mView = null;
    }
}
