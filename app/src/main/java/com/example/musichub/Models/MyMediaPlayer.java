package com.example.musichub.Models;

import android.media.MediaPlayer;

import java.util.ArrayList;
import java.util.List;

public class MyMediaPlayer {
    static MediaPlayer player;

    public static ArrayList<MusicDetail> musicDetails = new ArrayList<>();

    public static MediaPlayer players() {
        if (player == null) {
            player = new MediaPlayer();
        }
        return player;
    }

    public static int currentSong = -1;

    public static String DEVICE_ID = "String";
}
