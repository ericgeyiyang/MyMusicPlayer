package com.example.geyiyang.eric_x_music.Service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.geyiyang.eric_x_music.Adapter.MyMusicAdapter;
import com.example.geyiyang.eric_x_music.Model.MusicInfo;
import com.example.geyiyang.eric_x_music.Utils.MusicUtils;
import com.example.geyiyang.eric_x_music.Utils.SharedPreferenceUtils;

import java.util.List;

import static com.example.geyiyang.eric_x_music.Constant.Constant.PLAY_POS;


/**
 * Created by geyiyang on 2017/9/26.
 */

public class PlayingService extends Service implements MediaPlayer.OnCompletionListener,MediaPlayer.OnErrorListener {
    private static final String TAG = "PlayingService";
    private MediaPlayer mediaPlayer;
    private int playingPosition=0; // 当前正在播放
    private MusicUtils mMusicUtils;
    private boolean isInterrupted=true;

    public final IBinder binder = new MyBinder();
    private List<MusicInfo> musicInfoList=null;

    public List<MusicInfo> getMusicInfoList() {
        return musicInfoList;
    }

    public static boolean sServiceStarted;
    public void setMusicInfoList(List<MusicInfo> musicInfoList) {
        this.musicInfoList = musicInfoList;
//        MusicUtils.setMusicInfoList(musicInfoList);
    }

    private OnMusicEventListener musicEventListener;
    private Thread myThread;

    public Thread getMyThread() {
        return myThread;
    }


    public void StartThread() {
        if(myThread == null) {
            myThread = new MyThread();
        }
        if (isInterrupted){
            myThread.start();
            isInterrupted = false;
        }
    }
    // 单线程池
//    private ExecutorService progressUpdatedListener = Executors.newSingleThreadExecutor();//单线程池

//    public ExecutorService getProgressUpdatedListener() {
//        return progressUpdatedListener;
//    }

    public class MyBinder extends Binder {
        public PlayingService getService() {
            return PlayingService.this;
        }
    }

    /**
     * 启动服务时候调用，多次启动会多次调用,后续绑定不会调用
     * @param intent
     * @param flags
     * @param startId
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand: --->");

        return Service.START_STICKY;
    }

    /**
     * 启动服务时候调用，多次启动服务只调用一次
     */
    @Override
    public void onCreate() {
        Log.i(TAG, "onCreate: --->servicestarted,scanMusic");
        super.onCreate();
//        mMusicUtils = new MusicUtils(getApplicationContext());
//        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
//        {
//            MusicUtils.ScanSdcard(getApplicationContext(), Environment.getExternalStorageDirectory().getAbsolutePath() + "/Music");//扫描MediaStore的音乐，但List还没有更新
//        }
//        mMusicUtils.scanFile(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Music");
        sServiceStarted = true;
        musicInfoList = MusicUtils.ScanMusic(getApplicationContext());
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(this);
        playingPosition = (Integer) SharedPreferenceUtils.get(this, PLAY_POS, 0);//第三个参数是key不存在的默认返回值


        //开始更新进度的线程
//        progressUpdatedListener.execute(new Runnable() {
//            @Override
//            public void run() {
//                while (true) {
//                    if (mediaPlayer != null && mediaPlayer.isPlaying() && musicEventListener != null) {
//                        musicEventListener.onPublish(mediaPlayer.getCurrentPosition());//这是onPublish函数的触发条件，在新线程中
//                    }
//                    /*
//			         * SystemClock.sleep(millis) is a utility function very similar
//			         * to Thread.sleep(millis), but it ignores InterruptedException.
//			         * Use this function for delays if you do not use
//			         * Thread.interrupt(), as it will preserve the interrupted state
//			         * of the thread. 这种sleep方式不会被Thread.interrupt()所打断
//			         */
//                    SystemClock.sleep(200);
//                }
//            }
//        });
    }
    public class MyThread extends Thread{

        @Override
        public void run() {
            // TODO Auto-generated method stub
            while (true) {
                if (mediaPlayer != null && musicEventListener != null) {
                    musicEventListener.onPublish(mediaPlayer.getCurrentPosition());//这是onPublish函数的触发条件，在新线程中
                }
                /*
			     * SystemClock.sleep(millis) is a utility function very similar
			     * to Thread.sleep(millis), but it ignores InterruptedException.
			     * Use this function for delays if you do not use
			     * Thread.interrupt(), as it will preserve the interrupted state
			     * of the thread. 这种sleep方式不会被Thread.interrupt()所打断
			     */
                SystemClock.sleep(200);
                if (this.interrupted()) {
                    Log.i(TAG, "run: --->interrupted");
                    isInterrupted = true;
                    return;
                }
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind: ---->");

        return binder;
    }

    /**
     * 重新绑定时候调用
     * @param intent
     */
    @Override
    public void onRebind(Intent intent) {
        Log.i(TAG, "onRebind: --->");
        super.onRebind(intent);

    }

    /**
     * 解绑时候调用
     * @param intent
     * @return
     */
    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(TAG, "onUnbind: --->");
        sServiceStarted = false;
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
                    //这里很关键，play函数会调用onChange回调函数
                    //也就是调用play就会触发监听，main和play两个activity函数的onChange会触发
                    if (musicEventListener != null) {
                        musicEventListener.onChange(playingPosition);//Onchange函数的触发条件是执行了Play函数，绑定服务成功时也会调用
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        playingPosition = position;
        MyMusicAdapter.setPlayingPosition(position);
        SharedPreferenceUtils.put(PLAY_POS, playingPosition);
    }

    public void pause() {
        if (isPlaying()) {
            mediaPlayer.pause();
        }
    }
    //暂停后播放
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
//        if (!isPlaying())
//            return;
        mediaPlayer.seekTo(msec);
    }

    /**
     * 播放错误时候直接下一首
     */
    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        play(playingPosition);
        return false;
    }




    /**
     * 音乐播放回调接口定义
     */
    public interface OnMusicEventListener {
        public void onPublish(int percent);//更新进度条

        public void onChange(int position);//更换音乐

    }

    public void setOnMusicEventListener(OnMusicEventListener onMusicEventListener) {
       this.musicEventListener=onMusicEventListener;
    }
}
