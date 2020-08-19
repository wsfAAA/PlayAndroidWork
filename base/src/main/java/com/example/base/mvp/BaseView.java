package com.example.base.mvp;

import com.example.base.view.StatusView;

public interface BaseView {

    /**
     * 展示错误视图
     */
    default void showErrorView() {
    }

    /**
     * 展示空视图
     */
    default void showEmptyView() {
    }

    /**
     * 展示内容视图
     */
    default void showContentView() {
    }

    /**
     * 展示加载中视图
     */
    default void showLoadingView() {
    }

    /**
     * 缺省图 view
     */
    default StatusView getStatusView() {
        return null;
    }
}
