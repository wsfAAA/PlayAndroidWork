package com.example.playandroidwork.mvptest;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.base.mvp.BaseMvpViewGroup;
import com.example.base.mvp.InjectPresenter;
import com.example.playandroidwork.R;

/**
 * BaseViewGroup 使用
 */
public class TestView extends BaseMvpViewGroup {
    @InjectPresenter
    TestPresenter testPresenter;

    public TestView(@NonNull Context context) {
        super(context);
    }

    public TestView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TestView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initView(View view) {
        TextView textView = view.findViewById(R.id.tv_text_view);
        textView.setText(testPresenter.getTestTow() + "    viewGroup 测试使用...");
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.test_layout_view;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
