package com.helper.widgets.banner.indicator;

import android.view.View;

import androidx.annotation.NonNull;

import com.helper.widgets.banner.config.IndicatorConfig;
import com.helper.widgets.banner.listener.OnPageChangeListener;

public interface Indicator extends OnPageChangeListener {
    @NonNull
    View getIndicatorView();

    IndicatorConfig getIndicatorConfig();

    void onPageChanged(int count, int currentPosition);

}
