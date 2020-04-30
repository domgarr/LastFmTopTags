package com.domgarr.UI_Challenge;

import com.domgarr.UI_Challenge.models.Category;
import com.domgarr.UI_Challenge.models.TopTagResponse;


import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface LastFmService {
    @GET("?method=tag.getTopTags&format=json")
    Single<Response<TopTagResponse>> topTags(@Query("api_key") String apiKey);
}
