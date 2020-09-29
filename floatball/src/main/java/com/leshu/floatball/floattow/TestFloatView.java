package com.leshu.floatball.floattow;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.leshu.floatball.R;
import com.leshu.floatball.floatone.utils.DensityUtil;

import java.lang.ref.WeakReference;

public class TestFloatView extends LinearLayout implements View.OnTouchListener, View.OnClickListener {
    private WindowManager.LayoutParams windowManagerParams = new WindowManager.LayoutParams();
    private WindowManager mWindowManager;
    private Context mContext;

    private int floatIcon;//默认的悬浮球图片
    private int defaultSpeed = 60;//控制球的移动速度

    private int oldRawX, oldRawY;
    private int getX, getY;

    private int screenWidth, screenHeight;//屏幕的高度

    private boolean isMove = false;  //是否有在移动

    //    private static final int KEEP_TO_SIDE = 0;
    private static final int HIDE = 1;
    private static final int MOVE_SLOWLL = 2;
    private static final int MOVE_SLOWLR = 3;

    private boolean isLeftOrRight = false;//区分左边还是右边
    private boolean isHide;   //是否靠边隐藏

    private MyHandler mHandler;
    private ImageView mFloatIcoView;
    private int mFloatViewWidth;
    private int mFloatViewHeiht;

    public TestFloatView(Activity context, int floatIcon) {
        super(context);
        mContext = context;
        mFloatIcoView = new ImageView(context);

        this.floatIcon = floatIcon;
        mFloatIcoView.setImageResource(floatIcon);
//        mFloatIcoView.setOnTouchListener(this);

        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.width = DensityUtil.dip2px(context, 47);
        layoutParams.height = DensityUtil.dip2px(context, 47);
        mFloatIcoView.setLayoutParams(layoutParams);
        mFloatIcoView.setOnClickListener(onClickListener);
        this.addView(mFloatIcoView);

        screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        screenHeight = context.getResources().getDisplayMetrics().heightPixels;
        addFloat(context);
    }

    private PopupWindow popupWindow;
    private View.OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            View view = View.inflate(mContext, R.layout.ls_sdk_float_view_right, null);
            if (popupWindow != null && popupWindow.isShowing()) {
                popupWindow.dismiss();
            } else {
                popupWindow = new PopupWindow(mContext);
				/*popupWindow.setFocusable(true);
				popupWindow.setOutsideTouchable(true);
				popupWindow.setBackgroundDrawable(new BitmapDrawable());*/
                popupWindow.setClippingEnabled(false);
                //popupWindow.showAsDropDown(v);
//                if (isRight) {
//                    popupView.setBackgroundResource(Utils.getDrawable(context, "dropzonebg"));
//                    popupWindow.showAtLocation(floatView, Gravity.RIGHT, floatView.getWidth(), 0);
//                } else {
//                    popupView.setPadding(Utils.dip2px(context, 10), 0, 0, 0);
//                    popupView.setBackgroundResource(Utils.getDrawable(context, "dropzonebg_left"));
//                    popupWindow.showAtLocation(floatView, Gravity.LEFT, floatView.getWidth(), 0);
//                }
                popupWindow.setContentView(view);
                popupWindow.showAtLocation(mFloatIcoView, Gravity.RIGHT, mFloatIcoView.getWidth(), 0);
            }
        }
    };

    /**
     * 添加悬浮框
     *
     * @param context
     */
    private void addFloat(Activity context) {
        try {
            this.mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            mHandler = new MyHandler(this);

            //设置window type, 级别太高，dialog弹不出来,设置为LAST_APPLICATION_WINDOW并不需要权限：SYSTEM_ALERT_WINDOW
            windowManagerParams.type = WindowManager.LayoutParams.LAST_APPLICATION_WINDOW;

            windowManagerParams.format = PixelFormat.RGBA_8888; // 设置图片格式，效果为背景透明

            // 设置Window flag
            windowManagerParams.flags = windowManagerParams.flags |
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                    WindowManager.LayoutParams.FLAG_FULLSCREEN
                    | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
            // 调整悬浮窗口至左上角，便于调整坐标
            windowManagerParams.gravity = Gravity.LEFT | Gravity.TOP;

            //以屏幕左上角为原点，设置x、y初始值值
            windowManagerParams.x = 0;
            windowManagerParams.y = 200;

            // 设置悬浮窗口长宽数据
            windowManagerParams.width = LayoutParams.WRAP_CONTENT;
            windowManagerParams.height = LayoutParams.WRAP_CONTENT;

            // 显示FloatView悬浮球
            mWindowManager.addView(this, windowManagerParams);

            hideFloatHalfof();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("wsf", "addFloat this:  " + e.toString());
        }
    }

    @Override
    public boolean onTouch(final View v, MotionEvent event) {
        mFloatViewWidth = mFloatIcoView.getWidth();
        mFloatViewHeiht = mFloatIcoView.getHeight();
        oldRawX = (int) event.getRawX();
        oldRawY = (int) event.getRawY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                getX = (int) event.getX();
                getY = (int) event.getY();
                removeHideHandleFloat();
                mFloatIcoView.setImageResource(floatIcon);
                break;
            case MotionEvent.ACTION_MOVE:
                int xMove = Math.abs((int) (event.getX() - getX));
                int yMove = Math.abs((int) (event.getY() - getY));
                if (xMove > 10 || yMove > 10) {
                    //x轴或y轴方向的移动距离大于5个像素，视为拖动，否则视为点击
//                    if (mLeftView.getVisibility() == GONE) {//菜单显示的时候不能移动
                    removeHideHandleFloat();
                    isMove = true;
                    int x, y;
                    x = oldRawX - getX;
                    y = oldRawY - getY;
                    if (x < 0)//处理越界屏幕问题
                        x = 0;
                    if (x > screenWidth - mFloatViewWidth)
                        x = screenWidth - mFloatViewWidth;

                    if (y < 0)
                        y = 0;
                    if (y > screenHeight - mFloatViewHeiht - mFloatViewWidth / 2)
                        y = screenHeight - mFloatViewHeiht - mFloatViewWidth / 2;

                    moveView(x, y);
//                    }
                }
                break;

            case MotionEvent.ACTION_UP:
//                if (isMove) {
//                    isMove = false;
//                    final Message message = new Message();
//                    message.what = MOVE_SLOWLL;
//                    message.arg1 = oldRawX - getX;
//                    message.arg2 = oldRawY - getY;
//                    mHandler.sendMessageDelayed(message, 10);
//                } else {
//                    //普通的点击事件
//                    if (isHide) {//是否半边隐藏,如果是先移出来,在执行接下来的操作
//                        if (isLeftOrRight) {
//                            moveHalfView(screenWidth - mFloatViewWidth);
//                        } else {
//                            moveHalfView(0);
//                        }
//                    }
//
//                    if (isLeftOrRight) {
//
//                    } else {
//                        controlIco();
//                    }
//                }
                break;
        }
        return true;
    }

    /**
     * 更新悬浮球位置
     *
     * @param delatX
     * @param deltaY
     */
    public void moveView(int delatX, int deltaY) {
        try {
            if (deltaY < 0) {
                deltaY = 0;
            }
            if (deltaY > screenHeight - mFloatViewHeiht)
                deltaY = screenHeight - mFloatViewHeiht;

            windowManagerParams.x = delatX;
            windowManagerParams.y = deltaY;
            mWindowManager.updateViewLayout(this, windowManagerParams);   // 更新floatView

            if (delatX == 0 || delatX == screenWidth - mFloatViewWidth) {//移动达到了屏幕的边缘
                hideFloatHalfof();
            }
            isHide = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 悬浮框图片 移动到屏幕外 一半
     *
     * @param delatX
     */
    public void moveHalfView(int delatX) {
        try {
            windowManagerParams.x = delatX;
            mWindowManager.updateViewLayout(this, windowManagerParams); // 更新floatView
            isHide = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class MyHandler extends Handler {

        private final WeakReference<TestFloatView> newWinImageWeakReference;

        private MyHandler(TestFloatView mActivty) {
            this.newWinImageWeakReference = new WeakReference<TestFloatView>(mActivty);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            TestFloatView floatView = this.newWinImageWeakReference.get();
            if (floatView != null) {
                switch (msg.what) {
                    case MOVE_SLOWLR:
                        floatView.controlIco();
                        break;
                    case HIDE://半边隐藏
                        if (floatView.isLeftOrRight) {//右边
                            floatView.moveHalfView(floatView.screenWidth - floatView.mFloatViewWidth / 2);
                        } else {//左边
                            floatView.moveHalfView(-floatView.mFloatViewWidth / 2);
                        }
                        break;

                    case MOVE_SLOWLL://动画效果
                        int mDistance = msg.arg1;
                        int xMiddle = floatView.screenWidth / 2;

                        if (xMiddle > mDistance) {//左边
                            floatView.isLeftOrRight = false;
                            mDistance = mDistance - floatView.defaultSpeed;
                            if (mDistance < 0)//防止超过屏幕
                                mDistance = 0;
                        } else {//右边
                            floatView.isLeftOrRight = true;
                            mDistance = mDistance + floatView.defaultSpeed;
                            if (mDistance > floatView.screenWidth - floatView.mFloatViewWidth)//防止超过屏幕
                                mDistance = floatView.screenWidth - floatView.mFloatViewWidth;
                        }

                        floatView.moveView(mDistance, msg.arg2);
                        if (mDistance >= floatView.screenWidth - floatView.mFloatViewWidth)//结束动画
                            return;

                        if (mDistance == 0) //结束动画
                            return;

                        final Message message = new Message();
                        message.what = MOVE_SLOWLL;
                        message.arg1 = mDistance;
                        message.arg2 = msg.arg2;
                        message.obj = msg.obj;
                        floatView.mHandler.sendMessageDelayed(message, 18);
                        break;
                }
            }
        }

    }


    /**
     * 自动隐藏悬浮框菜单 倒计时
     */
    public void hideFloatMenu() {
        if (mHandler != null) {
            mHandler.removeMessages(MOVE_SLOWLR);
            mHandler.sendEmptyMessageDelayed(MOVE_SLOWLR, 4000);
        }
    }

    /**
     * 启动移动到半边计时器
     */
    public void hideFloatHalfof() {
        mFloatIcoView.setImageResource(floatIcon);
        if (mHandler != null && !isMove) { //移动的时候不触发功能
            mHandler.removeMessages(HIDE);
            mHandler.sendEmptyMessageDelayed(HIDE, 2000);
        }
    }

    public void cancelHideFloatMenu() {
        if (mHandler != null) {
            mHandler.removeMessages(MOVE_SLOWLR);
        }
    }

    private void removeHideHandleFloat() {
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }

    public void removeView() {
        try {
            removeHideHandleFloat();
            mWindowManager.removeView(this);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("wsf", "removeView this:  " + e.toString());
        }
    }

    public void controlIco() {
//        if (mLeftView.getVisibility() == GONE) {
//            mLeftView.setVisibility(VISIBLE);
//            hideFloatMenu();
//        } else {
//            mLeftView.setVisibility(GONE);
//            cancelHideFloatMenu();
////            hideFolat();
//            hideFloatHalfof();
//        }
    }

    private onMenuClick mOnMenuClick;

    public void setOnMenuClick(onMenuClick onMenuClick) {
        this.mOnMenuClick = onMenuClick;
    }

    @Override
    public void onClick(View view) {
        if (mOnMenuClick == null) {
            return;
        }
        controlIco();
        if (view.getId() == R.id.ll_custom_account_info) {
            mOnMenuClick.onAccountInfo();
        } else if (R.id.ll_custom_recharge_record == view.getId()) {
            mOnMenuClick.onRechargeRecord();
        }
    }
}
