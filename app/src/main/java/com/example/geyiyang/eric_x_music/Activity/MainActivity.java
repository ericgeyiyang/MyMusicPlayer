package com.example.geyiyang.eric_x_music.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.geyiyang.eric_x_music.Adapter.MyPagerAdapter;
import com.example.geyiyang.eric_x_music.Fragment.FragmentMusic;
import com.example.geyiyang.eric_x_music.R;
import com.example.geyiyang.eric_x_music.Service.PlayingService;

public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";

//    private MyMusicAdaper myMusicAdaper;
    private MyPagerAdapter mypagerAdapter;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FragmentMusic fragmentMusic;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.music_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_playing:
                startActivity(new Intent(this,MyMediaPlayerActivity.class));
                break;
            case R.id.action_search:
                break;

            case android.R.id.home: {
                finish();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

//        Log.i(TAG, "--->"+Environment.getExternalStorageState());
//        Log.i(TAG, "--->"+Environment.getExternalStorageDirectory().canWrite());
        Log.i(TAG, "onCreate: --->");
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.music_item_toolbar);
        initalView();
    }

    @Override
    public void onPublish(int progress) {
        return;
    }

    @Override
    public void onChange(int position) {
        Log.i(TAG, "onChange: --->position:"+position);
        if (viewPager.getCurrentItem() == 0) {
            fragmentMusic = (FragmentMusic) mypagerAdapter.getItem(0);
            fragmentMusic.onPlay(position);

        }
    }


    @Override
    protected void onDestroy() {
        Log.i(TAG, "onDestroy: --->");
        super.onDestroy();
    }


    public PlayingService getPlayService() {
        return playingService;//继承BaseActivity
    }
    private void initalView() {
//        fragmentMusic = new FragmentMusic();
        toolbar = (Toolbar) findViewById(R.id.toolBar);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        mypagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mypagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
//        toolbar.setNavigationIcon(R.drawable.actionbar_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
//        final TabLayout.TabLayoutOnPageChangeListener listener = new TabLayout.TabLayoutOnPageChangeListener(tabLayout);
//        viewPager.addOnPageChangeListener(listener);

    }


}
