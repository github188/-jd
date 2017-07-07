package com.taotao.common.bean;

/**
 * Created by Ellen on 2017/7/7.
 */
public class HttpResult {
    private int code;
    private String data;

    public HttpResult(){

    }

    public HttpResult(int code, String data) {
        this.code = code;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "HttpResult{" +
                "code=" + code +
                ", data='" + data + '\'' +
                '}';
    }
}
