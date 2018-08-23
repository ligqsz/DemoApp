package com.pax.demoapp.ui.anim;

import android.util.Log;
import android.view.animation.Interpolator;

/**
 * @author ligq
 * @date 2018/8/23
 */

public class SpeedUpInterpolator implements Interpolator {
    private final float mFactor;
    private final double mDoubleFactor;

    public SpeedUpInterpolator() {
        mFactor = 1.0f;
        mDoubleFactor = 2.0;
    }

    @Override
    public float getInterpolation(float v) {
        Log.i("getInterpolation", "getInterpolation: v=" + v);
        if (mFactor == 1.0f) {
            return v * v;
        } else {
            return (float) Math.pow(v, mDoubleFactor);
        }
    }
}
