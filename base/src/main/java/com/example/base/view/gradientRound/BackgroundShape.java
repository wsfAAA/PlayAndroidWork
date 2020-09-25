package com.example.base.view.gradientRound;

import android.graphics.drawable.GradientDrawable;


public class BackgroundShape extends GradientDrawable {

    public BackgroundShape(Orientation orientation, float[] radii, int leftColor, int rightColor, int borderWidth, int borderColor) {
        super(orientation,
                new int[]{leftColor,
                        rightColor});
        setCornerRadii(radii);
        setStroke(borderWidth, borderColor);
    }
}
