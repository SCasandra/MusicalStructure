package com.casii.droid.musicalstructure;

/**
 * Created by Casi on 04.05.2017.
 */

public class Song {
    private String title;
    private Artist artist;
    private String mp3File;

    public Song(String title, String mp3File, Artist artist) {
        this.title = title;
        this.artist = artist;
        this.mp3File = mp3File;
    }

    public String getTitle() {
        return title;
    }

    public Artist getArtist() {
        return artist;
    }

    public String getMp3File() {
        return mp3File;
    }
}
