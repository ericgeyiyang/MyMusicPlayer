package com.example.geyiyang.eric_x_music.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.geyiyang.eric_x_music.App.App;
import com.example.geyiyang.eric_x_music.Model.MusicInfo;
import com.example.geyiyang.eric_x_music.R;
import com.example.geyiyang.eric_x_music.Service.PlayingService;
import com.example.geyiyang.eric_x_music.Utils.MusicUtils;

import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class MyMediaPlayerActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "MyMediaPlayerActivity";
    private Toolbar toolbar;
    private TextView textView_title;
    private TextView textView_singer;
    private TextView textView_totalTime;
    private TextView textView_currentTime;

    private ImageView imageView_pre;
    private ImageView imageView_next;
    private ImageView imageView_play;
    private ImageView imageView_back;
    private ImageView imageView_background;
    private ImageView imageView_disc;

    private SeekBar seekBar;
    private int position;
    private List<MusicInfo> musicInfoList;

    private Myhandler handler;

    class Myhandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                int progress = msg.arg1;
                seekBar.setProgress(progress);
                int duration_min=progress/1000/60;
                int duration_sec=(progress/1000)%60;
                textView_currentTime.setText(String.format("%02d",duration_min) +":" + String.format("%02d",duration_sec));
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.playing_menu, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: --->bindservice");
        allowBindService();
    }
    @Override
    protected void onPause() {
        Log.i(TAG, "onPause: --->unbindservice");
        allowUnbindService();
        super.onPause();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.media_player_layout);
        initalView();
    }

    /**
     * 实现BaseActivity的更新进度方法
     * @param progress 进度
     */
    @Override
    public void onPublish(int progress) {
        Message msg = new Message();
        msg.what=0;
        msg.arg1 = progress;
        handler.sendMessage(msg);
    }
    @Override
    public void onChange(int position) {
        Log.i(TAG, "onChange: --->");
        onPlay(position);
        setBackground(position);
//        imageView_play.setImageResource(R.drawable.playing_btn_pause);
    }

    public void setBackground(int position) {
        //高斯模糊的处理
        Glide.with(MyMediaPlayerActivity.this)
                .load(musicInfoList.get(position).getCoverUri())
                .error(R.drawable.fm_run_result_bg)
                .placeholder(imageView_background.getDrawable())
                .crossFade(1000)
                .bitmapTransform(new BlurTransformation(MyMediaPlayerActivity.this))
                .into(imageView_background);
        //加载专辑图片
        Glide.with(MyMediaPlayerActivity.this)
                .load(musicInfoList.get(position).getCoverUri())
                .error(R.drawable.placeholder_disk_play_song)
                .centerCrop()
                .crossFade(500)
                .into(imageView_disc);
    }

    private void onPlay(int position) {
        MusicInfo music = musicInfoList.get(position);
        textView_title.setText(music.getTitle());
        textView_singer.setText(music.getArtist());
        seekBar.setMax(music.getDuration());
        imageView_play.setImageResource(R.drawable.playing_btn_pause);
        textView_totalTime.setText(getPlayService().getDuration());
        }
    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.playing_play:
                if (getPlayService().isPlaying()) {
                    imageView_play.setImageResource(R.drawable.playing_btn_play);
                    getPlayService().pause();
                } else
                {
                    getPlayService().replay();
                    imageView_play.setImageResource(R.drawable.playing_btn_pause);
                }
                break;
            case R.id.playing_pre:
                getPlayService().pre();
//                if (position <= 0) position = musicInfoList.size() - 1;
//                else position--;
//                onChange(position);
                break;
            case R.id.playing_next:
                getPlayService().next();
//                if (position >= musicInfoList.size() - 1) position=0;
//                else position++;
//                onChange(position);
                break;
            case R.id.back:
                finish();
                break;
            default:break;
        }
    }


    public PlayingService getPlayService() {
        return playingService;//继承BaseActivity
    }

    private void initalView() {
        handler = new Myhandler();
        toolbar = (Toolbar) findViewById(R.id.play_toolbar);
        imageView_next = (ImageView) findViewById(R.id.playing_next);
        imageView_play = (ImageView) findViewById(R.id.playing_play);
        imageView_pre = (ImageView) findViewById(R.id.playing_pre);
        imageView_back = (ImageView) findViewById(R.id.back);
        imageView_background = (ImageView) findViewById(R.id.play_background);
        imageView_disc = (ImageView) findViewById(R.id.default_disk_img);

        textView_singer = (TextView) findViewById(R.id.singer);
        textView_title = (TextView) findViewById(R.id.song);
        textView_currentTime = (TextView) findViewById(R.id.currentTime);
        textView_totalTime = (TextView) findViewById(R.id.totalTime);
        seekBar = (SeekBar) findViewById(R.id.playSeekBar);
        // 动态设置seekbar的margin
        ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) seekBar
                .getLayoutParams();
        p.leftMargin = (int) (App.screenWidth * 0.03);
        p.rightMargin = (int) (App.screenWidth * 0.03);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                getPlayService().seek(progress);
            }
        });
        imageView_play.setOnClickListener(this);
        imageView_pre.setOnClickListener(this);
        imageView_next.setOnClickListener(this);
        imageView_back.setOnClickListener(this);
        musicInfoList = MusicUtils.getMusicInfoList();
//        Intent intent = new Intent();
//        position = (Integer) SharedPreferenceUtils.get(this, Constant.PLAY_POS, 0);
//        onPlay(position);
//        textView_title.setText(musicInfoList.get(position).getTitle());
//        textView_singer.setText(musicInfoList.get(position).getArtist());
//        imageView_play.setImageResource(R.drawable.playing_btn_pause);

//        toolbar.setNavigationIcon(R.drawable.actionbar_back);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);
    }
}


