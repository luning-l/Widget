package com.helper.widgets.brvah.decoration.sticky;

import android.view.View;

import com.helper.widgets.brvah.BaseSectionQuickAdapter;

public class SectionStickyView implements StickyView {

    @Override
    public boolean isStickyView(View view) {
        return (Boolean) view.getTag();
    }

    @Override
    public int getStickViewType() {
        return BaseSectionQuickAdapter.SECTION_HEADER_VIEW;
    }
}
