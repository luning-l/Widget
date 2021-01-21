package com.helper.widgets.brvah.entity;

import java.io.Serializable;

public abstract class SectionMulti<T> implements Serializable, MultiItem {

    public boolean isHeader;
    public T t;
    public String header;

    public SectionMulti(boolean isHeader, String header) {
        this.isHeader = isHeader;
        this.header = header;
        this.t = null;
    }

    public SectionMulti(T t) {
        this.isHeader = false;
        this.header = null;
        this.t = t;
    }
}
