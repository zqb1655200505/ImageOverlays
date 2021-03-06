package com.example.zqb.imageoverlaysadmin.views;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import cn.jpush.sms.SMSSDK;
import cn.jpush.sms.listener.SmscheckListener;
import cn.jpush.sms.listener.SmscodeListener;

import com.example.zqb.imageoverlaysadmin.MyApplication;
import com.example.zqb.imageoverlaysadmin.R;
import com.example.zqb.imageoverlaysadmin.models.NetResultData;
import com.example.zqb.imageoverlaysadmin.models.NetUrl;
import com.example.zqb.imageoverlaysadmin.utils.NetHelper;
import com.example.zqb.imageoverlaysadmin.utils.NetResultListener;
import com.example.zqb.imageoverlaysadmin.utils.SnackbarHelper;

import java.util.Timer;
import java.util.TimerTask;


public class RegisterActivity extends AppCompatActivity {

    private EditText et_username;
    private EditText et_password;
    private EditText et_confer_password;
    private EditText et_code;
    private Button btn_get_code;
    private Button btn_register;
    private TextView tv_to_login;
    private TimerTask timerTask;
    private Timer timer;
    private int timess;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

                startTimer();
                SMSSDK.getInstance().getSmsCodeAsyn(username, 1 + "", new SmscodeListener() {
                    @Override
                    public void getCodeSuccess(final String uuid) {
                        Toast.makeText(RegisterActivity.this, uuid, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void getCodeFail(int errCode, final String errmsg) {
                        //失败后停止计时
                        stopTimer();
                        Toast.makeText(RegisterActivity.this, errmsg, Toast.LENGTH_SHORT).show();
                    }
                });
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

                //开始验证
                progressDialog.setTitle("正在验证...");
                progressDialog.show();
                SMSSDK.getInstance().checkSmsCodeAsyn(username, code, new SmscheckListener() {
                    @Override
                    public void checkCodeSuccess(final String code) {
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        //开始注册
                        NetHelper netHelper = new NetHelper(RegisterActivity.this, NetUrl.register);
                        netHelper.setParam("username", username);
                        netHelper.setParam("password", password);
                        netHelper.setParam("userType", 0 + "");
                        netHelper.setResultListener(new NetResultListener() {
                            @Override
                            public void getResult(NetResultData result) {
                                Toast.makeText(RegisterActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                                //注册成功，将注册信息返回登录界面
                                if (result.getCode() == 1) {
                                    Intent mIntent = new Intent();
                                    mIntent.putExtra("username", username);
                                    mIntent.putExtra("password", password);
                                    setResult(RESULT_OK, mIntent);
                                    finish();
                                } else {
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

                    @Override
                    public void checkCodeFail(int errCode, final String errmsg) {
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        Toast.makeText(RegisterActivity.this, errmsg, Toast.LENGTH_SHORT).show();
                    }
                });
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
        progressDialog = new ProgressDialog(this);
    }

    private void startTimer() {
        timess = (int) (SMSSDK.getInstance().getIntervalTime() / 1000);
        btn_get_code.setText(timess + " s");
        if (timerTask == null) {
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            timess--;
                            if (timess <= 0) {
                                stopTimer();
                                return;
                            }
                            btn_get_code.setText(timess + " s");
                        }
                    });
                }
            };
        }
        if (timer == null) {
            timer = new Timer();
        }
        timer.schedule(timerTask, 1000, 1000);
    }

    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
        btn_get_code.setText("重新获取");
        btn_get_code.setTextColor(Color.WHITE);
        btn_get_code.setClickable(true);
    }
}
