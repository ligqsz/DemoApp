package com.pax.demoapp.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.ViewAnimationUtils;

import com.pax.demoapp.R;
import com.pax.demoapp.template.base.BaseActivity;
import com.pax.utils.LogUtils;

/**
 * 揭露动画
 *
 * @author ligq
 */
public class ExposeActivity extends BaseActivity {

    private View puppet;
    private FloatingActionButton fab;
    private boolean flag;

    @Override
    public int getLayoutId() {
        return R.layout.activity_expose;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        flag = true;
        initView();
    }

    public void initView() {
        fab = findViewById(R.id.fab);
        puppet = findViewById(R.id.view_puppet);

        fab.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                launchRevealAnimation();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void launchRevealAnimation() {
        int[] vLocation = new int[2];
        fab.getLocationInWindow(vLocation);
        int centerX = vLocation[0] + fab.getWidth() / 2;
        int centerY = vLocation[1] + fab.getHeight() / 2;
        //求对角线长度,来作为扩散圆的最大半径
        int hypotenuse = (int) Math.hypot(puppet.getWidth(), puppet.getHeight());

        if (flag) {
            LogUtils.d("隐藏揭露动画");
            //隐藏揭露动画
            Animator circularReveal = ViewAnimationUtils.createCircularReveal(puppet, centerX, centerY, hypotenuse, 0);
            circularReveal.setDuration(500);
            circularReveal.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    fab.setClickable(false);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    puppet.setVisibility(View.GONE);
                    fab.setClickable(true);
                }
            });
            circularReveal.start();
            flag = false;
        } else {
            LogUtils.d("显示揭露动画");
            //显示揭露动画
            Animator circularReveal = ViewAnimationUtils.createCircularReveal(puppet, centerX, centerY, 0, hypotenuse);
            circularReveal.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    fab.setClickable(false);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    fab.setClickable(true);
                }
            });
            circularReveal.setDuration(500);
            puppet.setVisibility(View.VISIBLE);
            circularReveal.start();
            flag = true;
        }

    }
}
