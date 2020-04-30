package com.domgarr.UI_Challenge.models;

import com.google.gson.annotations.SerializedName;

public class Track {
    @SerializedName("name")
    private String name;

    @SerializedName("artist")
    private Artist artist;

    @SerializedName("@attr")
    private Attr attr;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public Attr getAttr() {
        return attr;
    }

    public void setAttr(Attr attr) {
        this.attr = attr;
    }
}
