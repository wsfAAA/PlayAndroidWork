package com.example.base.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.base.R;

/**
 * 缺省图
 */
public class StatusView extends FrameLayout {

    private ImageView imgPic;
    private TextView tvContent;

    public StatusView(@NonNull Context context) {
        super(context);
        initView(context);
    }

    public StatusView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public StatusView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }


    private void initView(Context context) {
        View view = inflate(context, R.layout.status_view_layout, this);
        imgPic = view.findViewById(R.id.img_pic);
        tvContent = view.findViewById(R.id.tv_content);

    }

    public void showErrorView() {
        setVisibility(VISIBLE);
        tvContent.setText("网络出小差了~");
    }

    public void showEmptyView() {
        setVisibility(VISIBLE);
        tvContent.setText("没有数据~");
    }

    public void showContentView() {
        setVisibility(GONE);
    }

    public void showLoadingView() {
        setVisibility(VISIBLE);
        tvContent.setText("数据正在加载中~");
    }
}
