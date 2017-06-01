package com.example.zqb.imageoverlaysadmin.views;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import com.example.zqb.imageoverlaysadmin.R;
import com.example.zqb.imageoverlaysadmin.models.NetUrl;
import com.example.zqb.imageoverlaysadmin.utils.FileHelper;
import com.example.zqb.imageoverlaysadmin.utils.RecyclerViewAdapter;
import com.example.zqb.imageoverlaysadmin.utils.RecyclerViewHolder;

import java.util.ArrayList;
import java.util.List;

public class ImageOverlaysResultActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView mRecyclerView;
    private TextView tv_toolbar_confer;
    private TextView tv_toolbar_nav;
    private List<String> mData=new ArrayList<>();
    private int width;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_overlays_result);
        init();
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
                holder.setText(R.id.tv_image_over_result,"你好你好");
                holder.setText(R.id.tv_result_time,"2017-04-26");
            }
        });

    }

    private void init()
    {
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        tv_toolbar_confer= (TextView) findViewById(R.id.toolbar_btn_confer);
        tv_toolbar_confer.setVisibility(View.GONE);
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
}
