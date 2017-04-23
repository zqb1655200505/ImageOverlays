package com.example.zqb.imageoverlaysadmin.models;


import java.util.List;

/**
 * Created by zqb on 2017/4/16.
 */

public class NetUrl {

    //状态码
    public static final int login_register_request_code= 1;
    public static final int main_personal_request_code= 2;
    public static final int personal_action_pick_request_code = 3;

    //网络请求url
    public static final String login="";
    public static final String upload_person_pic="";
    public static final String batch_upload_pic="";

    //全局变量
    public static List<String> imageUrls;
}
