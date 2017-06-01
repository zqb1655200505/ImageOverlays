package com.example.zqb.imageoverlaysadmin.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zqb.imageoverlaysadmin.MyApplication;
import com.example.zqb.imageoverlaysadmin.R;
import com.example.zqb.imageoverlaysadmin.models.NetResultData;
import com.example.zqb.imageoverlaysadmin.models.NetUrl;
import com.example.zqb.imageoverlaysadmin.utils.NetHelper;
import com.example.zqb.imageoverlaysadmin.utils.NetResultListener;
import com.example.zqb.imageoverlaysadmin.utils.SnackbarHelper;


public class RegisterActivity extends AppCompatActivity {

    private EditText et_username;
    private EditText et_password;
    private EditText et_confer_password;
    private EditText et_code;
    private Button btn_get_code;
    private Button btn_register;
    private TextView tv_to_login;

    private Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
//        getWindow().setFlags(flag, flag);
        setContentView(R.layout.activity_register);
        init();
        tv_to_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btn_get_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username=et_username.getText().toString();
                if(username.isEmpty())
                {
                    SnackbarHelper.make(v,"请先填写手机号");
                    return;
                }
                btn_get_code.setClickable(false);
                btn_get_code.setFocusable(false);
                btn_get_code.setTextColor(Color.GRAY);
                new Thread(new Runnable() {
                    int num=6;
                    @Override
                    public void run()
                    {
                        Message message;
                        while (num>0)
                        {
                            try {
                                Thread.sleep(1000);
                                message=new Message();
                                message.what=1;
                                message.arg1=--num;
                                handler.sendMessage(message);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                final String username=et_username.getText().toString();
                final String password=et_password.getText().toString();
                String confer_password=et_confer_password.getText().toString();
                String code=et_code.getText().toString();

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
                if(confer_password.isEmpty())
                {
                    SnackbarHelper.make(v,"请确认密码");
                    return;
                }
                if(!password.equals(confer_password))
                {
                    SnackbarHelper.make(v,"请保证密码一致");
                    return;
                }
                if(code.isEmpty())
                {
                    SnackbarHelper.make(v,"验证码不能为空");
                    return;
                }

                //在此处进行网络请求
                NetHelper netHelper=new NetHelper(RegisterActivity.this, NetUrl.register);
                netHelper.setParam("username",username);
                netHelper.setParam("password",password);
                netHelper.setParam("userType",1+"");
                netHelper.setResultListener(new NetResultListener() {
                    @Override
                    public void getResult(NetResultData result)
                    {
                        Toast.makeText(RegisterActivity.this,result.getMsg(),Toast.LENGTH_SHORT).show();
                        //注册成功，将注册信息返回登录界面
                        if(result.getCode()==1)
                        {
                            Intent mIntent = new Intent();
                            mIntent.putExtra("username",username);
                            mIntent.putExtra("password",password);
                            setResult(RESULT_OK,mIntent);
                            finish();
                        }
                        else
                        {
                            et_password.setText("");
                            et_confer_password.setText("");
                            et_username.setText("");
                            et_code.setText("");
                        }
                    }

                    @Override
                    public void getError(Context context) {
                        super.getError(context);
                    }
                });

                netHelper.doPost();
            }
        });

    }

    private void init()
    {
        et_code= (EditText) findViewById(R.id.code);
        et_confer_password= (EditText) findViewById(R.id.confer_password);
        et_password= (EditText) findViewById(R.id.password);
        et_username= (EditText) findViewById(R.id.username);
        tv_to_login= (TextView) findViewById(R.id.toLogin);
        btn_get_code= (Button) findViewById(R.id.getCode);
        btn_register= (Button) findViewById(R.id.register);
        handler=new Handler()
        {
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==1)
                {
                    if(msg.arg1>0)
                    {
                        btn_get_code.setText("("+msg.arg1+")秒");
                    }
                    else
                    {
                        btn_get_code.setText(R.string.getCode);
                        btn_get_code.setClickable(true);
                        btn_get_code.setFocusable(true);
                        //btn_get_code.setTextColor(ContextCompat.getColor(RegisterActivity.this, R.color.colorAccent));
                        btn_get_code.setTextColor(Color.WHITE);
                    }
                }

            }
        };
    }
}
