package com.example.musichub.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;

import com.example.musichub.Fragments.HomeFrag;
import com.example.musichub.Fragments.LibraryFrag;
import com.example.musichub.Models.MyMediaPlayer;
import com.example.musichub.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class Landing extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    private BottomNavigationView navigationView;
    private HomeFrag homeFrag;
    private LibraryFrag libraryFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        homeFrag = new HomeFrag();
        libraryFrag = new LibraryFrag();
        navigationView = findViewById(R.id.bottom_nav);
        navigationView.setOnItemSelectedListener(this);
        MyMediaPlayer.DEVICE_ID = String.valueOf(Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
        makeFragment(new HomeFrag());
    }

    private void makeFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.library) {
            makeFragment(new LibraryFrag());
        } else if (item.getItemId() == R.id.home) {
            makeFragment(new HomeFrag());
        }
        return false;
    }
}