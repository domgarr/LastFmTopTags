package com.domgarr.UI_Challenge;

import com.domgarr.UI_Challenge.models.TopTagResponse;
import com.domgarr.UI_Challenge.models.TopTrackResponse;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface LastFmService {
    @GET("?method=tag.getTopTags&format=json")
    Single<Response<TopTagResponse>> topTags(@Query("api_key") String apiKey);

    @GET("?method=tag.gettoptracks&format=json")
    Single<Response<TopTrackResponse>> topTracks(@Query("api_key") String apiKey, @Query("tag") String tagName, @Query("limit") int limit);
}
