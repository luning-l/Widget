package com.helper.widgets.statelayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.helper.widgets.R;

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
    public void showLoginView() {

    }

    @Override
    public void showLoadingView() {

    }

    @Override
    public void showTimeoutView() {

    }

    @Override
    public void showEmptyView() {

    }

    @Override
    public void showErrorView() {

    }

    @Override
    public void showNoNetworkView() {

    }

    @Override
    public void showContentView() {

    }

    @Override
    public void showCustomView(View view) {

    }
}
