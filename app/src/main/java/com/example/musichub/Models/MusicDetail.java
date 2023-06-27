package com.example.musichub.Models;

import java.io.Serializable;

public class MusicDetail implements Serializable {
    private String name;
    private String path;
    private String duration;

    private String artist;


    public MusicDetail(String name, String path, String duration, String artist) {
        this.name = name;
        this.path = path;
        this.duration = duration;
        this.artist = artist;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }
}
