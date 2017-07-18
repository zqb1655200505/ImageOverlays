package com.example.zqb.imageoverlaysadmin.utils;

import android.content.Context;

import com.android.internal.http.multipart.Part;
import com.android.volley.RequestQueue;
import com.example.zqb.imageoverlaysadmin.MyApplication;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zqb on 2017/7/18.
 *      可将所有网络请求的帮助类抽象出来
 */

public abstract class BaseNetHelper {
    public RequestQueue queue= MyApplication.netQueue;
    public NetResultListener netResultListener=null;

    public Context context;
    public String url;
//    public HashMap<String,String> map;
//    public List<Part> partList;
    public Object params;
//    public void setParam(String key,String value)
//    {
//        map.put(key,value);
//    }

    public abstract void doPost();
}
