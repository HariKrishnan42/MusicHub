package com.example.musichub.Models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "Music Hub")
public class LibraryInfo {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String deviceId;
    private List<SongUri> path;
    private String playlistName;

    public LibraryInfo(int id, String deviceId, List<SongUri> path, String playlistName) {
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

    public List<SongUri> getPath() {
        return path;
    }

    public void setPath(List<SongUri> path) {
        this.path = path;
    }

    public String getPlaylistName() {
        return playlistName;
    }

    public void setPlaylistName(String playlistName) {
        this.playlistName = playlistName;
    }
}
