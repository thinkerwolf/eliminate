package com.thinkerwolf.eliminate.common;

import java.io.Serializable;

public class OpResultSuccess extends OpResult implements Serializable {

    private Object data;

    public Object getData() {
        return data;
    }
    public void setData(Object data) {
        this.data = data;
    }

}
