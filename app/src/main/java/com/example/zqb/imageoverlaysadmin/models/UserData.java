package com.example.zqb.imageoverlaysadmin.models;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.zqb.imageoverlaysadmin.MyApplication;
import com.example.zqb.imageoverlaysadmin.utils.NetHelper;
import com.example.zqb.imageoverlaysadmin.utils.NetResultListener;
import com.example.zqb.imageoverlaysadmin.utils.ToastHelper;
import com.example.zqb.imageoverlaysadmin.views.LoginActivity;

/**
 * Created by zqb on 2017/4/16.
 */

public class UserData {
    public static int id;
    public static String username;
    public static String sign;
    public static String sex;
    public static String headImage;
    public static String nickname;
    //清除登录信息
    public static void clear(final Context context)
    {
        id=0;
        username=null;
        headImage=null;
        sign=null;
        sex=null;
        nickname=null;
        NetUrl.cookie="";

        //清除登录信息
        SharedPreferences.Editor editor= MyApplication.prefs.edit();
        editor.remove("username");
        editor.remove("password");
        editor.apply();

        //清除服务器session
//        NetHelper netHelper=new NetHelper(context,NetUrl.sign_out);
//        netHelper.setParam("userId",UserData.id+"");
//        netHelper.setResultListener(new NetResultListener() {
//            @Override
//            public void getResult(NetResultData result) {
//                if(result.getCode()==1)
//                {
//                    ToastHelper.make(context,result.getMsg());
//                    //客户端注销
//                    UserData.clear(context);
//                }
//            }
//        });

        //返回登录界面
        Intent intent=new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

}
