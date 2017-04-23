package com.example.zqb.imageoverlaysadmin.utils;

import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Created by zqb on 2017/4/16.
 */

public class SnackbarHelper {
    public static void make(View view,String msg)
    {
        final Snackbar snackbar=Snackbar.make(view,msg,Snackbar.LENGTH_SHORT);
        snackbar.setAction("知道了", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        }).show();
    }
}
