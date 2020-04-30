package com.domgarr.UI_Challenge;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.domgarr.UI_Challenge.models.Song;
import com.domgarr.UI_Challenge.models.TopTrackResponse;
import com.domgarr.UI_Challenge.models.Track;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class SongFragment extends Fragment {
    private RecyclerView recyclerView;
    private SongRecyclerViewAdapter songRecyclerViewAdapter;
    public static final String SELECTED_POSITION = "selectedPosition";
    private Integer selectedPosition;


    private OnListFragmentInteractionListener listener;
    private List<Track> tracks;
    private String tagName;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SongFragment() {
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(SELECTED_POSITION, songRecyclerViewAdapter.getSelectedPosition());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null) {
            selectedPosition = savedInstanceState.getInt(SELECTED_POSITION);
        }

        Bundle bundle = getArguments();
        if(bundle != null){
            tagName = bundle.getString(MainActivity.TAG_NAME);
        }

        requestTopTracks();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_song_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        }
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            listener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Track track);
    }

    private void requestTopTracks(){
        Single<Response<TopTrackResponse>> call = LastFm.getInstance().getLastFmService().topTracks(LastFm.API_KEY, tagName, MainActivity.TOP_TRACK_LIMIT);

                call
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new SingleObserver<Response<TopTrackResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Response<TopTrackResponse> topTrackResponseResponse) {
                        /*
                        getTracks() called twice is due to the layout of the API
                         */
                        tracks = topTrackResponseResponse.body().getTracks().getTracks();
                        songRecyclerViewAdapter = new SongRecyclerViewAdapter(tracks, listener, selectedPosition);
                        recyclerView.setAdapter(songRecyclerViewAdapter);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });

    }

}
