package com.example.geyiyang.eric_x_music.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.geyiyang.eric_x_music.Model.ArtistInfo;
import com.example.geyiyang.eric_x_music.R;

import java.util.List;

/**
 * Created by geyiyang on 2017/10/18.
 */

public class MyArtistAdapter extends BaseAdapter {
    private List<ArtistInfo> mArtistInfos;
    private Context mContext;


    public MyArtistAdapter(Context context, List<ArtistInfo> ArtistInfos ) {
        mArtistInfos = ArtistInfos;
        mContext = context;
    }


    @Override
    public int getCount() {
        return mArtistInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return mArtistInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if (convertView == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.singer_item_layout,null);
        }
        else
            view = convertView;
        TextView mTitle = (TextView) view.findViewById(R.id.song_artist);
        ImageView mpopup_menu = (ImageView) view.findViewById(R.id.popup_menu_singer);
        ImageView mArtist_pic = (ImageView) view.findViewById(R.id.artist_pic);
        TextView mNum = (TextView) view.findViewById(R.id.song_num);
        mTitle.setText(mArtistInfos.get(position).getArtist_name());
        mNum.setText(mArtistInfos.get(position).getNumber_of_tracks()+"首");
        Glide.with(mContext)
                .load(mArtistInfos.get(position).getCover_url())
                .error(R.drawable.placeholder_disk_play_song)
                .centerCrop()
                .crossFade(500)
                .into(mArtist_pic);
        //        mpopup_menu.setImageResource(R.drawable.actionbar_more);

        return view;
    }
}
