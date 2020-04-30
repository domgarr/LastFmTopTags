package com.domgarr.UI_Challenge;

import com.google.gson.Gson;

import java.net.URL;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class LastFm {
    private URL lastFmBaseURL;

    public static final String LAST_FM_BASE_URL = "https://ws.audioscrobbler.com/2.0/";
    public static String API_KEY = "c81b11017639d4f04e8fa12895dd02f5";
    public static String GET_TOP_TAGS = "/2.0/?method=tag.getTopTags&api_key=" + API_KEY + "&format=json";

    private Retrofit retrofit;
    private LastFmService lastFmService;
    private static LastFm instance;

    private LastFm() {
        this.retrofit = new Retrofit.Builder()
                .baseUrl(LAST_FM_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) //Allows return of Observables/Flowable etc by Service methods.
                .build();

        this.lastFmService = retrofit.create(LastFmService.class);

    }

    public static LastFm getInstance() {
        if (instance == null) {
            instance = new LastFm();
        }
        return instance;
    }

    public LastFmService getLastFmService() {
        return lastFmService;
    }

}
