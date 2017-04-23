package com.example.zqb.imageoverlaysadmin.views;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.zqb.imageoverlaysadmin.R;
import com.example.zqb.imageoverlaysadmin.models.NetUrl;
import com.example.zqb.imageoverlaysadmin.models.UserData;

import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.util.List;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String TAG="MainActivity";
    private TextView tv_title;
    private DrawerLayout drawer;

    private ImageView iv_head_pic;
    private TextView tv_username;
    private TextView tv_signature;
    private Toolbar toolbar;
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


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View nav_header = navigationView.getHeaderView(0);
        iv_head_pic= (ImageView)nav_header.findViewById(R.id.iv_user_head_pic);//此处必须得从navigationView中获取imageView

        tv_username= (TextView) nav_header.findViewById(R.id.tv_nickname);
        tv_signature= (TextView) nav_header.findViewById(R.id.tv_signature);
        iv_head_pic.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//                View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.image_detail,null,false);
//                builder.setView(view);
//                ImageView image_detail= (ImageView) view.findViewById(R.id.image_detail);
//                if(UserData.head_pic!=null)
//                {
//                    Glide.with(MainActivity.this)
//                            .load(UserData.head_pic)
//                            .into(image_detail);
//                }
//                else
//                {
//                    Glide.with(MainActivity.this)
//                            .load(R.drawable.icon_user)
//                            .into(image_detail);
//                }
//                AlertDialog dialog = builder.create();
//                dialog.setCancelable(true);
//                dialog.show();
                Intent intent=new Intent(MainActivity.this,PersonalActivity.class);
                startActivityForResult(intent, NetUrl.main_personal_request_code);
            }
        });

        if(UserData.head_pic!=null)
        {
            Glide.with(this)
                    .load(UserData.head_pic)
                    .into(iv_head_pic);
        }
    }

    @Override
    public void onBackPressed()
    {
        //若DrawerLayout已经展拉开来了，返回键效果为关闭DrawerLayout，否则为返回上栈顶的activity
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

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
                //注销其实是将TaskStack清空
//                Intent logoutIntent = new Intent(MainActivity.this, LoginActivity.class);
//                logoutIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(logoutIntent);
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
                    tv_username.setText(UserData.username==null?"":UserData.username);
                    tv_signature.setText(UserData.signature==null?"":UserData.signature);
                    if(UserData.head_pic!=null)
                    {
                        Glide.with(MainActivity.this)
                                .load(UserData.head_pic)
                                .into(iv_head_pic);
                    }
                    break;
                }
            }
        }
    }
}
