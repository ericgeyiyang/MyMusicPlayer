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

    /**
     * 不同Activity的change功能不同，更换Activity时需要从新解绑以前服务和绑定到当前Activity，
     * 绑定成功后会调用下面函数并执行覆盖后的onChange函数更新页面，
     * Service为当前Activity提供服务和监听功能，监听触发条件由服务定义，事件触发结果由下面两个函数覆盖实现
     */

    private ServiceConnection playingServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i(TAG, "onServiceConnected: --->>调用onChange");
            PlayingService.MyBinder binder = (PlayingService.MyBinder) service;
            playingService = binder.getService();
            //重写事件触发函数，子类Activity重写覆盖实习自身功能。其中onPublish和onChange的触发条件在Service中
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
            onChange(playingService.getPlayingPosition());//绑定服务时候调用，更新当前Activity的界面
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
        super.onDestroy();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 绑定服务是Application的context与服务绑定
     */
    public void allowBindService() {
        getApplicationContext().bindService(new Intent(this, PlayingService.class),
                playingServiceConnection,
                Context.BIND_AUTO_CREATE);
        Log.i(TAG, "allowBindService: --->bindService");

    }

    /**
     * fragment的view消失后回调，调用Service的onUnbind
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


