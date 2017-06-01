package com.example.zqb.imageoverlaysadmin.utils;

import com.android.internal.http.multipart.Part;
import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by zqb on 2017/4/21.
     使用volley上传文件工具类
     文件上传（支持多文件和参数，均封装在parts中）
     //构造参数列表
         List<Part> partList = new ArrayList<Part>();
         partList.add(new StringPart("username", "hellfire"));
         partList.add(new StringPart("email", "ouyangjun@aliyun.com"));
         try {
            partList.add(new FilePart("photo", new File("/mnt/sdcard/Test/hellfire.jpg")));
         } catch (FileNotFoundException e) {
            e.printStackTrace();
         }
 */

public class MultipartRequest extends MyStringRequest {
    private Part[] parts;
    public MultipartRequest(String url, Part[] parts, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, url, listener, errorListener);
        this.parts = parts;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            Part.sendParts(baos, parts);
        } catch (IOException e) {
            VolleyLog.e(e, "error when sending parts to output!");
        }
        return baos.toByteArray();
    }

    @Override
    public String getBodyContentType() {
        return "multipart/form-data; boundary=" +Part.getBoundary();
    }
}
