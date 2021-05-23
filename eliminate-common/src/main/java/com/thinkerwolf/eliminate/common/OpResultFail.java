package com.thinkerwolf.eliminate.common;

import java.io.Serializable;

public class OpResultFail extends OpResult implements Serializable {

    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
