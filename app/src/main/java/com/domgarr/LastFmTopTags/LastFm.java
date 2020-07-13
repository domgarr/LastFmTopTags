package com.domgarr.LastFmTopTags;

import com.google.gson.Gson;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * This is a Singleton class whose main purpose is to reuse the LastFmService.class to make
 * API calls to Last FM API.
 *
 * Retrofit is used in conjunction with RxJava.
 *  - RxJava2CallAdapterFactory is important here. It allows for Retrofit Service methods to
 *      return Observables/Single return types.
 *
 *  Gson is used to serialize objects.
 */
public class LastFm {

    public static final String LAST_FM_BASE_URL = "https://ws.audioscrobbler.com/2.0/";
    //TODO: Add your API key here.
    public static final String API_KEY = "";

    private Retrofit retrofit;
    private LastFmService lastFmService;
    private static LastFm instance;

    private LastFm() {
        this.retrofit = new Retrofit.Builder()
                .baseUrl(LAST_FM_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
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
