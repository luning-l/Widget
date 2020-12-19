package com.helper.widgets.banner.helper;

import android.view.View;
import android.view.ViewTreeObserver;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.helper.widgets.banner.adapter.BannerPageAdapter;
import com.helper.widgets.banner.listener.OnPageChangeListener;
import com.helper.widgets.banner.view.BannerLoopViewPager;

public class BannerLoopScaleHelper {
    private BannerLoopViewPager mRecyclerView;

    private int mPagePadding = 0; // 卡片的padding, 卡片间的距离等于2倍的mPagePadding
    private int mShowLeftCardWidth = 0;   // 左边卡片显示大小

    private int mFirstItemPos;

    private PagerSnapHelper mPagerSnapHelper = new PagerSnapHelper();
    private OnPageChangeListener onPageChangeListener;

    private int mCurrentPos = -1;

    public void attachToRecyclerView(final BannerLoopViewPager mRecyclerView) {
        if (mRecyclerView == null) {
            return;
        }
        this.mRecyclerView = mRecyclerView;
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (onPageChangeListener != null) {
                    onPageChangeListener.onScrollStateChanged(recyclerView, newState);
                }
//                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int position = getCurrentItem();
                    //这里变换位置实现循环
                    BannerPageAdapter adapter = (BannerPageAdapter) mRecyclerView.getAdapter();
                    int count = adapter.getRealItemCount();
                    if (adapter.isCanLoop()) {
                        position = count + position % count;
                    }
                    if (position % count == mCurrentPos) return;
                    setCurrentItem(position);
//                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                //Log.e("TAG", String.format("onScrolled dx=%s, dy=%s", dx, dy));
                super.onScrolled(recyclerView, dx, dy);
                if (onPageChangeListener != null) {
                    onPageChangeListener.onScrolled(recyclerView, dx, dy);
                }
//                onScrolledChangedCallback();
            }
        });
        initWidth();
        mPagerSnapHelper.attachToRecyclerView(mRecyclerView);
    }

    /**
     * 初始化卡片宽度
     */
    private void initWidth() {
        mRecyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mRecyclerView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                scrollToPosition(mFirstItemPos);
            }
        });
    }

    public synchronized void setCurrentItem(int item) {
        setCurrentItem(item, false);
    }

    public void setCurrentItem(int item, boolean smoothScroll) {
        if (mRecyclerView == null) {
            return;
        }
        if (smoothScroll) {
            mRecyclerView.smoothScrollToPosition(item);
        } else {
            scrollToPosition(item);
        }
        if (onPageChangeListener != null) {
            int count = getCount();
            if (count != 0) {
                mCurrentPos = item % count;
//                onPageChangeListener.onPageSelected(mCurrentPos);
            }
        }
    }

    private int getCount() {
        if (mRecyclerView == null) {
            return 0;
        }
        BannerPageAdapter adapter = (BannerPageAdapter) mRecyclerView.getAdapter();
        int count = adapter.getRealItemCount();
        return count;
    }

    public void scrollToPosition(int pos) {
        if (mRecyclerView == null) {
            return;
        }
        ((LinearLayoutManager) mRecyclerView.getLayoutManager()).
                scrollToPositionWithOffset(pos,
                        (mPagePadding + mShowLeftCardWidth));
        mRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                onScrolledChangedCallback();
            }
        });
    }

    public void setFirstItemPos(int firstItemPos) {
        this.mFirstItemPos = firstItemPos;
    }


    /**
     * RecyclerView位移事件监听, view大小随位移事件变化
     */
    private void onScrolledChangedCallback() {
        onPageChangeListener.onPageSelected(mCurrentPos);
    }

    public int getCurrentItem() {
        try {
            RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
            View view = mPagerSnapHelper.findSnapView(layoutManager);
            if (view != null)
                return layoutManager.getPosition(view);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getRealCurrentItem() {
        BannerPageAdapter adapter = (BannerPageAdapter) mRecyclerView.getAdapter();
        int count = adapter.getRealItemCount();
        return getCurrentItem() % count;
    }

    public void setPagePadding(int pagePadding) {
        mPagePadding = pagePadding;
    }

    public void setShowLeftCardWidth(int showLeftCardWidth) {
        mShowLeftCardWidth = showLeftCardWidth;
    }

    public int getFirstItemPos() {
        return mFirstItemPos;
    }

    public int getRealItemCount() {
        BannerPageAdapter adapter = (BannerPageAdapter) mRecyclerView.getAdapter();
        int count = adapter.getRealItemCount();
        return count;
    }

    public void setOnPageChangeListener(OnPageChangeListener onPageChangeListener) {
        this.onPageChangeListener = onPageChangeListener;
    }
}
