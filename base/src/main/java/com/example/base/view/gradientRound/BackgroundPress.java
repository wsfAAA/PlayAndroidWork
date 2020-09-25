package com.example.base.view.gradientRound;

import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;

public class BackgroundPress extends StateListDrawable {

    public BackgroundPress(GradientDrawable.Orientation bgOrientation, int borderWidth, int borderColor, float[] radius, int[] ints) {
        BackgroundShape normalDrawable = new BackgroundShape(
                bgOrientation,
                radius,
                ints[0],
                ints[1],
                borderWidth,
                borderColor
        );

        addState(new int[]{}, normalDrawable);
    }
}
