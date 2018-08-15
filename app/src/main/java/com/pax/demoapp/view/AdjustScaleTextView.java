package com.pax.demoapp.view;


import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.Gravity;

import com.pax.demoapp.utils.LogUtils;
import com.pax.demoapp.utils.ScreenUtils;

/**
 * @author ligq
 * @date 2018/8/8
 */

public class AdjustScaleTextView extends AppCompatTextView {

    private float scaleX = 1;
    private float scaleY = 1;
    private StringBuilder sb;

    public AdjustScaleTextView(Context context) {
        this(context, null);
    }

    public AdjustScaleTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AdjustScaleTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        sb = new StringBuilder();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension((int) (scaleX > 1 ? scaleX * widthSize : widthSize), (int) (scaleY * heightSize));
    }

    @Override
    public void setScaleX(float scaleX) {
        this.scaleX = scaleX;
        super.setScaleX(scaleX);
    }

    @Override
    public void setScaleY(float scaleY) {
        this.scaleY = scaleY;
        super.setScaleY(scaleY);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        String text = getText().toString();
        float textWidth = getPaint().measureText(text);
        LogUtils.d("onDraw-----------text width:" + textWidth + "\n" +
                "getMeasuredWidth:" + getMeasuredWidth() + "\n" +
                "screen width" + ScreenUtils.getScreenWidth());
        textWidth = textWidth * scaleX;
        if (textWidth > ScreenUtils.getScreenWidth() && scaleX > 1) {
            int breakNum = (int) (textWidth / ScreenUtils.getScreenWidth()) + 1;
            for (int i = 0; i < breakNum; i++) {
                if (i < breakNum - 1) {
                    sb.append(text.substring(i * text.length() / breakNum, (i + 1) * text.length() / breakNum)).append("\n");
                } else {
                    sb.append(text.substring(i * text.length() / breakNum, (i + 1) * text.length() / breakNum));
                }
            }
            setGravity(Gravity.CENTER_HORIZONTAL);
            setText(sb.toString());
            scaleX = 1;
        } else {
            super.onDraw(canvas);
        }

    }
}
