package com.example.geyiyang.eric_x_music.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.geyiyang.eric_x_music.Model.MusicInfo;
import com.example.geyiyang.eric_x_music.R;
import com.example.geyiyang.eric_x_music.Utils.MusicUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by geyiyang on 2017/9/27.
 */

public class SplashActivity extends AppCompatActivity
{
    private static final long DELAYTIME  = 2000;//2s
    private List<MusicInfo> musicInfoList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        // 全屏
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.welcome);
        musicInfoList = new ArrayList<>();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
                {
                    MusicUtils.ScanSdcard(SplashActivity.this, Environment.getExternalStorageDirectory().getAbsolutePath() + "/Music");//扫描MediaStore的音乐，但List还没有更新
                }
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        },DELAYTIME);

    }

}

