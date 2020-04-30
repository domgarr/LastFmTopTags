package com.domgarr.UI_Challenge.models.top_track_response;

import com.google.gson.annotations.SerializedName;

public class TopTrackResponse {
    @SerializedName("tracks")
    private Tracks tracks;

    public Tracks getTracks() {
        return tracks;
    }

    public void setTracks(Tracks tracks) {
        this.tracks = tracks;
    }
}
