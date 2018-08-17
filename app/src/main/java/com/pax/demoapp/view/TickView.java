package com.pax.demoapp.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.pax.demoapp.utils.DensityUtils;

/**
 * @author ligq
 * @date 2018/8/17
 */

public class TickView extends View {
    /**
     * 大圆画笔(背景就是圆环的颜色)
     */
    private Paint mRingPaint;
    /**
     * 大圆半径
     */
    private int ringRadius;
    /**
     * 空心部分小圆的画笔,与大圆组成圆环
     */
    private Paint mBlankPaint;
    /**
     * 小圆半径
     */
    private int blankRadius;
    /**
     * 圆环宽度
     */
    private int ringWidth;

    private Paint mTickPaint;

    private int centerX;
    private int centerY;
    private Path mTickPath;

    public TickView(Context context) {
        this(context, null);
    }

    public TickView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TickView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mRingPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRingPaint.setColor(Color.RED);

        mBlankPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBlankPaint.setColor(Color.WHITE);

        mTickPath = new Path();
        mTickPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTickPaint.setColor(Color.GREEN);
        mTickPaint.setStyle(Paint.Style.STROKE);
        mTickPaint.setStrokeCap(Paint.Cap.ROUND);
        mTickPaint.setStrokeWidth(DensityUtils.dp2px(2.5f));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        ringWidth = 8;
        ringRadius = 80;
        blankRadius = ringRadius - ringWidth;

        int width = getMySize(ringRadius * 2, widthMeasureSpec);
        int height = getMySize(ringRadius * 2, heightMeasureSpec);
        height = width = Math.max(width, height);
        setMeasuredDimension(width, height);
        centerX = getMeasuredWidth() / 2;
        centerY = getMeasuredHeight() / 2;

        //设置打钩的几个点坐标
        float dx = blankRadius / 3.0f;
        final float startX = centerX - dx * 4 / 3;
        final float startY = (float) centerY;

        final float middleX = centerX - dx / 3;
        final float middleY = centerY + dx;

        final float endX = centerX + dx * 5 / 3;
        final float endY = centerY - dx;

        mTickPath.reset();
        mTickPath.moveTo(startX, startY);
        mTickPath.lineTo(middleX, middleY);
        mTickPath.lineTo(endX, endY);
    }

    private int getMySize(int defaultSize, int measureSpec) {
        int mySize = defaultSize;

        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);

        switch (mode) {
            case MeasureSpec.UNSPECIFIED:
            case MeasureSpec.AT_MOST:
                break;
            case MeasureSpec.EXACTLY:
                mySize = size;
                break;
            default:
                break;
        }
        return mySize;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(centerX, centerY, ringRadius, mRingPaint);
        canvas.drawCircle(centerX, centerY, blankRadius, mBlankPaint);
        canvas.drawPath(mTickPath, mTickPaint);
    }
}
