package com.example.zqb.imageoverlaysadmin.views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zqb.imageoverlaysadmin.MyApplication;
import com.example.zqb.imageoverlaysadmin.R;
import com.example.zqb.imageoverlaysadmin.models.NetResultData;
import com.example.zqb.imageoverlaysadmin.models.UserData;
import com.example.zqb.imageoverlaysadmin.utils.AppHelper;
import com.example.zqb.imageoverlaysadmin.utils.NetHelper;
import com.example.zqb.imageoverlaysadmin.utils.NetResultListener;
import com.example.zqb.imageoverlaysadmin.models.NetUrl;
import com.example.zqb.imageoverlaysadmin.utils.ProgressDialogHelper;
import com.example.zqb.imageoverlaysadmin.utils.SnackbarHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Date;

public class LoginActivity extends AppCompatActivity {

    private TextView tv_to_register;
    private EditText et_username;
    private EditText et_password;
    private Button btn_login;

    private String username;
    private String password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
//        getWindow().setFlags(flag, flag);
        setContentView(R.layout.activity_login);
        initViews();

        if(!NetUrl.cookie.equals(""))
        {
            Intent intent=new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
            finish();//将登录界面销毁
            return;
        }

        if(MyApplication.prefs.contains("username"))
        {
            username=MyApplication.prefs.getString("username",null);
            password=MyApplication.prefs.getString("password",null);
            if(username!=null)
            {
                et_username.setText(username);
                et_password.setText(password);
            }
            doLogin();
            return;
        }



        tv_to_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivityForResult(intent, NetUrl.login_register_request_code);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username=et_username.getText().toString();
                password=et_password.getText().toString();
                AppHelper.hideSoftInput(v,LoginActivity.this);
                doLogin();
            }
        });
    }

    private void doLogin()
    {
        if(username.isEmpty())
        {
            SnackbarHelper.make(btn_login,"用户名不能为空");
            return;
        }
        if(password.isEmpty())
        {
            SnackbarHelper.make(btn_login,"密码不能为空");
            return;
        }
        ProgressDialogHelper.show(LoginActivity.this,"登录中,请稍后...");
        NetHelper netHelper=new NetHelper(LoginActivity.this,NetUrl.login);
        netHelper.setParam("username",username);
        netHelper.setParam("password",password);
        netHelper.setResultListener(new NetResultListener() {
            @Override
            public void getResult(NetResultData result) {
                ProgressDialogHelper.dismiss();

                //登录成功
                if(result.getCode()==1)
                {
                    JSONArray jsonArray=result.getData();
                    JSONObject jsonObject=jsonArray.optJSONObject(0);
                    //客户端cookie
                    UserData.username=jsonObject.optString("username");
                    UserData.id=jsonObject.optInt("id");
                    UserData.headImage=jsonObject.optString("headImage");
                    UserData.sex=jsonObject.optString("sex");
                    UserData.nickname=jsonObject.optString("nickname");
                    UserData.sign=jsonObject.optString("sign");

                    SharedPreferences.Editor editor=MyApplication.prefs.edit();
                    editor.putString("username",UserData.username);
                    editor.putString("password",jsonObject.optString("password"));
                    editor.apply();

                    Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();//将登录界面销毁
                }

                if(result.getCode()==0)
                {
                    SnackbarHelper.make(tv_to_register,result.getMsg());
                    et_username.setText("");
                    et_password.setText("");
                }
            }

            @Override
            public void getError(Context context) {
                super.getError(context);
                ProgressDialogHelper.dismiss();
            }
        });
        netHelper.doPost();
    }
    private void initViews()
    {
        tv_to_register= (TextView) findViewById(R.id.toRegister);
        et_username= (EditText) findViewById(R.id.username);
        et_password= (EditText) findViewById(R.id.password);
        btn_login= (Button) findViewById(R.id.login);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //从注册页面返回，把信息自动填入
        if(resultCode == RESULT_OK)
        {
            if(requestCode==NetUrl.login_register_request_code)
            {
                et_username.setText(data.getStringExtra("username"));
                et_password.setText(data.getStringExtra("password"));
            }
        }
    }


}
