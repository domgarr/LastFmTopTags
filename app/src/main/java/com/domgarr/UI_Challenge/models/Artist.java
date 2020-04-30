package com.domgarr.UI_Challenge.models;

import com.google.gson.annotations.SerializedName;

public class Artist {
    @SerializedName("name")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
