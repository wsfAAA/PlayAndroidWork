package com.example.base;

import android.app.Application;

import com.blankj.utilcode.util.Utils;

public class MyAppliction extends Application {

    private static MyAppliction myAppliction;

    @Override
    public void onCreate() {
        super.onCreate();
        myAppliction = this;

        Utils.init(this);
    }

    public static MyAppliction getApplication() {
        return myAppliction;
    }
}
