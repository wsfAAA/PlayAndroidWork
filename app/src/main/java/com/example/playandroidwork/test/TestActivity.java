package com.example.playandroidwork.test;


import android.widget.TextView;

import androidx.fragment.app.FragmentTransaction;

import com.example.base.mvp.BaseMvpActivity;
import com.example.base.mvp.InjectPresenter;
import com.example.base.view.StatusView;
import com.example.playandroidwork.R;
import com.example.playandroidwork.test.contract.TestContract;

/**
 * BaseMvpActivity 使用
 */
public class TestActivity extends BaseMvpActivity implements TestContract.TestView {

    @InjectPresenter
    TestPresenter testPresenter;
    @InjectPresenter
    TestPresenterTow testPresenterTow;
    private TextView tv_content;
    private TextView tv_text_test;

    @Override
    protected void initView() {
        showLoadingView();
        tv_content = findViewById(R.id.tv_content);
        tv_text_test = findViewById(R.id.tv_text_test);
        tv_content.setText(testPresenterTow.getTest() + "  activity测试数据");

        testPresenter.getTest();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment, new TestFragment(), "test");
        transaction.commitAllowingStateLoss();

        tv_content.postDelayed(new Runnable() {
            @Override
            public void run() {
                showContentView();
            }
        }, 1000);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_test;
    }

    @Override
    public StatusView getStatusView() {
        return findViewById(R.id.status_view);
    }

    @Override
    public void text(String data) {
        tv_text_test.setText(data + "   activity测试数据");
    }
}
