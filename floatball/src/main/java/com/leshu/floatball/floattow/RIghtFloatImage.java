package com.leshu.floatball.floattow;

import android.app.Activity;
import android.os.CountDownTimer;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.leshu.floatball.R;


public class RIghtFloatImage extends LinearLayout {

    private CountDownTimer timer;
    WindowManager.LayoutParams windowManagerParams;
    private WindowManager mWindowManager;

    public RIghtFloatImage(int x, onMenuClick onMenuClick, Activity context, final WindowManager mWindowManager, final WindowManager.LayoutParams windowParams,  final int floatIcon) {
        super(context);
        View view = View.inflate(context, R.layout.ls_sdk_float_view_right, null);
        this.addView(view);
        this.initItemView(view, onMenuClick,floatIcon);

        try {
            this.mWindowManager = mWindowManager;
            this.windowManagerParams = windowParams;

            windowManagerParams.x = x;
            mWindowManager.addView(this, windowManagerParams);
        }catch (Exception e){
            e.printStackTrace();
        }
        startTimerCount();
    }

    public void remoView() {
        try {
            cancelTimerCount();
            mWindowManager.removeView(this);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void initItemView(final View view, final onMenuClick mOnMenuClick,final int floatIcon) {
        view.findViewById(R.id.ll_custom_account_info).setOnClickListener(v -> {
            cancelTimerCount();
            remoView();
            mOnMenuClick.onAccountInfo();
            mOnMenuDiss.onDismiss();
        });
        view.findViewById(R.id.ll_custom_recharge_record).setOnClickListener(v -> {
            cancelTimerCount();
            remoView();
            mOnMenuClick.onRechargeRecord();
            mOnMenuDiss.onDismiss();
        });

        ImageView iconView=findViewById(R.id.ll_ico);
        iconView.setImageResource(floatIcon);
        iconView.setOnClickListener(v -> {
            cancelTimerCount();
            remoView();
            mOnMenuDiss.onDismiss();
        });
    }

    public void setMenuDismiss(onMenuDiss onMenuDiss) {
        this.mOnMenuDiss = onMenuDiss;
    }

    private onMenuDiss mOnMenuDiss;

    public void startTimerCount() {
        if (timer == null) {
            timer = new CountDownTimer(4000, 1000) {
                @Override
                public void onTick(long l) {
                }

                @Override
                public void onFinish() {
                    cancelTimerCount();
                    remoView();
                    if (mOnMenuDiss!=null){
                        mOnMenuDiss.onDismiss();
                    }
                }
            };
        }
        timer.start();
    }

    public void cancelTimerCount() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }


}
