package com.example.zqb.imageoverlaysadmin.models;

/**
 * 图片信息类（本地图片）
 * Created by zqb on 2017/4/10.
 */

public class ImageListData {
    //图片文件夹
    private String picDir;

    //第一张图片的路径 ,用于做文件夹的图标
    private String firstImagePath;

    //图片文件夹名称
    private String folderName;

    //图片数量
    private int count;

    public String getPicDir() {
        return picDir;
    }

    public void setPicDir(String picDir) {
        this.picDir = picDir;
        int lastIndexOf = this.picDir.lastIndexOf("/");
        this.folderName = this.picDir.substring(lastIndexOf);
    }

    public String getFirstImagePath() {
        return firstImagePath;
    }

    public void setFirstImagePath(String firstImagePath) {
        this.firstImagePath = firstImagePath;
    }

    public String getFolderName() {
        return folderName;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "ImageInfo{" +
                "picDir='" + picDir + '\'' +
                ", firstImagePath='" + firstImagePath + '\'' +
                ", folderName='" + folderName + '\'' +
                ", count=" + count +
                '}';
    }
}
