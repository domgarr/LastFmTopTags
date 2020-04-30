package com.domgarr.UI_Challenge;

import com.domgarr.UI_Challenge.models.top_tag_response.TopTagResponse;
import com.domgarr.UI_Challenge.models.top_track_response.TopTrackResponse;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface LastFmService {
    String TOP_TAGS_BASE_URL = "?method=tag.getTopTags&format=json";
    String TOP_TRACKS__BASE_URL = "?method=tag.gettoptracks&format=json";

    @GET(TOP_TAGS_BASE_URL)
    Single<Response<TopTagResponse>> topTags(@Query("api_key") String apiKey);

    @GET(TOP_TRACKS__BASE_URL)
    Single<Response<TopTrackResponse>> topTracks(@Query("api_key") String apiKey, @Query("tag") String tagName, @Query("limit") int limit);
}
