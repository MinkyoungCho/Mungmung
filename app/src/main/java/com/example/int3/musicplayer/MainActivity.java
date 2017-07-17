package com.example.int3.musicplayer;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;

public class MainActivity extends AppCompatActivity {

    Button button;
    SeekBar seekbar;
    MediaPlayer music;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        music = MediaPlayer.create(this, R.raw.konan);
        music.setLooping(true);

        button = (Button) findViewById(R.id.button1);
        button.setOnClickListener((v) -> {
            if (music.isPlaying()) {
                music.stop();
                try {
                    music.prepare();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                music.seekTo(0);
                button.setText("Music start");
                seekbar.setProgress(0);
            } else {
                music.start();
                button.setText("Music Stop");

                startThread();
            }
        });

        seekbar = (SeekBar) findViewById(R.id.seekbar1);
        seekbar.setMax(music.getDuration());
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    music.seekTo(progress); // 재생위치 변경
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        editText = (EditText) findViewById(R.id.edittext1);
    }

    // Seekbar 위치 조정
    public void startThread() {
        Thread thread = new Thread(() -> {
            while (music.isPlaying()) {
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                seekbar.setProgress(music.getCurrentPosition());
            }
        });

        thread.start();
    }

    public void startMusicService(View view) {
        Intent service = new Intent(this, MusicPlayService.class);
        service.putExtra("FilePath", editText.getText().toString());
        startService(service);
    }

    public void stopMusicService(View v) {
        Intent service = new Intent(this, MusicPlayService.class);
        stopService(service);
    }

}
