package com.helper.widgets.statelayout;

import android.view.View;

public interface IShowView {
    void showLoginView();

    void showLoadingView();

    void showTimeoutView();

    void showEmptyView();

    void showErrorView();

    void showNoNetworkView();

    void showContentView();

    void showCustomView(View view);
}
