package com.pax.demoapp.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

import com.pax.demoapp.R;
import com.pax.demoapp.template.base.BaseActivity;
import com.pax.demoapp.ui.anim.MyEvaluator;
import com.pax.demoapp.ui.anim.PropertyBean;
import com.pax.demoapp.ui.anim.SpeedUpInterpolator;

import java.util.ArrayList;

/**
 * @author ligq
 */
public class PropertyActivity extends BaseActivity {

    private TextView viewPuppet;

    @Override
    public int getLayoutId() {
        return R.layout.activity_property;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        initView();
    }

    @SuppressWarnings("ConstantConditions")
    public void initView() {
        viewPuppet = findViewById(R.id.view_puppet);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private Animator getAnimatorByXml() {
        int width = viewPuppet.getLayoutParams().width;
        int height = viewPuppet.getLayoutParams().height;
        AnimatorSet animatorSet = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.property_anim);
        ArrayList<Animator> childAnimations = animatorSet.getChildAnimations();
        ValueAnimator valueAnimator = (ValueAnimator) childAnimations.get(childAnimations.size() - 1);
        valueAnimator.addUpdateListener(animation -> {
            float animatedValue = (float) animation.getAnimatedValue();
            viewPuppet.getLayoutParams().height = (int) (height * animatedValue);
            viewPuppet.getLayoutParams().width = (int) (width * animatedValue);
            viewPuppet.requestLayout();
        });
        animatorSet.setTarget(viewPuppet);
        return animatorSet;
    }

    private AnimatorSet getAnimationSet() {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(getObjectAnimatorByPropertyValuesHolder(), getValueAnimator());
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        return animatorSet;
    }

    private Animator getValueAnimator() {
        int width = viewPuppet.getWidth();
        int height = viewPuppet.getHeight();
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(1.0f, 3.0f);
        valueAnimator.setDuration(3000);
        valueAnimator.setTarget(viewPuppet);
        valueAnimator.setRepeatCount(1);
        valueAnimator.setRepeatMode(ValueAnimator.REVERSE);
        valueAnimator.addUpdateListener(animation -> {
            float animatedValue = (float) animation.getAnimatedValue();
            viewPuppet.getLayoutParams().width = (int) (width * animatedValue);
            viewPuppet.getLayoutParams().height = (int) (height * animatedValue);
            viewPuppet.requestLayout();
        });
        return valueAnimator;
    }

    private Animator getObjectAnimatorByPropertyValuesHolder() {
        PropertyValuesHolder bgColorAnimator = PropertyValuesHolder.ofObject("backgroundColor"
                , new ArgbEvaluator(), 0xff009688, 0xff795548);
        PropertyValuesHolder rotationXAnimator = PropertyValuesHolder.ofFloat("rotationX"
                , 0f, 360f);
        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(viewPuppet, bgColorAnimator, rotationXAnimator);
        objectAnimator.setDuration(3000);
        objectAnimator.setRepeatCount(1);
        objectAnimator.setRepeatMode(ValueAnimator.REVERSE);
        return objectAnimator;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_property, menu);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_do_by_xml:
                doAnimation(getAnimatorByXml());
                break;
            case R.id.action_by_code:
                doAnimation(getAnimationSet());
                break;
            case R.id.action_by_layout_animator:
                doLayoutAnimator();
                break;
            case R.id.action_by_view_property_animator:
                doAnimation(getAnimationByViewPropertyAnimator());
                break;
            case R.id.action_by_custom:
                doAnimation(getAnimationByCustom());
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private Animator getAnimationByCustom() {
        int width = viewPuppet.getWidth();
        int height = viewPuppet.getHeight();
        PropertyBean startBean = new PropertyBean(0xff009688, 1f, 0f);
        PropertyBean endBean = new PropertyBean(0xff795535, 1.2f, 360f);
        ValueAnimator animator = new ValueAnimator();
        animator.setInterpolator(new SpeedUpInterpolator());
        animator.setObjectValues(startBean, endBean);
        animator.setEvaluator(new MyEvaluator());
        animator.setRepeatCount(1);
        animator.setRepeatMode(ValueAnimator.REVERSE);
        animator.setDuration(3000);
        animator.addUpdateListener(animation -> {
            PropertyBean propertyBean = (PropertyBean) animation.getAnimatedValue();
            ViewGroup.LayoutParams layoutParams = viewPuppet.getLayoutParams();
            layoutParams.width = (int) (width * propertyBean.getSize()) + width;
            layoutParams.height = (int) (height * propertyBean.getSize()) + height;
            if (propertyBean.getBackgroundColor() != 0 || propertyBean.getBackgroundColor() != 1) {
                viewPuppet.setBackgroundColor(propertyBean.getBackgroundColor());
            }
            viewPuppet.setRotationX(propertyBean.getRotationX());
            viewPuppet.requestLayout();
            Log.i("addUpdateListener", "height:" + layoutParams.height + "\n" +
                    "width:" + layoutParams.width + "\n" +
                    "propertyBean:" + propertyBean.toString());
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Log.i("onAnimationEnd", "onAnimationEnd: width="
                        + viewPuppet.getLayoutParams().width);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        return animator;
    }

    private ViewPropertyAnimator getAnimationByViewPropertyAnimator() {
        return viewPuppet.animate()
                .rotationX(360f)
                .alpha(0.5f)
                .scaleX(3).scaleY(3)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .setDuration(3000)
                .setStartDelay(0);
    }

    private void doAnimation(Object animator) {
        if (animator instanceof Animator) {
            ((Animator) animator).start();
        } else if (animator instanceof ViewPropertyAnimator) {
            ((ViewPropertyAnimator) animator).start();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void doLayoutAnimator() {
        LayoutTransition layoutTransition = new LayoutTransition();
        layoutTransition.setAnimator(LayoutTransition.APPEARING, getBgColorAnimator());
        layoutTransition.setAnimator(LayoutTransition.DISAPPEARING, getRotationXAnimator());
        layoutTransition.setDuration(2000);

        ViewGroup contentView = (ViewGroup) ((ViewGroup) getWindow().getDecorView()
                .findViewById(android.R.id.content)).getChildAt(0);
        contentView.setLayoutTransition(layoutTransition);
        if (contentView.findViewById(R.id.view_puppet) == null) {
            contentView.addView(viewPuppet);
        } else {
            contentView.removeView(viewPuppet);
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private ObjectAnimator getBgColorAnimator() {
        ObjectAnimator bgColorAnimator = ObjectAnimator
                .ofArgb(viewPuppet, "backgroundColor", 0xff1234ab, 0xaaddaaef);
        bgColorAnimator.setRepeatMode(ValueAnimator.REVERSE);
        bgColorAnimator.setRepeatCount(1);
        bgColorAnimator.setDuration(2000);
        bgColorAnimator.setStartDelay(0);
        return bgColorAnimator;
    }

    private ObjectAnimator getRotationXAnimator() {
        ObjectAnimator rotationXAnimator = ObjectAnimator.ofFloat(viewPuppet, "rotationX"
                , 0f, 360f);
        rotationXAnimator.setRepeatCount(1);
        rotationXAnimator.setRepeatMode(ValueAnimator.REVERSE);
        rotationXAnimator.setDuration(2000);
        rotationXAnimator.setStartDelay(0);
        return rotationXAnimator;
    }
}
