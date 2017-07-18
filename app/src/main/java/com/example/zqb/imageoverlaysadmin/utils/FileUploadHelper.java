package com.example.zqb.imageoverlaysadmin.utils;

import android.content.Context;

import com.android.internal.http.multipart.Part;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.zqb.imageoverlaysadmin.MyApplication;
import com.example.zqb.imageoverlaysadmin.models.NetResultData;
import com.example.zqb.imageoverlaysadmin.models.NetUrl;
import com.example.zqb.imageoverlaysadmin.models.UserData;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;

/**
 * Created by zqb on 2017/4/21.
 */

public class FileUploadHelper {
    private RequestQueue queue= MyApplication.netQueue;
    private NetResultListener netResultListener=null;

    private Context context;
    private String url;
    List<Part> partList;
    public FileUploadHelper(Context context, String url, List<Part> partList)
    {
        this.context=context;
        this.url=url;
        this.partList=partList;
    }

    public void setResultListener(NetResultListener listener)
    {
        this.netResultListener = listener;
    }

    public void doPost()
    {
        MultipartRequest profileUpdateRequest = new MultipartRequest(url,
                partList.toArray(new Part[partList.size()]),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        NetResultData result = new NetResultData();
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            if(result.getCode()==-1)
                            {
                                ToastHelper.make(context,"登录状态失效，请重新登录");
                                UserData.clear(context);
                            }
                            else
                            {
                                result.setCode(jsonObject.optInt("code"));
                                result.setMsg(jsonObject.optString("message"))   ;
                                result.setData(jsonObject.optJSONArray("data"));
                                netResultListener.getResult(result);
                            }

                        } catch (JSONException e)
                        {
                            e.printStackTrace();
                            ToastHelper.make(context, "数据解析错误");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                netResultListener.getError(context);
            }
        });
        if(!NetUrl.cookie.equals(""))
        {
            profileUpdateRequest.setSendCookie(NetUrl.cookie);
        }
        queue.add(profileUpdateRequest);
    }
}
