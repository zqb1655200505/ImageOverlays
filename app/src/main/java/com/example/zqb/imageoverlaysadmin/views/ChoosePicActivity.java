package com.example.zqb.imageoverlaysadmin.views;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.internal.http.multipart.FilePart;
import com.android.internal.http.multipart.Part;
import com.example.zqb.imageoverlaysadmin.R;
import com.example.zqb.imageoverlaysadmin.models.NetUrl;
import com.example.zqb.imageoverlaysadmin.utils.RecyclerViewAdapter;
import com.example.zqb.imageoverlaysadmin.utils.RecyclerViewHolder;
import com.example.zqb.imageoverlaysadmin.utils.SnackbarHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ChoosePicActivity extends AppCompatActivity {

    private String dir=null;
    private RecyclerView mGirdView;
    private List<String> pic_list;
    private Toolbar toolbar;
    private TextView tv_toolbar_nav;
    private TextView tv_toolbar_confer;
    private RecyclerViewAdapter<String> my_adapter;
    // 用户选择的图片，存储为图片的完整路径
    private List<String> mSelectedImage = new LinkedList<>();
    private boolean isOnLongClick = false;   //是否正在长按状态
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_pic);
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        tv_toolbar_confer= (TextView) findViewById(R.id.toolbar_btn_confer);
        tv_toolbar_confer.setVisibility(View.GONE);
        tv_toolbar_nav= (TextView) findViewById(R.id.toolbar_nav);
        tv_toolbar_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isOnLongClick)
                {
                    isOnLongClick=false;
                    mSelectedImage.clear();
                    tv_toolbar_nav.setText("");
                    tv_toolbar_confer.setVisibility(View.GONE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        tv_toolbar_nav.setBackground(ResourcesCompat.getDrawable(getResources(),R.drawable.back_ic,null));
                    }
                    else
                    {
                        tv_toolbar_nav.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.back_ic,null));
                    }
                    my_adapter.notifyDataSetChanged();
                }
                else
                {
                    finish();
                }
            }
        });
        dir=getIntent().getStringExtra("dir");
        File dir_file=new File(dir);
        pic_list= Arrays.asList(dir_file.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                if (filename.endsWith(".jpg") ||
                        filename.endsWith(".png") ||
                        filename.endsWith(".jpeg"))
                    return true;
                return false;
            }
        }));
        NetUrl.imageUrls=pic_list;
        mGirdView = (RecyclerView) findViewById(R.id.id_gridView);
        mGirdView.setLayoutManager(new GridLayoutManager(ChoosePicActivity.this,3));
        mGirdView.setAdapter(my_adapter=new RecyclerViewAdapter<String>(ChoosePicActivity.this,pic_list,R.layout.grid_item){

            @Override
            public void convert(final RecyclerViewHolder holder, final String data, final int position) {
                //设置no_pic(默认图片)
                //holder.setImageResource(R.id.id_item_image,R.drawable.pictures_no);
                //设置no_selected（未选中）
                //holder.setImageResource(R.id.id_item_select, R.drawable.picture_unselected);

                holder.setImageByUrl(R.id.id_item_image,dir+"/"+data);
                final ImageView mImageView=holder.getView(R.id.id_item_image);
                ImageView mSelect=holder.getView(R.id.id_item_select);
                mImageView.setColorFilter(null);//设置有无变暗效果

                mImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(isOnLongClick)
                        {
                            if(mSelectedImage.contains(dir+"/"+data))
                            {
                                mSelectedImage.remove(dir+"/"+data);
                                //holder.setImageResource(R.id.id_item_select, R.drawable.picture_unselected);
                                holder.setVisibility(R.id.id_item_select,View.GONE);
                                mImageView.setColorFilter(null);
                            }
                            else
                            {
                                mSelectedImage.add(dir+"/"+data);
                                //holder.setImageResource(R.id.id_item_select, R.drawable.pictures_selected);
                                holder.setVisibility(R.id.id_item_select,View.VISIBLE);
                                mImageView.setColorFilter(Color.parseColor("#77000000"));
                            }
                        }
                        else
                        {
                            Intent intent=new Intent(ChoosePicActivity.this,PicViewerActivity.class);
                            intent.putExtra("image_index",position);
                            intent.putExtra("dir",dir);
                            startActivity(intent);
                        }
                    }
                });

                mImageView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v)
                    {
                        if(!isOnLongClick)
                        {
                            isOnLongClick=true;
                            tv_toolbar_confer.setVisibility(View.VISIBLE);
                            tv_toolbar_nav.setText(R.string.cancel);
                            tv_toolbar_nav.setBackgroundColor(ResourcesCompat.getColor(getResources(),R.color.colorPrimary,null));
                            mSelectedImage.add(dir+"/"+data);
                            holder.setVisibility(R.id.id_item_select,View.VISIBLE);
                            //holder.setImageResource(R.id.id_item_select, R.drawable.pictures_selected);
                            mImageView.setColorFilter(Color.parseColor("#77000000"));
                            return true;
                        }
                        return false;
                    }
                });
                /**
                 * 已经选择过的图片，显示出选择过的效果
                 */
                if (mSelectedImage.contains(dir + "/" + data))
                {
                    //mSelect.setImageResource(R.drawable.pictures_selected);
                    mSelect.setVisibility(View.VISIBLE);
                    mImageView.setColorFilter(Color.parseColor("#77000000"));
                }
            }
        });
//        AlertDialog.Builder builder=new AlertDialog.Builder(ChoosePicActivity.this);
//        View title_view= LayoutInflater.from(ChoosePicActivity.this).inflate(R.layout.dialog_title,null,false);
//        builder.setCustomTitle(title_view);
//        TextView title= (TextView) title_view.findViewById(R.id.dialog_title);
//        title.setText("提示");
//        //builder.setTitle("提示");
//        builder.setMessage("长按可选择图片");
//        AlertDialog dialog = builder.create();
//        dialog.setCancelable(true);
//        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "知道了", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//        dialog.show();
        findViewById(R.id.toolbar_btn_confer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int len=mSelectedImage.size();

                //开始上传图片
                List<Part> partList = new ArrayList<>();
                for(int i=0;i<len;i++)
                {
                    try {
                        partList.add(new FilePart("uploadFile", new File(mSelectedImage.get(i))));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
//                FileUploadHelper fileUpload=new FileUploadHelper(ChoosePicActivity.this, NetUrl.batch_upload_pic,partList);
//                fileUpload.setResultListener(new NetResultListener() {
//                    @Override
//                    public void getResult(NetResultData result) {
//
//                    }
//                });
//                fileUpload.doUpload();
            }
        });

        SnackbarHelper.make(toolbar,"长按可以选择图片");
    }
}
