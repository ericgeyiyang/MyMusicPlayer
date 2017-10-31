package com.example.geyiyang.eric_x_music.Model;

/**
 * Created by geyiyang on 2017/10/19.
 */

public class AlbumInfo {
    private String album;
    private String coverUri;
    private String artist;
    private int NumofTracks;

    public int getNumofTracks() {return NumofTracks;}
    public void setNumofTracks(int numofTracks) {NumofTracks = numofTracks;}

    public String getCoverUri() {
        return coverUri;
    }
    public void setCoverUri(String coverUri) {
        this.coverUri = coverUri;
    }

    public String getAlbum() {
        return album;
    }
    public void setAlbum(String album) {
        this.album = album;
    }

    public String getArtist() {
        return artist;
    }
    public void setArtist(String artist) {
        this.artist = artist;
    }
}
