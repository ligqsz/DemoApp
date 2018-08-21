package com.pax.demoapp.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.pax.demoapp.R;

import java.util.ArrayList;

/**
 * @author ligq
 */
public class PropertyActivity extends AppCompatActivity implements IActivity {

    private TextView viewPuppet;

    @Override
    public int getLayoutId() {
        return R.layout.activity_property;
    }

    @Override
    public void initData() {

    }

    @SuppressWarnings("ConstantConditions")
    @Override
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_property, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_do_by_xml:
                doAnimation(getAnimatorByXml());
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void doAnimation(Animator animator) {
        animator.start();
    }
}
