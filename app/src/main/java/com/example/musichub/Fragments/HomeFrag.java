package com.example.musichub.Fragments;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musichub.Activities.Media_Play;
import com.example.musichub.Adapter.MusicListAdapter;
import com.example.musichub.Models.MusicDetail;
import com.example.musichub.Models.MyMediaPlayer;
import com.example.musichub.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class HomeFrag extends Fragment {
    private ArrayList<String> songName = new ArrayList<>();
    private RecyclerView musicList;
    private ArrayList<MusicDetail> musicDetail = new ArrayList<>();

    private MusicListAdapter musicListAdapter;

    private ImageView playBtn, pauseBtn;

    private TextView currentSongName, currentArtistName;

    private final MediaPlayer player = MyMediaPlayer.players();

    private RelativeLayout minimizeLayout;

    private RelativeLayout minimizeNavi;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_frag, container, false);
        return view;
    }


    public void getAudioList() {
        ContentResolver contentResolver = getActivity().getContentResolver();
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
            if (musicDetail.size() != 0) {
                musicListAdapter = new MusicListAdapter(HomeFrag.this, musicDetail);
                musicList.setAdapter(musicListAdapter);
            }
        }

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        musicList = (RecyclerView) view.findViewById(R.id.listMusic);
        currentArtistName = (TextView) view.findViewById(R.id.minimize_artistName);
        currentSongName = (TextView) view.findViewById(R.id.minimize_songName);
        playBtn = (ImageView) view.findViewById(R.id.minimize_play);
        pauseBtn = (ImageView) view.findViewById(R.id.minimize_pause);
        minimizeLayout = (RelativeLayout) view.findViewById(R.id.minimize_layout);
        minimizeNavi = (RelativeLayout) view.findViewById(R.id.minimize_navi);
        musicList.setLayoutManager(new LinearLayoutManager(getContext()));
        getAudioList();
        onClickListener();
    }

    private void onClickListener() {
        playBtn.setOnClickListener(view -> {
            pauseBtn.setVisibility(View.VISIBLE);
            playBtn.setVisibility(View.GONE);
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
        minimizeNavi.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), Media_Play.class);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200) {
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
}
