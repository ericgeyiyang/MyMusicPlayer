package com.example.geyiyang.eric_x_music.Utils;

/**
 * Created by geyiyang on 2017/9/21.
 */

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import com.example.geyiyang.eric_x_music.Model.MusicInfo;
import com.example.geyiyang.eric_x_music.Model.MusicInfoComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 开机，增加/删除歌曲时扫描本地歌曲
 */

public class MusicUtils {
    private static final String TAG = "MusicUtils";
//    private AlertDialog mAlertDialog;
//    private AlertDialog.Builder mBuilder;
    private Context mContext = null;
//    private MediaScannerConnection mMediaScannerConnection = null;
//    private int scanTimes = 0;
//    private File mFile;
    /**文件路径集合**/
//    private String[] filePaths;

    private static List<MusicInfo> musicInfoList = new ArrayList<>();//音乐列表储存在这里

    public static void setMusicInfoList(List<MusicInfo> smusicInfoList) {
        musicInfoList = smusicInfoList;
    }
    public static List<MusicInfo> getMusicInfoList() {
        return musicInfoList;
    }

    public MusicUtils() {

    }
    public MusicUtils(Context context) {
//        if (mMediaScannerConnection == null) {
//            mMediaScannerConnection = new MediaScannerConnection(context,this);
//        }
        mContext = context;
    }

//    @Override
//    public void onMediaScannerConnected() {
//        for (int i = 0; i < filePaths.length; i++) {
//            mMediaScannerConnection.scanFile(filePaths[i], null);//服务回调执行扫描
//        }
//    }
//
//    @Override
//    public void onScanCompleted(String path, Uri uri) {
//        scanTimes++;
//        if (scanTimes == filePaths.length) {
//            musicInfoList = ScanMusic();
//            mMediaScannerConnection.disconnect();
//            Intent intent = new  Intent();
//            //设置intent的动作为com.example.broadcast，可以任意定义
//            intent.setAction("com.example.completescanfile");
//            mContext.sendBroadcast(intent);
//            scanTimes=0;
//        }


//    }


//    private void scan(File file) {
//        if (file.exists())
//        {
//            if (file.isFile()) {
//                mMediaScannerConnection.scanFile(file.getAbsolutePath(), null);
//                return;
//        }
//            File[] fileArray = file.listFiles();
//            for (File f : file.listFiles()) {
//                scan(f);
//            }
//        }
//    }
//
//    public void scanFile(String filePath) {
//        try{
//            File file = new File(filePath);
//            Log.i(TAG, "scanFile: --->canread:"+file.canRead());
//            if (file.exists()) {
//                File[] files = file.listFiles();
//                if (files != null) {
//                    Log.i(TAG, "scanFile: ---listfile1："+files[1].getAbsolutePath());
//                }
//                for (int i = 0; i <files.length ; i++) {
//                    if(files[i].isFile())
//                        filePaths[i]=files[i].getAbsolutePath();
//                }
//                mMediaScannerConnection.connect();
//            }
//
//        }catch (Exception e) {
//            Log.e(TAG, "--->" + Log.getStackTraceString(e));
//        }
//    }



//    public static void ScanSdcard(Context context, String filePath) {
//
//        try {
//            File file = new File(filePath);
//            boolean flag = file.canRead();
//            if (file.canRead()) {
//                File[] fileArray = file.listFiles();
//                List<String> stringFileArray = new ArrayList<>();
//                for (int i = 0; i < fileArray.length; i++) {
//
//                    File f = fileArray[i];
//                    if (f.isFile()) {
//                        String name = f.getName();
//                        if (name.endsWith(".mp3") || name.endsWith(".wav") || name.endsWith("" +
//                                ".flac")) {
//                            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + f
//                                    .getAbsolutePath())));
//                            //                            stringFileArray.add(f.getAbsolutePath());
//                        }
//                    }
//                }
//                ////                String[] paths = stringFileArray.toArray(new
//                /// String[stringFileArray.size()]);
//                ////                MediaScannerConnection.scanFile(context, paths, null,
//                ////                        new MediaScannerConnection.OnScanCompletedListener() {
//                ////                            public void onScanCompleted(String path, Uri uri) {
//                //////                                Log.i("ExternalStorage", "Scanned " + path
//                /// + ":");
//                //////                                Log.i("ExternalStorage", "-> uri=" + uri);
//                ////                            }
//                ////                        });
//            }
//        } catch (Exception e) {
//            Log.e(TAG, "--->" + Log.getStackTraceString(e));
//        }
//
//    }


    public static List<MusicInfo> ScanMusic(Context context) {
        MusicInfoComparator comparator = new MusicInfoComparator();
        musicInfoList.clear();
        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media
                .EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        if (cursor == null) {
            return null;
        } else if (!cursor.moveToFirst()) {
            return null;
        } else {
            while (cursor.moveToNext()) {
                int isMusic = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC));
                if (isMusic == 0) continue;
                long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
                // 标题
                String title = cursor.getString((cursor.getColumnIndexOrThrow(MediaStore.Audio
                        .Media.TITLE)));
                // 艺术家
                String artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio
                        .Media.ARTIST));
                // 专辑
                String album = cursor.getString((cursor.getColumnIndexOrThrow(MediaStore.Audio
                        .Media.ALBUM)));
                // 持续时间
                int duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media
                        .DURATION));
                // 音乐uri
                String uri = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media
                        .DATA));
                // 专辑封面id，根据该id可以获得专辑图片uri
                long albumId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media
                        .ALBUM_ID));
                String coverUri = getCoverUri(context, albumId);
                // 音乐文件名
                String fileName = cursor.getString((cursor.getColumnIndexOrThrow(MediaStore.Audio
                        .Media.DISPLAY_NAME)));
                // 音乐文件大小
                long fileSize = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio
                        .Media.SIZE));
                // 发行时间
                String year = cursor.getString((cursor.getColumnIndexOrThrow(MediaStore.Audio
                        .Media.YEAR)));
                MusicInfo music = new MusicInfo();
                music.setId(id);
                music.setType(MusicInfo.Type.LOCAL);
                music.setTitle(title);
                music.setArtist(artist);
                music.setAlbum(album);
                music.setDuration(duration);
                music.setMusicUri(uri);
                music.setCoverUri(coverUri);
                music.setFileName(fileName);
                music.setSize(fileSize);
                music.setCover(BitmapFactory.decodeFile(coverUri));
                music.setPubYear(year);
                music.setmLike(false);
                musicInfoList.add(music);
            }
        }
        cursor.close();
        Collections.sort(musicInfoList,comparator);
        for (int i=0;i<musicInfoList.size();i++ )
        {
            musicInfoList.get(i).setPosition(i);
        }
        Intent intent = new  Intent();
        //设置intent的动作为com.example.broadcast，可以任意定义
        intent.setAction("com.example.completescanfile");
        context.sendBroadcast(intent);
        return musicInfoList;
    }

    private static String getCoverUri(Context context, long albumId) {
        String uri = null;
        Cursor cursor = context.getContentResolver().query(Uri.parse
                ("content://media/external/audio/albums/" + albumId), new String[]{"album_art"},
                null, null, null);
        if (cursor != null) {
            cursor.moveToNext();
            uri = cursor.getString(0);
            cursor.close();
        }
        return uri;
    }

}
