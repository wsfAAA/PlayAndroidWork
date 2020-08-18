package com.example.playandroidwork.test;


import com.example.base.mvp.BaseMvpFragment;
import com.example.base.mvp.InjectPresenter;
import com.example.playandroidwork.databinding.FratmentTestLayoutBinding;

/**
 * BaseMvpFragment 使用
 */
public class TestFragment extends BaseMvpFragment<FratmentTestLayoutBinding> {

    @InjectPresenter
    TestPresenter testPresenter;

    @Override
    protected FratmentTestLayoutBinding getViewBinding() {
        return FratmentTestLayoutBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initView() {
        viewBinding.tvFragmentText.setText(testPresenter.getTestTow()+"    fragment测试");
    }
}
