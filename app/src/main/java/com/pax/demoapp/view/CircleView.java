package com.pax.demoapp.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.pax.demoapp.R;
import com.pax.demoapp.utils.DensityUtils;


/**
 * @author ligq
 * @date 2018/8/7
 */

public class CircleView extends View {

    private int circleColor;
    private int textColor;
    private float textSize;
    private String textStr;
    private Paint mPaint;
    private Rect bounds;
    private int mHeight;
    private int mWidth;
    private int mRadius;
    private int mPieCenterX;
    private int mPieCenterY;

    public CircleView(Context context) {
        this(context, null);
    }

    public CircleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CircleView);
            circleColor = ta.getColor(R.styleable.CircleView_circle_color, ContextCompat.getColor(context, R.color.red));
            textColor = ta.getColor(R.styleable.CircleView_text_color, ContextCompat.getColor(context, R.color.white));
            textSize = ta.getDimension(R.styleable.CircleView_text_size, DensityUtils.dp2px(16));
            textStr = ta.getString(R.styleable.CircleView_text_str);
            ta.recycle();
        }
        init(context);
    }

    private void init(Context context) {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        bounds = new Rect();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mHeight = getHeight();
        mWidth = getWidth();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mRadius = Math.min(mHeight, mWidth) / 2;
        mPieCenterX = mWidth / 2;
        mPieCenterY = mHeight / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(circleColor);
        canvas.drawCircle(mPieCenterX, mPieCenterY, mRadius, mPaint);
        drawText(canvas);
    }

    private void drawText(Canvas canvas) {
        if (!TextUtils.isEmpty(textStr)) {
            mPaint.getTextBounds(textStr, 0, textStr.length(), bounds);
            mPaint.setColor(textColor);
            mPaint.setTextAlign(Paint.Align.CENTER);
            mPaint.setTextSize(textSize);
            int baseline = mPieCenterY + bounds.height() / 2;
            canvas.drawText(textStr, mPieCenterX, baseline, mPaint);
        }
    }

    public String getTextStr() {
        return textStr;
    }

    public void setTextStr(String textStr) {
        this.textStr = textStr;
        invalidate();
    }
}
