package com.example.geyiyang.eric_x_music.Fragment;

import android.content.Context;
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
import com.example.geyiyang.eric_x_music.Adapter.MyArtistAdapter;
import com.example.geyiyang.eric_x_music.Model.ArtistInfo;
import com.example.geyiyang.eric_x_music.Model.MusicInfo;
import com.example.geyiyang.eric_x_music.R;
import com.example.geyiyang.eric_x_music.Utils.MusicUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by geyiyang on 2017/9/27.
 */

public class FragmentArtist extends Fragment {
    private static final String TAG = "FragmentArtist";
    private MyArtistAdapter mMyArtistAdapter;
    private List<MusicInfo> mMusicInfos;
    private List<ArtistInfo> mArtistInfos;
    private ListView mListView;
    private MainActivity mMainActivity;
    private List<String> mArtist;
    private Map<String,Integer> mMap;//歌手-数目Map

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mMainActivity = (MainActivity) getActivity();//获得MainActivity对象
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
        View v = inflater.inflate(R.layout.fragment_singerlistview, container, false);
        return v;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onViewCreated: --->");
        super.onViewCreated(view, savedInstanceState);
        mListView=(ListView)view.findViewById(R.id.SingerList);
        mListView.setOnItemClickListener(musicItemClickListener);
        initWidgets();

    }
    private void initWidgets() {
        mArtistInfos=new ArrayList<>();
        mMusicInfos = MusicUtils.getMusicInfoList();
        mArtist=new ArrayList<>();
        //填充数据
        for (int i=0;i< mMusicInfos.size();i++){

            mArtist.add(mMusicInfos.get(i).getArtist());
        }
        //去重,计算歌手的歌曲数目
        UpdateInfo(mArtist);
        mMyArtistAdapter = new MyArtistAdapter(getActivity(), mArtistInfos);
        mListView.setAdapter(mMyArtistAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }
    //用coverUri代替ArtistCover
    private void UpdateInfo(List<String> list)   {
        List<MusicInfo> temp = new ArrayList<>(mMusicInfos.size());
        for (int i = 0; i < mMusicInfos.size(); i++) {
            temp.add(mMusicInfos.get(i));
        }
        int num=1;
        for  ( int  i  =   0 ; i  <  list.size()  -   1 ; i ++ )   {
            num=1;
            for  ( int  j  =  list.size()  -   1 ; j  >  i; j -- )   {
                if  (list.get(j).equals(list.get(i)))   {
                    list.remove(j);
                    temp.remove(j);
                    num++;
                }
            }
            ArtistInfo artistInfo=new ArtistInfo();
            artistInfo.setArtist_name(mArtist.get(i));
            artistInfo.setCover_url(temp.get(i).getCoverUri());
            artistInfo.setNumber_of_tracks(num);
            mArtistInfos.add(artistInfo);
        }
    }
    /**
     * view创建完毕 回调通知activity绑定歌曲播放服务,onServiceConnected调用onChange更新页面
     */
    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "onStart: --->");

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onActivityCreated: --->");
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onPause() {
//        Log.i(TAG, "onPause: --->unbindservice");
        super.onPause();
    }

    @Override
    public void onDestroyView() {
//        Log.i(TAG, "onDestroyView: --->");
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, "onStop: --->");
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
        }
    };

}
