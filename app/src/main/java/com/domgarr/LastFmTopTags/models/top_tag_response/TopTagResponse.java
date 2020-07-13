package com.domgarr.LastFmTopTags.models.top_tag_response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TopTagResponse {
    @SerializedName("toptags")
    @Expose
    private TopTags topTags;

    public TopTags getTopTags() {
        return topTags;
    }

    public void setTopTags(TopTags topTags) {
        this.topTags = topTags;
    }
}

