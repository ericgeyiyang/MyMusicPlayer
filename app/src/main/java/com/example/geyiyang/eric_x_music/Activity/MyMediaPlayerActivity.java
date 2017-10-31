package com.example.geyiyang.eric_x_music.Activity;

import android.animation.ObjectAnimator;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.geyiyang.eric_x_music.App.App;
import com.example.geyiyang.eric_x_music.Model.MusicInfo;
import com.example.geyiyang.eric_x_music.R;
import com.example.geyiyang.eric_x_music.Service.PlayingService;
import com.example.geyiyang.eric_x_music.Utils.MusicUtils;
import com.example.geyiyang.eric_x_music.View.CDview;

import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * 播放界面
 */
public class MyMediaPlayerActivity extends BaseActivity implements View.OnClickListener{
    private static final String TAG = "MyMediaPlayerActivity";
    private static final int MSG = 1;
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
    private CDview imageView_disc_internal;
    private ImageView imageView_disc_external;
    private ImageView imageView_needle;
    private ImageView imageView_like;
    private SeekBar seekBar;
    private List<MusicInfo> musicInfoList;

    private Myhandler handler;
    FrameLayout rotate_layout;
//    ObjectAnimator rotateAnimation;
    ObjectAnimator needleAnimation;

    private PointF mCenter;//光盘的中心相对坐标
    private float mDegree=0;//上一次触发ontouch的角度
    private float mStart_degree=0;//开始点击的角度
    private float mRadius=0;
    private int mProgress=0;//旋转光盘时对应的progress
    private int mProgress_old=0;//旋转光盘时上一次触发ontouch对应的progress
    private float mOld_x=0;//上一次触发ontouch的x坐标
    private long mOld_time=0;//上一次触发ontouch的系统时间
    private float[] mIncreased_degree={0,0,0,0,0};//储存5次的旋转增加角度，用于计算平均角速度
    private int[] mDurationTime={0,0,0,0,0};//两次ontouch触发的时间间隔
    private int mCount=0;


    /**
     * 更新seekbar等UI
     */
    class Myhandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MSG) {
                int progress = msg.arg1;
                seekBar.setProgress(progress);
                int duration_min = progress / 1000 / 60;
                int duration_sec = (progress / 1000) % 60;
                textView_currentTime.setText(String.format("%02d", duration_min) + ":" + String
                        .format("%02d", duration_sec));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.playing_menu, menu);
        return true;
    }

    /**
     * 绑定服务，为当前Activity提供服务和监听
     */
    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: --->bindService");
        allowBindService();
    }

    @Override
    protected void onPause() {
        Log.i(TAG, "onPause: --->UnbindService");
        allowUnbindService();
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate: --->");
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.media_player_layout);
        initalView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: --->");
        getPlayService().getMyThread().interrupt();
        //        rotateAnimation.setAutoCancel(true);
        needleAnimation.setAutoCancel(true);

    }

    /**
     * 实现BaseActivity的更新进度方法，
     * 因为onPublish在Service中是新开线程，所以要加入looper消息队列通知UI线程更新
     *
     * @param progress 进度
     */
    @Override
    public void onPublish(int progress) {
        Message msg = new Message();
        msg.what = MSG;
        msg.arg1 = progress;
        handler.sendMessage(msg);
    }

    /**
     * 在主线程中触发的onChange函数（Service的play函数）,不需要Handler
     *
     * @param position 歌曲在list中的位置
     */
    @Override
    public void onChange(int position) {
        Log.i(TAG, "onChange: --->");
        onPlay(position);
        setBackground(position);
        getPlayService().StartThread();
        //        imageView_play.setImageResource(R.drawable.playing_btn_pause);
    }

    public void setBackground(int position) {
        //高斯模糊的处理
        Glide.with(MyMediaPlayerActivity.this).load(musicInfoList.get(position).getCoverUri())
                .error(R.drawable.fm_run_result_bg).placeholder(imageView_background.getDrawable
                ()).crossFade(1000).bitmapTransform(new BlurTransformation(MyMediaPlayerActivity
                .this)).into(imageView_background);
        //        加载专辑图片

        //        Glide.with(MyMediaPlayerActivity.this)
        //                .load(musicInfoList.get(position).getCoverUri())
        //                .error(R.drawable.placeholder_disk_play_song)
        //                .centerCrop()
        //                .crossFade(500)
        //                .into(imageView_disc_internal);


        if (getPlayService().isPlaying()) {
            //            rotateAnimation.start();
            needleAnimation.start();
        }
        else {
            //            rotateAnimation.pause();
            needleAnimation.pause();
        }
    }

    /**
     * 更新UI
     *
     * @param position 当前播放歌曲
     */
    private void onPlay(int position) {
        MusicInfo music = musicInfoList.get(position);
        textView_title.setText(music.getTitle());
        textView_singer.setText(music.getArtist());
        imageView_like.setImageResource(R.drawable.play_rdi_icn_loved_dis);
        seekBar.setMax(music.getDuration());
        if (musicInfoList.get(position).getCover() != null)
            imageView_disc_internal.setImage(musicInfoList.get(position).getCover());
        if (getPlayService().isPlaying()) {
            imageView_disc_internal.start();
            Log.i(TAG, "onPlay: --->start rotating");
            imageView_play.setImageResource(R.drawable.playing_btn_pause);
        } else {
            imageView_disc_internal.pause();
            imageView_play.setImageResource(R.drawable.playing_btn_play);
        }

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
                    needleAnimation.reverse();
                    //                    rotateAnimation.pause();
                    imageView_disc_internal.pause();
                } else

                {
                    getPlayService().replay();
                    imageView_play.setImageResource(R.drawable.playing_btn_pause);
                    needleAnimation.start();
                    imageView_disc_internal.start();
                    //                    rotateAnimation.resume();
                }

                break;
            case R.id.playing_pre:
                getPlayService().pre();
                break;
            case R.id.playing_next:
                getPlayService().next();
                break;
            case R.id.back:
                finish();
                break;
            case R.id.playing_fav:
                if (musicInfoList.get(getPlayService().getPlayingPosition()).getmLike()) {
                    musicInfoList.get(getPlayService().getPlayingPosition()).setmLike(false);
                    imageView_like.setImageResource(R.drawable.play_rdi_icn_loved);
                } else {
                    musicInfoList.get(getPlayService().getPlayingPosition()).setmLike(true);
                    imageView_like.setImageResource(R.drawable.play_rdi_icn_loved_dis);
                }
            default:
                break;
        }
    }

    private float calcDegree(float x, float y) {
        float vector_X = x - mCenter.x;
        float vector_Y = y - mCenter.y;
        double distance_X = Math.sqrt(Math.pow(vector_X, 2) + Math.pow(vector_Y, 2));
        double distance_X_top = Math.sqrt(Math.pow(x-mCenter.x, 2) + Math.pow(y, 2));

        double cos_degree = (Math.pow(distance_X, 2) + Math.pow(mRadius, 2) - Math.pow
                (distance_X_top, 2)) / (2 * distance_X * mRadius);
        if (cos_degree >= 1) {
            cos_degree = 1f;
        }

        double radian = Math.acos(cos_degree);
        float degree = (float) (radian * 180 / Math.PI);
        if(x<mCenter.x) degree = 360 - degree;
        return degree;
    }

    private int calcProgress(float degree) {
        int progress = 0;
        float division=seekBar.getMax()/360f;//每度对应的progress值
         progress=(int)(mProgress_old +degree*division);
//        Log.i(TAG, "calcProgress: --->"+mProgress_old+","+progress+","+degree*division+"--");
        if ( progress > seekBar.getMax()) {
            progress = seekBar.getMax();
//            Log.i(TAG, "calcProgress: --->bigger");
        }
        if ( progress < 0) {
             progress = 0;
        }
//        Log.i(TAG, "calcProgress: --->"+progress+","+seekBar.getMax());
        return  progress;
    }
    private float calcIncreasedDegree(float x,float y,float current_degree)
    {
        float increased_degree;
        if ((mOld_x - mCenter.x)<0&& (x - mCenter.x)>0&&
                y<mCenter.y)
        {
            increased_degree = current_degree-mDegree+360;
        } else if ((mOld_x - mCenter.x) > 0 && (x - mCenter.x) < 0 &&
                y < mCenter.y) {
            increased_degree = current_degree-mDegree-360;
        }
        else
            increased_degree = current_degree-mDegree;
        return  increased_degree;
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
        imageView_disc_internal = (CDview) findViewById(R.id.default_disk_img);
        imageView_disc_external = (ImageView) findViewById(R.id.img_disc);
        imageView_needle = (ImageView) findViewById(R.id.needle);
        imageView_like = (ImageView) findViewById(R.id.playing_fav);

        textView_singer = (TextView) findViewById(R.id.singer);
        textView_title = (TextView) findViewById(R.id.song);
        textView_currentTime = (TextView) findViewById(R.id.currentTime);
        textView_totalTime = (TextView) findViewById(R.id.totalTime);

        rotate_layout = (FrameLayout) findViewById(R.id.rotate_layout);

        seekBar = (SeekBar) findViewById(R.id.playSeekBar);
        needleAnim();
        //        rotateAnim();
        // 动态设置seekbar的margin
        ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) seekBar.getLayoutParams();
        p.leftMargin = (int) (App.screenWidth * 0.03);
        p.rightMargin = (int) (App.screenWidth * 0.03);
        imageView_disc_external.setLongClickable(true);
        ViewTreeObserver vto2 = imageView_disc_external.getViewTreeObserver();
        vto2.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                imageView_disc_external.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                float height = (float) imageView_disc_external.getHeight();
                mCenter = new PointF(height / 2, height / 2);
                mRadius = mCenter.x;
                Log.i(TAG, "onGlobalLayout: --->" + mCenter);

            }
        });
        imageView_disc_external.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
//                Log.i(TAG, "onTouch: --->");
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    imageView_disc_internal.pause();
                    mStart_degree=calcDegree(event.getX(),event.getY());
                    getPlayService().getMyThread().interrupt();
                    mDegree = mStart_degree;
                    mProgress=mProgress_old = seekBar.getProgress();
                    mOld_time = System.currentTimeMillis();
                    mOld_x = event.getX();
                    for (int i = 0; i < mIncreased_degree.length; i++) {
                        mDurationTime[i]=0;
                        mIncreased_degree[i]=0;
                    }
                    mCount=0;
                }

                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    if (mCount >= 5) {
                        mCount=0;
                    }
                    long now=System.currentTimeMillis();
                    mDurationTime[mCount] = (int)(now - mOld_time);
                    mOld_time=now;
                    float current_degree=calcDegree(event.getX(),event.getY());
                    mIncreased_degree[mCount]=calcIncreasedDegree(event.getX(),event.getY(),current_degree);

                    mOld_x = event.getX();
                    mProgress = calcProgress(mIncreased_degree[mCount]);
                    mProgress_old = mProgress;
                    seekBar.setProgress(mProgress);
                    imageView_disc_internal.setRotation(mIncreased_degree[mCount]);//更新CDview
                    mDegree = current_degree;
                    mCount++;
//                    Log.i(TAG, "onTouch: --->"+mIncreased_degree+","+mDurationTime+","+mIncreased_degree/mDurationTime);
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    int sum_time=0;
                    float sum_increasedDegree=0;
                    //计算最后5次ontouch的平均角速度
                    for (int i = 0; i < mIncreased_degree.length; i++) {
                        sum_time += mDurationTime[i];
                        sum_increasedDegree += mIncreased_degree[i];
                    }
                    if (sum_time != 0) {
                        imageView_disc_internal.decelerate(sum_increasedDegree / sum_time, getPlayService().isPlaying());
                        getPlayService().seek(mProgress);
                    }
                    else if (getPlayService().isPlaying()) {
                        imageView_disc_internal.start();
                    }
                    Log.i(TAG, "onTouch: --->"+sum_increasedDegree/5+","+sum_time/5);
                    getPlayService().StartThread();
                }
                return false;
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                getPlayService().getMyThread().interrupt();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                getPlayService().seek(progress);
                getPlayService().StartThread();
            }
        });
        imageView_play.setOnClickListener(this);
        imageView_pre.setOnClickListener(this);
        imageView_next.setOnClickListener(this);
        imageView_back.setOnClickListener(this);
        imageView_like.setOnClickListener(this);
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

    //黑胶旋转动画效果
//    public void rotateAnim() {
//        rotateAnimation = ObjectAnimator.ofFloat(rotate_layout, "rotation", 0, 359);
//        rotateAnimation.setInterpolator(new LinearInterpolator());
//        rotateAnimation.setDuration(25 * 1000);
//        rotateAnimation.setRepeatCount(ValueAnimator.INFINITE);
//    }

    //指针旋转动画效果
    public void needleAnim() {
        needleAnimation = ObjectAnimator.ofFloat(imageView_needle, "rotation", -25, 0);
        needleAnimation.setDuration(500);
        needleAnimation.setRepeatCount(0);
        needleAnimation.setInterpolator(new LinearInterpolator());
    }
}


