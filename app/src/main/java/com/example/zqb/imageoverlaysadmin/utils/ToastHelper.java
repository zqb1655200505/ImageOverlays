package com.example.zqb.imageoverlaysadmin.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by zqb on 2017/4/30.
 */

public class ToastHelper {
    public static void make(Context context,String msg)
    {
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }
}
