package com.example.zqb.imageoverlaysadmin.utils;

import android.content.Context;
import android.os.IBinder;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by zqb on 2017/5/2.
 */

public class AppHelper
{
    public static void hideSoftInput(View view, Context context)
    {
        IBinder token=view.getWindowToken();
        if (token != null) {
            InputMethodManager im = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token,
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
