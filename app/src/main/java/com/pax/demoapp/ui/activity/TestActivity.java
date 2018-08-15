package com.pax.demoapp.ui.activity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pax.demoapp.DemoApp;
import com.pax.demoapp.R;
import com.pax.demoapp.utils.ScreenUtils;

/**
 * @author ligq
 */
public class TestActivity extends AppCompatActivity implements IActivity {

    private Button btShowImg;
    private ImageView ivImg;

    @Override
    public int getLayoutId() {
        return R.layout.activity_test;
    }

    @Override
    public void initData() {

    }

    @Override
    public void initView() {
        btShowImg = findViewById(R.id.bt_show_img);
        ivImg = findViewById(R.id.iv_img);

        btShowImg.setOnClickListener(v -> {
            TextView view = new TextView(DemoApp.getApp());
            LinearLayout ll = new LinearLayout(DemoApp.getApp());
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
}
