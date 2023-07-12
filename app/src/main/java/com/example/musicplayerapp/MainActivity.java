package com.example.musicplayerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    Button forward_btn,Rewind_btn,Play_btn,Stop_btn;
    TextView time_txt,title_txt;
    SeekBar seekbar;

    MediaPlayer mediaPlayer;
    Handler handlers;

    //Variables
    double startTime=0;
    double finalTime=0;
    int forwardTime=10000;
    int backwardTime=10000;
    static int oneTimeOnly=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Play_btn=findViewById(R.id.idPlayBtn);
        Rewind_btn=findViewById(R.id.idRewindBtn);
        forward_btn=findViewById(R.id.idForwardBtn);
        Stop_btn=findViewById(R.id.idPauseBtn);
        time_txt=findViewById(R.id.idTVTime_left);
        title_txt=findViewById(R.id.idTVSongtitle);
        seekbar=findViewById(R.id.seekBar);

        mediaPlayer=MediaPlayer.create(this,R.raw.song1);
        seekbar.setClickable(false);

        Play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlayMusic();
            }
        });

        Stop_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.pause();
            }
        });
        forward_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int temp=(int) startTime;
                if((temp+forwardTime)<=finalTime){
                    startTime=startTime+forwardTime;
                    mediaPlayer.seekTo((int) startTime);
                }
                else{
                    Toast.makeText(MainActivity.this, "Can't Move Forward", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Rewind_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int temp=(int) startTime;
                if((temp-backwardTime)>0){
                    startTime=startTime-backwardTime;
                    mediaPlayer.seekTo((int) startTime);
                }
                else{
                    Toast.makeText(MainActivity.this, "Can't go back", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void PlayMusic() {
        mediaPlayer.start();
        finalTime= mediaPlayer.getDuration();
        startTime= mediaPlayer.getCurrentPosition();
        if(oneTimeOnly==0){
            seekbar.setMax((int) finalTime);
            oneTimeOnly=1;

        }
        time_txt.setText(String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes((long)finalTime),TimeUnit.MILLISECONDS.toSeconds((long)finalTime)-TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)finalTime))));
        seekbar.setProgress((int) startTime);
        handlers.postDelayed(UpdateSongTime,100);
    }

    //Creating a runnable

    private Runnable UpdateSongTime=new Runnable() {
        @Override
        public void run() {
            startTime=mediaPlayer.getCurrentPosition();
            time_txt.setText(String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes((long)finalTime),TimeUnit.MILLISECONDS.toSeconds((long)finalTime)-TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)finalTime))));

            seekbar.setProgress((int)startTime);
            handlers.postDelayed(this,100);

        }
    };
}