package com.example.zqb.imageoverlaysadmin.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.zqb.imageoverlaysadmin.models.NetUrl;
import com.example.zqb.imageoverlaysadmin.views.PictureSlideFragment;

/**
 * Created by zqb on 2017/4/22.
 */

public class PictureSlidePagerAdapter extends FragmentStatePagerAdapter {
    private int init_index=-1;
    private boolean firstIn=true;
    private String dir;
    public PictureSlidePagerAdapter(FragmentManager fm, int index,String dir) {
        super(fm);
        init_index=index;
        this.dir=dir;
    }

    @Override
    public Fragment getItem(int position)
    {
        if(firstIn)
        {
            firstIn=false;
            System.out.println(dir+"/"+NetUrl.imageUrls.get(init_index));
            return PictureSlideFragment.getInstance(dir+"/"+NetUrl.imageUrls.get(init_index));
        }
        return PictureSlideFragment.getInstance(dir+"/"+NetUrl.imageUrls.get(position));
    }

    @Override
    public int getCount() {
        return NetUrl.imageUrls.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }
}
