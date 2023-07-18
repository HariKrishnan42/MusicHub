package com.example.musichub.Activities;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.musichub.Fragments.HomeFrag;
import com.example.musichub.Fragments.LibraryFrag;
import com.example.musichub.Models.MusicDetail;
import com.example.musichub.Models.MyMediaPlayer;
import com.example.musichub.Models.PlayerController;
import com.example.musichub.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class Landing extends AppCompatActivity {

    private final HomeFrag homeFrag = new HomeFrag();
    private final LibraryFrag libraryFrag = new LibraryFrag();

    private ImageView lpauseBtn, lplayBtn, skippedPrevious, skippedNext, songCover;

    private RelativeLayout lminimizeLayout, songAbout;

    private RecyclerView swipeLayout;

    private final MediaPlayer player = MyMediaPlayer.players();

    private final ArrayList<MusicDetail> musicDetail = new ArrayList<>();
    private int position = 0;

    private TextView songName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PlayerController.controllerContext = getApplicationContext();
        setContentView(R.layout.activity_landing);
        MyMediaPlayer.DEVICE_ID = String.valueOf(Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
        lplayBtn = findViewById(R.id.landing_minimize_play);
        lpauseBtn = findViewById(R.id.landing_minimize_pause);
        lminimizeLayout = findViewById(R.id.landing_minimize_layout);
        songAbout = findViewById(R.id.minimize_song_About);
        swipeLayout = findViewById(R.id.minimize_recycler);
        skippedNext = findViewById(R.id.skipped_next);
        skippedPrevious = findViewById(R.id.skipped_previous);
        songCover = findViewById(R.id.minimize_song_image);
        songName = findViewById(R.id.minimize_song_name);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        swipeLayout.setLayoutManager(linearLayoutManager);
        swipeLayout.scrollToPosition(0);
//        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
//        itemTouchHelper.attachToRecyclerView(swipeLayout);
        if (player.isPlaying()) {
            player.setOnCompletionListener(mediaPlayer -> PlayerController.nextSong());
        }
        bottomNavSelected();
        makeFragment(homeFrag);
        setOnClickListener();
        getSongs();
        swipe();
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
            PlayerController.details = musicDetail;
        }
    }

    private void bottomNavSelected() {
        BottomNavigationView navigationView = findViewById(R.id.bottom_nav);
        navigationView.setOnNavigationItemSelectedListener(item -> {
            boolean state = false;
            if (item.getItemId() == R.id.home) {
                makeFragment(homeFrag);
                state = true;
            } else if (item.getItemId() == R.id.library) {
                makeFragment(libraryFrag);
                state = true;
            }
            return state;
        });
    }

//    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback() {
//        @Override
//        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
//            return false;
//        }
//
//        @Override
//        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
//
//        }
//    };

    private void makeFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
    }

    private void setOnClickListener() {
        lpauseBtn.setOnClickListener(view -> {
            lplayBtn.setVisibility(View.VISIBLE);
            lpauseBtn.setVisibility(View.GONE);
            PlayerController.pause();
        });

        lplayBtn.setOnClickListener(view -> {
            lplayBtn.setVisibility(View.GONE);
            lpauseBtn.setVisibility(View.VISIBLE);
            PlayerController.resume();
        });
        skippedPrevious.setOnClickListener(view -> {
            prevSong();
        });

        skippedNext.setOnClickListener(view -> {
            nextSong();
        });

        songAbout.setOnClickListener(view -> {
            Intent intent = new Intent(Landing.this, Media_Play.class);
            intent.putExtra("boolean", true);
            startActivity(intent);
        });
    }

    private void swipe() {

        /*lminimizeNavi.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
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
                            position = PlayerController.position;
                            if (d1 > 0) {
                                prevSong();
                            } else {
                                nextSong();
                            }
                        }
                    }
                }
                return false;
            }
        });*/
    }

    private void prevSong() {
        PlayerController.previousSong();
        songName.setText(PlayerController.details.get(PlayerController.position).getName());
        byte[] image = getSongImage(PlayerController.details.get(PlayerController.position).getPath());
        if (image != null) {
            Glide.with(getApplicationContext()).load(image).into(songCover);
        } else {
            Glide.with(getApplicationContext()).load(R.drawable.app_icon).into(songCover);
        }
        lpauseBtn.setVisibility(View.VISIBLE);
        lplayBtn.setVisibility(View.GONE);
    }

    private void nextSong() {
        PlayerController.nextSong();
        songName.setText(PlayerController.details.get(PlayerController.position).getName());
        byte[] image = getSongImage(PlayerController.details.get(PlayerController.position).getPath());
        if (image != null) {
            Glide.with(getApplicationContext()).load(image).into(songCover);
        } else {
            Glide.with(getApplicationContext()).load(R.drawable.app_icon).into(songCover);
        }
        if (PlayerController.player.isPlaying()) {
            lpauseBtn.setVisibility(View.VISIBLE);
            lplayBtn.setVisibility(View.GONE);
        }
    }

    public void openMinimizePlayer() {
        lminimizeLayout.setVisibility(View.VISIBLE);
        songName.setText(PlayerController.details.get(PlayerController.position).getName());
        byte[] image = getSongImage(PlayerController.details.get(PlayerController.position).getPath());
        if (image != null) {
            Glide.with(getApplicationContext()).load(image).into(songCover);
        } else {
            Glide.with(getApplicationContext()).load(R.drawable.app_icon).into(songCover);
        }
        if (player.isPlaying()) {
            lpauseBtn.setVisibility(View.VISIBLE);
            lplayBtn.setVisibility(View.GONE);
        } else if (!player.isPlaying()) {
            lplayBtn.setVisibility(View.VISIBLE);
            lpauseBtn.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private byte[] getSongImage(String path) {
        byte[] art = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(path);
        art = retriever.getEmbeddedPicture();
        return art;
    }

}

