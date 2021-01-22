package com.helper.widgets.statelayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.helper.widgets.R;
import com.helper.widgets.statelayout.anim.ViewAnimProvider;
import com.helper.widgets.statelayout.helper.AnimationHelper;

public class StateLayout extends FrameLayout implements IShowView {
    public static final int LOGIN = 1;
    public static final int LOADING = 2;
    public static final int TIMEOUT = 3;
    public static final int EMPTY = 4;
    public static final int ERROR = 5;
    public static final int NO_NETWORK = 6;

    @IntDef({LOGIN, LOADING, TIMEOUT, EMPTY, ERROR, NO_NETWORK})
    public @interface State {
    }

    private View contentView;
    private View emptyView;
    private View errorView;
    private View loadingView;
    private View timeOutView;
    private View notNetworkView;
    private View loginView;

    private View currentShowingView;
    private boolean useAnimation = false;
    private ViewAnimProvider viewAnimProvider;

    public StateLayout(@NonNull Context context) {
        this(context, null);
    }

    public StateLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StateLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initTypedArray(context, attrs, defStyleAttr);
    }

    private void initTypedArray(Context context, AttributeSet attrs, int defStyleAttr) {
        if (attrs == null) return;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.StateLayout);
        int loginLayout = a.getResourceId(R.styleable.StateLayout_loginLayout, -1);
        handleLayout(context, loginLayout, LOGIN);
        int loadingLayout = a.getResourceId(R.styleable.StateLayout_loadingLayout, -1);
        handleLayout(context, loadingLayout, LOADING);
        int timeOutLayout = a.getResourceId(R.styleable.StateLayout_timeOutLayout, -1);
        handleLayout(context, timeOutLayout, TIMEOUT);
        int emptyLayout = a.getResourceId(R.styleable.StateLayout_emptyLayout, -1);
        handleLayout(context, emptyLayout, EMPTY);
        int errorLayout = a.getResourceId(R.styleable.StateLayout_errorLayout, -1);
        handleLayout(context, errorLayout, ERROR);
        int noNetworkLayout = a.getResourceId(R.styleable.StateLayout_noNetworkLayout, -1);
        handleLayout(context, noNetworkLayout, NO_NETWORK);
        useAnimation = a.getBoolean(R.styleable.StateLayout_useAnimation, false);
        a.recycle();
    }

    private void handleLayout(Context context, int layout, @State int state) {
        if (layout == -1) return;
        View view = LayoutInflater.from(context).inflate(layout, null);
        if (view == null) return;
        switch (state) {
            case LOGIN:
                loginView = view;
                break;
            case LOADING:
                loadingView = view;
                break;
            case TIMEOUT:
                timeOutView = view;
                break;
            case EMPTY:
                emptyView = view;
                break;
            case ERROR:
                errorView = view;
                break;
            case NO_NETWORK:
                notNetworkView = view;
                break;
        }
        addView(view);
        view.setVisibility(GONE);
    }

    private void checkIsContentView(View view) {
        if (contentView == null && view != errorView && view != notNetworkView
                && view != loadingView && view != timeOutView && view != loginView
                && view != emptyView) {
            contentView = view;
            currentShowingView = contentView;
        }
    }

    @Override
    public void addView(View child) {
        checkIsContentView(child);
        super.addView(child);
    }

    @Override
    public void addView(View child, int index) {
        checkIsContentView(child);
        super.addView(child, index);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        checkIsContentView(child);
        super.addView(child, index, params);
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        checkIsContentView(child);
        super.addView(child, params);
    }

    @Override
    public void addView(View child, int width, int height) {
        checkIsContentView(child);
        super.addView(child, width, height);
    }

    @Override
    protected boolean addViewInLayout(View child, int index, ViewGroup.LayoutParams params) {
        checkIsContentView(child);
        return super.addViewInLayout(child, index, params);
    }

    @Override
    protected boolean addViewInLayout(View child, int index, ViewGroup.LayoutParams params, boolean preventRequestLayout) {
        checkIsContentView(child);
        return super.addViewInLayout(child, index, params, preventRequestLayout);
    }

    @Override
    public void showLoginView() {
        AnimationHelper.switchViewByAnim(useAnimation, viewAnimProvider, currentShowingView, loginView);
        currentShowingView = loginView;
    }

    @Override
    public void showLoadingView() {
        AnimationHelper.switchViewByAnim(useAnimation, viewAnimProvider, currentShowingView, loadingView);
        currentShowingView = loadingView;
    }

    @Override
    public void showTimeoutView() {
        AnimationHelper.switchViewByAnim(useAnimation, viewAnimProvider, currentShowingView, timeOutView);
        currentShowingView = timeOutView;
    }

    @Override
    public void showEmptyView() {
        AnimationHelper.switchViewByAnim(useAnimation, viewAnimProvider, currentShowingView, emptyView);
        currentShowingView = emptyView;
    }

    @Override
    public void showErrorView() {
        AnimationHelper.switchViewByAnim(useAnimation, viewAnimProvider, currentShowingView, errorView);
        currentShowingView = errorView;
    }

    @Override
    public void showNoNetworkView() {
        AnimationHelper.switchViewByAnim(useAnimation, viewAnimProvider, currentShowingView, notNetworkView);
        currentShowingView = notNetworkView;
    }

    @Override
    public void showContentView() {
        AnimationHelper.switchViewByAnim(useAnimation, viewAnimProvider, currentShowingView, contentView);
        currentShowingView = contentView;
    }

    @Override
    public void showCustomView(View view) {
        view.setLayoutParams(this.getLayoutParams());
        AnimationHelper.switchViewByAnim(useAnimation, viewAnimProvider, currentShowingView, view);
        currentShowingView = view;
    }

    public boolean isUseAnimation() {
        return useAnimation;
    }

    public void setUseAnimation(boolean useAnimation) {
        this.useAnimation = useAnimation;
    }

    public void setViewSwitchAnimProvider(ViewAnimProvider animProvider) {
        if (animProvider != null) {
            this.viewAnimProvider = animProvider;
        }
    }

    public ViewAnimProvider getViewAnimProvider() {
        return viewAnimProvider;
    }

    public View getEmptyView() {
        return emptyView;
    }

    public void setEmptyView(View emptyView) {
        this.emptyView = emptyView;
    }

    public View getErrorView() {
        return errorView;
    }

    public void setErrorView(View errorView) {
        this.errorView = errorView;
    }

    public View getLoadingView() {
        return loadingView;
    }

    public void setLoadingView(View loadingView) {
        this.loadingView = loadingView;
    }

    public View getTimeOutView() {
        return timeOutView;
    }

    public void setTimeOutView(View timeOutView) {
        this.timeOutView = timeOutView;
    }

    public View getNotNetworkView() {
        return notNetworkView;
    }

    public void setNotNetworkView(View notNetworkView) {
        this.notNetworkView = notNetworkView;
    }

    public View getLoginView() {
        return loginView;
    }

    public void setLoginView(View loginView) {
        this.loginView = loginView;
    }
}
