package com.pax.demoapp.common.adapter;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.util.SparseArray;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * @author ligq
 * @date 2018-9-27 09:26:00
 */
@SuppressWarnings({"unused", "WeakerAccess", "unchecked"})
public class Converter {
    private View mConvertView;
    private SparseArray<View> mViews;

    public Converter(View convertView) {
        mViews = new SparseArray<>();
        this.mConvertView = convertView;
    }

    public static Converter get(View convertView) {
        Converter converter = (Converter) convertView.getTag();
        if (converter == null) {
            converter = new Converter(convertView);
            convertView.setTag(converter);
        }
        return converter;
    }

    /**
     * 根据viewId获取View视图实例
     *
     * @param viewId viewId
     * @return T extends View
     */
    private <T extends View> T getView(int viewId) {
        View childView = mViews.get(viewId);
        if (childView == null) {
            childView = mConvertView.findViewById(viewId);
            mViews.put(viewId, childView);
        }
        return (T) childView;
    }

    /**
     * 设置控件显示隐藏
     *
     * @param viewId  viewId
     * @param visible visible
     * @return Converter
     */
    public Converter setVisible(@IdRes int viewId, boolean visible) {
        View view = getView(viewId);
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }

    /**
     * 设置文本
     *
     * @param viewId viewId
     * @param value  text
     * @return Converter
     */
    public Converter setText(@IdRes int viewId, CharSequence value) {
        TextView view = getView(viewId);
        view.setText(value);
        return this;
    }

    /**
     * 设置文本
     *
     * @param viewId viewId
     * @param strId  String id
     * @return Converter
     */
    public Converter setText(@IdRes int viewId, @StringRes int strId) {
        TextView view = getView(viewId);
        view.setText(strId);
        return this;
    }

    /**
     * 设置文字颜色
     *
     * @param viewId    viewId
     * @param textColor textColor
     * @return Converter
     */
    public Converter setTextColor(@IdRes int viewId, @ColorInt int textColor) {
        TextView view = getView(viewId);
        view.setTextColor(textColor);
        return this;
    }

    /**
     * 给指定viewId的ImageView控件设置资源图片
     *
     * @param viewId viewId
     * @param resId  resId
     * @return Converter
     */
    public Converter setImageResource(@IdRes int viewId, int resId) {
        ImageView iv = getView(viewId);
        iv.setImageResource(resId);
        return this;
    }

    /**
     * 设置适配器
     *
     * @param viewId  viewId
     * @param adapter adapter
     * @return Converter
     */
    public Converter setAdapter(@IdRes int viewId, Adapter adapter) {
        AdapterView view = getView(viewId);
        view.setAdapter(adapter);
        return this;
    }

    /**
     * 设置控件Alpha值 不允许重复调用
     * Alpha 范围 0-1.
     *
     * @param viewId viewId
     * @param value  value
     * @return Converter
     */
    @SuppressLint("ObsoleteSdkInt")
    public Converter setAlpha(@IdRes int viewId, float value) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getView(viewId).setAlpha(value);
        } else {
            AlphaAnimation alpha = new AlphaAnimation(value, value);
            alpha.setDuration(0);
            alpha.setFillAfter(true);
            getView(viewId).startAnimation(alpha);
        }
        return this;
    }

    /**
     * 设置控件的背景色
     *
     * @param viewId viewId
     * @param color  color
     * @return Converter
     */
    public Converter setBackgroundColor(@IdRes int viewId, @ColorInt int color) {
        View view = getView(viewId);
        view.setBackgroundColor(color);
        return this;
    }

    /**
     * 设置控件背景资源
     *
     * @param viewId        viewId
     * @param backgroundRes backgroundRes
     * @return Converter
     */
    public Converter setBackgroundRes(@IdRes int viewId, @DrawableRes int backgroundRes) {
        View view = getView(viewId);
        view.setBackgroundResource(backgroundRes);
        return this;
    }

    /**
     * 设置控件选中
     *
     * @param viewId  viewId
     * @param checked is checked
     * @return Converter
     */
    public Converter setChecked(@IdRes int viewId, boolean checked) {
        View view = getView(viewId);
        if (view instanceof Checkable) {
            ((Checkable) view).setChecked(checked);
        }
        return this;
    }

    /**
     * 设置图片控件图片显示
     *
     * @param viewId   viewId
     * @param drawable Drawable
     * @return Converter
     */
    public Converter setImageDrawable(@IdRes int viewId, Drawable drawable) {
        ImageView view = getView(viewId);
        view.setImageDrawable(drawable);
        return this;
    }

    /**
     * 设置图片控件图片显示
     *
     * @param viewId viewId
     * @param bitmap bitmap
     * @return Converter
     */
    public Converter setImageBitmap(@IdRes int viewId, Bitmap bitmap) {
        ImageView view = getView(viewId);
        view.setImageBitmap(bitmap);
        return this;
    }

    /**
     * 设置进度
     *
     * @param viewId   viewId
     * @param progress progress
     * @return Converter
     */
    public Converter setProgress(@IdRes int viewId, int progress) {
        ProgressBar view = getView(viewId);
        view.setProgress(progress);
        return this;
    }

    /**
     * 设置进度
     *
     * @param viewId   viewId
     * @param progress progress
     * @param max      max value
     * @return Converter
     */
    public Converter setProgress(@IdRes int viewId, int progress, int max) {
        ProgressBar view = getView(viewId);
        view.setMax(max);
        view.setProgress(progress);
        return this;
    }

    /**
     * 设置RatingBar的rating
     *
     * @param viewId viewId
     * @param rating rating
     * @return Converter
     */
    public Converter setRating(@IdRes int viewId, float rating) {
        RatingBar view = getView(viewId);
        view.setRating(rating);
        return this;
    }

    /**
     * 设置RatingBar的rating
     *
     * @param viewId viewId
     * @param rating rating
     * @param max    max
     * @return Converter
     */
    public Converter setRating(@IdRes int viewId, float rating, int max) {
        RatingBar view = getView(viewId);
        view.setMax(max);
        view.setRating(rating);
        return this;
    }

    /**
     * 设置checked事件
     *
     * @param viewId   viewId
     * @param listener OnCheckedChangeListener
     * @return Converter
     */
    public Converter setOnCheckedChangeListener(@IdRes int viewId, CompoundButton.OnCheckedChangeListener listener) {
        CompoundButton view = getView(viewId);
        view.setOnCheckedChangeListener(listener);
        return this;
    }

    /**
     * 设置点击事件
     *
     * @param viewId   viewId
     * @param listener OnClickListener
     * @return Converter
     */
    public Converter setOnClickListener(@IdRes int viewId, View.OnClickListener listener) {
        View view = getView(viewId);
        view.setOnClickListener(listener);
        return this;
    }

    /**
     * 设置长按事件
     *
     * @param viewId   viewId
     * @param listener OnLongClickListener
     * @return Converter
     */
    public Converter setOnLongClickListener(@IdRes int viewId, View.OnLongClickListener listener) {
        View view = getView(viewId);
        view.setOnLongClickListener(listener);
        return this;
    }
}
