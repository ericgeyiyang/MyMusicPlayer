package com.example.geyiyang.eric_x_music.Fragment;

import android.content.BroadcastReceiver;
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
import android.widget.ListView;

import com.example.geyiyang.eric_x_music.Activity.MainActivity;
import com.example.geyiyang.eric_x_music.Activity.MyMediaPlayerActivity;
import com.example.geyiyang.eric_x_music.Adapter.MyMusicAdapter;
import com.example.geyiyang.eric_x_music.Model.MusicInfo;
import com.example.geyiyang.eric_x_music.R;
import com.example.geyiyang.eric_x_music.Utils.MusicUtils;

import java.util.List;

/**
 * Created by geyiyang on 2017/9/27.
 */

public class FragmentMusic extends Fragment {
    private static final String TAG = "FragmentMusic";
    private MyMusicAdapter myMusicAdapter;
    private MusicUtils mMusicUtils;

    public void setMusicInfoList(List<MusicInfo> musicInfoList) {
        this.musicInfoList = musicInfoList;
    }

    private List<MusicInfo> musicInfoList=null;//得到MusicUtils中的音乐列表
    private ListView listView;
    private MainActivity mainActivity;
    public class MyReceiver extends BroadcastReceiver {
        public MyReceiver() {
        }
        @Override
        public void onReceive(Context context, Intent intent) {

        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) getActivity();//获得MainActivity对象
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreate: --->");
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView: --->");
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
        myMusicAdapter = new MyMusicAdapter(getActivity(), musicInfoList);
        listView.setAdapter(myMusicAdapter);
        listView.setTextFilterEnabled(true);
    }
    /**
     * view创建完毕 回调通知activity绑定歌曲播放服务,onServiceConnected调用onChange更新页面
     */
    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "onStart: --->");

//        myMusicAdaper.notifyDataSetChanged();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onActivityCreated: --->");
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * 解绑服务，让MyMediaPlayerActivity能够绑定服务，必须解绑而且在前者绑定服务（onResume)前
     */
    @Override
    public void onPause() {
        Log.i(TAG, "onPause: --->unbindservice");
        super.onPause();
        mainActivity.allowUnbindService();
    }

    @Override
    public void onDestroyView() {
        Log.i(TAG, "onDestroyView: --->");
        super.onDestroyView();
    }

    /**
     * 绑定服务（第一次调用onBind，后面调用Rebind)
     * 绑定成功后会调用Onchange，所以要在页面创建完成后再绑定服务
     */
    @Override
    public void onResume() {
        Log.i(TAG, "onResume: --->bindService");
        super.onResume();
        mainActivity.allowBindService();
    }

    //不能在这里解绑服务，因为新Activity创建后才会onStop
    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, "onStop: --->");
    }


    //mainactivity和PlayingActivity更换音乐时以及重新开启应用绑定服务时Onchange调用该函数
    public void onPlay(int position) {
            onItemPlay(position);
    }

    public void clearTextFilter() {
        myMusicAdapter.setMusicInfoList(MusicUtils.getMusicInfoList());
//        mainActivity.getPlayService().setMusicInfoList(myMusicAdapter.getAllMusicInfoList());
        myMusicAdapter.notifyDataSetChanged();
    }

    public void setFilterText(String text) {
        myMusicAdapter.getFilter().filter(text);
        musicInfoList = myMusicAdapter.getMusicInfoList();
    }
    /**
     * 播放时小喇叭显示当前播放条目实现播放的歌曲条目可见
     * 位置传递给MyMusicAdapter，更新listview时显示小喇叭
     *
     * @param position 当前播放歌曲位置
     */

    private void onItemPlay(final int position) {
        MyMusicAdapter.setPlayingPosition(position);
        Log.i(TAG, "onItemPlay: --->");
        myMusicAdapter.notifyDataSetChanged();
        listView.clearFocus();
        listView.post(new Runnable() {
            @Override
            public void run() {
                listView.setSelection(position);
            }
        });
//        listView.clearFocus();
        //发送消息给handler更新listview
//        threadPool.execute(new Runnable() {
//            @Override
//            public void run() {
//                Message msg = new Message();
//                msg.what=0;
////                msg.arg1 = oldPosition;
//                myhandler.sendMessage(msg);
//            }
//        });
//        threadPool.execute(new Runnable() {
//            @Override
//            public void run() {
//                Message msg = new Message();
//                msg.what=1;
//                myhandler.sendMessage(msg);
//            }
//        });


//
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy: --->");
        super.onDestroy();
    }

    /**
     * 点击歌曲响应，开启另一应用
     */
    private AdapterView.OnItemClickListener musicItemClickListener=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Log.i(TAG, "onItemClick: --->listitem clicked");
            int absolute_position = musicInfoList.get(position).getPosition();//获得点击的歌曲在总列表中的绝对位置
            mainActivity.getPlayService().play(absolute_position);//开始播放，瞬间更新本应用后开启播放界面
            MyMusicAdapter.setPlayingPosition(position);
            Intent intent = new Intent(getActivity(), MyMediaPlayerActivity.class);
//            intent.putExtra("position", position);
            startActivity(intent);
        }
    };


}
