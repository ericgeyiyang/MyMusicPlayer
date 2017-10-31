package com.example.geyiyang.eric_x_music.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.geyiyang.eric_x_music.Adapter.MyPagerAdapter;
import com.example.geyiyang.eric_x_music.Fragment.FragmentMusic;
import com.example.geyiyang.eric_x_music.R;
import com.example.geyiyang.eric_x_music.Service.PlayingService;
import com.example.geyiyang.eric_x_music.Utils.MusicUtils;

import static com.example.geyiyang.eric_x_music.R.id.tabLayout;

public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";

    private MyPagerAdapter mMyPagerAdapter;
    private Toolbar mToolbar;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private SearchView mSearchView;
    private FragmentMusic mFragmentMusic;

    public FragmentMusic getFragmentMusic() {
        return mFragmentMusic;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.music_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        mSearchView=(SearchView) menuItem.getActionView();
        mSearchView.setQueryHint("搜索歌曲");
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!TextUtils.isEmpty(newText)&&mViewPager.getCurrentItem()==0) {
                    mFragmentMusic.setFilterText(newText);//调用过滤算法
                }
                else if (mViewPager.getCurrentItem()==0)
                {
                    mFragmentMusic.clearTextFilter();
                }
                return false;
            }
        });
        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setIconifiedByDefault(true);
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
        playingService.setMusicInfoList(MusicUtils.getMusicInfoList());
        if (mViewPager.getCurrentItem() == 0) {
            mFragmentMusic = (FragmentMusic) mMyPagerAdapter.getItem(0);
            mFragmentMusic.onPlay(position);
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
        mToolbar = (Toolbar) findViewById(R.id.toolBar);
        mTabLayout = (TabLayout) findViewById(tabLayout);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mTabLayout.addTab(mTabLayout.newTab());
        mTabLayout.addTab(mTabLayout.newTab());
        mTabLayout.addTab(mTabLayout.newTab());
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        mMyPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());//这里开始初始化Fragment
        mViewPager.setAdapter(mMyPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
//        toolbar.setNavigationIcon(R.drawable.actionbar_back);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
//        final TabLayout.TabLayoutOnPageChangeListener listener = new TabLayout.TabLayoutOnPageChangeListener(tabLayout);
//        viewPager.addOnPageChangeListener(listener);

    }


}
