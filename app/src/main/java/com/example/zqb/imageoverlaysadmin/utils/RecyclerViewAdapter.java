package com.example.zqb.imageoverlaysadmin.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.zqb.imageoverlaysadmin.R;

import java.util.List;

/**
 * 通用的RecyclerViewAdapter
 * Created by zqb on 2017/4/16.
 */

public abstract class RecyclerViewAdapter<T> extends RecyclerView.Adapter<RecyclerViewHolder>
{
    private Context mContext;
    private List<T> mDatas;
    private int mLayoutId;
    private LayoutInflater mInflater;

    private OnItemClickListener onItemClickListener=null;

    public RecyclerViewAdapter(Context mContext,List<T> mDatas,int mLayoutId)
    {
        this.mContext=mContext;
        this.mDatas=mDatas;
        this.mLayoutId=mLayoutId;
        mInflater=LayoutInflater.from(mContext);
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //这里是创建ViewHolder的地方，RecyclerAdapter内部已经实现了ViewHolder的重用
        //这里直接new就好了
        return new RecyclerViewHolder(mInflater.inflate(mLayoutId, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, int position)
    {
        if (onItemClickListener != null)
        {
            //设置背景
            holder.itemView.setBackgroundResource(R.drawable.recycler_bg);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //注意，这里的position不要用上面参数中的position，会出现位置错乱
                    onItemClickListener.OnItemClick(holder.itemView, holder.getLayoutPosition());
                }
            });
        }
        convert(holder, mDatas.get(position), position);
    }

    public abstract void convert(RecyclerViewHolder holder, T data, int position);
    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
    /**自定义RecyclerView item的点击事件的点击事件*/
    public interface OnItemClickListener {
        void OnItemClick(View view, int position);
    }
}
