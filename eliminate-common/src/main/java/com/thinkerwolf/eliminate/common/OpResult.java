package com.thinkerwolf.eliminate.common;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 操作结果
 *
 * @author wukai
 */
public class OpResult implements Serializable {

    private Integer status;

    private int requestId;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    @JsonIgnore
    public boolean isOk() {
        return this.status != OpState.FAIL.getId() && this.status != OpState.EXCEPTION.getId();
    }

    public static OpResult build(OpState state, Object data, String msg) {
        OpResultFull op = new OpResultFull();
        op.setStatus(state.getId());
        op.setMsg(msg);
        op.setData(data);
        return op;
    }

    public static OpResult ok(Object data) {
        OpResultSuccess op = new OpResultSuccess();
        op.setStatus(OpState.SUCCESS.getId());
        op.setData(data);
        return op;
    }

    public static OpResult ok() {
        OpResultSuccess op = new OpResultSuccess();
        op.setStatus(OpState.SUCCESS.getId());
        op.setData("");
        return op;
    }

    public static OpResult fail(String msg) {
        OpResultFail op = new OpResultFail();
        op.setStatus(OpState.FAIL.getId());
        op.setMsg(msg);
        return op;
    }

    public static OpResult push(PushCommand pushCommand, Object data) {
        OpResultPush op = new OpResultPush();
        op.setStatus(OpState.PUSH.getId());
        op.setRequestId(0);
        op.setCmd(pushCommand.getCommand());
        Map<String, Object> map = new HashMap<>(1, 1);
        map.put(pushCommand.getModule(), data);
        op.setData(map);
        return op;
    }

    public static OpResult push(PushCommand pushCommand, String sessionId, Object data) {
        return push(pushCommand.getCommand(), pushCommand.getModule(), sessionId, data);
    }

    public static OpResult push(String cmd, String module, String sessionId, Object data) {
        OpResultPush op = new OpResultPush();
        op.setStatus(OpState.PUSH.getId());
        op.setRequestId(0);
        op.setCmd(cmd);
        op.setSessionId(sessionId);
        Map<String, Object> map = new HashMap<>(1, 1);
        map.put(module, data);
        op.setData(map);
        return op;
    }
}
