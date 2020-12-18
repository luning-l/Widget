package com.eryanet.widget;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.helper.widgets.banner.holder.BannerViewHolder;

public class BannerViewHolderAdapter extends BannerViewHolder<String> {

    private Context mContext;
    private ImageView iv_banner;
    private RequestOptions requestOptions;

    public BannerViewHolderAdapter(View itemView, Context context) {
        super(itemView);
        mContext = context;
        requestOptions = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
    }


    @Override
    protected void initView(View itemView) {
        iv_banner = itemView.findViewById(R.id.iv_banner);
    }

    @Override
    public void updateUI(String url) {
        if (TextUtils.isEmpty(url)) return;
        Glide.with(mContext).load(url).apply(requestOptions).into(iv_banner);
    }
}
