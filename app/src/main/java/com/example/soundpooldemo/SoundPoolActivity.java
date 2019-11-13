package com.example.soundpooldemo;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kxyu on 2019-11-08
 */
public class SoundPoolActivity extends AppCompatActivity {

    //Adapter
    private ArrayAdapter adapter;
    private List<Object> soundList = new ArrayList();

    private SoundPool mSoundPool;
    Handler handler ;

    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_player);

        listView = findViewById(R.id.listView);
        handler  = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                adapter.notifyDataSetChanged();
                listView.setOnItemClickListener(onItemClickListener);
                return false;
            }
        });

        createSoundPoolIfNeeded();

        soundList.add(mSoundPool.load(SoundPoolActivity.this, R.raw.tran_9, 1));
        soundList.add(mSoundPool.load(SoundPoolActivity.this, R.raw.one, 1));
        soundList.add(mSoundPool.load(SoundPoolActivity.this, R.raw.two, 1));
        soundList.add(mSoundPool.load(SoundPoolActivity.this, R.raw.three, 1));
        soundList.add(mSoundPool.load(SoundPoolActivity.this, R.raw.four, 1));
        soundList.add(mSoundPool.load(SoundPoolActivity.this, R.raw.five, 1));
        soundList.add(mSoundPool.load(SoundPoolActivity.this, R.raw.six, 1));
        soundList.add(mSoundPool.load(SoundPoolActivity.this, R.raw.seven, 1));
        soundList.add(mSoundPool.load(SoundPoolActivity.this, R.raw.eight, 1));
        soundList.add(mSoundPool.load(SoundPoolActivity.this, R.raw.nine, 1));
        soundList.add(mSoundPool.load(SoundPoolActivity.this, R.raw.suc0, 1));

        adapter = Adapter.songListAdapter(this, android.R.layout.simple_list_item_1, soundList);
        listView.setAdapter(adapter);

        new Thread(new Runnable() {
            @Override
            public void run() {
                String[] soundsNames = null;
                try {
                    //返回指定路径下的所有文件及目录名。
                    soundsNames = getAssets().list("sound");
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if(soundsNames != null){
                    for (String path : soundsNames){
                        String assetPath="sound"+"/"+path;
                        soundList.add(mSoundPool.load(getAssetFileDescription(assetPath), 1));
                    }
                }
                handler.sendEmptyMessage(0);
            }
        }).start();
    }

    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mSoundPool.play((Integer) soundList.get(position), 1, 1, 0, 0, 1);  //③
        }
    };

    /**
     * 创建SoundPool ，注意 api 等级
     */
    private void createSoundPoolIfNeeded() {
        if (mSoundPool == null) {
            // 5.0 及 之后
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                AudioAttributes audioAttributes = null;
                audioAttributes = new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build();

                mSoundPool = new SoundPool.Builder()
                        .setMaxStreams(16)
                        .setAudioAttributes(audioAttributes)
                        .build();
            } else { // 5.0 以前
                /***
                 * 1.maxStreams 同时播放流的最大数量，当播放的流的数目大于此值，则会选择性停止优先级较低的流
                 * 2.streamType 流类型，比如 STREAM_MUSIC
                 * 3.srcQuality 采样率转换器质量,目前没有什么作用,默认填充0
                 */
                mSoundPool = new SoundPool(30, AudioManager.STREAM_MUSIC, 0);  // 创建SoundPool
            }
            mSoundPool.setOnLoadCompleteListener(onLoadCompleteListener);  // 设置加载完成监听
        }
    }

    SoundPool.OnLoadCompleteListener onLoadCompleteListener = new SoundPool.OnLoadCompleteListener() {
        @Override
        public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
            Log.i("kxyu_tag"," sampleId : "+ sampleId+ " status  : "+status);
        }
    };

    /**
     * 释放资源
     */
    private void releaseSoundPool() {
        if (mSoundPool != null) {
            mSoundPool.autoPause();
//            mSoundPool.unload(mSoundId);
//            mSoundId = DEFAULT_INVALID_SOUND_ID;
            mSoundPool.release();
            mSoundPool = null;
        }
    }

    //获取assets下的资源文件，没找的直接catch成指定语音
    private AssetFileDescriptor getAssetFileDescription(String filename) {
        AssetFileDescriptor fileDescriptor = null;
        AssetManager manager = getAssets();
        try {
            fileDescriptor = manager.openFd(filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileDescriptor;
    }


}
