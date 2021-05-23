package com.thinkerwolf.eliminate.common;

import java.io.Serializable;

public class OpResultFull extends OpResult implements Serializable {

    private Object data;
    private String msg;

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
