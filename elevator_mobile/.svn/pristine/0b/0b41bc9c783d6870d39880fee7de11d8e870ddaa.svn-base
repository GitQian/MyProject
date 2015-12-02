package com.chinacnit.elevatorguard.mobile.http.task;

import java.io.Serializable;

public class Result<T> implements IResult<T>, Serializable {
    private static final long serialVersionUID = 3772151742762591327L;

    private boolean flag;
    private String err;
    private T ret;

    public Result(boolean flag, String err, T ret) {
        this.flag = flag;
        this.err = err;
        this.ret = ret;
    }

    public Result(boolean flag, String err) {
        this.flag = flag;
        this.err = err;
    }

    public Result() {
        this(true, "");
    }

    public Result(String err) {
        this(false, err);
    }

    public Result(T ret) {
        this(true, "", ret);
    }

    public String getErrorMsg() {
        return err;
    }

    public boolean isOK() {
        return flag;
    }

    public T getReturn() {
        return ret;
    }

    public void setRet(T ret) {
        this.ret = ret;
    }

    public void setError(String errMsg) {
        this.flag = false;
        this.err = errMsg;
    }
}

