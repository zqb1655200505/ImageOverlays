package com.example.zqb.imageoverlaysadmin.utils;

import android.content.Context;
import android.widget.Toast;

import com.example.zqb.imageoverlaysadmin.models.NetResultData;

/**
 * 网络请求回调监听类，当网络请求返回时回调该接口
 * Created by zqb on 2017/4/16.
 */

public abstract class NetResultListener {
    public abstract void getResult(NetResultData result);

    public void getError(Context context) {
        Toast.makeText(context, "网络错误", Toast.LENGTH_SHORT).show();
    }
}
