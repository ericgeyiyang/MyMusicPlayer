package com.example.geyiyang.eric_x_music.Model;

import android.graphics.Bitmap;

/**
 * Created by geyiyang on 2017/9/20.
 */

public class MusicInfo {
    public enum Type{LOCAL,ONLINE}

    private Type mType=Type.LOCAL;
    private long id=-1;
    private double mSize=.0;
    private String mTitle="";
    private String mArtist="";
    private String Album="";
    private int duration=0;
    private String mMusicUri="";
    private String pubYear="";
    private String coverUri;
    // 文件名
    private String fileName;
    // [网络歌曲]专辑封面bitmap
    private Bitmap cover;
    // 文件大小

    public MusicInfo() {
    }
    public MusicInfo(Type type, String title, String artist, String album, long size, String musicUri) {
        mType = type;
        mTitle = title;
        mArtist = artist;
        Album = album;
        mSize=size;
        mMusicUri = musicUri;
    }

    public String getCoverUri() {return coverUri;}
    public void setCoverUri(String coverUri) {this.coverUri = coverUri;}

    public String getFileName() {return fileName;}
    public void setFileName(String fileName) {this.fileName = fileName;}

    public Bitmap getCover() {return cover;}
    public void setCover(Bitmap cover) {this.cover = cover;}

    public String getPubYear() {return pubYear;}
    public void setPubYear(String pubYear) {this.pubYear = pubYear;}

    public int getDuration() {return duration;}
    public void setDuration(int duration) {this.duration = duration;}

    public long getId() {return id;}
    public void setId(long id) {this.id = id;}


    public String getSize() {return String.format("%.1f",mSize)+"M";}
    public void setSize(long size) {mSize = (double)size/1024.0/1024.0;}

    public void setTitle(String title) {mTitle = title;}
    public String getTitle() {return mTitle;}

    public void setArtist(String artist) {mArtist = artist;}
    public String getArtist() {return mArtist;}

    public void setAlbum(String album) {Album = album;}
    public String getAlbum() {return Album;}

    public void setType(Type type) {mType = type;}
    public Type getType() {
        return mType;
    }

    public void setMusicUri(String musicUri) {mMusicUri = musicUri;}
    public String getMusicUri() {
        return mMusicUri;
    }
}

