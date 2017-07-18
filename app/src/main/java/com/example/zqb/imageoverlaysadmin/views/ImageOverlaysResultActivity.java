package com.example.zqb.imageoverlaysadmin.views;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;
import com.beloo.widget.chipslayoutmanager.gravity.IChildGravityResolver;
import com.example.zqb.imageoverlaysadmin.R;
import com.example.zqb.imageoverlaysadmin.models.NetUrl;
import com.example.zqb.imageoverlaysadmin.utils.FileHelper;
import com.example.zqb.imageoverlaysadmin.utils.RecyclerViewAdapter;
import com.example.zqb.imageoverlaysadmin.utils.RecyclerViewHolder;
import com.example.zqb.imageoverlaysadmin.utils.SnackbarHelper;

import net.rdrei.android.dirchooser.DirectoryChooserConfig;
import net.rdrei.android.dirchooser.DirectoryChooserFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImageOverlaysResultActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView mRecyclerView;
    private TextView tv_toolbar_confer;
    private TextView tv_toolbar_nav;
    private int width;
    private String initPath;
    private DirectoryChooserFragment mDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_overlays_result);
        init();
        initPath = Environment.getExternalStorageDirectory()+ File.separator+"imageOverlayResult";
        tv_toolbar_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mRecyclerView.setAdapter(new RecyclerViewAdapter<String>(ImageOverlaysResultActivity.this, NetUrl.imageUrls,R.layout.gird_item_result) {
            @Override
            public void convert(RecyclerViewHolder holder, String data, int position)
            {
                //按宽度等比例缩放，不然会OOM
                int[] width_height= FileHelper.getImageWidthHeight(NetUrl.dir+"/"+data);
                float ratio=(float) ((width_height[0]*1.0)/(width*1.0));
                int height=(int) (width_height[1]*1.0/ratio);
                holder.setImageByUrl(R.id.ivImage, NetUrl.dir+"/"+data,width,height);
                //holder.setText(R.id.tv_image_over_result,"你好你好");
                holder.setText(R.id.tv_result_time,"2017-04-26");
                RecyclerView labelRecyclerView=holder.getView(R.id.label_recycler_view);
                List<String> labelList=new ArrayList<>();
                labelList.add("label1");
                labelList.add("label2label1");
                labelList.add("label3label1");
                labelList.add("label4label1");
                labelList.add("label5");
                labelRecyclerView.setAdapter(new RecyclerViewAdapter<String>(ImageOverlaysResultActivity.this, labelList,R.layout.label_item) {
                    @Override
                    public void convert(RecyclerViewHolder holder1, String data1, int position1) {
                        holder1.setText(R.id.btn_item,data1);
                        System.out.println("进来了");
                    }

                });

                //水平流式布局
                ChipsLayoutManager chipsLayoutManager = ChipsLayoutManager.newBuilder(ImageOverlaysResultActivity.this)
                        .setChildGravity(Gravity.CENTER_HORIZONTAL)
                        .setScrollingEnabled(true)
                        .setMaxViewsInRow(5)
                        .setGravityResolver(new IChildGravityResolver() {
                            @Override
                            public int getItemGravity(int position) {
                                return Gravity.CENTER;
                            }
                        })
                        .setOrientation(ChipsLayoutManager.HORIZONTAL)
                        .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
                        .withLastRow(true)
                        .build();
                labelRecyclerView.setLayoutManager(chipsLayoutManager);
            }
        });

    }

    private void init()
    {
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        tv_toolbar_confer= (TextView) findViewById(R.id.toolbar_btn_confer);
        //tv_toolbar_confer.setVisibility(View.GONE);
        tv_toolbar_confer.setText("导出结果");
        tv_toolbar_confer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DirectoryChooserConfig config = DirectoryChooserConfig.builder()
                        .initialDirectory(initPath)
                        .newDirectoryName("imageOverlay")
                        .allowNewDirectoryNameModification(true)
                        .build();
                mDialog=DirectoryChooserFragment.newInstance(config);
                mDialog.setDirectoryChooserListener(new DirectoryChooserFragment.OnFragmentInteractionListener() {
                    @Override
                    public void onSelectDirectory(@NonNull String path) {
                        initPath = path;
                        mDialog.dismiss();
                    }

                    @Override
                    public void onCancelChooser() {
                        mDialog.dismiss();
                    }
                });
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
                    intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
                    try {
                        startActivityForResult(intent, NetUrl.image_overlay_result_request_code);
                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                        mDialog.show(getFragmentManager(), null);
                    }
                } else {
                    mDialog.show(getFragmentManager(), null);
                }
            }
        });
        tv_toolbar_nav= (TextView) findViewById(R.id.toolbar_nav);

        mRecyclerView= (RecyclerView) findViewById(R.id.my_recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        //获取屏幕宽度
        DisplayMetrics outMetrics = new DisplayMetrics();
        WindowManager manager=getWindowManager();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        width = outMetrics.widthPixels/2;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode)
        {
            case NetUrl.image_overlay_result_request_code:
                if (resultCode == RESULT_OK)
                {
                    Uri uriTree = data.getData();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        try {
                            getContentResolver().takePersistableUriPermission(
                                    uriTree, Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                        } catch (SecurityException e) {
                            e.printStackTrace();
                        }
                    }
                    //需要从uri中截取出路径，此为文件夹路径

                    String savePath=File.separator+uriTree.getPath().substring(14);
                    SnackbarHelper.make(toolbar,savePath);
                    //开始文件下载


                    //将文件保存到指定文件夹

                }
                else
                {
                    // Nothing selected
                }
            break;
        }
    }
}
