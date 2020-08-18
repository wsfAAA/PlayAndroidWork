package com.example.playandroidwork.test;


import androidx.fragment.app.FragmentTransaction;

import com.example.base.mvp.BaseMvpActivity;
import com.example.base.mvp.InjectPresenter;
import com.example.playandroidwork.databinding.ActivityTestBinding;
import com.example.playandroidwork.test.contract.TestViewContract;

/**
 * BaseMvpActivity 使用
 */
public class TestActivity extends BaseMvpActivity<ActivityTestBinding> implements TestViewContract {

    @InjectPresenter
    TestPresenter testPresenter;
    @InjectPresenter
    TestPresenterTow testPresenterTow;


    @Override
    protected void initView() {
        viewBinding.tvContent.setText(testPresenterTow.getTest() + "  activity测试数据");
        testPresenter.getTest();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(viewBinding.fragment.getId(), new TestFragment(), "test");
        transaction.commitAllowingStateLoss();
    }

    @Override
    protected ActivityTestBinding getViewBinding() {
        return ActivityTestBinding.inflate(getLayoutInflater());
    }

    @Override
    public void setTest(String message) {
        viewBinding.tvTextTest.setText(message + "   activity测试数据");
    }
}
