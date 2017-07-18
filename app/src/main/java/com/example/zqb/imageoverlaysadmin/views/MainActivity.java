package com.example.zqb.imageoverlaysadmin.views;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.zqb.imageoverlaysadmin.R;
import com.example.zqb.imageoverlaysadmin.models.NetResultData;
import com.example.zqb.imageoverlaysadmin.models.NetUrl;
import com.example.zqb.imageoverlaysadmin.models.UserData;
import com.example.zqb.imageoverlaysadmin.utils.NetHelper;
import com.example.zqb.imageoverlaysadmin.utils.NetResultListener;
import com.example.zqb.imageoverlaysadmin.utils.SnackbarHelper;
import com.example.zqb.imageoverlaysadmin.utils.ToastHelper;



public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    //记录用户首次点击返回键的时间
    private long firstTime = 0;

    private String TAG="MainActivity";
    private TextView tv_title;
    private DrawerLayout drawer;

    private ImageView iv_head_pic;
    private TextView tv_username;
    private TextView tv_signature;
    private Toolbar toolbar;
    private NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);//设置隐藏原始标题
        tv_title= (TextView) findViewById(R.id.toolbar_title);
        tv_title.setText(R.string.app_name);


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();
        drawer.addDrawerListener(toggle);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View nav_header = navigationView.getHeaderView(0);
        iv_head_pic= (ImageView)nav_header.findViewById(R.id.iv_user_head_pic);//此处必须得从navigationView中获取imageView
        tv_username= (TextView) nav_header.findViewById(R.id.tv_nickname);
        tv_signature= (TextView) nav_header.findViewById(R.id.tv_signature);
        if(UserData.headImage!=null&&!UserData.headImage.equals("null"))
        {
            Glide.with(this)
                    .load(UserData.headImage)
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .placeholder(R.drawable.icon_user)
                    .into(iv_head_pic);
        }
        //点击头像查看大图
        iv_head_pic.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.image_detail,null,false);
                builder.setView(view);
                ImageView image_detail= (ImageView) view.findViewById(R.id.image_detail);
                if(UserData.headImage!=null&&!UserData.headImage.equals("null"))
                {
                    Glide.with(MainActivity.this)
                            .load(UserData.headImage)
                            .into(image_detail);
                }
                else
                {
                    Glide.with(MainActivity.this)
                            .load(R.drawable.icon_user)
                            .into(image_detail);
                }
                AlertDialog dialog = builder.create();
                dialog.setCancelable(true);
                dialog.show();

            }
        });

        if(UserData.username!=null&&!UserData.username.equals("null"))
        {
            tv_username.setText(UserData.username);
        }
        if(UserData.sign!=null&&!UserData.sign.equals("null"))
        {
            tv_signature.setText(UserData.sign);
        }
        else
        {
            tv_signature.setText("暂无签名");
        }
        nav_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,PersonalActivity.class);
                startActivityForResult(intent, NetUrl.main_personal_request_code);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(UserData.headImage!=null)
        {
            Glide.with(this)
                    .load(UserData.headImage)
                    .into(iv_head_pic);
        }
        if(UserData.username!=null)
        {
            tv_username.setText(UserData.username);
        }
        else
        {
            tv_username.setText("暂无昵称");
            //tv_username.setTextColor(ResourcesCompat.getColor(getResources(),R.color.text_Grey,null));
            //tv_username.setTextColor(Color.parseColor("#8496a0"));
        }
        if(UserData.sign!=null)
        {
            tv_signature.setText(UserData.sign);
        }
        else
        {
            tv_signature.setText("暂无签名");
        }
    }

    @Override
    public void onBackPressed()
    {
        //若DrawerLayout已经展拉开来了，返回键效果为关闭DrawerLayout，否则为返回上栈顶的activity
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > 2000) {
                ToastHelper.make(MainActivity.this, "再按一次退出程序");
                firstTime = secondTime;
            } else {
                finish();
            }

        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        //drawer.closeDrawer(GravityCompat.START);
        switch (item.getItemId())
        {
            case R.id.imageUpload:
            {
                Intent intent=new Intent(MainActivity.this,ScanImageActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.imageManage:
            {
                break;
            }
            case R.id.userManage:
            {
                break;
            }
            case R.id.result:
            {
                Intent intent=new Intent(MainActivity.this,ImageOverlaysResultActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.personal:
            {
                Intent intent=new Intent(MainActivity.this,PersonalActivity.class);
                startActivityForResult(intent, NetUrl.main_personal_request_code);
                break;
            }
            case R.id.signOut:
            {

                UserData.clear(MainActivity.this);
                break;
            }
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK)
        {
            switch (requestCode)
            {
                case NetUrl.main_personal_request_code:
                {
                    tv_signature.setText(UserData.sign==null?"暂无签名":UserData.sign);
                    if(UserData.headImage!=null)
                    {
                        Glide.with(MainActivity.this)
                                .load(UserData.headImage)
                                .into(iv_head_pic);
                    }
                    break;
                }
            }
        }
    }
}
