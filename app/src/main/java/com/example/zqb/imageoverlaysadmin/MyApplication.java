package com.example.zqb.imageoverlaysadmin;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.request.target.ViewTarget;

import cn.jpush.sms.SMSSDK;

/**
 * Created by zqb on 2017/4/16.
 */

public class MyApplication extends Application {
    public static RequestQueue netQueue;//程序中只需创建一个volley请求线程队列
    public static SharedPreferences prefs;
    @Override
    public void onCreate() {
        super.onCreate();

        //初始化极光推送短信服务
        SMSSDK.getInstance().initSdk(this);
        SMSSDK.getInstance().setIntervalTime(60*1000);//设置时间为60秒


        ViewTarget.setTagId(R.id.glide_tag);

        netQueue = Volley.newRequestQueue(this);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);


    }

}
