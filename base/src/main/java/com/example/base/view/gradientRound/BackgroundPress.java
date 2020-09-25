package com.example.base.view.gradientRound;

import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;

public class BackgroundPress extends StateListDrawable {

    public BackgroundPress(GradientDrawable.Orientation bgOrientation, GradientDrawable.Orientation pressOrientation, int borderWidth, int borderColor, float[] radius, int[] ints, int[] intsPress) {
        BackgroundShape normalDrawable = new BackgroundShape(
                bgOrientation,
                radius,
                ints[0],
                ints[1],
                borderWidth,
                borderColor
        );

        BackgroundShape pressedDrawable = new BackgroundShape(
                pressOrientation,
                radius,
                intsPress[0],
                intsPress[1],
                borderWidth,
                borderColor
        );

        addState(new int[]{android.R.attr.state_pressed}, pressedDrawable);
        addState(new int[]{}, normalDrawable);
    }
}
