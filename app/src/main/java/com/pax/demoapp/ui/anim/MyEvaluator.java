package com.pax.demoapp.ui.anim;

import android.animation.ArgbEvaluator;
import android.animation.TypeEvaluator;
import android.util.Log;

/**
 * @author ligq
 * @date 2018/8/23
 */

public class MyEvaluator implements TypeEvaluator<PropertyBean> {
    private ArgbEvaluator mArgbEvaluator = new ArgbEvaluator();

    @Override
    public PropertyBean evaluate(float fraction, PropertyBean startValue, PropertyBean endValue) {
        int currentColor = (int) mArgbEvaluator.evaluate(fraction, startValue.getBackgroundColor()
                , endValue.getBackgroundColor());
        float currentSize = (endValue.getSize() - startValue.getSize()) * fraction;
        float currentRotationX = fraction * (endValue.getRotationX() - startValue.getRotationX());
        Log.i("MyEvaluator", "currentColor:" + currentColor + "\n" +
                "currentSize:" + currentSize + "\n" +
                "currentRotationX:" + currentRotationX);
        return new PropertyBean(currentColor, currentSize, currentRotationX);
    }
}
