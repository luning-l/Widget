package com.helper.widgets.banner;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.helper.widgets.R;
import com.helper.widgets.banner.adapter.BannerPageAdapter;
import com.helper.widgets.banner.helper.BannerLoopScaleHelper;
import com.helper.widgets.banner.holder.BannerViewHolderCreator;
import com.helper.widgets.banner.listener.BannerPageChangeListener;
import com.helper.widgets.banner.listener.OnItemClickListener;
import com.helper.widgets.banner.listener.OnPageChangeListener;
import com.helper.widgets.banner.view.BannerLoopViewPager;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * 页面翻转控件，极方便的广告栏
 * 支持无限循环，自动翻页，翻页特效
 */
public class Banner<T> extends RelativeLayout {
    private List<T> mDatas;
    private int[] page_indicatorId;
    private ArrayList<ImageView> mPointViews = new ArrayList<ImageView>();
    private BannerPageAdapter pageAdapter;
    private BannerLoopViewPager viewPager;
    private ViewGroup loPageTurningPoint;
    private long autoTurningTime = -1;
    private boolean turning;
    private boolean canTurn = false;
    private boolean canLoop = true;
    private BannerLoopScaleHelper bannerLoopScaleHelper;
    private BannerPageChangeListener pageChangeListener;
    private OnPageChangeListener onPageChangeListener;
    private AdSwitchTask adSwitchTask;
    private boolean isVertical = false;
    private int mIndicatorWidth, mIndicatorHeight;

    public enum PageIndicatorAlign {
        ALIGN_PARENT_LEFT, ALIGN_PARENT_RIGHT, CENTER_HORIZONTAL
    }

    public Banner(Context context) {
        super(context);
        init(context);
    }

    public Banner(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Banner);
        canLoop = a.getBoolean(R.styleable.Banner_canLoop, true);
        autoTurningTime = a.getInteger(R.styleable.Banner_autoTurningTime, -1);
        a.recycle();
        init(context);
    }

    private void init(Context context) {
        View hView = LayoutInflater.from(context).inflate(
                R.layout._banner_viewpager, this, true);
        viewPager = (BannerLoopViewPager) hView.findViewById(R.id.bannerLoopViewPager);
        loPageTurningPoint = (ViewGroup) hView
                .findViewById(R.id.loPageTurningPoint);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        viewPager.setLayoutManager(linearLayoutManager);

        bannerLoopScaleHelper = new BannerLoopScaleHelper();

        adSwitchTask = new AdSwitchTask(this);
    }

    public Banner setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        viewPager.setLayoutManager(layoutManager);
        return this;
    }

    public Banner setPages(BannerViewHolderCreator holderCreator, List<T> datas) {
        this.mDatas = datas;
        pageAdapter = new BannerPageAdapter(holderCreator, mDatas, canLoop);
        viewPager.setAdapter(pageAdapter);

        if (page_indicatorId != null) {
            setPageIndicator(page_indicatorId);
        }

        bannerLoopScaleHelper.setFirstItemPos(canLoop ? mDatas.size() : 0);
        bannerLoopScaleHelper.attachToRecyclerView(viewPager);
        setPointViewVisible(mDatas != null && mDatas.size() > 1);
        return this;
    }

    public Banner setCanLoop(boolean canLoop) {
        this.canLoop = canLoop;
        pageAdapter.setCanLoop(canLoop);
        notifyDataSetChanged();
        return this;
    }

    public boolean isCanLoop() {
        return canLoop;
    }


    /**
     * 通知数据变化
     */
    public void notifyDataSetChanged() {
        viewPager.getAdapter().notifyDataSetChanged();
        if (page_indicatorId != null) {
            setPageIndicator(page_indicatorId);
        }
        bannerLoopScaleHelper.setCurrentItem(canLoop ? mDatas.size() : 0);
    }

    /**
     * 设置底部指示器是否可见
     *
     * @param visible
     */
    public Banner setPointViewVisible(boolean visible) {
        loPageTurningPoint.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }

    public boolean isPointViewVisible() {
        return loPageTurningPoint.getVisibility() == View.VISIBLE;
    }

    public Banner setPageIndicatorSize(int width, int height) {
        mIndicatorWidth = width;
        mIndicatorHeight = height;
        return this;
    }

    /**
     * 底部指示器资源图片
     *
     * @param page_indicatorId
     */
    public Banner setPageIndicator(int[] page_indicatorId) {
        loPageTurningPoint.removeAllViews();
        mPointViews.clear();
        this.page_indicatorId = page_indicatorId;
        if (mDatas == null || mDatas.size() <= 1) return this;
        for (int count = 0; count < mDatas.size(); count++) {
            // 翻页指示的点
            ImageView pointView = new ImageView(getContext());
            pointView.setPadding(10, 0, 10, 0);
            if (bannerLoopScaleHelper.getFirstItemPos() % mDatas.size() == count) {
                pointView.setImageResource(page_indicatorId[1]);
            } else {
                pointView.setImageResource(page_indicatorId[0]);
            }
            mPointViews.add(pointView);
            LinearLayout.LayoutParams layoutParams = null;
            if (mIndicatorWidth > 0 || mIndicatorHeight > 0) {
                layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                if (mIndicatorWidth > 0) layoutParams.width = mIndicatorWidth;
                if (mIndicatorHeight > 0) layoutParams.height = mIndicatorHeight;
            }
            if (layoutParams == null) {
                loPageTurningPoint.addView(pointView);
            } else {
                loPageTurningPoint.addView(pointView, layoutParams);
            }
        }
        pageChangeListener = new BannerPageChangeListener(mPointViews,
                page_indicatorId);
        bannerLoopScaleHelper.setOnPageChangeListener(pageChangeListener);
        if (onPageChangeListener != null) {
            pageChangeListener.setOnPageChangeListener(onPageChangeListener);
        }
        return this;
    }

    public OnPageChangeListener getOnPageChangeListener() {
        return onPageChangeListener;
    }

    /**
     * 设置翻页监听器
     *
     * @param onPageChangeListener
     * @return
     */
    public Banner setOnPageChangeListener(OnPageChangeListener onPageChangeListener) {
        this.onPageChangeListener = onPageChangeListener;
        //如果有默认的监听器（即是使用了默认的翻页指示器）则把用户设置的依附到默认的上面，否则就直接设置
        if (pageChangeListener != null){
            pageChangeListener.setOnPageChangeListener(onPageChangeListener);
        } else {
            bannerLoopScaleHelper.setOnPageChangeListener(onPageChangeListener);
        }
        return this;
    }

    /**
     * 监听item点击
     *
     * @param onItemClickListener
     */
    public Banner setOnItemClickListener(final OnItemClickListener onItemClickListener) {
        if (onItemClickListener == null) {
            pageAdapter.setOnItemClickListener(null);
            return this;
        }
        pageAdapter.setOnItemClickListener(onItemClickListener);
        return this;
    }

    /**
     * 获取当前页对应的position
     *
     * @return
     */
    public int getCurrentItem() {
        return bannerLoopScaleHelper.getRealCurrentItem();
    }

    /**
     * 设置当前页对应的position
     *
     * @return
     */
    public Banner setCurrentItem(int position, boolean smoothScroll) {
        bannerLoopScaleHelper.setCurrentItem(canLoop ? mDatas.size() + position : position, smoothScroll);
        return this;
    }

    /**
     * 设置第一次加载当前页对应的position
     * setPageIndicator之前使用
     *
     * @return
     */
    public Banner setFirstItemPos(int position) {
        bannerLoopScaleHelper.setFirstItemPos(canLoop ? mDatas.size() + position : position);
        return this;
    }

    /**
     * 指示器的方向
     *
     * @param align 三个方向：居左 （RelativeLayout.ALIGN_PARENT_LEFT），居中 （RelativeLayout.CENTER_HORIZONTAL），居右 （RelativeLayout.ALIGN_PARENT_RIGHT）
     * @return
     */
    public Banner setPageIndicatorAlign(PageIndicatorAlign align) {
        LayoutParams layoutParams = (LayoutParams) loPageTurningPoint.getLayoutParams();
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, align == PageIndicatorAlign.ALIGN_PARENT_LEFT ? RelativeLayout.TRUE : 0);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, align == PageIndicatorAlign.ALIGN_PARENT_RIGHT ? RelativeLayout.TRUE : 0);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, align == PageIndicatorAlign.CENTER_HORIZONTAL ? RelativeLayout.TRUE : 0);
        loPageTurningPoint.setLayoutParams(layoutParams);
        return this;
    }

    /***
     * 是否开启了翻页
     * @return
     */
    public boolean isTurning() {
        return turning;
    }

    /***
     * 开始翻页
     * @param autoTurningTime 自动翻页时间
     * @return
     */
    public Banner startTurning(long autoTurningTime) {
        if (autoTurningTime < 0) return this;
        //如果是正在翻页的话先停掉
        if (turning) {
            stopTurning();
        }
        //设置可以翻页并开启翻页
        canTurn = true;
        this.autoTurningTime = autoTurningTime;
        turning = true;
        postDelayed(adSwitchTask, autoTurningTime);
        return this;
    }

    public Banner startTurning() {
        startTurning(autoTurningTime);
        return this;
    }


    public void stopTurning() {
        turning = false;
        removeCallbacks(adSwitchTask);
    }

    //触碰控件的时候，翻页应该停止，离开的时候如果之前是开启了翻页的话则重新启动翻页

    float startX, startY;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (!isPointViewVisible()) return true;
        int action = ev.getAction();
        if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_OUTSIDE) {
            // 开始翻页
            if (canTurn) startTurning(autoTurningTime);
        } else if (action == MotionEvent.ACTION_DOWN) {
            // 停止翻页
            if (canTurn) stopTurning();
        }

        return super.dispatchTouchEvent(ev);
    }

    static class AdSwitchTask implements Runnable {

        private final WeakReference<Banner> reference;

        AdSwitchTask(Banner banner) {
            this.reference = new WeakReference<Banner>(banner);
        }

        @Override
        public void run() {
            Banner banner = reference.get();

            if (banner != null) {
                if (banner.viewPager != null && banner.turning) {
                    int page = banner.bannerLoopScaleHelper.getCurrentItem() + 1;
                    banner.bannerLoopScaleHelper.setCurrentItem(page, true);
                    banner.postDelayed(banner.adSwitchTask, banner.autoTurningTime);
                }
            }
        }
    }

}
