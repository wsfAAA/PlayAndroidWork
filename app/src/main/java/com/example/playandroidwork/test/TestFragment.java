package com.example.playandroidwork.test;


import android.view.View;
import android.widget.TextView;

import com.example.base.mvp.BaseMvpFragment;
import com.example.base.mvp.InjectPresenter;
import com.example.playandroidwork.R;

/**
 * BaseMvpFragment 使用
 */
public class TestFragment extends BaseMvpFragment {

    @InjectPresenter
    TestPresenter testPresenter;

    @Override
    protected void initView(View view) {
        TextView textView = view.findViewById(R.id.tv_fragment_text);
        textView.setText(testPresenter.getTestTow() + "    fragment测试");
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fratment_test_layout;
    }
}
