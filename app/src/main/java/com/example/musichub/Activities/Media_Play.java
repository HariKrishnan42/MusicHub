package com.example.musichub.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.musichub.Models.MusicDetail;
import com.example.musichub.Models.MyMediaPlayer;
import com.example.musichub.R;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Media_Play extends AppCompatActivity {

    private SeekBar musicBar;
    private TextView startTime, endTime, songName;

    private ArrayList<MusicDetail> musicDetailArrayList = new ArrayList<>();

    private int position = MyMediaPlayer.currentSong;

    private ImageView playButton, pauseButton, previousButton, nextButton, dropDownButton;

    private final MediaPlayer player = MyMediaPlayer.players();

    private float x1, x2, y1, y2, d1, d2;

    private RelativeLayout swipeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_play);
        musicBar = findViewById(R.id.seekBar);
        startTime = findViewById(R.id.startTime);
        endTime = findViewById(R.id.endTime);
        songName = findViewById(R.id.currentSongName);
        pauseButton = findViewById(R.id.pau_btn);
        previousButton =  findViewById(R.id.prev_btn);
        nextButton =  findViewById(R.id.next_btn);
        playButton = findViewById(R.id.play_btn);
        dropDownButton =  findViewById(R.id.dropDownButton);
        swipeLayout = findViewById(R.id.swipe_layout1);
        intentValue();
        clickListener();
        Handler handler = new Handler();
        Media_Play.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                musicBar.setProgress(player.getCurrentPosition());
                startTime.setText(timeConvert(player.getCurrentPosition()));
                handler.postDelayed(this, 50);
            }

        });
        musicBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (player != null && b) {
                    player.seekTo(i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void clickListener() {
        previousButton.setOnClickListener(view -> previousSong());

        nextButton.setOnClickListener(view -> nextSong());

        pauseButton.setOnClickListener(view -> {
            pauseButton.setVisibility(View.GONE);
            playButton.setVisibility(View.VISIBLE);
            pauseMusic();
        });

        playButton.setOnClickListener(view -> {
            pauseButton.setVisibility(View.VISIBLE);
            playButton.setVisibility(View.GONE);
            resumeMusic();
        });

        dropDownButton.setOnClickListener(view -> {
            MyMediaPlayer.currentSong = position;
            Intent returnIntent = new Intent();
            setResult(RESULT_OK, returnIntent);
            finish();
        });

        swipeLayout.setOnClickListener(view -> {
        });
        swipeLayout.setOnTouchListener((view, motionEvent) -> {
            switch (motionEvent.getAction()) {
                case (MotionEvent.ACTION_DOWN) -> {
                    x1 = motionEvent.getX();
                    y1 = motionEvent.getY();
                }
                case (MotionEvent.ACTION_UP) -> {
                    x2 = motionEvent.getX();
                    y2 = motionEvent.getY();
                    d1 = x2 - x1;
                    d2 = y2 - y1;
                    if (Math.abs(d1) > Math.abs(d2)) {
                        if (d1 > 0) {
                            //Toast.makeText(Media_Play.this, "Left", Toast.LENGTH_SHORT).show();
                            previousSong();
                        } else {
                            //Toast.makeText(Media_Play.this, "Right", Toast.LENGTH_SHORT).show();
                            nextSong();
                        }
                    }
                }
            }
            return false;
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MyMediaPlayer.currentSong = position;
        Intent returnIntent = new Intent();
        setResult(RESULT_OK, returnIntent);
        finish();
    }

    private void intentValue() {
        musicDetailArrayList = (ArrayList<MusicDetail>) getIntent().getSerializableExtra("bundle");
        playMusic();
        player.setOnCompletionListener(mediaPlayer -> nextSong());
    }

    private void playMusic() {
        player.reset();
        playButton.setVisibility(View.GONE);
        pauseButton.setVisibility(View.VISIBLE);
        songName.setText(musicDetailArrayList.get(position).getName());
        int duration = Integer.parseInt(musicDetailArrayList.get(position).getDuration());
        endTime.setText(timeConvert(duration));
        try {
            player.setDataSource(musicDetailArrayList.get(position).getPath());
            player.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }
        musicBar.setProgress(0);
        musicBar.setMax(Integer.parseInt(musicDetailArrayList.get(position).getDuration()));
        player.start();
    }

    private void pauseMusic() {
        if (player.isPlaying()) {
            player.pause();
            musicBar.setProgress(player.getCurrentPosition());
        }
    }

    private void resumeMusic() {
        if (!player.isPlaying()) {
            player.start();
        }
    }

    private void nextSong() {
        if (position == musicDetailArrayList.size() - 1) {
            position = 0;
            playMusic();
        }
        position += 1;
        playMusic();
    }

    private void previousSong() {
        if (position == 0) {
            position = musicDetailArrayList.size();
            playMusic();
        }
        position -= 1;
        playMusic();
    }

    @SuppressLint("DefaultLocale")
    private String timeConvert(int duration) {
        return String.format("%02d : %02d",
                TimeUnit.MILLISECONDS.toMinutes(duration),
                TimeUnit.MILLISECONDS.toSeconds(duration) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration))
        );
    }
}