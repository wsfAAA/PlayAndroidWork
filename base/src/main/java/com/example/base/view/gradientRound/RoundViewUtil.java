package com.example.base.view.gradientRound;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;

import com.example.base.R;

public class RoundViewUtil {

    public static Drawable init(Context context, AttributeSet attributeSet) {
        TypedArray ta = context.obtainStyledAttributes(attributeSet, R.styleable.GradientRound);
        // 背景色
        int bgColor = ta.getColor(R.styleable.GradientRound_bgColor, Color.TRANSPARENT);
        int bgGradientAngle = ta.getInteger(R.styleable.GradientRound_bgGradientAngle,-1);
        int bgColorStart = ta.getColor(R.styleable.GradientRound_bgColorStart, bgColor);
        int bgColorEnd = ta.getColor(R.styleable.GradientRound_bgColorEnd, bgColor);

        // 边框
        int borderColor = ta.getColor(R.styleable.GradientRound_borderColor, Color.TRANSPARENT);
        int borderWidth = ta.getDimensionPixelOffset(R.styleable.GradientRound_borderWidth, 0);

        // 圆角
        int cornerRadius = ta.getDimensionPixelOffset(R.styleable.GradientRound_cornerRadius, 1);
        int leftTopRadius = ta.getDimensionPixelOffset(R.styleable.GradientRound_leftTopRadius, cornerRadius);
        int rightTopRadius = ta.getDimensionPixelOffset(R.styleable.GradientRound_rightTopRadius, cornerRadius);
        int rightBottomRadius = ta.getDimensionPixelOffset(R.styleable.GradientRound_rightBottomRadius, cornerRadius);
        int leftBottomRadius = ta.getDimensionPixelOffset(R.styleable.GradientRound_leftBottomRadius, cornerRadius);
        ta.recycle();

        int[] bgColors;
        int[] pressColors;
        // 圆角 ordered top-left, top-right, bottom-right, bottom-left
        float[] radius = new float[]{leftTopRadius, leftTopRadius, rightTopRadius, rightTopRadius, rightBottomRadius, rightBottomRadius, leftBottomRadius, leftBottomRadius};
        // 背景渐变色
        if (bgGradientAngle>0) {
            bgColors = new int[]{bgColorStart, bgColorEnd};
        } else {
            bgColors = new int[]{bgColor, bgColor};
        }

        // 渐变角度
        GradientDrawable.Orientation bgOrientation = getOrientation(bgGradientAngle);
        return new BackgroundPress(bgOrientation, bgOrientation, borderWidth, borderColor, radius, bgColors, bgColors);
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
