package com.helper.widgets.brvah.entity;

import java.io.Serializable;

public abstract class SectionMultipleEntity<T> implements Serializable, IMultipleItem {

    public boolean isHeader;
    public T t;
    public String header;

    public SectionMultipleEntity(boolean isHeader, String header) {
        this.isHeader = isHeader;
        this.header = header;
        this.t = null;
    }

    public SectionMultipleEntity(T t) {
        this.isHeader = false;
        this.header = null;
        this.t = t;
    }
}
