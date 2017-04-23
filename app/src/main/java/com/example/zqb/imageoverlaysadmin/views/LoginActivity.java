package com.example.zqb.imageoverlaysadmin.views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zqb.imageoverlaysadmin.MyApplication;
import com.example.zqb.imageoverlaysadmin.R;
import com.example.zqb.imageoverlaysadmin.models.NetResultData;
import com.example.zqb.imageoverlaysadmin.models.UserData;
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
    private CheckBox cb_remember;
    private Button btn_login;
    private TextView tv_remember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if(MyApplication.prefs.contains("username"))
        {
            et_username.setText(MyApplication.prefs.getString("username",null));
            et_password.setText(MyApplication.prefs.getString("username",null));
        }

        initViews();
        tv_remember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb_remember.isChecked())
                {
                    cb_remember.setChecked(false);
                }
                else
                {
                    cb_remember.setChecked(true);
                }
            }
        });
        tv_to_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                //startActivity(intent);
                //finish();
                startActivityForResult(intent, NetUrl.login_register_request_code);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username=et_username.getText().toString();
                String password=et_password.getText().toString();
                if(username.isEmpty())
                {
                    SnackbarHelper.make(v,"用户名不能为空");
                    return;
                }
                if(password.isEmpty())
                {
                    SnackbarHelper.make(v,"密码不能为空");
                    return;
                }

//                NetHelper netHelper=new NetHelper(LoginActivity.this,NetUrl.login);
//                netHelper.setParam("username",username);
//                netHelper.setParam("password",password);
//                ProgressDialogHelper.show(LoginActivity.this);
//                netHelper.setResultListener(new NetResultListener() {
//                    @Override
//                    public void getResult(NetResultData result) {
//                        ProgressDialogHelper.dismiss();
//                        //具体操作
//                        //登录成功
//                        if(result.getCode()==200)
//                        {
//                            JSONArray jsonArray=result.getData();
//                            JSONObject jsonObject=jsonArray.optJSONObject(0);
//                            SharedPreferences.Editor editor=MyApplication.prefs.edit();
//                            if(cb_remember.isChecked())
//                            {
//                                editor.putString("username",jsonObject.optString("username"));
//                                editor.putString("password",jsonObject.optString("password"));
//                            }
//                            else
//                            {
//                                editor.remove("username");
//                                editor.remove("password");
//                            }

//
//                            //客户端cookie
//                            UserData.username=jsonObject.optString("username");
//                            UserData.password=jsonObject.optString("password");
//
                            //保存登录状态，默认保存时间为半个小时
                            //editor.putString("login_time",new Date().getTime()+"");
                            //editor.apply();
                            Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                            startActivity(intent);
                            finish();//将登录界面销毁
//                        }
//
//                    }
//
//                    @Override
//                    public void getError(Context context) {
//                        super.getError(context);
//                        ProgressDialogHelper.dismiss();
//                    }
//                });


            }
        });

    }

    private void initViews()
    {
        tv_to_register= (TextView) findViewById(R.id.toRegister);
        et_username= (EditText) findViewById(R.id.username);
        et_password= (EditText) findViewById(R.id.password);
        cb_remember= (CheckBox) findViewById(R.id.remember);
        btn_login= (Button) findViewById(R.id.login);
        tv_remember= (TextView) findViewById(R.id.tv_remember);
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
