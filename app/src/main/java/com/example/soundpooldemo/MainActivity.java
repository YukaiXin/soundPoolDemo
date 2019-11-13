package com.example.soundpooldemo;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

/**
 * Created by kxyu on 2019-11-08
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Check the access permission
        if (AccessUtil.verifyStoragePermissions(this)) {

        }
    }

    public void goToSoundPoolActivity(View view){
        Intent intent = new Intent();
        intent.setClass(this, SoundPoolActivity.class);
        startActivity(intent);
    }

    public void goToMediaPlayerActivity(View view){
        Intent intent = new Intent();
        intent.setClass(this, MediaPlayerActivity.class);
        startActivity(intent);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast.makeText(this, "你拒绝了访问SD卡的权限！", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }
}
