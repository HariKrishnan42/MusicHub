package com.example.musichub.Services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationService {
    private static final String ACTION_PLAY = "com.example.musichub.ACTION_PLAY";
    private static final String ACTION_PAUSE = "com.example.musichub.ACTION_PAUSE";
    private static final String ACTION_PREVIOUS = "com.example.musichub.ACTION_PREVIOUS";
    private static final String ACTION_NEXT = "com.example.musichub.ACTION_NEXT";

    public static RemoteViews remoteViews;
    private NotificationManager manager;

    public NotificationManagerCompat compat1;
    public NotificationCompat.Builder notification;

    public Context context;

    public NotificationService(Context context) {
        this.context = context;
    }

    public void makeNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel("MusicPlayerChannel", "MusicPlayer", NotificationManager.IMPORTANCE_LOW);
            notificationChannel.setLightColor(Color.BLUE);
            manager = context.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(notificationChannel);
        }
    }

}
