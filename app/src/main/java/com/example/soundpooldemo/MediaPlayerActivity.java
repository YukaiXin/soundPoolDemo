package com.example.soundpooldemo;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
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
public class MediaPlayerActivity extends AppCompatActivity {

    //Adapter
    private ArrayAdapter adapter;
    private List<Object> soundList = new ArrayList();

    private MediaPlayer mediaPlayer;
    Handler handler ;

    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_player);

        listView = findViewById(R.id.listView);
        handler  = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                adapter.notifyDataSetChanged();
                listView.setOnItemClickListener(onItemClickListener);
                return false;
            }
        });

        mediaPlayer = new MediaPlayer();
        adapter = Adapter.songListAdapter(this, android.R.layout.simple_list_item_1, soundList);
        listView.setAdapter(adapter);

        soundList.add("/storage/emulated/0/Alarms/print_微信支付线下.mp3");
        soundList.add("/storage/emulated/0/Alarms/tran_ten.mp3");
        soundList.add("sound/print_微信支付线下.mp3");
        soundList.add("sound/tran_ten.mp3");

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
                        soundList.add(assetPath);
                    }
                }
                handler.sendEmptyMessage(0);
            }
        }).start();
    }

    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mediaPlayer.reset();
            try {
                //Set the data source
                String path = (String) soundList.get(position);
                if(path.contains("sound/")){
                    AssetFileDescriptor fd = getAssetFileDescription(path);
                    mediaPlayer.setDataSource(fd.getFileDescriptor(),fd.getStartOffset(),fd.getLength());
                }else {
                    mediaPlayer.setDataSource(path);
                }
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                    mediaPlayer.setDataSource(fd);
//                }
//                mediaPlayer.setDataSource(fd.getFileDescriptor());
//                mediaPlayer.setDataSource();
//                mediaPlayer.setDataSource(MediaPlayerActivity.this, Uri.parse("android.resource://" + MediaPlayerActivity.this.getPackageName() + "/" + R.raw.two));

                //Prepare the media
                mediaPlayer.prepare();

            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                Log.i("kxyu_m"," item : "+ soundList.get(position)+" IllegalArgumentException  : "+e.getMessage());
            } catch (IllegalStateException e) {
                e.printStackTrace();
                Log.i("kxyu_m"," item : "+ soundList.get(position)+" IllegalStateException  : "+e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
                Log.i("kxyu_m"," item : "+ soundList.get(position)+" IOException  : "+e.getMessage());
            }
            mediaPlayer.start();

            Log.i("kxyu_m"," item : "+ soundList.get(position));
        }
    };


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

    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.reset();
    }
}
