package com.helper.widgets.brvah.entity;

public class MultipleItemEntity<T> implements IMultipleItem {

    private int itemType;
    private int spanSize;
    public T t;

    public MultipleItemEntity(int itemType, int spanSize) {
        this.itemType = itemType;
        this.spanSize = spanSize;
        this.t = null;
    }

    public MultipleItemEntity(int itemType, int spanSize, T t) {
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
