package com.helper.widgets.banner.holder;

import android.view.View;

public interface BannerViewHolderCreator {
    int getLayoutId();

    BannerViewHolder createHolder(View itemView);
}
