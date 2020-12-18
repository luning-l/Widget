package com.helper.widgets.banner.holder;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public abstract class BannerViewHolder<T> extends RecyclerView.ViewHolder {
    public BannerViewHolder(View itemView) {
        super(itemView);
        initView(itemView);
    }

    protected abstract void initView(View itemView);

    public abstract void updateUI(T data);
}
