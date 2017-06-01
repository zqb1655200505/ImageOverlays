package com.example.zqb.imageoverlaysadmin.views;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.zqb.imageoverlaysadmin.R;
import com.example.zqb.imageoverlaysadmin.models.NetUrl;
import com.example.zqb.imageoverlaysadmin.utils.PictureSlidePagerAdapter;

public class PicViewerActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private TextView tv_indicator;
    private static final String TAG="PicViewerActivity";
    private Toolbar toolbar;
    private TextView tv_toolbar_nav;
    private TextView tv_toolbar_confer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_viewer);

        toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        tv_toolbar_nav= (TextView) findViewById(R.id.toolbar_nav);
        tv_toolbar_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_toolbar_confer= (TextView) findViewById(R.id.toolbar_btn_confer);
        tv_toolbar_confer.setVisibility(View.GONE);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        //tv_indicator = (TextView) findViewById(R.id.tv_indicator);
        int index=getIntent().getIntExtra("image_index",1);
        String dir=getIntent().getStringExtra("dir");
        viewPager.setAdapter(new PictureSlidePagerAdapter(getSupportFragmentManager(),index,dir));

        viewPager.setCurrentItem(index);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //tv_indicator.setText(String.valueOf(position+1)+"/"+ length);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
