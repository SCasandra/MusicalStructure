package com.casii.droid.musicalstructure;

/**
 * Created by Casi on 04.05.2017.
 */

public class Artist {
    private String name;
    private String url;

    public Artist(String name, String url) {
        this.url = url;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}
