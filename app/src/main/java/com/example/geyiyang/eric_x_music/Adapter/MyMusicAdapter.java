package com.example.geyiyang.eric_x_music.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.geyiyang.eric_x_music.Activity.MainActivity;
import com.example.geyiyang.eric_x_music.Model.MusicInfo;
import com.example.geyiyang.eric_x_music.R;
import com.example.geyiyang.eric_x_music.Utils.MusicUtils;
import com.example.geyiyang.eric_x_music.Utils.PinyinUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by geyiyang on 2017/9/20.
 */

public class MyMusicAdapter extends BaseAdapter implements Filterable {
    private List<MusicInfo> mMusicInfoList;

    public List<MusicInfo> getMusicInfoList() {
        return mMusicInfoList;
    }

    public void setMusicInfoList(List<MusicInfo> musicInfoList) {
        mMusicInfoList = musicInfoList;
    }

    private Context mContext;
    private MyFilter mFilter;
    private static int playingPostion;

    public static void setPlayingPosition(int position) {
        playingPostion = position;
    }

    public static int getPlayingPosition() {
        return playingPostion;
    }

    public MyMusicAdapter(Context context, List<MusicInfo> musicInfoList ) {
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
        mTitle.setText(mMusicInfoList.get(position).getTitle());
        mSize.setText(mMusicInfoList.get(position).getSize());
        mAlbum.setText(mMusicInfoList.get(position).getAlbum());
        mArtist.setText(mMusicInfoList.get(position).getArtist());
        mdld.setImageResource(R.drawable.list_icn_dld_ok);
//        mpopup_menu.setImageResource(R.drawable.actionbar_more);
        mplaying.setVisibility(View.GONE);
        if (position == playingPostion) {
            mplaying.setVisibility(View.VISIBLE);
        }
        return view;
    }

    @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new MyFilter();
        }
        return mFilter;
    }

    private class MyFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String prefix = constraint.toString();
            FilterResults results = new FilterResults();
            List<MusicInfo> filterMusicInfoList = new ArrayList<>();
            if (prefix == null || prefix.length() == 0) {
                results.values = MusicUtils.getMusicInfoList();
                results.count = MusicUtils.getMusicInfoList().size();//此时返回的results就是原始的数据，不进行过滤
            }
            else
            {
                String regEx = "[\\u4e00-\\u9fa5]";
                Pattern p = Pattern.compile(regEx);
                filterMusicInfoList.clear();
                char[] input = prefix.trim().toCharArray();
                //如果输入第一个字符是中文，则执行中文搜索
                if (java.lang.Character.toString(input[0]).matches("[\\u4E00-\\u9FA5]+"))
                {
                    for (MusicInfo musicInfo : MusicUtils.getMusicInfoList()) {
                        String title = musicInfo.getTitle();
                        String artist = musicInfo.getArtist();
                        String album = musicInfo.getAlbum();
                        if (title.contains(prefix) || artist.contains(prefix) || album.contains(prefix)) {
                            filterMusicInfoList.add(musicInfo);
                        }
                    }
                    results.values = filterMusicInfoList;
                    results.count = filterMusicInfoList.size();
                }
                //如果开头第一个是字母或数字，要考虑拼音匹配
                else if(java.lang.Character.toString(input[0]).matches("^[a-zA-Z0-9]+$")) {
                    for (MusicInfo musicInfo :  MusicUtils.getMusicInfoList())
                    {
                        String title = musicInfo.getTitle();
                        String artist = musicInfo.getArtist();
                        String album = musicInfo.getAlbum();
                        String full_title=title;
                        String full_artist=artist;
                        String full_album=album;
                        String Alpha_title="";
                        String Alpha_artist="";
                        String Alpha_album="";
                        Matcher m = p.matcher(title);
                        if(m.find()) {
                            full_title = PinyinUtils.getPingYin(title);//title包含中文，则全部转换为字母}
                            Alpha_title = PinyinUtils.getAlpha(title);
                        }
                        m = p.matcher(artist);
                        if(m.find()) {
                            full_artist = PinyinUtils.getPingYin(artist);//artist包含中文，则全部转换为字母}
                            Alpha_artist = PinyinUtils.getAlpha(artist);
                        }
                        m = p.matcher(album);
                        if (m.find()) {
                            full_album = PinyinUtils.getPingYin(album);//album包含中文，则全部转换为字母}
                            Alpha_album = PinyinUtils.getAlpha(album);
                        }
                            if (full_title.toLowerCase().contains(prefix) || full_artist.toLowerCase().contains(prefix) || full_album.toLowerCase().contains(prefix)||
                                    Alpha_title.toLowerCase().contains(prefix) || Alpha_artist.toLowerCase().contains(prefix) || Alpha_album.toLowerCase().contains(prefix))
                            {
                                filterMusicInfoList.add(musicInfo);
                            }
                        }
                    results.values = filterMusicInfoList;
                    results.count = filterMusicInfoList.size();
                }
                //否则输入非法字符，返回空列表
                else
                    filterMusicInfoList.clear();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mMusicInfoList = (List<MusicInfo>) results.values;
//          mainActivity.getPlayService().setMusicInfoList(mMusicInfoList);
            if (results.count > 0) {
                MainActivity mainActivity = (MainActivity) mContext;
                mainActivity.getFragmentMusic().setMusicInfoList(mMusicInfoList);//Fragment的MusicInfoList指针要换才能正确点击
                notifyDataSetChanged();//这个相当于从mMusicInfoList中删除了一些数据，只是数据的变化，故使用notifyDataSetChanged()
            }
            else notifyDataSetInvalidated();
        }
    }
}
