package com.example.playandroidwork.activity;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.playandroidwork.R;
import com.leshu.floatball.floatone.FloatBallManager;
import com.leshu.floatball.floatone.floatball.FloatBallCfg;
import com.leshu.floatball.floatone.menu.FloatMenuCfg;
import com.leshu.floatball.floatone.menu.MenuItem;
import com.leshu.floatball.floatone.utils.BackGroudSeletor;
import com.leshu.floatball.floatone.utils.DensityUtil;
import com.leshu.floatball.floattow.FloatManager;
import com.leshu.floatball.floattow.TestFloatView;
import com.leshu.floatball.floattow.onMenuClick;

public class FloatBallActivity extends AppCompatActivity {

    private FloatBallManager mFloatballManager;
    private TestFloatView mFloatView;
    private PopupWindow popupWindow;
    private View viewById;

    public void showFloatBall(View v) {
//        mFloatballManager.show();
        setFullScreen(v);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_float_ball);
        oneFloat();

        towFloat();

    }

    private void test() {
        View view = View.inflate(this, com.leshu.floatball.R.layout.ls_sdk_float_view_right, null);
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        } else {
            popupWindow = new PopupWindow(this);
//            popupWindow.setFocusable(true);
//            popupWindow.setOutsideTouchable(true);
//            popupWindow.setBackgroundDrawable(new BitmapDrawable());
            popupWindow.setClippingEnabled(false);
//                if (isRight) {
//                    popupView.setBackgroundResource(Utils.getDrawable(context, "dropzonebg"));
//                    popupWindow.showAtLocation(floatView, Gravity.RIGHT, floatView.getWidth(), 0);
//                } else {
//                    popupView.setPadding(Utils.dip2px(context, 10), 0, 0, 0);
//                    popupView.setBackgroundResource(Utils.getDrawable(context, "dropzonebg_left"));
//                    popupWindow.showAtLocation(floatView, Gravity.LEFT, floatView.getWidth(), 0);
//                }
            popupWindow.setContentView(view);
//            popupWindow.showAsDropDown(viewById, Gravity.BOTTOM,0,0);
            popupWindow.showAsDropDown(viewById);
        }
    }

    private void towFloat() {
        Button button = findViewById(R.id.bt_tow);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                test();
                if (button.getText().toString().equals("悬浮球2")) {
                    button.setText("关闭悬浮框");
//                    openFloat(FloatBallActivity.this);
                    FloatManager.getFloatManager(FloatBallActivity.this).createView();
                } else {
                    button.setText("悬浮球2");
//                    closeFloat();
                    FloatManager.getFloatManager(FloatBallActivity.this).destroyFloat();
                }
            }
        });
    }

    private void oneFloat() {
        boolean showMenu = true;//换成false试试
        initSinglePageFloatball(showMenu);
        //5 如果没有添加菜单，可以设置悬浮球点击事件
        if (mFloatballManager.getMenuItemSize() == 0) {
            mFloatballManager.setOnFloatBallClickListener(new FloatBallManager.OnFloatBallClickListener() {
                @Override
                public void onFloatBallClick() {
                    toast("点击了悬浮球");
                }
            });
        }

        findViewById(R.id.bt_hint).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFloatballManager.hide();
            }
        });
        viewById = findViewById(R.id.bt);
        viewById.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFloatballManager.show();
            }
        });
    }

    private void exitFullScreen() {
        final WindowManager.LayoutParams attrs = getWindow().getAttributes();
        attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setAttributes(attrs);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        isfull = false;
    }

    private boolean isfull = false;

    public void setFullScreen(View view) {
        if (isfull == true) {
            exitFullScreen();
        } else {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            isfull = true;
        }
    }

    private void initSinglePageFloatball(boolean showMenu) {
        //1 初始化悬浮球配置，定义好悬浮球大小和icon的drawable
        int ballSize = DensityUtil.dip2px(this, 45);
        Drawable ballIcon = BackGroudSeletor.getdrawble("ic_floatball", this);
        FloatBallCfg ballCfg = new FloatBallCfg(ballSize, ballIcon, FloatBallCfg.Gravity.RIGHT_CENTER);
        //设置悬浮球不半隐藏
//        ballCfg.setHideHalfLater(false);
        if (showMenu) {
            //2 需要显示悬浮菜单
            //2.1 初始化悬浮菜单配置，有菜单item的大小和菜单item的个数
            int menuSize = DensityUtil.dip2px(this, 180);
            int menuItemSize = DensityUtil.dip2px(this, 40);
            FloatMenuCfg menuCfg = new FloatMenuCfg(menuSize, menuItemSize);
            //3 生成floatballManager
            //必须传入Activity
            mFloatballManager = new FloatBallManager(this, ballCfg, menuCfg);
            addFloatMenuItem();
        } else {
            //必须传入Activity
            mFloatballManager = new FloatBallManager(this, ballCfg);
        }
    }


    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void addFloatMenuItem() {
        MenuItem personItem = new MenuItem(BackGroudSeletor.getdrawble("ic_weixin", this)) {
            @Override
            public void action() {
                toast("打开微信");
                mFloatballManager.closeMenu();
            }
        };
        MenuItem walletItem = new MenuItem(BackGroudSeletor.getdrawble("ic_weibo", this)) {
            @Override
            public void action() {
                toast("打开微博");
            }
        };
        MenuItem settingItem = new MenuItem(BackGroudSeletor.getdrawble("ic_email", this)) {
            @Override
            public void action() {
                toast("打开邮箱");
                mFloatballManager.closeMenu();
            }
        };
        mFloatballManager.addMenuItem(personItem)
                .addMenuItem(walletItem)
                .addMenuItem(personItem)
                .addMenuItem(walletItem)
                .addMenuItem(settingItem)
                .buildMenu();
    }

    public void openFloat(AppCompatActivity activity) {
        if (activity == null) {
            return;
        }
        mFloatView = new TestFloatView(activity, R.mipmap.ic_launcher_round);
    }

    public void closeFloat() {
        if (mFloatView != null) {
            mFloatView.removeView();
            mFloatView = null;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}