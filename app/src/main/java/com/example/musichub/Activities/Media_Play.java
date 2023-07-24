package com.example.musichub.Activities;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.musichub.Models.MusicDetail;
import com.example.musichub.Models.MyMediaPlayer;
import com.example.musichub.Models.PlayerController;
import com.example.musichub.R;
import com.example.musichub.Services.MyService;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Media_Play extends AppCompatActivity {

    private SeekBar musicBar;
    private TextView startTime, endTime, songName;

    private final ArrayList<MusicDetail> musicDetailArrayList = new ArrayList<>();

    private final int position = MyMediaPlayer.currentSong;

    private ImageView playButton, pauseButton, previousButton, nextButton, dropDownButton;

    private final MediaPlayer player = MyMediaPlayer.players();

    private float x1, x2, y1, y2, d1, d2;

    private RelativeLayout swipeLayout;

    private ImageView songImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_play);
        musicBar = findViewById(R.id.seekBar);
        startTime = findViewById(R.id.startTime);
        endTime = findViewById(R.id.endTime);
        songName = findViewById(R.id.currentSongName);
        pauseButton = findViewById(R.id.pau_btn);
        previousButton = findViewById(R.id.prev_btn);
        nextButton = findViewById(R.id.next_btn);
        playButton = findViewById(R.id.play_btn);
        dropDownButton = findViewById(R.id.dropDownButton);
        swipeLayout = findViewById(R.id.swipe_layout1);
        songImage = findViewById(R.id.songImage);
        player.setOnCompletionListener(mediaPlayer -> nextSong());
        clickListener();
        intentValue();
        Handler handler = new Handler();
        Media_Play.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                musicBar.setProgress(PlayerController.player.getCurrentPosition());
                startTime.setText(timeConvert(PlayerController.player.getCurrentPosition()));
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
                            previousSong();
                        } else {
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
        boolean value = getIntent().getBooleanExtra("boolean", false);
        Log.d(TAG, "intentValue: " + value);
        if (!value) {
            playMusic();
        } else {
            prePlay();
        }
    }

    private void prePlay() {
        int duration = Integer.parseInt(PlayerController.details.get(PlayerController.position).getDuration());
        endTime.setText(timeConvert(duration));
        songName.setText(PlayerController.details.get(PlayerController.position).getName());
        musicBar.setMax(Integer.parseInt(PlayerController.details.get(PlayerController.position).getDuration()));
        musicBar.setProgress(PlayerController.player.getCurrentPosition());
        if (PlayerController.player.isPlaying()) {
            pauseButton.setVisibility(View.VISIBLE);
            playButton.setVisibility(View.GONE);
        }
    }

    private void playMusic() {
        Intent intent = new Intent(this, MyService.class);
        startService(intent);
        playButton.setVisibility(View.GONE);
        pauseButton.setVisibility(View.VISIBLE);
        songName.setText(PlayerController.details.get(PlayerController.position).getName());
        int duration = Integer.parseInt(PlayerController.details.get(PlayerController.position).getDuration());
        endTime.setText(timeConvert(duration));
        byte[] image = getSongImage(PlayerController.details.get(PlayerController.position).getPath());
        if (image != null) {
            Glide.with(getApplicationContext()).load(image).into(songImage);
        } else {
            Glide.with(getApplicationContext()).load(R.drawable.app_icon).into(songImage);
        }
        new PlayerController().playing();
        musicBar.setProgress(0);
        musicBar.setMax(Integer.parseInt(PlayerController.details.get(PlayerController.position).getDuration()));
    }

    private byte[] getSongImage(String path) {
        byte[] art = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(path);
        art = retriever.getEmbeddedPicture();
        return art;
    }

    private void pauseMusic() {
        new PlayerController().pauseI();
        musicBar.setProgress(PlayerController.player.getCurrentPosition());
    }

    private void resumeMusic() {
        new PlayerController().resumeI();
    }

    private void nextSong() {
        new PlayerController().next();
        playMusic();
    }

    private void previousSong() {
        new PlayerController().previous();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent(this, MyService.class);
        stopService(intent);
    }
}