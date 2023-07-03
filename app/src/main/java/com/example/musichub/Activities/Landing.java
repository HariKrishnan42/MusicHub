package com.example.musichub.Activities;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.musichub.Adapter.MusicListAdapter;
import com.example.musichub.Fragments.HomeFrag;
import com.example.musichub.Fragments.LibraryFrag;
import com.example.musichub.Models.MusicDetail;
import com.example.musichub.Models.MyMediaPlayer;
import com.example.musichub.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class Landing extends AppCompatActivity {

    private final HomeFrag homeFrag = new HomeFrag();
    private final LibraryFrag libraryFrag = new LibraryFrag();

    private ImageView lpauseBtn, lplayBtn;

    private RelativeLayout lminimizeLayout;

    private RelativeLayout lminimizeNavi;

    private TextView songName, artistName;

    private final MediaPlayer player = MyMediaPlayer.players();

    private final ArrayList<MusicDetail> musicDetail = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        MyMediaPlayer.DEVICE_ID = String.valueOf(Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
        songName = findViewById(R.id.landing_minimize_songName);
        artistName = findViewById(R.id.landing_minimize_artistName);
        lplayBtn = findViewById(R.id.landing_minimize_play);
        lpauseBtn = findViewById(R.id.landing_minimize_pause);
        lminimizeLayout = findViewById(R.id.landing_minimize_layout);
        lminimizeNavi = findViewById(R.id.landing_song_navi);
        bottomNavSelected();
        makeFragment(homeFrag);
        getSongs();
    }

    private void getSongs() {
        ContentResolver contentResolver = getContentResolver();
        Uri songURI = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = contentResolver.query(songURI, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int TITLE = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int ARTIST = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int PATH = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            int DURATION = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
            do {
                if (cursor.getString(DURATION) != null) {
                    String name = cursor.getString(TITLE);
                    String path = cursor.getString(PATH);
                    String duration = cursor.getString(DURATION);
                    String artist = cursor.getString(ARTIST);
                    MusicDetail detail = new MusicDetail(name, path, duration, artist);
                    musicDetail.add(detail);
                }
            } while (cursor.moveToNext());
            MyMediaPlayer.musicDetails = musicDetail;
        }
    }

    private void bottomNavSelected() {
        BottomNavigationView navigationView = findViewById(R.id.bottom_nav);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                boolean state = false;
                if (item.getItemId() == R.id.home) {
                    makeFragment(homeFrag);
                    state = true;
                } else if (item.getItemId() == R.id.library) {
                    makeFragment(libraryFrag);
                    state = true;
                }
                return state;
            }
        });
    }

    private void makeFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
    }

    private void setOnClickListener() {
        lpauseBtn.setOnClickListener(view -> {
            lplayBtn.setVisibility(View.VISIBLE);
            lpauseBtn.setVisibility(View.GONE);
            if (player.isPlaying()) {
                player.pause();
            }
        });

        lplayBtn.setOnClickListener(view -> {
            lplayBtn.setVisibility(View.GONE);
            lpauseBtn.setVisibility(View.VISIBLE);
            if (!player.isPlaying()) {
                player.start();
            }
        });
    }
}

