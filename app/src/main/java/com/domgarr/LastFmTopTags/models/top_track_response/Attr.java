package com.domgarr.LastFmTopTags.models.top_track_response;

import com.google.gson.annotations.SerializedName;

public class Attr {
    @SerializedName("rank")
    private int rank;

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
}
