package com.example.musichub.Models;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;

import com.example.musichub.App;
import com.example.musichub.Interfaces.CheckProgress;
import com.example.musichub.Interfaces.PlayerIndicator;
import com.example.musichub.Interfaces.SongPosition;
import com.example.musichub.Services.MyService;

import java.util.ArrayList;

public class PlayerController implements PlayerIndicator {
    public static MediaPlayer player = MyMediaPlayer.players();
    public static ArrayList<MusicDetail> details = new ArrayList<>();
    public static int position = -1;

    public static Context controllerContext;

    public static PlayerIndicator playerIndicator;

    private void playSong() {
        player.reset();
        try {
            player.setDataSource(details.get(position).getPath());
            player.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }
        player.start();
    }

    @Override
    public void playing() {
        playSong();
    }

    @Override
    public void pauseI() {
        if (player.isPlaying()) {
            player.pause();
        }
    }


    @Override
    public void previous() {
        if (position == 0) {
            position = details.size();
            playSong();
        }
        position -= 1;
        playSong();
    }

    @Override
    public void next() {
        if (position == details.size() - 1) {
            position = 0;
            playSong();
        }
        position += 1;
        playSong();
    }

    @Override
    public void resumeI() {
        if (!player.isPlaying()) {
            player.start();
        }
    }
}
