package com.example.zqb.imageoverlaysadmin.views;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.example.zqb.imageoverlaysadmin.R;
import com.example.zqb.imageoverlaysadmin.utils.SnackbarHelper;
import java.util.Timer;
import java.util.TimerTask;
import cn.jpush.sms.SMSSDK;
import cn.jpush.sms.listener.SmscheckListener;
import cn.jpush.sms.listener.SmscodeListener;

public class ReSetPasswordActivity extends AppCompatActivity {

    private EditText et_username;
    private EditText et_code;
    private Button btn_get_code;
    private Button btn_reset;
    private TimerTask timerTask;
    private Timer timer;
    private int timess;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_re_set_password);

        init();
        btn_get_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
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
                        SnackbarHelper.make(v, uuid);
                    }

                    @Override
                    public void getCodeFail(int errCode, final String errmsg) {
                        //失败后停止计时
                        stopTimer();
                        SnackbarHelper.make(v, errmsg);
                    }
                });
            }
        });

        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final String username=et_username.getText().toString();
                String code=et_code.getText().toString();
                if(username.isEmpty())
                {
                    SnackbarHelper.make(v,"手机号不能为空");
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

                    }

                    @Override
                    public void checkCodeFail(int errCode, final String errmsg) {
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        SnackbarHelper.make(v, errmsg);
                    }
                });
            }
        });
    }

    private void init()
    {
        et_username= (EditText) findViewById(R.id.phone);
        et_code= (EditText) findViewById(R.id.code);
        btn_get_code= (Button) findViewById(R.id.getCode);
        btn_reset= (Button) findViewById(R.id.conferReset);
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
