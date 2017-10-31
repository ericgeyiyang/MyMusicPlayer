package com.example.geyiyang.eric_x_music.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.geyiyang.eric_x_music.Model.AlbumInfo;
import com.example.geyiyang.eric_x_music.R;

import java.util.List;

/**
 * Created by geyiyang on 2017/10/19.
 */

public class MyAlbumAdapter extends BaseAdapter {
    private List<AlbumInfo> mAlbumInfos;
    private Context mContext;


    public MyAlbumAdapter(Context context, List<AlbumInfo> AlbumInfo) {
        mAlbumInfos = AlbumInfo;
        mContext = context;
    }


    @Override
    public int getCount() {
        return mAlbumInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return mAlbumInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if (convertView == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.album_item_layout, null);
        } else view = convertView;
        TextView title = (TextView) view.findViewById(R.id.albumn_name);
        TextView artistName = (TextView) view.findViewById(R.id.singer_name_album);
        TextView num = (TextView) view.findViewById(R.id.song_num_album);
        ImageView popup_menu = (ImageView) view.findViewById(R.id.popup_menu_album);
        ImageView album_pic = (ImageView) view.findViewById(R.id.albumn_pic);

        title.setText(mAlbumInfos.get(position).getAlbum());
        num.setText(mAlbumInfos.get(position).getNumofTracks() + "首");
        artistName.setText(mAlbumInfos.get(position).getArtist());
        Glide.with(mContext)
                .load(mAlbumInfos.get(position).getCoverUri())
                .error(R.drawable.placeholder_disk_play_song)
                .centerCrop().crossFade(500)
                .into(album_pic);

        return view;
    }
}
