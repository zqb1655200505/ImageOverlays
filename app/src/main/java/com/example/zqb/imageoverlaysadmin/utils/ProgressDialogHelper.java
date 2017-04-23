package com.example.zqb.imageoverlaysadmin.utils;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * 耗时加载时展示以禁止用户操作
 * Created by zqb on 2017/4/16.
 */

public class ProgressDialogHelper {
    private static ProgressDialog mProgressDialog=null;

    public static void show(Context context,String msg)
    {
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setMessage(msg);
        mProgressDialog.show();
    }
    public static void dismiss()
    {
        if(mProgressDialog!=null)
            mProgressDialog.dismiss();
    }
}
