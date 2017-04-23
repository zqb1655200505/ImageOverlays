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
import android.widget.TextView;

import com.android.internal.http.multipart.FilePart;
import com.android.internal.http.multipart.Part;
import com.android.internal.http.multipart.StringPart;
import com.example.zqb.imageoverlaysadmin.R;
import com.example.zqb.imageoverlaysadmin.models.NetUrl;
import com.example.zqb.imageoverlaysadmin.models.UserData;
import com.example.zqb.imageoverlaysadmin.utils.SnackbarHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class PersonalActivity extends AppCompatActivity {

    private ImageView iv_head_pic;
    private TextView tv_username;
    private TextView tv_signature;
    private Toolbar toolbar;
    private TextView tv_title;
    private TextView tv_confer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        initView();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);//设置隐藏原始标题
        tv_title= (TextView) findViewById(R.id.toolbar_title);
        tv_title.setText(R.string.personal);
        tv_confer.setVisibility(View.INVISIBLE);

        initEvent();
    }

    private void initView()
    {
        iv_head_pic= (ImageView) findViewById(R.id.head_pic);
        tv_username= (TextView) findViewById(R.id.username);
        tv_signature= (TextView) findViewById(R.id.signature);
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        tv_confer = (TextView) findViewById(R.id.toolbar_btn_confer);
    }

    private void initEvent()
    {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
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

        tv_username.setOnClickListener(new View.OnClickListener() {
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
                        dialog.dismiss();
                        SnackbarHelper.make(tv_username,til_nickname.getEditText().getText().toString()+"");
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
                View view = LayoutInflater.from(PersonalActivity.this).inflate(R.layout.dialog_update_nickname,null,false);
                builder.setView(view);
                final AlertDialog dialog = builder.create();

                Button tv_confer= (Button) view.findViewById(R.id.confer_update);
                tv_confer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                TextView dialog_title= (TextView) view.findViewById(R.id.dialog_title);
                dialog_title.setText("修改签名");
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
//                    FileUploadHelper fileUpload=new FileUploadHelper(PersonalActivity.this,NetUrl.upload_person_pic,partList);
//                    fileUpload.setResultListener(new NetResultListener() {
//                        @Override
//                        public void getResult(NetResultData result) {
//
//                        }
//                    });
//                    fileUpload.doUpload();
                    break;
                }

            }
        }
    }
}
