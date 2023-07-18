package com.example.musichub.Models;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;

import com.example.musichub.Interfaces.CheckProgress;
import com.example.musichub.Interfaces.PlayerIndicator;
import com.example.musichub.Interfaces.SongPosition;
import com.example.musichub.Services.MyService;

import java.util.ArrayList;

public class PlayerController {
    public static MediaPlayer player = MyMediaPlayer.players();
    public static ArrayList<MusicDetail> details = new ArrayList<>();
    public static int position = -1;

    public static Context controllerContext;

    public static PlayerIndicator playerIndicator;

    public static void playSong() {
        player.reset();
        try {
            player.setDataSource(details.get(position).getPath());
            player.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }
        player.start();
    }

    public static void pause() {
        if (player.isPlaying()) {
            player.pause();
        }
    }

    public static void resume() {
        if (!player.isPlaying()) {
            player.start();
        }
    }

    public static void previousSong() {
        if (position == 0) {
            position = details.size();
            playSong();
        }
        position -= 1;
        playSong();
    }

    public static void nextSong() {
        if (position == details.size() - 1) {
            position = 0;
            playSong();
        }
        position += 1;
        playSong();
    }
}
