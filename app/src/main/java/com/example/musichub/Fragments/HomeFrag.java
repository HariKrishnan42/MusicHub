package com.example.musichub.Fragments;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musichub.Adapter.MusicListAdapter;
import com.example.musichub.Models.MusicDetail;
import com.example.musichub.Models.MyMediaPlayer;
import com.example.musichub.R;

import java.util.ArrayList;

public class HomeFrag extends Fragment {
    private RecyclerView musicList;
    private ArrayList<MusicDetail> musicDetail = new ArrayList<>();

    private ImageView playBtn, pauseBtn;

    private TextView currentSongName, currentArtistName;

    private final MediaPlayer player = MyMediaPlayer.players();

    private RelativeLayout minimizeLayout;

    private RelativeLayout minimizeNavi;

    private int position = 0;
    private float x1, x2, y1, y2, d1, d2;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_frag, container, false);
    }


    @SuppressLint("Recycle")
    public void getAudioList() {
        ContentResolver contentResolver = requireActivity().getContentResolver();
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
                    //musicDetail.add(detail);
                }
            } while (cursor.moveToNext());
            musicDetail = MyMediaPlayer.musicDetails;
            if (musicDetail.size() != 0) {
                MusicListAdapter musicListAdapter = new MusicListAdapter(HomeFrag.this, musicDetail);
                musicList.setAdapter(musicListAdapter);
            }
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        musicList = view.findViewById(R.id.listMusic);
        currentArtistName = view.findViewById(R.id.minimize_artistName);
        currentSongName = view.findViewById(R.id.minimize_songName);
        playBtn = view.findViewById(R.id.minimize_play);
        pauseBtn = view.findViewById(R.id.minimize_pause);
        minimizeLayout = view.findViewById(R.id.minimize_layout);
        minimizeNavi = view.findViewById(R.id.minimize_navi);
        musicList.setLayoutManager(new LinearLayoutManager(getContext()));
        getAudioList();
        onClickListener();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void onClickListener() {
        playBtn.setOnClickListener(view -> {
            playBtn.setVisibility(View.GONE);
            pauseBtn.setVisibility(View.VISIBLE);
            if (!player.isPlaying()) {
                player.start();
            }
        });
        pauseBtn.setOnClickListener(view -> {
            pauseBtn.setVisibility(View.GONE);
            playBtn.setVisibility(View.VISIBLE);
            if (player.isPlaying()) {
                player.pause();
            }
        });
        minimizeNavi.setOnTouchListener((view, motionEvent) -> {

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
                            prevSong();
                        } else {
                            nextSong();
                        }
                    }
                }
            }
            return false;
        });
    }
    private void prevSong() {
        if (position == 0) {
            position = musicDetail.size();
            playSong();
        }
        position -= 1;
        playSong();
    }
    private void nextSong() {
        if (position == musicDetail.size() - 1) {
            position = 0;
            playSong();
        }
        position += 1;
        playSong();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 201) {
            position = MyMediaPlayer.currentSong;
            minimizeLayout.setVisibility(View.VISIBLE);
            currentSongName.setText(musicDetail.get(MyMediaPlayer.currentSong).getName());
            currentArtistName.setText(musicDetail.get(MyMediaPlayer.currentSong).getArtist());
            if (player.isPlaying()) {
                playBtn.setVisibility(View.GONE);
                pauseBtn.setVisibility(View.VISIBLE);
            } else if (!player.isPlaying()) {
                playBtn.setVisibility(View.VISIBLE);
                pauseBtn.setVisibility(View.GONE);
            }
        }
    }

    private void playSong() {
        player.reset();
        currentArtistName.setText(musicDetail.get(position).getArtist());
        currentSongName.setText(musicDetail.get(position).getName());
        playBtn.setVisibility(View.GONE);
        pauseBtn.setVisibility(View.VISIBLE);
        try {
            player.setDataSource(musicDetail.get(position).getPath());
            player.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }
        player.start();
    }
}
