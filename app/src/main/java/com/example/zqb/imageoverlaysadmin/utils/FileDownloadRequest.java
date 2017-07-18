package com.example.zqb.imageoverlaysadmin.utils;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by zqb on 2017/7/18.
 */

public class FileDownloadRequest extends Request<Map<String,Object>> {


    private String fileName;
    private final Response.Listener<Map<String,Object>> mListener;

    public FileDownloadRequest(int method, String url, Response.Listener<Map<String,Object>> mListener, Response.ErrorListener listener) {
        super(method, url, listener);
        this.mListener = mListener;
    }


    @Override
    protected Response<Map<String,Object>> parseNetworkResponse(NetworkResponse response) {
        byte[] data = response.data;
        //获取下载文件名
        fileName=response.headers.get("Content-Disposition");
        System.out.println("fileName="+fileName);
        Map<String,Object> map=new HashMap<>();
        map.put("fileName",fileName);//文件名
        map.put("data",data);//文件内容
        return Response.success(map, HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    protected void deliverResponse(Map<String,Object> response) {
        if (mListener != null) {
            mListener.onResponse(response);
        }
    }
}
