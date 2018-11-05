package com.pax.demoapp.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.pax.utils.LogUtils;

/**
 * @author ligq
 * @date 2018/8/27
 */

public class PercentView extends View {

    private Paint mPaint;
    private RectF mRectF;
    private int radius;
    private int startColor;
    private boolean isStart;
    private int percent;
    private int prepareColor;
    private Paint textPaint;
    private Paint mPaintTick;
    private Rect textBounds;
    private int centerY;
    private int centerX;
    private float strokeWidth;

    private Path mTickPath;
    private PathMeasure mPathMeasure;
    private Path mTickPathMeasureDst;
    private float tickProgress;
    private ValueAnimator mTickAnimator;
    private boolean isAnimationStart;
    private int paintStrokeWidth;
    private int textColor;
    private float textSize;

    public PercentView(Context context) {
        this(context, null);
    }

    public PercentView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PercentView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        radius = 60;
        paintStrokeWidth = 6;
        prepareColor = Color.GRAY;
        startColor = Color.GREEN;
        textColor = Color.RED;
        textSize = 30;
        strokeWidth = radius / 10.0f;

        initPaint();

        mRectF = new RectF();
        textBounds = new Rect();
        isStart = false;

        mTickPath = new Path();
        mTickPathMeasureDst = new Path();
        mPathMeasure = new PathMeasure();

        initAnimator();
    }

    private void initPaint() {
        if (mPaint == null) {
            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        }
        mPaint.setStrokeWidth(paintStrokeWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setColor(prepareColor);

        if (textPaint == null) {
            textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        }
        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);

        if (mPaintTick == null) {
            mPaintTick = new Paint(Paint.ANTI_ALIAS_FLAG);
        }
        mPaintTick.setColor(startColor);
        mPaintTick.setStyle(Paint.Style.STROKE);
        mPaintTick.setStrokeCap(Paint.Cap.ROUND);
        mPaintTick.setStrokeWidth(strokeWidth);
    }

    private void initAnimator() {
        mTickAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);
        mTickAnimator.setDuration(600);
        mTickAnimator.setInterpolator(new DecelerateInterpolator());
        mTickAnimator.addUpdateListener(animation -> {
            float animatedValue = (float) animation.getAnimatedValue();
            LogUtils.d("animator:" + animatedValue);
            setTickProgress(animatedValue);
        });
        mTickAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                LogUtils.d("onAnimationStart");
                setTickProgress(0);
                mPathMeasure.nextContour();
                mPathMeasure.setPath(mTickPath, false);
                mTickPathMeasureDst.reset();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isStart = false;
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int size = (int) ((radius + strokeWidth) * 2);
        setMeasuredDimension(size, size);
        centerX = size / 2;
        centerY = size / 2;
        mRectF.set(centerX - radius * 1.0f, centerY - radius * 1.0f
                , centerX + radius * 1.0f, centerY + radius * 1.0f);

        //设置打钩的几个点坐标
        float dx = (radius - strokeWidth) / 3.0f;
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

    @Override
    protected void onDraw(Canvas canvas) {
        LogUtils.i("onDraw: .....percent=" + percent);
        LogUtils.i("onDraw: .....tickProgress=" + tickProgress);
        if (percent == 100) {
            LogUtils.d("onDraw:Finish");
            canvas.drawArc(mRectF, -90, 360, false, mPaint);
            canvas.drawText("", centerX - textBounds.width() / 2.0f, centerY + textBounds.height() / 2.0f, textPaint);
            drawTick(canvas);
            return;
        }

        mPaint.setColor(prepareColor);
        canvas.drawArc(mRectF, -90, 360, false, mPaint);
        mPaint.setColor(startColor);
        canvas.drawArc(mRectF, -90, percent * 360f / 100, false, mPaint);

        String percentStr = percent + " %";
        textPaint.getTextBounds(percentStr, 0, percentStr.length(), textBounds);
        canvas.drawText(percentStr, centerX - textBounds.width() / 2.0f, centerY + textBounds.height() / 2.0f, textPaint);

    }

    private void drawTick(Canvas canvas) {
        mPaintTick.setAlpha((int) (255 * tickProgress));
        mPathMeasure.getSegment(0, tickProgress * mPathMeasure.getLength(), mTickPathMeasureDst, true);
        canvas.drawPath(mTickPathMeasureDst, mPaintTick);
        if (!isAnimationStart) {
            mTickAnimator.start();
            isAnimationStart = true;
        }
    }

    public void updateProgress(@IntRange(from = 0, to = 100) int progress) {
        if (!isStart) {
            isStart = true;
        }
        this.percent = progress;
        postInvalidate();
    }

    public void setTickProgress(float tickProgress) {
        this.tickProgress = tickProgress;
        LogUtils.i("tickProgress: " + tickProgress);
        postInvalidate();
    }

    public void reset() {
        isStart = false;
        isAnimationStart = false;
        tickProgress = 0.f;
        mTickAnimator.cancel();
    }

    public boolean isStart() {
        return isStart;
    }
}
