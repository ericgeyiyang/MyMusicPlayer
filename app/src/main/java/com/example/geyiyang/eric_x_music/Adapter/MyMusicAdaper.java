package com.example.geyiyang.eric_x_music.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.geyiyang.eric_x_music.Model.MusicInfo;
import com.example.geyiyang.eric_x_music.R;

import java.util.List;

/**
 * Created by geyiyang on 2017/9/20.
 */

public class MyMusicAdaper extends BaseAdapter {
    private List<MusicInfo> mMusicInfoList;
    private Context mContext;


    public MyMusicAdaper(Context context, List<MusicInfo> musicInfoList ) {
        mMusicInfoList = musicInfoList;
        mContext = context;
    }


    @Override
    public int getCount() {
        return mMusicInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return mMusicInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if (convertView == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.music_item_layout,null);
        }
        else
            view = convertView;
        TextView mTitle = (TextView) view.findViewById(R.id.song_title);
        ImageView mdld = (ImageView) view.findViewById(R.id.dld_ok);
        ImageView mpopup_menu = (ImageView) view.findViewById(R.id.popup_menu);
        ImageView mplaying = (ImageView) view.findViewById(R.id.playing);
        TextView mSize = (TextView) view.findViewById(R.id.song_size);
        TextView mArtist = (TextView) view.findViewById(R.id.song_artist);
        TextView mAlbum = (TextView) view.findViewById(R.id.song_albumn);
        mTitle.setText(mMusicInfoList.get(position).getTitle().toString());
        mSize.setText(mMusicInfoList.get(position).getSize());
        mAlbum.setText(mMusicInfoList.get(position).getAlbum().toString());
        mArtist.setText(mMusicInfoList.get(position).getArtist().toString());
        mdld.setImageResource(R.drawable.list_icn_dld_ok);
//        mpopup_menu.setImageResource(R.drawable.actionbar_more);
        mplaying.setVisibility(View.GONE);
        return view;
    }
}
