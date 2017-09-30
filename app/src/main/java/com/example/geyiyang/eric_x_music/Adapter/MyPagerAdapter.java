package com.example.geyiyang.eric_x_music.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.geyiyang.eric_x_music.Fragment.FragmentAlbum;
import com.example.geyiyang.eric_x_music.Fragment.FragmentMusic;
import com.example.geyiyang.eric_x_music.Fragment.FragmentSinger;

/**
 * Created by geyiyang on 2017/9/22.
 */

public class MyPagerAdapter extends FragmentStatePagerAdapter{
    private static final int NUM_PAGE=3;
    public MyPagerAdapter(FragmentManager fm) {
        super(fm);
    }
    private FragmentMusic fragment_music = new FragmentMusic();
    private FragmentSinger fragment_singer = new FragmentSinger();
    private FragmentAlbum fragment_album = new FragmentAlbum();
    @Override
    public Fragment getItem(int position) {
//        return ArrayListFragment.newInstance(position);

        switch (position) {
            case 0:
                return fragment_music;
            case 1:
                return fragment_singer;
            case 2:
                return fragment_album;
        }
        return null;
    }

    @Override
    public int getCount() {
        return NUM_PAGE;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "歌曲";
            case 1:
                return "歌手";
            case 2:
                return "专辑";
            default:
                return "歌曲";
        }
    }
}
