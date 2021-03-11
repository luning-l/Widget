package com.eryanet.widget;

import android.Manifest;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

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
import com.helper.utils.Utils;
import com.helper.widgets.banner.Banner;
import com.helper.widgets.banner.adapter.BannerImageAdapter;
import com.helper.widgets.banner.holder.BannerImageHolder;
import com.helper.widgets.banner.indicator.RectangleIndicator;
import com.helper.widgets.banner.listener.OnPageChangeListener;
import com.helper.widgets.banner.util.BannerUtils;
import com.helper.widgets.brvah.BaseQuickAdapter;
import com.helper.widgets.brvah.BaseViewHolder;
import com.helper.widgets.magicindicator.MagicIndicator;
import com.helper.widgets.magicindicator.ViewPagerHelper;
import com.helper.widgets.magicindicator.buildins.commonnavigator.CommonNavigator;
import com.helper.widgets.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import com.helper.widgets.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import com.helper.widgets.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import com.helper.widgets.magicindicator.buildins.commonnavigator.indicators.TriangularPagerIndicator;
import com.helper.widgets.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;
import com.helper.widgets.statelayout.StateLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    public void setBar(boolean isLightMode, @ColorInt int color, boolean isDecor) {
        BarUtils.setStatusBarLightMode(this, isLightMode);
        View fakeStatusBar = findViewById(R.id.statusBarCustomFakeStatusBar);
        fakeStatusBar.setBackgroundColor(color);
        BarUtils.setStatusBarCustom(fakeStatusBar, isDecor);
    }

    //    @BindView(R.id.banner_parent)
//    FrameLayout bannerParent;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.rv)
    RecyclerView mRecyclerView;
    @BindView(R.id.state_layout)
    StateLayout stateLayout;
    @BindView(R.id.rl_custom)
    View customView;
    @BindView(R.id.magicindicator)
    MagicIndicator magicIndicator;

    private List<String> bannerList;
    private BaseQuickAdapter baseQuickAdapter;
    private List<String> list;
    private String[] images = {
            "http://img2.3lian.com/2014/f2/37/d/40.jpg",
            "http://img2.3lian.com/2014/f2/37/d/39.jpg",
            "http://f.hiphotos.baidu.com/image/h%3D200/sign=1478eb74d5a20cf45990f9df460b4b0c/d058ccbf6c81800a5422e5fdb43533fa838b4779.jpg",
            "http://f.hiphotos.baidu.com/image/pic/item/09fa513d269759ee50f1971ab6fb43166c22dfba.jpg"
    };
    private  List<String> mDataList;
    {
        mDataList = new ArrayList<>();
        mDataList.add("aaa");
        mDataList.add("bbb");
        mDataList.add("ccc");
        mDataList.add("ddd");
        mDataList.add("eee");
        mDataList.add("fff");
        mDataList.add("ggg");
    }

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
//        initMagicIndicator();
    }

    private void initBanner() {
        if (bannerList == null) {
            bannerList = new ArrayList<>();
        }
//        banner = new Banner(this);
//        banner.setIsInfiniteLoop(true);
//        banner.setIsAutoLoop(false);
//        bannerParent.addView(banner);
        banner.setImmediateCallBack(true);
        banner.addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Logcat.i("onPageSelected: " + position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
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
        });
        banner.setIndicator(new RectangleIndicator(Utils.getApp()));
        banner.setIndicatorSelectedWidth((int) BannerUtils.dp2px(15));
        banner.setIndicatorNormalWidth((int) BannerUtils.dp2px(15));
        banner.setIndicatorNormalColor(Color.WHITE);
        banner.setIndicatorSelectedColor(getResources().getColor(R.color.colorPrimary));
        banner.setIndicatorRadius(0);
        banner.setOnBannerListener((data, position) -> {
            Logcat.i("OnBannerClick: " + position);
            ToastUtils.showShort("" + position);
        });
        banner.setUserInputEnabled(true);
    }

    private void updateBanner(List<String> list) {
        if (list == null || list.isEmpty()) return;
        if (bannerList == null || banner == null) {
            initBanner();
        }
        bannerList.clear();
        bannerList.addAll(list);
        banner.setImmediateCallBack(true);
        banner.setDatas(DataBean.getTestData3());
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
            protected void convert(@NonNull BaseViewHolder holder, int position, String item) {
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

    @OnClick({R.id.btn_content, R.id.btn_empty, R.id.btn_error, R.id.btn_loading, R.id.btn_time_out, R.id.btn_not_network, R.id.btn_login, R.id.btn_custom})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.btn_content:
                stateLayout.showContentView();
                // TODO test
                banner.setImmediateCallBack(true);
                banner.setCurrentItem(banner.getStartPosition(), false);
                break;
            case R.id.btn_empty:
                stateLayout.showEmptyView();
                break;
            case R.id.btn_error:
                stateLayout.showErrorView();
                break;
            case R.id.btn_loading:
                stateLayout.showLoadingView();
                break;
            case R.id.btn_time_out:
                stateLayout.showTimeoutView();
                break;
            case R.id.btn_not_network:
                stateLayout.showNoNetworkView();
                break;
            case R.id.btn_login:
                stateLayout.showLoginView();
                break;
            case R.id.btn_custom:
                stateLayout.showCustomView(customView);
                break;
        }
    }

    private void initMagicIndicator() {
        magicIndicator.setBackgroundColor(Color.WHITE);
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setScrollPivotX(0.15f);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mDataList == null ? 0 : mDataList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new SimplePagerTitleView(context);
                simplePagerTitleView.setText(mDataList.get(index));
                simplePagerTitleView.setNormalColor(Color.parseColor("#333333"));
                simplePagerTitleView.setSelectedColor(Color.parseColor("#e94220"));
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        mViewPager.setCurrentItem(index);
                    }
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                TriangularPagerIndicator indicator = new TriangularPagerIndicator(context);
                indicator.setLineColor(Color.parseColor("#e94220"));
                return indicator;
            }
        });
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, null);
    }
}
