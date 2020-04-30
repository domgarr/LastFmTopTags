package com.domgarr.UI_Challenge;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.domgarr.UI_Challenge.models.TopTrackResponse;
import com.domgarr.UI_Challenge.models.Track;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

import static androidx.recyclerview.widget.RecyclerView.VERTICAL;

public class TrackFragment extends Fragment {
    private RecyclerView recyclerView;
    private TrackRecyclerViewAdapter songRecyclerViewAdapter;
    public static final String SELECTED_POSITION = "selectedPosition";
    private Integer selectedPosition;


    private OnListFragmentInteractionListener listener;
    private List<Track> tracks;
    private String tagName;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TrackFragment() {
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(SELECTED_POSITION, songRecyclerViewAdapter.getSelectedPosition());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            selectedPosition = savedInstanceState.getInt(SELECTED_POSITION);
        }

        Bundle bundle = getArguments();
        if (bundle != null) {
            tagName = bundle.getString(MainActivity.TAG_NAME);
        }

        requestTopTracks();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_track_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            DividerItemDecoration itemDecor = new DividerItemDecoration(getContext(), VERTICAL);
            recyclerView.addItemDecoration(itemDecor);
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

    private void requestTopTracks() {
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
                        songRecyclerViewAdapter = new TrackRecyclerViewAdapter(tracks, listener, selectedPosition);
                        recyclerView.setAdapter(songRecyclerViewAdapter);
                    }

                    @Override
                    public void onError(Throwable e) {
                        //TODO: Maybe notify user with a Toast
                    }
                });

    }

}
