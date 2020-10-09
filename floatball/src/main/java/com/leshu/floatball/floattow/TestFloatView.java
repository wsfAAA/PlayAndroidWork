package com.leshu.floatball.floattow;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.leshu.floatball.floatone.utils.DensityUtil;

import java.lang.ref.WeakReference;

public class TestFloatView extends LinearLayout implements View.OnTouchListener {
    private WindowManager.LayoutParams windowManagerParams = new WindowManager.LayoutParams();
    private WindowManager mWindowManager;
    private Context mContext;

    private int floatIcon;//默认的悬浮球图片
    private int defaultSpeed = 60;//控制球的移动速度

    private int oldRawX, oldRawY;
    private int getX, getY;

    private int screenWidth, screenHeight;//屏幕的高度

    private boolean isMove = false;  //是否有在移动

    private static final int HIDE = 1;
    private static final int MOVE_SLOWLL = 2;

    private boolean isLeftOrRight = false;//区分左边还是右边
    private boolean isHide;   //是否靠边隐藏

    private MyHandler mHandler;
    private ImageView mFloatIcoView;
    private int mFloatViewWidth;
    private int mFloatViewHeiht;

    public TestFloatView(Context context, int floatIcon) {
        super(context);
        mContext = context;
        mFloatIcoView = new ImageView(context);

        this.floatIcon = floatIcon;
        mFloatIcoView.setImageResource(floatIcon);
        mFloatIcoView.setOnTouchListener(this);

        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.width = DensityUtil.dip2px(context, 47);
        layoutParams.height = DensityUtil.dip2px(context, 47);
        mFloatIcoView.setLayoutParams(layoutParams);
        this.addView(mFloatIcoView);

        screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        screenHeight = context.getResources().getDisplayMetrics().heightPixels;
        addFloat(context);
    }

    /**
     * 添加悬浮框
     *
     * @param context
     */
    private void addFloat(Context context) {
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
//                if (xMove > 5 || yMove > 5) {
                    removeHideHandleFloat();
                    if (onMoveListener != null) {
                        onMoveListener.onMove();
                    }
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
                    if (y > screenHeight - mFloatViewHeiht)
                        y = screenHeight - mFloatViewHeiht;

                    moveView(x, y);
//                }
                break;

            case MotionEvent.ACTION_UP:
                if (isMove) {
                    isMove = false;
                    final Message message = new Message();
                    message.what = MOVE_SLOWLL;
                    message.arg1 = oldRawX - getX;
                    message.arg2 = oldRawY - getY;
                    mHandler.sendMessageDelayed(message, 18);
                } else {
                    if (isHide) {//是否半边隐藏,如果是先移出来,在执行接下来的操作
                        if (isLeftOrRight) {
                            moveHalfView(screenWidth - mFloatViewWidth);
                        } else {
                            moveHalfView(0);
                        }
                    }
                    if (onMoveListener != null) {
                        onMoveListener.showMenu(isLeftOrRight);
                    }
                }
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
     * 启动移动到半边计时器
     */
    public void hideFloatHalfof() {
        if (mHandler != null && !isMove) { //移动的时候不触发功能
            mHandler.removeMessages(HIDE);
            mHandler.sendEmptyMessageDelayed(HIDE, 2000);
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
            mWindowManager = null;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("wsf", "removeView this:  " + e.toString());
        }
    }

    private OnMoveListener onMoveListener;

    public void setOnMoveListener(OnMoveListener m) {
        this.onMoveListener = m;
    }

    public interface OnMoveListener {
        void onMove();

        void showMenu(boolean isLeftOrRight);
    }
}
