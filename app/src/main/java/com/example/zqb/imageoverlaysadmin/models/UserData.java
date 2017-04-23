package com.example.zqb.imageoverlaysadmin.models;

import android.content.Context;
import android.content.Intent;

import com.example.zqb.imageoverlaysadmin.views.LoginActivity;

/**
 * Created by zqb on 2017/4/16.
 */

public class UserData {
    public static int id;
    public static String username;
    public static String password;
    public static String head_pic;
    public static String signature;


    //清除登录信息
    public static void clear(Context context)
    {
        id=0;
        username=null;
        password=null;
        head_pic=null;
        signature=null;
        //返回登录界面
        Intent intent=new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }
}
