package com.pax.demoapp.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.support.annotation.Keep;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.pax.demoapp.R;
import com.pax.utils.LogUtils;

/**
 * @author ligq
 * @date 2018/8/17
 */

@SuppressWarnings("unused")
public class TickView extends View {
    /**
     * 大圆画笔(背景就是圆环的颜色)
     */
    private Paint mPaintRing;
    /**
     * 大圆半径
     */
    private int ringRadius;
    /**
     * 內圆画笔
     */
    private Paint mPaintCircle;
    /**
     * 圆环宽度
     */
    private int ringWidth;

    /**
     * 打钩的画笔
     */
    private Paint mPaintTick;

    /**
     * 控件中心的X,Y坐标
     */
    private int centerX;
    private int centerY;

    /**
     * 圆外切矩形
     */
    private RectF mRectF;
    /**
     * 是否被点亮
     */
    private boolean isChecked;

    private float tickProgress;

    /**
     * 计数器
     */
    private int circleRadius = -1;
    private int ringProgress;

    /**
     * 是否处于动画中
     */
    private boolean isAnimationRunning = false;

    /**
     * 最后扩大缩小动画中,画笔的宽度的最大倍数
     */
    private static final int SCALE_TIMES = 3;

    /**
     * 全部动画的组合
     */
    private AnimatorSet mFinalAnimator;

    /**
     * 打钩的相关设置
     */
    private Path mTickPath;
    private PathMeasure mPathMeasure;
    private Path mTickPathMeasureDst;

    private int checkedColor;
    private int unCheckedColor;
    private int checkedTickColor;
    private int backGroundColor;

    public TickView(Context context) {
        this(context, null);
    }

    public TickView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TickView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TickView);
        ringWidth = (int) ta.getDimension(R.styleable.TickView_ring_width, 0);
        ringRadius = (int) ta.getDimension(R.styleable.TickView_ring_radius, 0);
        checkedColor = ta.getColor(R.styleable.TickView_checked_color, Color.RED);
        unCheckedColor = ta.getColor(R.styleable.TickView_unchecked_color, Color.GRAY);
        checkedTickColor = ta.getColor(R.styleable.TickView_checked_tick_color, Color.WHITE);
        backGroundColor = ta.getColor(R.styleable.TickView_background_color, Color.WHITE);
        ta.recycle();
        init();
    }

    private void init() {
        initPaint();
        initAnimator();
        setUpEvent();
    }

    private void initPaint() {
        mRectF = new RectF();
        if (mTickPath == null) {
            mTickPath = new Path();
        }
        if (mTickPathMeasureDst == null) {
            mTickPathMeasureDst = new Path();
        }
        if (mPathMeasure == null) {
            mPathMeasure = new PathMeasure();
        }

        if (mPaintRing == null) {
            mPaintRing = new Paint(Paint.ANTI_ALIAS_FLAG);
        }

        mPaintRing.setColor(isChecked ? checkedColor : unCheckedColor);
        mPaintRing.setStyle(Paint.Style.STROKE);
        mPaintRing.setStrokeCap(Paint.Cap.ROUND);
        mPaintRing.setStrokeWidth(ringWidth);

        if (mPaintCircle == null) {
            mPaintCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
        }
        mPaintCircle.setColor(unCheckedColor);

        if (mPaintTick == null) {
            mPaintTick = new Paint(Paint.ANTI_ALIAS_FLAG);
        }

        mPaintTick.setColor(isChecked ? checkedTickColor : unCheckedColor);
        mPaintTick.setStyle(Paint.Style.STROKE);
        mPaintTick.setStrokeCap(Paint.Cap.ROUND);
        mPaintTick.setStrokeWidth(ringWidth);

    }

    private void setUpEvent() {
        setOnClickListener(v -> toggle());
    }

    public void toggle() {
        setChecked(!isChecked);
    }

    public void setChecked(boolean isChecked) {
        if (this.isChecked != isChecked) {
            this.isChecked = isChecked;
            reset();
        }
    }

    private void reset() {
        initPaint();
        mFinalAnimator.cancel();
        ringProgress = 0;
        circleRadius = -1;
        isAnimationRunning = false;
        mRectF.set(centerX - ringRadius * 1.0f, centerY - ringRadius * 1.0f
                , centerX + ringRadius * 1.0f, centerY + ringRadius * 1.0f);
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMySize((ringRadius + ringWidth * SCALE_TIMES) * 2, widthMeasureSpec);
        int height = getMySize((ringRadius + ringWidth * SCALE_TIMES) * 2, heightMeasureSpec);
        height = width = Math.max(width, height);
        setMeasuredDimension(width, height);
        centerX = getMeasuredWidth() / 2;
        centerY = getMeasuredHeight() / 2;
        mRectF.set(centerX - ringRadius * 1.0f, centerY - ringRadius * 1.0f
                , centerX + ringRadius * 1.0f, centerY + ringRadius * 1.0f);

        //设置打钩的几个点坐标
        float dx = (ringRadius - ringWidth) / 3.0f;
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
                mySize = size + ringWidth * SCALE_TIMES * 2;
                ringRadius = size / 2;
                ringWidth = ringRadius / 10;
                break;
            default:
                break;
        }
        if (ringWidth > ringRadius) {
            ringWidth = ringRadius / 10;
        }
        return mySize;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!isChecked) {
            canvas.drawPath(mTickPath, mPaintTick);
            canvas.drawArc(mRectF, 90, 360, false, mPaintRing);
            return;
        }
        //画圆弧弧度
        canvas.drawArc(mRectF, 90, ringProgress, false, mPaintRing);
        //画选中背景，随着动画会紧缩，最后变为0
        mPaintCircle.setColor(checkedColor);
        canvas.drawCircle(centerX, centerY, ringProgress == 360 ? ringRadius : 0, mPaintCircle);
        //画收缩內圆
        if (ringProgress == 360) {
            mPaintCircle.setColor(backGroundColor);
            canvas.drawCircle(centerX, centerY, circleRadius, mPaintCircle);
        }

        //画勾及放大收缩动画
        if (circleRadius == 0) {
            mPaintTick.setAlpha((int) (255 * tickProgress));
            mPathMeasure.getSegment(0, tickProgress * mPathMeasure.getLength(), mTickPathMeasureDst, true);
            canvas.drawPath(mTickPathMeasureDst, mPaintTick);
            canvas.drawArc(mRectF, 0, 360, false, mPaintRing);
        }

        //动画
        if (!isAnimationRunning) {
            isAnimationRunning = true;
            mFinalAnimator.start();
        }
    }

    private void initAnimator() {
        //圆环进进度
        ObjectAnimator ringAnimator = ObjectAnimator.ofInt(this, "ringProgress", 0, 360);
        ringAnimator.setDuration(500);
        ringAnimator.setInterpolator(null);

        //收缩动画
        ObjectAnimator circleAnimator = ObjectAnimator.ofInt(this, "circleRadius", ringRadius - ringWidth, 0);
        circleAnimator.setInterpolator(new DecelerateInterpolator());
        circleAnimator.setDuration(300);

        //打钩动画
        ValueAnimator tickAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);
        tickAnimator.setDuration(400);
        tickAnimator.addUpdateListener(animation -> setTickProgress((Float) animation.getAnimatedValue()));
        tickAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                setTickProgress(0);
                mPathMeasure.nextContour();
                mPathMeasure.setPath(mTickPath, false);
                mTickPathMeasureDst.reset();
            }
        });
        tickAnimator.setInterpolator(new DecelerateInterpolator());

        //放大回弹动画
        ObjectAnimator scaleAnimator = ObjectAnimator.ofFloat(this, "ringStrokeWidth", mPaintRing.getStrokeWidth()
                , mPaintRing.getStrokeWidth() * SCALE_TIMES, mPaintRing.getStrokeWidth() / SCALE_TIMES);
        scaleAnimator.setInterpolator(null);
        scaleAnimator.setDuration(200);

        //打钩和放大动画一起执行
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(tickAnimator, scaleAnimator);

        mFinalAnimator = new AnimatorSet();
        mFinalAnimator.playSequentially(ringAnimator, circleAnimator, animatorSet);
    }

    public int getRingProgress() {
        return ringProgress;
    }

    @Keep
    public void setRingProgress(int ringProgress) {
        this.ringProgress = ringProgress;
        postInvalidate();
    }

    public int getCircleRadius() {
        return circleRadius;
    }

    @Keep
    public void setCircleRadius(int circleRadius) {
        this.circleRadius = circleRadius;
        LogUtils.i("setCircleRadius: " + circleRadius);
        invalidate();
    }

    private void setTickProgress(float tickProgress) {
        this.tickProgress = tickProgress;
        LogUtils.i("setTickProgress: " + tickProgress);
        postInvalidate();
    }

    private float getRingStrokeWidth() {
        return mPaintRing.getStrokeWidth();
    }

    @Keep
    private void setTickAlpha(int tickAlpha) {
        mPaintTick.setAlpha(tickAlpha);
        postInvalidate();
    }

    @Keep
    private void setRingStrokeWidth(float strokeWidth) {
        mPaintRing.setStrokeWidth(strokeWidth);
        postInvalidate();
    }
}
