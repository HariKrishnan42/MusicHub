package com.example.musichub.Fragments;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musichub.Adapter.MusicListAdapter;
import com.example.musichub.Models.MusicDetail;
import com.example.musichub.R;

import java.util.ArrayList;
import java.util.List;

public class HomeFrag extends Fragment {
    private ArrayList<String> songName = new ArrayList<>();
    private RecyclerView musicList;
    private ArrayList<MusicDetail> musicDetail = new ArrayList<>();

    private MusicListAdapter musicListAdapter;

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
                musicListAdapter = new MusicListAdapter(getContext(), musicDetail);
                musicList.setAdapter(musicListAdapter);
            }
        }

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        musicList = (RecyclerView) view.findViewById(R.id.listMusic);
        musicList.setLayoutManager(new LinearLayoutManager(getContext()));
        getAudioList();
    }

}
