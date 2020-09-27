package com.example.base.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;

import androidx.appcompat.widget.LinearLayoutCompat;

import com.blankj.utilcode.util.ConvertUtils;
import com.example.base.R;
import com.example.base.view.gradientRound.BackgroundPress;

public class GradientLinearlayout extends LinearLayoutCompat {
    private int bgColor;
    private int borderWidth;
    private int borderColor;
    private GradientDrawable.Orientation bgOrientation;

    private float[] radius;
    private int[] bgColors;

    private int cornerRadius;
    private int leftTopRadius;
    private int rightTopRadius;
    private int rightBottomRadius;
    private int leftBottomRadius;

    private int bgGradientAngle;
    private int bgColorStart;
    private int bgColorEnd;

    public GradientLinearlayout(Context context) {
        this(context, null, 0);
    }

    public GradientLinearlayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GradientLinearlayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.GradientRound);
        // 背景色
        bgColor = ta.getColor(R.styleable.GradientRound_bgColor, Color.TRANSPARENT);

        //背景渐变色
        bgGradientAngle = ta.getInteger(R.styleable.GradientRound_bgGradientAngle, -1);
        bgColorStart = ta.getColor(R.styleable.GradientRound_bgColorStart, bgColor);
        bgColorEnd = ta.getColor(R.styleable.GradientRound_bgColorEnd, bgColor);

        // 边框
        borderColor = ta.getColor(R.styleable.GradientRound_borderColor, Color.TRANSPARENT);
        borderWidth = ta.getDimensionPixelOffset(R.styleable.GradientRound_borderWidth, 0);

        // 圆角
        cornerRadius = ta.getDimensionPixelOffset(R.styleable.GradientRound_cornerRadius, 0);
        leftTopRadius = ta.getDimensionPixelOffset(R.styleable.GradientRound_leftTopRadius, cornerRadius);
        rightTopRadius = ta.getDimensionPixelOffset(R.styleable.GradientRound_rightTopRadius, cornerRadius);
        rightBottomRadius = ta.getDimensionPixelOffset(R.styleable.GradientRound_rightBottomRadius, cornerRadius);
        leftBottomRadius = ta.getDimensionPixelOffset(R.styleable.GradientRound_leftBottomRadius, cornerRadius);
        ta.recycle();

        // 圆角 ordered top-left, top-right, bottom-right, bottom-left
        radius = new float[]{leftTopRadius, leftTopRadius, rightTopRadius, rightTopRadius, rightBottomRadius, rightBottomRadius, leftBottomRadius, leftBottomRadius};

        // 背景渐变色
        if (bgGradientAngle > 0) {
            bgColors = new int[]{bgColorStart, bgColorEnd};
        } else {
            bgColors = new int[]{bgColor, bgColor};
        }

        bgOrientation = getOrientation(bgGradientAngle);// 渐变角度
        this.setBackground(new BackgroundPress(bgOrientation, borderWidth, borderColor, radius, bgColors));
    }


    /**
     * 设置边框
     *
     * @param borderColor 边框颜色
     * @param borderWidth 边框宽度
     */
    public void setBorder(int borderColor, int borderWidth) {
        this.borderColor = borderColor;
        this.borderWidth = ConvertUtils.dp2px(borderWidth);
        this.setBackground(new BackgroundPress(bgOrientation, borderWidth, borderColor, radius, bgColors));
    }

    /**
     * 设置背景渐变
     *
     * @param bgGradientAngle 渐变角度
     * @param bgColorStart    渐变开始颜色
     * @param bgColorEnd      渐变结束颜色
     */
    public void setBgGradient(int bgGradientAngle, int bgColorStart, int bgColorEnd) {
        this.bgGradientAngle = bgGradientAngle;
        this.bgColorStart = bgColorStart;
        this.bgColorEnd = bgColorEnd;
        // 背景渐变色
        if (bgGradientAngle > 0) {
            bgColors = new int[]{bgColorStart, bgColorEnd};
        } else {
            bgColors = new int[]{bgColor, bgColor};
        }
        bgOrientation = getOrientation(bgGradientAngle);  // 渐变角度
        this.setBackground(new BackgroundPress(bgOrientation, borderWidth, borderColor, radius, bgColors));
    }

    /**
     * 设置背景
     *
     * @param bgcolor
     */
    public void setBgColor(int bgcolor) {
        this.bgColor = bgcolor;
        bgColors = new int[]{bgColor, bgColor};
        this.setBackground(new BackgroundPress(bgOrientation, borderWidth, borderColor, radius, bgColors));
    }

    /**
     * 设置圆角
     *
     * @param cornerRadius 圆角大小
     */
    public void setRadius(int cornerRadius) {
        this.cornerRadius = ConvertUtils.dp2px(cornerRadius);
        this.leftTopRadius = ConvertUtils.dp2px(cornerRadius);
        this.rightTopRadius = ConvertUtils.dp2px(cornerRadius);
        this.rightBottomRadius = ConvertUtils.dp2px(cornerRadius);
        this.leftBottomRadius = ConvertUtils.dp2px(cornerRadius);
        radius = new float[]{leftTopRadius, leftTopRadius, rightTopRadius, rightTopRadius, rightBottomRadius, rightBottomRadius, leftBottomRadius, leftBottomRadius};
        this.setBackground(new BackgroundPress(bgOrientation, borderWidth, borderColor, radius, bgColors));
    }

    /**
     * 设置圆角
     *
     * @param leftTopRadius
     * @param rightTopRadius
     * @param rightBottomRadius
     * @param leftBottomRadius
     */
    public void setRadius(int leftTopRadius, int rightTopRadius, int rightBottomRadius, int leftBottomRadius) {
        this.leftTopRadius = ConvertUtils.dp2px(leftTopRadius);
        this.rightTopRadius = ConvertUtils.dp2px(rightTopRadius);
        this.rightBottomRadius = ConvertUtils.dp2px(rightBottomRadius);
        this.leftBottomRadius = ConvertUtils.dp2px(leftBottomRadius);
        radius = new float[]{leftTopRadius, leftTopRadius, rightTopRadius, rightTopRadius, rightBottomRadius, rightBottomRadius, leftBottomRadius, leftBottomRadius};
        this.setBackground(new BackgroundPress(bgOrientation, borderWidth, borderColor, radius, bgColors));
    }

    /**
     * 渐变方向
     *
     * @return
     */
    private static GradientDrawable.Orientation getOrientation(int mAngle) {
        GradientDrawable.Orientation mOrientation = GradientDrawable.Orientation.RIGHT_LEFT;
        switch (mAngle) {
            case 0:
                mOrientation = GradientDrawable.Orientation.LEFT_RIGHT;
                break;
            case 45:
                mOrientation = GradientDrawable.Orientation.BL_TR;
                break;
            case 90:
                mOrientation = GradientDrawable.Orientation.BOTTOM_TOP;
                break;
            case 135:
                mOrientation = GradientDrawable.Orientation.BR_TL;
                break;
            case 180:
                mOrientation = GradientDrawable.Orientation.RIGHT_LEFT;
                break;
            case 225:
                mOrientation = GradientDrawable.Orientation.TR_BL;
                break;
            case 270:
                mOrientation = GradientDrawable.Orientation.TOP_BOTTOM;
                break;
            case 315:
                mOrientation = GradientDrawable.Orientation.TL_BR;
                break;
        }
        return mOrientation;
    }

}
