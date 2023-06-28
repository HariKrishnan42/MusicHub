package com.example.musichub.Activities;

import android.content.Intent;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_play);
        musicBar = findViewById(R.id.seekBar);
        startTime = findViewById(R.id.startTime);
        endTime = findViewById(R.id.endTime);
        songName = findViewById(R.id.currentSongName);
        pauseButton = (ImageView) findViewById(R.id.pau_btn);
        previousButton = (ImageView) findViewById(R.id.prev_btn);
        nextButton = (ImageView) findViewById(R.id.next_btn);
        playButton = (ImageView) findViewById(R.id.play_btn);
        dropDownButton = (ImageView) findViewById(R.id.dropDownButton);
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

    private void clickListener() {
        previousButton.setOnClickListener(view -> {
            previousSong();
        });

        nextButton.setOnClickListener(view -> {
            nextSong();
        });

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
            Intent returnIntent = new Intent();
            setResult(RESULT_OK, returnIntent);
            finish();
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent returnIntent = new Intent();
        setResult(RESULT_OK, returnIntent);
        finish();
    }

    private void intentValue() {
        musicDetailArrayList = (ArrayList<MusicDetail>) getIntent().getSerializableExtra("bundle");
        playMusic();
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                nextSong();
            }
        });
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

    private String timeConvert(int duration) {
        String time = String.format("%02d : %02d",
                TimeUnit.MILLISECONDS.toMinutes(duration),
                TimeUnit.MILLISECONDS.toSeconds(duration) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration))
        );
        return time;
    }
}