package com.example.musichub.Models;

import android.media.MediaPlayer;

public class MyMediaPlayer {
    static MediaPlayer player;

    public static MediaPlayer players() {
        if (player == null) {
            player = new MediaPlayer();
        }
        return player;
    }

    public static int currentSong = -1;

    public static String DEVICE_ID = "String";
}
