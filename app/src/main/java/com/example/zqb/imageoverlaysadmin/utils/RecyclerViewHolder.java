package com.example.zqb.imageoverlaysadmin.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.zqb.imageoverlaysadmin.R;

import java.io.File;

/**
 * 通用的RecyclerView.vVewHolder
 * Created by zqb on 2017/4/16.
 */

public class RecyclerViewHolder extends RecyclerView.ViewHolder {

    /** 用于存储当前item当中的View */
    private SparseArray<View> mViews;
    private int mPosition;
    public RecyclerViewHolder(View itemView) {
        super(itemView);
        mViews = new SparseArray<>();
    }


    /**
     * 拿到一个ViewHolder对象
     * @param convertView
     * @param parent
     * @param layoutId
     * @param position
     * @return
     */
    public static RecyclerViewHolder get(Context context, View convertView, ViewGroup parent, int layoutId, int position)
    {
        RecyclerViewHolder holder = null;
        if (convertView == null)
        {
            holder = new RecyclerViewHolder(LayoutInflater.from(context)
                    .inflate(layoutId,parent,false));
        }
        else
        {
            holder = (RecyclerViewHolder) convertView.getTag();
            holder.mPosition = position;
        }
        return holder;
    }
    /**
     * 通过控件的Id获取对于的控件，如果没有则加入views
     * @param viewId
     * @return
     */
    public <T extends View> T getView(int viewId)
    {
        View view = mViews.get(viewId);
        if (view == null)
        {
            view = itemView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    /**
     * 为TextView设置字符串
     * @param viewId
     * @param text
     * @return
     */
    public RecyclerViewHolder setText(int viewId, String text)
    {
        TextView view = getView(viewId) ;
        view.setText(text);
        return this;
    }

    /**
     * 为ImageView设置图片
     * @param viewId
     * @param drawableId
     * @return
     */
    public RecyclerViewHolder setImageResource(int viewId, int drawableId)
    {
        ImageView view = getView(viewId);
        view.setImageResource(drawableId);
        return this;
    }

    //设置元素可见性
    public RecyclerViewHolder setVisibility(int viewId,int visibility)
    {
        View view = getView(viewId);
        view.setVisibility(visibility);
        return this;
    }

    /**
     * 为ImageView设置图片
     * @param viewId
     * @param bm
     * @return
     */
    public RecyclerViewHolder setImageBitmap(int viewId, Bitmap bm)
    {
        ImageView view = getView(viewId);
        view.setImageBitmap(bm);
        return this;
    }

    /**
     * 为ImageView设置图片
     * @param viewId
     * @param url
     * @return
     */
    public RecyclerViewHolder setImageByUrl(int viewId, String url)
    {
        Glide.with(itemView.getContext())
                .load(url)
                .asBitmap()
                .placeholder(R.drawable.error_pic)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into((ImageView)getView(viewId));
        return this;
    }


}

