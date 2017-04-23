package com.example.zqb.imageoverlaysadmin.models;

import org.json.JSONArray;

/**
 * 网络请求返回数据体
 * Created by zqb on 2017/4/16.
 */

public class NetResultData {
    private int code;
    private String msg;
    private JSONArray data;
    public String getMsg() {
        return msg;
    }

    public int getCode() {
        return code;
    }

    public JSONArray getData() {
        return data;
    }


    public void setCode(int code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(JSONArray data) {
        this.data = data;
    }
}
