package com.example.zqb.imageoverlaysadmin.views;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.internal.http.multipart.FilePart;
import com.android.internal.http.multipart.Part;
import com.android.internal.http.multipart.StringPart;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.zqb.imageoverlaysadmin.R;
import com.example.zqb.imageoverlaysadmin.models.NetResultData;
import com.example.zqb.imageoverlaysadmin.models.NetUrl;
import com.example.zqb.imageoverlaysadmin.models.UserData;
import com.example.zqb.imageoverlaysadmin.utils.AppHelper;
import com.example.zqb.imageoverlaysadmin.utils.FileUploadHelper;
import com.example.zqb.imageoverlaysadmin.utils.NetHelper;
import com.example.zqb.imageoverlaysadmin.utils.NetResultListener;
import com.example.zqb.imageoverlaysadmin.utils.SnackbarHelper;
import com.example.zqb.imageoverlaysadmin.utils.ToastHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class PersonalActivity extends AppCompatActivity {

    private ImageView iv_head_pic;
    private TextView tv_nickname;
    private TextView tv_signature;

    private TextView tv_username;
    private TextView tv_sex;
    private LinearLayout ll_sex;
    private TextView tv_update_password;
    private Toolbar toolbar;
    private TextView tv_title;
    private TextView tv_confer;
    private TextView tv_toolbar_nav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        initView();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);//设置隐藏原始标题
        tv_title= (TextView) findViewById(R.id.toolbar_title);
        tv_title.setText(R.string.personal);
        tv_confer.setVisibility(View.GONE);

        if(UserData.sex!=null)
        {
            tv_sex.setText(UserData.sex);
        }
        if(UserData.username!=null)
        {
            tv_username.setText(UserData.username);
        }
        if(UserData.headImage!=null)
        {
            Glide.with(PersonalActivity.this)
                    .load(UserData.headImage)
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .centerCrop()
                    .into(iv_head_pic);
        }
        if(UserData.sign!=null)
        {
            tv_signature.setText(UserData.sign);
        }
        if(UserData.nickname!=null)
        {
            tv_nickname.setText(UserData.nickname);
        }
        initEvent();
    }

    private void initView()
    {
        iv_head_pic= (ImageView) findViewById(R.id.head_pic);
        tv_nickname= (TextView) findViewById(R.id.nickname);
        tv_signature= (TextView) findViewById(R.id.signature);
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        tv_confer = (TextView) findViewById(R.id.toolbar_btn_confer);
        tv_toolbar_nav= (TextView) findViewById(R.id.toolbar_nav);
        tv_sex= (TextView) findViewById(R.id.sex);
        ll_sex= (LinearLayout) findViewById(R.id.ll_sex);
        tv_update_password= (TextView) findViewById(R.id.update_password);
        tv_username= (TextView) findViewById(R.id.username);
        tv_update_password= (TextView) findViewById(R.id.update_password);
    }

    private void initEvent()
    {
        tv_toolbar_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        iv_head_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(intent, NetUrl.personal_action_pick_request_code);
            }
        });

        tv_nickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PersonalActivity.this);
                View view = LayoutInflater.from(PersonalActivity.this).inflate(R.layout.dialog_update_nickname,null,false);
                builder.setView(view);
                final AlertDialog dialog = builder.create();

                final TextInputLayout til_nickname= (TextInputLayout) view.findViewById(R.id.til_nickname);
                Button tv_confer= (Button) view.findViewById(R.id.confer_update);
                tv_confer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String nickname=til_nickname.getEditText().getText().toString();
                        NetHelper netHelper=new NetHelper(PersonalActivity.this,NetUrl.update_nickname);
                        //netHelper.setParam("userId",UserData.id+"");
                        netHelper.setParam("nickname",nickname);
                        netHelper.setResultListener(new NetResultListener() {
                            @Override
                            public void getResult(NetResultData result) {
                                if(result.getCode()==1)
                                {
                                    tv_nickname.setText(nickname);
                                    UserData.username=nickname;
                                    setResult(RESULT_OK);
                                }
                                SnackbarHelper.make(toolbar,result.getMsg());
                            }
                        });
                        netHelper.doPost();
                        dialog.dismiss();
                    }
                });
                TextView dialog_title= (TextView) view.findViewById(R.id.dialog_title);
                dialog_title.setText("修改昵称");
                dialog.setCancelable(true);
                dialog.show();
            }
        });
        tv_signature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PersonalActivity.this);
                View view = LayoutInflater.from(PersonalActivity.this).inflate(R.layout.dialog_update_signature,null,false);
                builder.setView(view);
                final AlertDialog dialog = builder.create();
                final TextInputLayout til_signature= (TextInputLayout) view.findViewById(R.id.til_signature);
                Button tv_confer= (Button) view.findViewById(R.id.confer_update);
                tv_confer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String signature=til_signature.getEditText().getText().toString();
                        NetHelper netHelper=new NetHelper(PersonalActivity.this,NetUrl.update_signature);
                        netHelper.setParam("sign",signature);
                        netHelper.setResultListener(new NetResultListener() {
                            @Override
                            public void getResult(NetResultData result) {
                                if(result.getCode()==1)
                                {
                                    UserData.sign=signature;
                                    tv_signature.setText(signature);
                                    setResult(RESULT_OK);
                                }
                                SnackbarHelper.make(toolbar,result.getMsg());
                            }
                        });
                        netHelper.doPost();
                        dialog.dismiss();
                    }
                });
                TextView dialog_title= (TextView) view.findViewById(R.id.dialog_title);
                dialog_title.setText("修改签名");
                dialog.setCancelable(true);
                dialog.show();
            }
        });

        ll_sex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PersonalActivity.this);
                View view = LayoutInflater.from(PersonalActivity.this).inflate(R.layout.dialog_update_sex,null,false);
                builder.setView(view);
                final AlertDialog dialog = builder.create();
                final Spinner sex_spinner= (Spinner) view.findViewById(R.id.sex_spinner);
                sex_spinner.setSelection(1);
                Button tv_confer= (Button) view.findViewById(R.id.confer_update);

                tv_confer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String sex=(String)(sex_spinner.getSelectedItem());
                        NetHelper netHelper=new NetHelper(PersonalActivity.this,NetUrl.update_sex);
                        netHelper.setParam("sex",sex);
                        netHelper.setResultListener(new NetResultListener() {
                            @Override
                            public void getResult(NetResultData result) {
                                if(result.getCode()==1)
                                {
                                    UserData.sex=sex;
                                    tv_sex.setText(sex);
                                    setResult(RESULT_OK);
                                }
                                SnackbarHelper.make(toolbar,result.getMsg());
                            }
                        });
                        netHelper.doPost();
                        dialog.dismiss();
                    }
                });

                TextView dialog_title= (TextView) view.findViewById(R.id.dialog_title);
                dialog_title.setText("修改性别");
                dialog.setCancelable(true);
                dialog.show();
            }
        });

        tv_update_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PersonalActivity.this);
                View view = LayoutInflater.from(PersonalActivity.this).inflate(R.layout.dialog_update_password,null,false);
                builder.setView(view);
                final AlertDialog dialog = builder.create();
                final TextInputLayout til_old_password= (TextInputLayout) view.findViewById(R.id.til_old_password);
                final TextInputLayout til_new_password= (TextInputLayout) view.findViewById(R.id.til_new_password);
                final TextInputLayout til_confer_password= (TextInputLayout) view.findViewById(R.id.til_confer_password);
                Button tv_confer= (Button) view.findViewById(R.id.confer_update);
                tv_confer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String old_password=til_old_password.getEditText().getText().toString();
                        String new_password=til_new_password.getEditText().getText().toString();
                        String confer_password=til_confer_password.getEditText().getText().toString();
                        if(old_password.equals(""))
                        {
                            AppHelper.hideSoftInput(v,PersonalActivity.this);
                            ToastHelper.make(PersonalActivity.this,"请填写原密码");
                            return;
                        }
                        if(new_password.equals(""))
                        {
                            AppHelper.hideSoftInput(v,PersonalActivity.this);
                            ToastHelper.make(PersonalActivity.this,"请填写新密码");
                            return;
                        }
                        if(confer_password.equals(""))
                        {
                            ToastHelper.make(PersonalActivity.this,"请确认新密码");
                            return;
                        }
                        if(!new_password.equals(confer_password))
                        {
                            AppHelper.hideSoftInput(v,PersonalActivity.this);
                            ToastHelper.make(PersonalActivity.this,"新密码前后不一致");
                            return;
                        }
                        NetHelper netHelper=new NetHelper(PersonalActivity.this,NetUrl.update_password);
                        netHelper.setParam("oldPassword",old_password);
                        netHelper.setParam("newPassword",new_password);
                        netHelper.setResultListener(new NetResultListener() {
                            @Override
                            public void getResult(NetResultData result) {
                                SnackbarHelper.make(toolbar,result.getMsg());
                            }
                        });
                        netHelper.doPost();
                        dialog.dismiss();
                    }
                });

                TextView dialog_title= (TextView) view.findViewById(R.id.dialog_title);
                dialog_title.setText("修改密码");
                dialog.setCancelable(true);
                dialog.show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK)
        {
            switch (requestCode)
            {
                case NetUrl.personal_action_pick_request_code:
                {
                    //调用系统相册获取图片
                    Uri selectImage=data.getData();
                    String[] filePathColumn= {MediaStore.Images.Media.DATA};//图片选取范围
                    Cursor cursor=getContentResolver().query(selectImage,filePathColumn,
                            null,null,null);
                    cursor.moveToFirst();
                    int columnIndex=cursor.getColumnIndex(filePathColumn[0]);
                    final String picPath=cursor.getString(columnIndex);
                    cursor.close();
                    Bitmap bitmap= BitmapFactory.decodeFile(picPath);
                    iv_head_pic.setImageBitmap(bitmap);
                    setResult(RESULT_OK);
                    //开始上传图片
                    List<Part> partList = new ArrayList<>();
                    partList.add(new StringPart("userId", UserData.id+""));
                    partList.add(new StringPart("username", UserData.username+""));
                    try {
                        partList.add(new FilePart("uploadFile", new File(picPath)));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    FileUploadHelper fileUpload=new FileUploadHelper(PersonalActivity.this,NetUrl.upload_person_pic,partList);
                    fileUpload.setResultListener(new NetResultListener() {
                        @Override
                        public void getResult(NetResultData result) {
                            SnackbarHelper.make(toolbar,result.getMsg());
                        }
                    });
                    fileUpload.doUpload();
                    break;
                }

            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
