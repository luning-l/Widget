package com.helper.widgets.brvah.entity;

public class MultipleItemImpl<T> implements MultiItem {

    private int itemType;
    private int spanSize;
    public T t;

    public MultipleItemImpl(int itemType, int spanSize) {
        this.itemType = itemType;
        this.spanSize = spanSize;
        this.t = null;
    }

    public MultipleItemImpl(int itemType, int spanSize, T t) {
        this.itemType = itemType;
        this.spanSize = spanSize;
        this.t = t;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    @Override
    public int getSpanSize() {
        return spanSize;
    }

}
