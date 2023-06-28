package com.example.musichub.Models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Music Hub")
public class UserAccount {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String deviceId;
    private String path;

    private String playlistName;

    public UserAccount(int id, String deviceId, String path, String playlistName) {
        this.id = id;
        this.deviceId = deviceId;
        this.path = path;
        this.playlistName = playlistName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPlaylistName() {
        return playlistName;
    }

    public void setPlaylistName(String playlistName) {
        this.playlistName = playlistName;
    }
}
