package com.example.geyiyang.eric_x_music.Service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.example.geyiyang.eric_x_music.Model.MusicInfo;
import com.example.geyiyang.eric_x_music.Utils.MusicUtils;
import com.example.geyiyang.eric_x_music.Utils.SharedPreferenceUtils;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.example.geyiyang.eric_x_music.Constant.Constant.PLAY_POS;


/**
 * Created by geyiyang on 2017/9/26.
 */

public class PlayingService extends Service implements MediaPlayer.OnCompletionListener,MediaPlayer.OnErrorListener {
    private static final String TAG = "PlayingService";
    private MediaPlayer mediaPlayer;
    private int playingPosition=0; // 当前正在播放
    public final IBinder binder = new MyBinder();
    private List<MusicInfo> musicInfoList;
    private OnMusicEventListener musicEventListener;
    // 单线程池
    private ExecutorService progressUpdatedListener = Executors.newSingleThreadExecutor();
    public class MyBinder extends Binder {
        public PlayingService getService() {
            return PlayingService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand: --->");
        return Service.START_STICKY;
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "onCreate: --->servicestarted");
        super.onCreate();
        musicInfoList = MusicUtils.ScanMusic(getApplicationContext());
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(this);
        playingPosition = (Integer)
                SharedPreferenceUtils.get(this, PLAY_POS, 0);//第三个参数是key不存在的默认返回值
        if (musicInfoList.size() <= 0) {
            Toast.makeText(getApplicationContext(),
                    "当前手机没有MP3文件", Toast.LENGTH_LONG).show();
        }
        //开始更新进度的线程

        progressUpdatedListener.execute(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (mediaPlayer != null && mediaPlayer.isPlaying() && musicEventListener != null) {
                        musicEventListener.onPublish(mediaPlayer.getCurrentPosition());
                    }
                    /*
			         * SystemClock.sleep(millis) is a utility function very similar
			         * to Thread.sleep(millis), but it ignores InterruptedException.
			         * Use this function for delays if you do not use
			         * Thread.interrupt(), as it will preserve the interrupted state
			         * of the thread. 这种sleep方式不会被Thread.interrupt()所打断
			         */
                    SystemClock.sleep(200);
                }
            }
        });
    }
    @Override
    public void onRebind(Intent intent) {
        Log.i(TAG, "onRebind: --->");
        super.onRebind(intent);
//        if (musicEventListener != null)
//            musicEventListener.onChange(playingPosition);
    }
    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(TAG, "onUnbind: --->");
        return true;
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy: --->Service Stop");
        if (mediaPlayer != null)
            mediaPlayer.release();
        mediaPlayer = null;
        super.onDestroy();

    }

    public int getPlayingPosition() {
        return playingPosition;
    }

    public boolean isPlaying() {
        return mediaPlayer != null && mediaPlayer.isPlaying();
    }

    /**
     * 播放完成时的回调函数
     *
     * @param mp MediaPlayer当前音乐对象
     */
    @Override
    public void onCompletion(MediaPlayer mp) {
        next();
    }


    public void play(int position) {
        if (position == playingPosition&&isPlaying()) {
            return;
        }
        if (position < 0)
            position = 0;
        if (position >= musicInfoList.size())
            position = musicInfoList.size() - 1;
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(musicInfoList.get(position).getMusicUri());
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    Log.i(TAG, "onPrepared: --->startPlaying");
                    mediaPlayer.start();
                }
            });
            //这里很关键，play函数会调用onChange回调函数
            //也就是调用play就会触发监听，main和play两个activity函数的onChange会触发
            if (musicEventListener != null) {
                musicEventListener.onChange(position);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        playingPosition = position;
        SharedPreferenceUtils.put(PLAY_POS, playingPosition);
    }

    public void pause() {
        if (isPlaying()) {
            mediaPlayer.pause();
        }
    }

    public void replay() {
        mediaPlayer.start();
    }

    public void next() {
        if (playingPosition >= musicInfoList.size() - 1) {
            play(0);
        } else play(playingPosition + 1);
    }

    public void pre() {
        if (playingPosition <= 0) {
            play(musicInfoList.size() - 1);
        } else
            play(playingPosition - 1);
    }

    public String getDuration() {
        if (!isPlaying()) {
            return "0:00";
        }
        int duration_min=mediaPlayer.getDuration()/1000/60;
        int duration_sec=(mediaPlayer.getDuration()/1000)%60;
        String str =null;
        str=(String.format("%02d",duration_min))+":"+String.format("%02d",duration_sec);
        return str;
    }

    /**
     * 拖放到指定位置进行播放
     */
    public void seek(int msec) {
        if (!isPlaying())
            return;
        mediaPlayer.seekTo(msec);
    }

    /**
     * 播放错误时候直接下一首
     */
    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        next();
        return false;
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    /**
     * 音乐播放回调接口
     */
    public interface OnMusicEventListener {
        public void onPublish(int percent);//更新进度条

        public void onChange(int position);//更换音乐

    }

    public void setOnMusicEventListener(OnMusicEventListener onMusicEventListener) {
       this.musicEventListener=onMusicEventListener;
    }
}
