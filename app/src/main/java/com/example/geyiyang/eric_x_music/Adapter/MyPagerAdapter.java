package com.example.geyiyang.eric_x_music.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.geyiyang.eric_x_music.Fragment.FragmentAlbum;
import com.example.geyiyang.eric_x_music.Fragment.FragmentArtist;
import com.example.geyiyang.eric_x_music.Fragment.FragmentMusic;

/**
 * Created by geyiyang on 2017/9/22.
 */

public class MyPagerAdapter extends FragmentStatePagerAdapter{
    private static final int NUM_PAGE=3;
    public MyPagerAdapter(FragmentManager fm) {
        super(fm);
    }
    private FragmentMusic mFragmentMusic = new FragmentMusic();
    private FragmentArtist mFragmentArtist = new FragmentArtist();
    private FragmentAlbum mFragmentAlbum = new FragmentAlbum();
    @Override
    public Fragment getItem(int position) {
//        return ArrayListFragment.newInstance(position);

        switch (position) {
            case 0:
                return mFragmentMusic;
            case 1:
                return mFragmentArtist;
            case 2:
                return mFragmentAlbum;
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
