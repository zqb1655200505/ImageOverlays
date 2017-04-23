package com.example.zqb.imageoverlaysadmin.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.example.zqb.imageoverlaysadmin.R;


import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by zqb on 2017/4/22.
 */

public class PictureSlideFragment extends Fragment {
    private String pic_url;
    private PhotoViewAttacher mAttacher;
    private PhotoView imageView;
    public static PictureSlideFragment getInstance(String url)
    {
        PictureSlideFragment mFragment=new PictureSlideFragment();

        //activity与fragment通信（传递参数）
        Bundle bundle=new Bundle();
        bundle.putString("pic_url",url);
        mFragment.setArguments(bundle);
        return mFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pic_url =  getArguments().getString("pic_url");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView=inflater.inflate(R.layout.fragment_picture_slide,container,false);
        imageView= (PhotoView) mView.findViewById(R.id.iv_main_pic);
        mAttacher = new PhotoViewAttacher(imageView);//使用PhotoViewAttacher为图片添加支持缩放、平移的属性
        Glide.with(getActivity())
                .load(pic_url)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(new GlideDrawableImageViewTarget(imageView){
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                        super.onResourceReady(resource, animation);
                        mAttacher.update();//调用PhotoViewAttacher的update()方法，使图片重新适配布局
                    }
                });


        return mView;
    }
}
