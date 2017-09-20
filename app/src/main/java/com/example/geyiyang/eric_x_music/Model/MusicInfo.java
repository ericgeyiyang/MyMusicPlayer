package com.example.geyiyang.eric_x_music.Model;

/**
 * Created by geyiyang on 2017/9/20.
 */

public class MusicInfo {
    private String mType="";



    private double mSize=0;
    //    private long id=0;
    private String mTitle="";
    private String mArtist="";
    private String Album="";
    private String mMusicUri="";

    public MusicInfo() {

    }


    public MusicInfo(String type, String title, String artist, String album, double size, String musicUri) {
        mType = type;
        mTitle = title;
        mArtist = artist;
        Album = album;
        mSize=size;
        mMusicUri = musicUri;
    }
    public String getSize() {
        return mSize+"M";
    }
    public String getType() {
        return mType;
    }
//
//    public long getId() {
//        return id;
//    }

    public String getTitle() {
        return mTitle;
    }

    public String getArtist() {
        return mArtist;
    }

    public String getAlbum() {
        return Album;
    }

    public String getMusicUri() {
        return mMusicUri;
    }
}

