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

import com.domgarr.UI_Challenge.models.top_tag_response.Tag;
import com.domgarr.UI_Challenge.models.top_tag_response.TopTagResponse;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

import static androidx.recyclerview.widget.RecyclerView.VERTICAL;

public class TagFragment extends Fragment {
    private RecyclerView recyclerView;

    private OnListFragmentInteractionListener listener;
    private Integer lastTagPosition; //Used to restore state after orientation change.
    private List<Tag> tags; //Populated with API call to Last FM.

    public TagFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Bundle arguments are set via MainActivity before committing Fragment.
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            lastTagPosition = bundle.getInt(MainActivity.TAG_SELECTED);
        }
        requestTopTags();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tag_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            //The recycler view is set aysn. after data is fetched from Last FM. See requestTopTags()
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));

            //Add divier between list items
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
        void onListFragmentInteraction(String tagName);
    }

    private void requestTopTags() {
        Single<Response<TopTagResponse>> topTagsRequest = LastFm.getInstance().getLastFmService().topTags(LastFm.API_KEY);
        topTagsRequest.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<TopTagResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Response<TopTagResponse> topTagResponseResponse) {
                        tags = topTagResponseResponse.body().getTopTags().getTags();
                        recyclerView.setAdapter(new TagRecyclerViewAdapter(tags, listener, lastTagPosition));
                    }

                    @Override
                    public void onError(Throwable e) {
                        //TODO: Maybe notify user with a Toast and retry
                    }
                });
    }
}
