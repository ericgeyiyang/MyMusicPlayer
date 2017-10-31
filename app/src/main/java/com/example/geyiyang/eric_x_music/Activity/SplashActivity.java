package com.example.geyiyang.eric_x_music.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.geyiyang.eric_x_music.R;
import com.example.geyiyang.eric_x_music.Service.PlayingService;

/**
 * Created by geyiyang on 2017/9/27.
 */

public class SplashActivity extends AppCompatActivity
{
    private static final long DELAYTIME  = 2000;//2s
    //    private List<MusicInfo> musicInfoList;
    private MyReceiver mMyReceiver;
    public class MyReceiver extends BroadcastReceiver {
        public MyReceiver() {
        }
        @Override
        public void onReceive(Context context, Intent intent) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                }
            },DELAYTIME);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mMyReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMyReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.completescanfile");
        registerReceiver(mMyReceiver, intentFilter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        startService(new Intent(this, PlayingService.class));

        //        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        // 全屏
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.welcome);
        if(PlayingService.sServiceStarted==true)
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                }
            },DELAYTIME);
//        musicInfoList = new ArrayList<>();
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
////                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
////                {
////                    MusicUtils.ScanSdcard(SplashActivity.this, Environment.getExternalStorageDirectory().getAbsolutePath() + "/Music");//扫描MediaStore的音乐，但List还没有更新
////                }
//                startActivity(new Intent(SplashActivity.this, MainActivity.class));
//                finish();
//            }
//        },DELAYTIME);

    }


}

