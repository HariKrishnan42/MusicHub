package com.example.musichub.Services;


import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.example.musichub.Interfaces.CheckProgress;
import com.example.musichub.Models.PlayerController;
import com.example.musichub.Models.ProgressThread;
import com.example.musichub.R;

import java.util.concurrent.TimeUnit;

public class MyService extends Service implements CheckProgress {
    private static final String ACTION_PLAY = "com.example.musichub.ACTION_PLAY";
    private static final String ACTION_PAUSE = "com.example.musichub.ACTION_PAUSE";
    private static final String ACTION_PREVIOUS = "com.example.musichub.ACTION_PREVIOUS";
    private static final String ACTION_NEXT = "com.example.musichub.ACTION_NEXT";
    public static RemoteViews remoteViews;
    Notification notification1;
    public NotificationManagerCompat compat1;
    public NotificationCompat.Builder notification;
    public Context context;
    private ProgressThread progressThread;

    private Handler handler;

    public MyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = MyService.this;
        progressThread = new ProgressThread(this);
        handler = new Handler();
        progressThread.run();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel("MusicPlayerChannel", "MusicPlayer", NotificationManager.IMPORTANCE_LOW);
            notificationChannel.setLightColor(Color.BLUE);
            NotificationManager manager = this.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(notificationChannel);
        }
        notificationNew();
    }

    private void notificationNew() {
        MediaSessionCompat compat = new MediaSessionCompat(this, "tag");
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.circleback);
        remoteViews = new RemoteViews(getPackageName(), R.layout.notification_layout);
        remoteViews.setTextViewText(R.id.notification_music_name, PlayerController.details.get(PlayerController.position).getName());
        remoteViews.setOnClickPendingIntent(R.id.notification_previous_btn, pendingIntent(ACTION_PREVIOUS));
        remoteViews.setOnClickPendingIntent(R.id.notification_pause_btn, pendingIntent(ACTION_PAUSE));
        remoteViews.setOnClickPendingIntent(R.id.notification_play_btn, pendingIntent(ACTION_PLAY));
        remoteViews.setOnClickPendingIntent(R.id.notification_next_btn, pendingIntent(ACTION_NEXT));
        notification = new NotificationCompat.Builder(this, "MusicPlayerChannel")
                .setLargeIcon(bitmap)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setContentTitle(PlayerController.details.get(PlayerController.position).getName())
                .setContentText(PlayerController.details.get(PlayerController.position).getArtist())
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setColor(ContextCompat.getColor(this, R.color.primary_color))
                .addAction(R.drawable.skip_previous, "Previous", pendingIntent(ACTION_PREVIOUS))
                .addAction(PlayerController.player.isPlaying() ? R.drawable.pause_2 : R.drawable.play_arrow, "Play", pendingIntent(ACTION_PLAY))
                .addAction(R.drawable.skip_next, "Next", pendingIntent(ACTION_NEXT))
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                        .setShowActionsInCompactView(0, 1, 2 /* #1: pause button */)
                        .setMediaSession(compat.getSessionToken()));
        compat1 = NotificationManagerCompat.from(this);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.S_V2) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        } else {
            compat1.notify(2, notification.build());
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getAction() != null) {
            switch (intent.getAction()) {
                case ACTION_NEXT -> playNextSong();
                case ACTION_PAUSE -> playOrPauseCurrentSong();
                case ACTION_PLAY -> playOrPauseCurrentSong();
                case ACTION_PREVIOUS -> playPreviousSong();
            }
        }
        return START_NOT_STICKY;
    }

    private void playPreviousSong() {
        new PlayerController().previous();
        notificationNew();
    }

    private void playNextSong() {
        new PlayerController().next();
        notificationNew();
    }

    private void playOrPauseCurrentSong() {
        if (PlayerController.player.isPlaying()) {
            new PlayerController().pauseI();
        } else {
            new PlayerController().playing();
        }
        notificationNew();
    }

    private PendingIntent pendingIntent(String action) {
        Intent intent = new Intent(this, MyService.class);
        intent.setAction(action);
        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @SuppressLint("DefaultLocale")
    private String timeConvert(int duration) {
        return String.format("%02d : %02d",
                TimeUnit.MILLISECONDS.toMinutes(duration),
                TimeUnit.MILLISECONDS.toSeconds(duration) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration))
        );
    }

    @Override
    public void onProgress(int b) {
        //Log.d(TAG, "onProgress: " + b);// start with this
        //int startTime = Integer.parseInt(timeConvert(b));
        Log.d(TAG, "onProgress: 2 " + timeConvert(b));
        //notification.setProgress(Integer.parseInt(PlayerController.details.get(PlayerController.position).getDuration()), Integer.parseInt(timeConvert(PlayerController.player.getCurrentPosition())), true);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.S_V2) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        } else {
//            compat1.notify(2, notification.build());
        }
    }
}