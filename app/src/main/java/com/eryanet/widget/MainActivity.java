package com.eryanet.widget;

import android.Manifest;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.helper.base.BaseActivity;
import com.helper.logging.Logcat;
import com.helper.utils.BarUtils;
import com.helper.utils.ColorUtils;
import com.helper.utils.PermissionUtils;
import com.helper.utils.ToastUtils;
import com.helper.widgets.banner.Banner;
import com.helper.widgets.banner.adapter.BannerImageAdapter;
import com.helper.widgets.banner.holder.BannerImageHolder;
import com.helper.widgets.banner.indicator.RoundLinesIndicator;
import com.helper.widgets.banner.util.BannerUtils;
import com.helper.widgets.brvah.BaseQuickAdapter;
import com.helper.widgets.brvah.BaseViewHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    public void setBar(boolean isLightMode, @ColorInt int color, boolean isDecor) {
        BarUtils.setStatusBarLightMode(this, isLightMode);
        View fakeStatusBar = findViewById(R.id.statusBarCustomFakeStatusBar);
        fakeStatusBar.setBackgroundColor(color);
        BarUtils.setStatusBarCustom(fakeStatusBar, isDecor);
    }

    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.rv)
    RecyclerView mRecyclerView;

    private List<String> bannerList;
    private BaseQuickAdapter baseQuickAdapter;
    private List<String> list;
    private String[] images = {
            "http://img2.3lian.com/2014/f2/37/d/40.jpg",
            "http://img2.3lian.com/2014/f2/37/d/39.jpg",
            "http://f.hiphotos.baidu.com/image/h%3D200/sign=1478eb74d5a20cf45990f9df460b4b0c/d058ccbf6c81800a5422e5fdb43533fa838b4779.jpg",
            "http://f.hiphotos.baidu.com/image/pic/item/09fa513d269759ee50f1971ab6fb43166c22dfba.jpg"
    };

    @Override
    public void initData(@Nullable Bundle bundle) {

    }

    @Override
    public int bindLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState, @Nullable View contentView) {
        ButterKnife.bind(this);
        setBar(true, ColorUtils.getRandomColor(), true);
    }

    @Override
    public void doBusiness() {
        permission();
        initBanner();
        initRecyclerView();

        updateBanner(Arrays.asList(images));
        List<String> strings = new ArrayList<>();
        for (int i = 0; i < 200; i++) {
            strings.add(String.valueOf(i));
        }
        updateRecycleView(strings);
    }

    private void initBanner() {
        if (bannerList == null) {
            bannerList = new ArrayList<>();
        }
        banner.setAdapter(new BannerImageAdapter<DataBean>(DataBean.getTestData3()) {
                    @Override
                    public void onBindView(BannerImageHolder holder, DataBean data, int position, int size) {
                        //图片加载自己实现
                        Glide.with(holder.itemView)
                                .load(data.imageUrl)
//                        .thumbnail(Glide.with(holder.itemView).load(R.drawable.loading))
                                .apply(RequestOptions.bitmapTransform(new RoundedCorners(30)))
                                .into(holder.imageView);
                    }
                })
                .setIndicator(new RoundLinesIndicator(this))
                .setIndicatorSelectedWidth((int) BannerUtils.dp2px(15));
    }

    private void updateBanner(List<String> list) {
        if (list == null || list.isEmpty()) return;
        if (bannerList == null || banner == null) {
            initBanner();
        }
        bannerList.clear();
        bannerList.addAll(list);
    }

    private void initRecyclerView() {
        if (list == null) {
            list = new ArrayList<>();
        }
//        mRecyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        gridLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        baseQuickAdapter = new BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_main_rv, list) {
            @Override
            protected void convert(@NonNull BaseViewHolder holder, String item) {
                holder.setText(R.id.tv_text, item);
            }
        };
        baseQuickAdapter.setOnItemClickListener((adapter, view, position) -> ToastUtils.showShort((String) adapter.getItem(position)));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(this, R.drawable.main_rv_decoration));
        mRecyclerView.addItemDecoration(dividerItemDecoration);
//        mRecyclerView.addItemDecoration(new ListDividerItemDecoration());
        mRecyclerView.setAdapter(baseQuickAdapter);
    }

    private void updateRecycleView(List<String> list) {
        if (list == null || list.isEmpty()) return;
        if (this.list == null || baseQuickAdapter == null) {
            initRecyclerView();
        }
        this.list.clear();
        this.list.addAll(list);
        baseQuickAdapter.notifyDataSetChanged();
    }

    private void permission() {
        if (PermissionUtils.getInstance().isGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Logcat.i("已经授权了");
        } else {
            PermissionUtils.getInstance(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .callback(new PermissionUtils.SimpleCallback() {
                        @Override
                        public void onGranted() {
                            Logcat.i("已授权");
                        }

                        @Override
                        public void onDenied() {
                            Logcat.i("已拒绝");
                        }
                    }).request();
        }
    }
}
