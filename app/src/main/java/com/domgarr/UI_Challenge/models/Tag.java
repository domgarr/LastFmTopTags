package com.domgarr.UI_Challenge.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Tag {
    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("count")
    @Expose
    private int count;

    @SerializedName("reach")
    @Expose
    private int reach;

    public Tag() {
    }

    public Tag(String name, int count, int reach) {
        this.name = name;
        this.count = count;
        this.reach = reach;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getReach() {
        return reach;
    }

    public void setReach(int reach) {
        this.reach = reach;
    }
}
