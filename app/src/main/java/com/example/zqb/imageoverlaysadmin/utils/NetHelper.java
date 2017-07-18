package com.example.zqb.imageoverlaysadmin.utils;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.zqb.imageoverlaysadmin.MyApplication;
import com.example.zqb.imageoverlaysadmin.models.NetResultData;
import com.example.zqb.imageoverlaysadmin.models.NetUrl;
import com.example.zqb.imageoverlaysadmin.models.UserData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zqb on 2017/4/16.
 */

public class NetHelper {
    private RequestQueue queue= MyApplication.netQueue;
    private NetResultListener netResultListener=null;
    private Context context;
    private String url;
    private HashMap<String,String> map;

    public NetHelper(Context context,String url)
    {
        this.context=context;
        this.url=url;
        map=new HashMap<>();
    }

    public void setParam(String key,String value)
    {
        map.put(key,value);
    }


    public void doPost()
    {
        final MyStringRequest request=new MyStringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                System.out.println(response);
                NetResultData result = new NetResultData();
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    if(jsonObject.optInt("code")==-1)
                    {
                        ToastHelper.make(context,"登录状态失效，请重新登录");
                        UserData.clear(context);
                    }
                    else
                    {
                        result.setCode(jsonObject.optInt("code"));
                        result.setMsg(jsonObject.optString("message"));
                        if(NetUrl.cookie.equals(""))
                        {
                            NetUrl.cookie=jsonObject.optString("cookie");
                            System.out.println(NetUrl.cookie);
                        }
                        result.setData(jsonObject.optJSONArray("data"));
                        netResultListener.getResult(result);
                    }
                } catch (JSONException e)
                {
                    e.printStackTrace();
                    Toast.makeText(context, "数据解析错误", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("error message: "+error.getMessage());
                netResultListener.getError(context);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return map;
            }
        };
        if(!NetUrl.cookie.equals(""))
        {
            request.setSendCookie(NetUrl.cookie);
        }
        queue.add(request);
    }

    public void setResultListener(NetResultListener listener)
    {
        this.netResultListener = listener;
    }
}
