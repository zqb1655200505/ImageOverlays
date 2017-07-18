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
    public static final int image_overlay_result_request_code=4;
    //网络请求url
    private static final String baseUrl="http://182.254.137.240:8080/picture_cognition/basic/";
    public static final String login=baseUrl+"login";
    public static final String register=baseUrl+"register";
    public static final String sign_out=baseUrl+"signOut";
    public static final String update_signature=baseUrl+"updateSign";
    public static final String update_nickname=baseUrl+"updateNickname";
    public static final String update_sex=baseUrl+"updateSex";
    public static final String update_password=baseUrl+"updatePassword";
    public static final String batch_upload_pic=baseUrl+"upload";
    public static final String upload_person_pic=baseUrl+"";
    public static final String download_result=baseUrl+"";
    //全局变量
    public static List<String> imageUrls;
    public static String dir;


    public static String cookie="";
    public static final String unSet="未设置";


}
