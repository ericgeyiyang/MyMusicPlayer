package com.example.geyiyang.eric_x_music.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.geyiyang.eric_x_music.Activity.MainActivity;
import com.example.geyiyang.eric_x_music.Activity.MyMediaPlayerActivity;
import com.example.geyiyang.eric_x_music.Adapter.MyMusicAdaper;
import com.example.geyiyang.eric_x_music.Model.MusicInfo;
import com.example.geyiyang.eric_x_music.R;
import com.example.geyiyang.eric_x_music.Utils.MusicUtils;

import java.util.List;

/**
 * Created by geyiyang on 2017/9/27.
 */

public class FragmentAlbum extends Fragment {
    private static final String TAG = "FragmentAlbum";
    private MyMusicAdaper myMusicAdaper;
    private List<MusicInfo> musicInfoList;//得到MusicUtils中的音乐列表
    private ListView listView;
    private ImageView imageView;
    private ImageView imageViewPre=null;//上一首播放歌曲的标志
    private MainActivity mainActivity;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) getActivity();//获得MainActivity对象
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_musiclistview, container, false);
        return v;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onViewCreated: --->");
        super.onViewCreated(view, savedInstanceState);
        listView=(ListView)view.findViewById(R.id.MusicList);
        listView.setOnItemClickListener(musicItemClickListener);
        musicInfoList = MusicUtils.getMusicInfoList();
        myMusicAdaper = new MyMusicAdaper(getActivity(), musicInfoList);
        listView.setAdapter(myMusicAdaper);
    }
    /**
     * view创建完毕 回调通知activity绑定歌曲播放服务,onServiceConnected调用onChange更新页面
     */
    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "onStart: --->");
//        mainActivity.allowBindService();
    }
    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, "onStop: --->");
//        mainActivity.allowUnbindService();//音乐不会停止
    }


    //mainactivity和PlayingActivity更换音乐时以及重新开启应用绑定服务时Onchange调用该函数
    public void onPlay(int position) {
        onItemPlay(position);
    }
    /**
     * 播放时小喇叭显示当前播放条目
     * 实现播放的歌曲条目可见
     * @param position 当前播放歌曲位置
     */
    private void onItemPlay(int position) {
        Log.i(TAG, "onItemPlay: --->");
//        listView.smoothScrollToPosition(position);
//        //之前播放音乐位置
//        int prePosition = myMusicAdaper.getPlayingPosition();
//
////        // 如果新的播放位置不在可视区域
////        // 则直接返回
////        if (listView.getLastVisiblePosition() < position
////                || listView.getFirstVisiblePosition() > position)
////            return;
//
//
//        // 如果上次播放的位置在可视区域内
//        // 则手动设置小喇叭invisible
//        if (prePosition >= listView.getFirstVisiblePosition() && prePosition <= listView.getLastVisiblePosition()) {
//            imageView = (ImageView) listView.getChildAt(prePosition-listView.getFirstVisiblePosition()).findViewById(R.id.playing);
//            imageView.setVisibility(View.GONE);
//        }
//        imageViewPre = imageView;
//        imageView = (ImageView) listView.getChildAt(position).findViewById(R.id.playing);
//        if (imageViewPre != null) {
//            imageViewPre.setVisibility(View.GONE);
//        }
//        imageView.setVisibility(View.VISIBLE);
    }
    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy: --->");
        super.onDestroy();
    }

    private AdapterView.OnItemClickListener musicItemClickListener=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Log.i(TAG, "onItemClick: --->listitem clicked");
            mainActivity.getPlayService().play(position);
//            imageViewPre = imageView;
//            imageView = (ImageView) listView.getChildAt(position).findViewById(R.id.playing);
//            if (imageViewPre != null) {
//                imageViewPre.setVisibility(View.GONE);
//            }
//            imageView.setVisibility(View.VISIBLE);

            Intent intent = new Intent(getActivity(), MyMediaPlayerActivity.class);
//            intent.putExtra("Position", position);
            startActivity(intent);
        }
    };

}
