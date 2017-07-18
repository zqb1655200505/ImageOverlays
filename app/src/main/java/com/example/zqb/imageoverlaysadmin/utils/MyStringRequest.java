package com.example.zqb.imageoverlaysadmin.utils;


import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.example.zqb.imageoverlaysadmin.models.NetUrl;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zqb on 2017/4/30.
 */

public class MyStringRequest extends StringRequest
{
    public String cookieFromResponse;
    private String mHeader;
    private Map<String, String> sendHeader=new HashMap<>(1);
    public MyStringRequest(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener)
    {
        super(method, url, listener, errorListener);
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response)
    {

        if(NetUrl.cookie.equals(""))
        {
            try {
                String jsonString =
                        new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                mHeader = response.headers.toString();
                //使用正则表达式从reponse的头中提取cookie内容的子串
                Pattern pattern=Pattern.compile("Set-Cookie.*?;");
                Matcher m=pattern.matcher(mHeader);
                if(m.find()){
                    cookieFromResponse =m.group();
                }
                JSONObject jsonObject = new JSONObject(jsonString);
                if(cookieFromResponse!=null&&cookieFromResponse.length()>0)
                {
                    //去掉cookie末尾的分号
                    cookieFromResponse = cookieFromResponse.substring(11,cookieFromResponse.length()-1);
                    //将cookie字符串添加到jsonObject中，该jsonObject会被deliverResponse递交，调用请求时则能在onResponse中得到
                    jsonObject.put("cookie",cookieFromResponse);
                }
                return Response.success(jsonObject.toString(),
                        HttpHeaderParser.parseCacheHeaders(response));
            } catch (UnsupportedEncodingException e) {
                return Response.error(new ParseError(e));
            } catch (JSONException je) {
                return Response.error(new ParseError(je));
            }
        }
        else
        {
            return super.parseNetworkResponse(response);
        }
    }
    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return sendHeader;
    }

    public void setSendCookie(String cookie){
        sendHeader.put("Cookie",cookie);
    }
}
