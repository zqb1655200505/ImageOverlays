package com.example.zqb.imageoverlaysadmin.views;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.zqb.imageoverlaysadmin.R;
import com.example.zqb.imageoverlaysadmin.models.ImageListData;
import com.example.zqb.imageoverlaysadmin.utils.ProgressDialogHelper;
import com.example.zqb.imageoverlaysadmin.utils.RecyclerViewAdapter;
import com.example.zqb.imageoverlaysadmin.utils.RecyclerViewHolder;
import com.example.zqb.imageoverlaysadmin.utils.SnackbarHelper;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ScanImageActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView tv_title;
    private RecyclerView rv_dir_list;
    private TextView tv_confer;
    private TextView tv_toolbar_nav;

    private ArrayList<ImageListData> mImageFloders = new ArrayList<>();
    //临时的辅助类，用于防止同一个文件夹的多次扫描
    private HashSet<String> mDirPaths = new HashSet<>();
    private Handler mHandler;

    private RecyclerViewAdapter<ImageListData> dir_list_adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_image);
        init();
        tv_toolbar_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_title.setText(R.string.app_name);
        tv_confer.setVisibility(View.GONE);
        // 显示进度条
        ProgressDialogHelper.show(ScanImageActivity.this, "正在扫描...");
        requestReadExternalPermission();

        rv_dir_list.setAdapter(dir_list_adapter = new RecyclerViewAdapter<ImageListData>(ScanImageActivity.this, mImageFloders, R.layout.image_dir_item) {

            @Override
            public void convert(RecyclerViewHolder holder, ImageListData data, int position) {
                holder.setImageByUrl(R.id.list_item_image, data.getFirstImagePath());
                holder.setText(R.id.id_dir_item_name, data.getFolderName());
                holder.setText(R.id.id_dir_item_count, data.getCount() + "");
            }
        });
        dir_list_adapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                Intent intent = new Intent(ScanImageActivity.this, ChoosePicActivity.class);
                intent.putExtra("dir", mImageFloders.get(position).getPicDir());
                startActivity(intent);

            }
        });

    }


    private void scanImage()
    {
        //新开一个线程进行图片扫描
        new Thread(new Runnable() {
            @Override
            public void run() {
                String firstImage = null;
                Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver mContentResolver = ScanImageActivity.this.getContentResolver();

                // 只查询jpeg和png的图片
                Cursor mCursor = mContentResolver.query(mImageUri, null,
                        MediaStore.Images.Media.MIME_TYPE + "=? or "
                                + MediaStore.Images.Media.MIME_TYPE + "=?",
                        new String[]{"image/jpeg", "image/png"},
                        MediaStore.Images.Media.DATE_MODIFIED);

                mCursor.moveToFirst();
                while (mCursor.moveToNext()) {
                    String path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    if (firstImage == null)
                        firstImage = path;
                    // 获取该图片的父路径名
                    File parentFile = new File(path).getParentFile();
                    if (parentFile == null)
                        continue;
                    String dirPath = parentFile.getAbsolutePath();
                    ImageListData imageInfo = null;

                    //扫描到新文件夹
                    if (!mDirPaths.contains(dirPath)) {
                        mDirPaths.add(dirPath);
                        imageInfo = new ImageListData();
                        imageInfo.setPicDir(dirPath);
                        imageInfo.setFirstImagePath(path);
                    } else {
                        continue;
                    }

                    int picSize = parentFile.list(new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String filename) {
                            if (filename.endsWith(".jpg") ||
                                    filename.endsWith(".png") ||
                                    filename.endsWith(".jpeg"))
                                return true;
                            return false;
                        }
                    }).length;
                    imageInfo.setCount(picSize);
                    mImageFloders.add(imageInfo);
                }
                mCursor.close();
                // 通知Handler扫描图片完成
                Message message = new Message();
                message.what = 1;
                mHandler.sendMessage(message);
            }
        }).start();
    }

    private void init() {
        tv_confer = (TextView) findViewById(R.id.toolbar_btn_confer);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tv_toolbar_nav= (TextView) findViewById(R.id.toolbar_nav);
        getSupportActionBar().setDisplayShowTitleEnabled(false);//设置隐藏原始标题
        tv_title = (TextView) findViewById(R.id.toolbar_title);

        rv_dir_list = (RecyclerView) findViewById(R.id.dir_list);
        rv_dir_list.setLayoutManager(new LinearLayoutManager(ScanImageActivity.this));
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                ProgressDialogHelper.dismiss();
                dir_list_adapter.notifyDataSetChanged();
            }
        };
    }


    /*
    *   android7.0 之后需要动态申请读写权限
     */
    @SuppressLint("NewApi")
    private void requestReadExternalPermission() {
        if (ContextCompat.checkSelfPermission(ScanImageActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            Log.d("TAG", "READ permission IS NOT granted...");
            if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE))
            {
                Log.d("TAG", "11111111111111");
            }
            else
            {
                // 0 是自己定义的请求code
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            }
        }
        else
        {
            scanImage();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 0:
            {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    scanImage();
                }
                else
                {
                    SnackbarHelper.make(findViewById(R.id.toolbar),"未赋予权限");
                }
                break;
            }
            default:
                break;

        }
    }
}
