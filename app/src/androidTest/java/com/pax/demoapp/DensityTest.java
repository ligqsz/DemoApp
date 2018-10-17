package com.pax.demoapp;

import android.app.Activity;
import android.content.res.Resources;
import android.support.test.runner.AndroidJUnit4;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.blankj.utilcode.util.Utils;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author ligq
 * @date 2018/10/17
 */
@RunWith(AndroidJUnit4.class)
public class DensityTest {
    /**
     * dp和px的转换公式为：px = dp * density
     * dp转换的场景都是通过DisplayMetrics来进行计算的，
     * DisplayMetrics#density 就是上述的density
     * DisplayMetrics#densityDpi 就是上述的dpi
     * DisplayMetrics#scaledDensity 字体的缩放因子，正常情况下和density相等，但是调节系统字体大小后会改变这个值
     * <p>
     * {@link #getMd()}
     *
     * @see #getMd()
     * DisplayMetrics类分为三个层面，第一个是System(可以理解成初始分配)的，第二个是APP(可以理解成Application)的，
     * 第三个是Activity的。当你适配的时候，尽量不要去修改第一个System中的DisplayMetrics的，
     * 因为可能第三方的库不会按照你的方式去适配，所以这里只修改后面两个就可以了。第一个不修改是便于之后的还原.
     * </>
     * 问题:
     * 1. 适配之后Toast,dialog的问题
     * 解决方案:其实就想上面说的，使用System的density对App和Activity进行还原
     * 其实就是在show()方法之前还原，在之后在进行适配。
     * @see #showToast()
     * 2.webview加载后发现density复原
     * 解决方案:由于 WebView 初始化的时候会还原 density 的值导致适配失效，继承 WebView，重写setOverScrollMode
     * @see #setOverScrollMode(int)
     */
    @Test
    public void testMd() {
        DisplayMetrics appDm = DemoApp.getApp().getResources().getDisplayMetrics();
        DisplayMetrics sysDm = Resources.getSystem().getDisplayMetrics();
        DisplayMetrics actDm = DemoApp.getApp().getCurrentActivity().getResources().getDisplayMetrics();
        //这里以宽度为360dp为例,appDm.widthPixels为屏幕宽度px
        //1.计算屏幕density,注意竖屏用其高度appDm.heightPixels
        actDm.density = actDm.widthPixels / 360;
        //2.计算字体density
        actDm.scaledDensity = actDm.density * (sysDm.scaledDensity / sysDm.density);
        //3.计算相应的dpi
        actDm.densityDpi = (int) (160 * actDm.density);
        //4.复制相应的内容
        appDm.density = actDm.density;
        appDm.scaledDensity = actDm.scaledDensity;
        appDm.densityDpi = actDm.densityDpi;
    }

    public void getMd() {
        // 系统的屏幕尺寸
        DisplayMetrics sysDm = Resources.getSystem().getDisplayMetrics();
        // app整体的屏幕尺寸
        DisplayMetrics appDm = DemoApp.getApp().getResources().getDisplayMetrics();
        // app整体的屏幕尺寸
        DisplayMetrics actDm = DemoApp.getApp().getCurrentActivity().getResources().getDisplayMetrics();
    }

    public static void cancelAdaptScreen(final Activity activity) {
        final DisplayMetrics systemDm = Resources.getSystem().getDisplayMetrics();
        final DisplayMetrics appDm = Utils.getApp().getResources().getDisplayMetrics();
        final DisplayMetrics activityDm = activity.getResources().getDisplayMetrics();
        activityDm.density = systemDm.density;
        activityDm.scaledDensity = systemDm.scaledDensity;
        activityDm.densityDpi = systemDm.densityDpi;

        appDm.density = systemDm.density;
        appDm.scaledDensity = systemDm.scaledDensity;
        appDm.densityDpi = systemDm.densityDpi;
    }

    public void restoreAdaptScreen(Activity activity, boolean isVerticalSlide, int sizeInPx) {
        final DisplayMetrics systemDm = Resources.getSystem().getDisplayMetrics();
        final DisplayMetrics appDm = Utils.getApp().getResources().getDisplayMetrics();
        final DisplayMetrics activityDm = activity.getResources().getDisplayMetrics();
        if (isVerticalSlide) {
            activityDm.density = activityDm.widthPixels / (float) sizeInPx;
        } else {
            activityDm.density = activityDm.heightPixels / (float) sizeInPx;
        }
        activityDm.scaledDensity = activityDm.density * (systemDm.scaledDensity / systemDm.density);
        activityDm.densityDpi = (int) (160 * activityDm.density);

        appDm.density = activityDm.density;
        appDm.scaledDensity = activityDm.scaledDensity;
        appDm.densityDpi = activityDm.densityDpi;
    }

    public void showToast() {
        //取消适配
        cancelAdaptScreen(DemoApp.getApp().getCurrentActivity());
        //弹出Toast
        Toast.makeText(DemoApp.getApp().getCurrentActivity(), "点击了第一个内容", Toast.LENGTH_SHORT).show();
        //重新适配
        restoreAdaptScreen(DemoApp.getApp().getCurrentActivity(), true, 720);
    }

    public void setOverScrollMode(int mode) {
//        super.setOverScrollMode(mode);
        restoreAdaptScreen(DemoApp.getApp().getCurrentActivity(), true, 720);
    }
}
