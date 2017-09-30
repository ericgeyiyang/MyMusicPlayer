package com.example.geyiyang.eric_x_music.Activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.geyiyang.eric_x_music.Service.PlayingService;

/**
 * Created by geyiyang on 2017/9/27.
 */

public abstract class BaseActivity extends AppCompatActivity {
    protected PlayingService playingService;
    private static final String TAG = "BaseActivity";
    private ServiceConnection playingServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i(TAG, "onServiceConnected: --->>调用onChange");
            PlayingService.MyBinder binder = (PlayingService.MyBinder) service;
            playingService = binder.getService();
            playingService.setOnMusicEventListener(new PlayingService.OnMusicEventListener() {
                @Override
                public void onPublish(int percent) {
                    BaseActivity.this.onPublish(percent);
                }

                @Override
                public void onChange(int position) {
                    BaseActivity.this.onChange(position);
                }
            });
            onChange(playingService.getPlayingPosition());//绑定服务时候调用
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i(TAG, "onServiceDisconnected: --->");
            playingService = null;

        }
    };

    @Override
    protected void onDestroy() {
        Log.i(TAG, "onDestroy: --->");
//        unbindService(playingServiceConnection);
        super.onDestroy();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        bindService(new Intent(this, PlayingService.class), playingServiceConnection, BIND_AUTO_CREATE);
    }
    public void allowBindService() {
        getApplicationContext().bindService(new Intent(this, PlayingService.class),
                playingServiceConnection,
                Context.BIND_AUTO_CREATE);
    }

    /**
     * fragment的view消失后回调
     */
    public void allowUnbindService() {
        getApplicationContext().unbindService(playingServiceConnection);
    }
    /**
     * 更新进度
     * 抽象方法由子类实现
     * 实现service与主界面通信
     * @param progress 进度
     */
    public abstract void onPublish(int progress);
    /**
     * 切换歌曲
     * 抽象方法由子类实现
     * 实现service与主界面通信
     * @param position 歌曲在list中的位置
     */
    public abstract void onChange(int position);


}


