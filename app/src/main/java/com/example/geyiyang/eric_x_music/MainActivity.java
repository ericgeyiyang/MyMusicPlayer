package com.example.geyiyang.eric_x_music;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

import com.example.geyiyang.eric_x_music.Adapter.MyMusicAdaper;
import com.example.geyiyang.eric_x_music.Model.MusicInfo;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView mListView;
    private TextView mTitle,mArtist,mAlbum;
    private MyMusicAdaper mMyadpter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        List<MusicInfo> musicInfoList = new ArrayList<>();
        musicInfoList.add(new MusicInfo("本地","我又想你了", "陈信喆", "我又想你了", 11.2, ""));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = (ListView) findViewById(R.id.listview);
        mMyadpter = new MyMusicAdaper(this, musicInfoList);
        mListView.setAdapter(mMyadpter);

    }
}
