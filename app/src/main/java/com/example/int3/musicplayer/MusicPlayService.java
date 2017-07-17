package com.example.int3.musicplayer;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.IntDef;

import java.io.File;

// onCreate -> onStartCommand -> Sevice Running -> onDestroy
public class MusicPlayService extends Service {

    MediaPlayer music;
    String FilePath;

    public MusicPlayService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        music = new MediaPlayer();
        music.setOnCompletionListener((mp) -> {stopSelf();});
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        FilePath = intent.getStringExtra("FilePath");
        File mp3 = new File(FilePath);
        if (! mp3.exists()) {
            stopSelf();
        } else {
            // Thread를 통해 음악 재생
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        music.setDataSource(FilePath);
                        music.prepare();
                        music.start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public void onDestroy() {
        music.stop();
        music.release();
    }
}
