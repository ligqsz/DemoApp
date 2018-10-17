package com.pax.demoapp.common.adapter.rcy;


/**
 * @author ligq
 */
public interface ItemViewDelegate<T> {

    int getItemViewLayoutId();

    boolean isForViewType(T item, int position);

    void convert(CommViewHolder holder, T t, int position);

}
