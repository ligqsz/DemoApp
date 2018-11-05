package com.pax.demoapp.ui.activity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.StringDef;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pax.demoapp.R;
import com.pax.demoapp.template.base.BaseActivity;
import com.pax.utils.LogUtils;
import com.pax.utils.ScreenUtils;
import com.pax.utils.Utils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author ligq
 */
public class TestActivity extends BaseActivity {

    private ImageView ivImg;

    @Override
    public int getLayoutId() {
        return R.layout.activity_test;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        initData();
        initView();
    }


    public void initData() {
        setCurrentDay(MONDAY | SATURDAY & FRIDAY);
        setState(SUCCESS);
        @WeekDays int today = getCurrentDay();
        @TransState int state = getState();
        LogUtils.d("today:" + today);
    }


    public void initView() {
        Button btShowImg = findViewById(R.id.bt_show_img);
        ivImg = findViewById(R.id.iv_img);

        btShowImg.setOnClickListener(v -> {
            TextView view = new TextView(Utils.getApp());
            LinearLayout ll = new LinearLayout(Utils.getApp());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                    , ViewGroup.LayoutParams.WRAP_CONTENT);
            ll.setLayoutParams(params);
            ll.setOrientation(LinearLayout.VERTICAL);
            view.setLayoutParams(params);
            view.setScaleX(2);
            view.setTextColor(Color.RED);
            String result = getAdjustText(view, getString(R.string.test_string), 2);
            view.setText(result);
            ll.addView(view);
            ivImg.setImageBitmap(viewToBitmap(ll));
        });
    }

    private String getAdjustText(TextView textView, String text, int scale) {
        StringBuilder sb = new StringBuilder();
        float width = textView.getPaint().measureText(text) * scale;
        int screenWidth = ScreenUtils.getScreenWidth();
        if (width > screenWidth) {
            int breakNum = (int) (width / screenWidth) + 1;
            for (int i = 0; i < breakNum; i++) {
                if (i < breakNum - 1) {
                    sb.append(text.substring(i * text.length() / breakNum, (i + 1) * text.length() / breakNum)).append("\n");
                } else {
                    sb.append(text.substring(i * text.length() / breakNum, (i + 1) * text.length() / breakNum));
                }
            }
        } else {
            sb.append(text);
        }
        return sb.toString();
    }

    private Bitmap viewToBitmap(View view) {
        view.measure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        return view.getDrawingCache();
    }

    @WeekDays
    private int currentDay = SUNDAY;

    public static final int SUNDAY = 0;
    public static final int MONDAY = 1;
    public static final int TUESDAY = 1 << 1;
    public static final int WEDNESDAY = 3;
    public static final int THURSDAY = 1 << 2;
    public static final int FRIDAY = 5;
    public static final int SATURDAY = 6;

    @IntDef(flag = true, value = {SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY})
    @Retention(RetentionPolicy.SOURCE)
    @interface WeekDays {
    }

    public void setCurrentDay(@WeekDays int currentDay) {
        this.currentDay = currentDay;
    }

    @WeekDays
    public int getCurrentDay() {
        return currentDay;
    }


    public static final int SUCCESS = 0;
    public static final int FAILED = 1;
    public static final int UNKNOWN = 2;
    @TransState
    private int state = SUCCESS;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({SUCCESS, FAILED, UNKNOWN})
    @interface TransState {
    }

    @TransState
    public int getState() {
        return state;
    }

    public void setState(@TransState int state) {
        this.state = state;
    }


    public static final String TEST_STRING = "test string";

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({TEST_STRING})
    @interface Test {
    }
}
