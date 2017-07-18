package com.example.zqb.imageoverlaysadmin.utils;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.zqb.imageoverlaysadmin.MyApplication;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zqb on 2017/7/18.
 */

public class FileDownloadHelper {
    private RequestQueue queue= MyApplication.netQueue;
    private NetResultListener netResultListener=null;

    private Context context;
    private String url;

    private String baseSavePath;
    private HashMap<String,String> map;

    public FileDownloadHelper(Context context,String url,String baseSavePath)
    {
        this.context=context;
        this.url=url;
        map=new HashMap<>();
        this.baseSavePath=baseSavePath;
    }

    public void setParam(String key,String value)
    {
        map.put(key,value);
    }

    public void doPost()
    {
        FileDownloadRequest fileDownloadRequest =new FileDownloadRequest(Request.Method.POST, "", new Response.Listener<Map<String,Object>>() {
            @Override
            public void onResponse(Map<String,Object> response) {
                InputStream input=new ByteArrayInputStream((byte[]) response.get("data"));
                String fileName=(String) response.get("fileName");
                FileOutputStream outputStream=null;
                try {
                    outputStream=new FileOutputStream(baseSavePath+ File.separator+fileName);
                    File dir=new File(baseSavePath);
                    //文件夹不存在时须要创建
                    if(fileName==null)
                    {
                        dir.mkdir();
                    }
                    //开始写文件
                    byte[] buffer = new byte[4 * 1024];
                    while(input.read(buffer) != -1){
                        outputStream.write(buffer);
                        outputStream.flush();
                    }

                    //关闭流
                    outputStream.close();
                    input.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return map;
            }
        };
    }
}
