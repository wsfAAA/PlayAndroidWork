package com.leshu.floatball.floattow;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.leshu.floatball.R;


/**
 * 悬浮球管理类
 */
public class FloatManager {
    private TestFloatView floatView = null;
    private boolean isDisplay = false;
    private Context context;
    private PopupWindow popupWindow;

    private static FloatManager instance = null;

    public static FloatManager getFloatManager(Context context) {
        if (instance == null) {
            instance = new FloatManager(context);
        }
        return instance;
    }

    private FloatManager(Context context) {
        this.context = context;
    }

    public void createView() {
        if (isDisplay)
            return;
        floatView = new TestFloatView(context, R.mipmap.ic_launcher_round);
        floatView.setOnMoveListener(new TestFloatView.OnMoveListener() {
            @Override
            public void onMove() {
                dismisPopupWindow();
            }

            @Override
            public void showMenu(boolean isLeftOrRight) {
                View view = View.inflate(context, com.leshu.floatball.R.layout.ls_sdk_float_view_right, null);
                LinearLayout linearLayout = view.findViewById(R.id.ll_menu);
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) linearLayout.getLayoutParams();
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                } else {
                    popupWindow = new PopupWindow(context);
                    popupWindow.setContentView(view);
                    popupWindow.setClippingEnabled(false);
                    popupWindow.setBackgroundDrawable(new BitmapDrawable());
                    if (isLeftOrRight) {
                        layoutParams.rightMargin = 14;
                        linearLayout.setBackgroundResource(R.mipmap.icon_float_right);
                        popupWindow.showAtLocation(floatView, Gravity.RIGHT, floatView.getWidth(), 0);
                    } else {
                        layoutParams.leftMargin = 14;
                        linearLayout.setBackgroundResource(R.mipmap.icon_float_bg);
                        popupWindow.showAtLocation(floatView, Gravity.LEFT, floatView.getWidth(), 0);
                    }
                }
            }
        });
        isDisplay = true;
    }


    private void dismisPopupWindow() {
        if (popupWindow != null) {
            popupWindow.dismiss();
            popupWindow = null;
        }
    }

    public void destroyFloat() {
        if (!isDisplay)
            return;
        if (floatView != null) {
            floatView.removeView();
        }
        floatView = null;
        isDisplay = false;
        instance = null;
    }

}
