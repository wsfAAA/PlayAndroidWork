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
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;


import com.leshu.floatball.R;

import java.lang.ref.WeakReference;

public class FloatView extends LinearLayout implements View.OnTouchListener, View.OnClickListener {
    private WindowManager.LayoutParams windowManagerParams = new WindowManager.LayoutParams();
    private WindowManager mWindowManager;
    private Activity mActivity;

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

    private int mFloatViewWidth, mFloatViewHeiht;  //悬浮框按钮宽高
    private int mRightWidth = 0; //悬浮框展开宽度
    private int mRightHeight = 0; //悬浮框展开高度

    private boolean isLeftOrRight = false;//区分左边还是右边
    private boolean isHide;   //是否靠边隐藏

    private View mLeftView;  //悬浮框展开的view
    private ImageView mFloatIcoView;
    private RIghtFloatImage rIghtWinImage;   //右边视图
    private MyHandler mHandler;

    public FloatView(Activity context, int floatIcon) {
        super(context);
        mActivity = context;
        initView(context);

        this.floatIcon = floatIcon;
        screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        screenHeight = context.getResources().getDisplayMetrics().heightPixels;
//        setOnTouchListener(this);

        addFloat(context);
    }

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

//        hideFolat();
            hideFloatHalfof();
        }catch (Exception e){
            e.printStackTrace();
            Log.e("wsf", "addFloat this:  "+e.toString());
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
                    if (mLeftView.getVisibility() == GONE) {//显示的时候不能移动
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
                        Log.e("wsf", "x  : " + x + "    Y  :" + y + "   oldRawY:" + oldRawY+"     getY: "+getY);
                    }
                }
                break;

            case MotionEvent.ACTION_UP:
                if (isMove) {
                    isMove = false;
                    final Message message = new Message();
                    message.what = MOVE_SLOWLL;
                    message.arg1 = oldRawX - getX;
                    message.arg2 = oldRawY - getY;
                    mHandler.sendMessageDelayed(message, 10);
                } else {
                    //普通的点击事件
                    if (isHide) {//是否半边隐藏,如果是先移出来,在执行接下来的操作
                        if (isLeftOrRight) {
                            moveHalfView(screenWidth - mFloatViewWidth);
                        } else {
                            moveHalfView(0);
                        }
                    }

                    if (isLeftOrRight) {
                        rIghtWinImage = new RIghtFloatImage(screenWidth - mRightWidth, mOnMenuClick, mActivity, mWindowManager, windowManagerParams, floatIcon);
                        rIghtWinImage.setMenuDismiss(() -> {
                            //点击消失的时候
//                                hideFolat();
                            hideFloatHalfof();
                            rIghtWinImage = null;
                        });

                        mHandler.postDelayed(() -> {
                            //移出来之后在消失,而不是马上消失效果
                            mFloatIcoView.setVisibility(INVISIBLE);
                        }, 18);
                    } else {
                        controlIco();
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
            //        Log.e("wsf", "结束的位置" + delatX + ":" + deltaY);
            if (deltaY < 0) {
                deltaY = 0;
            }
            if (deltaY > screenHeight - mRightHeight)
                deltaY = screenHeight - mRightHeight;

            windowManagerParams.x = delatX;
            windowManagerParams.y = deltaY;
            mWindowManager.updateViewLayout(this, windowManagerParams);   // 更新floatView

            if (delatX == 0 || delatX == screenWidth - mFloatViewWidth) {//移动达到了屏幕的边缘
//            hideFolat();
                hideFloatHalfof();
            }
            isHide = false;
        }catch (Exception e){
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
//        Log.e("wsf", " //更新屏幕半边view位置:" + delatX);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static class MyHandler extends Handler {

        private final WeakReference<FloatView> newWinImageWeakReference;

        private MyHandler(FloatView mActivty) {
            this.newWinImageWeakReference = new WeakReference<FloatView>(mActivty);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            FloatView floatView = this.newWinImageWeakReference.get();
            if (floatView != null) {
                switch (msg.what) {
                    case MOVE_SLOWLR:
                        floatView.controlIco();
                        break;

//                    case KEEP_TO_SIDE:  //三秒之后图片变暗
//                        floatView.mFloatIcoView.setImageResource(floatView.darkResource);
//                        floatView.hideFloatHalfof();
//                        break;

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

    private void initView(Context context) {
        View view = View.inflate(context, R.layout.ls_sdk_float_view_left, null);
        mFloatIcoView = (ImageView) view.findViewById(R.id.ll_ico);
        mLeftView = (View) view.findViewById(R.id.ll_layout_left);
        mFloatIcoView.setOnTouchListener(this);
        if (mRightWidth == 0) {
            mLeftView.setVisibility(INVISIBLE);
            ViewTreeObserver vto = view.getViewTreeObserver();
            vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                public boolean onPreDraw() {
                    if (mRightWidth == 0) {
                        mRightWidth = view.getMeasuredWidth();
                        mRightHeight = view.getMeasuredHeight();
                        mLeftView.setVisibility(GONE);
                        mFloatViewWidth = mFloatIcoView.getMeasuredWidth();
//                        Log.e("wsf", "成功:" + mRightWidth);
                    }
                    return true;
                }
            });
        }
        view.findViewById(R.id.ll_custom_account_info).setOnClickListener(this);
        view.findViewById(R.id.ll_custom_recharge_record).setOnClickListener(this);
        this.addView(view);
    }


//    /**
//     * 变成半透明状态
//     */
//    public void hideFolat() {
//        if (mFloatIcoView.getVisibility() == INVISIBLE) {
//            mFloatIcoView.setVisibility(VISIBLE);
//        }
//        if (!isMove && mHandler != null) {//移动的时候不触发功能
//            mHandler.removeMessages(KEEP_TO_SIDE);
//            mHandler.sendEmptyMessageDelayed(KEEP_TO_SIDE, 2000);
//        }
//    }

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
        if (mFloatIcoView.getVisibility() == INVISIBLE) {
            mFloatIcoView.setVisibility(VISIBLE);
        }
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
//            mHandler.removeMessages(KEEP_TO_SIDE);
//            mHandler.removeMessages(MOVE_SLOWLR);
//            mHandler.removeMessages(HIDE);
            mHandler.removeCallbacksAndMessages(null);
        }
    }

    public void removeView() {
        try {
            removeHideHandleFloat();
            if (rIghtWinImage != null) {
                rIghtWinImage.remoView();
            }
            mWindowManager.removeView(this);
        }catch (Exception e){
            e.printStackTrace();
            Log.e("wsf", "removeView this:  "+e.toString());
        }
    }

    public void controlIco() {
        if (mLeftView.getVisibility() == GONE) {
            mLeftView.setVisibility(VISIBLE);
            hideFloatMenu();
        } else {
            mLeftView.setVisibility(GONE);
            cancelHideFloatMenu();
//            hideFolat();
            hideFloatHalfof();
        }
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
