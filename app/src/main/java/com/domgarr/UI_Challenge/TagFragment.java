package com.domgarr.UI_Challenge;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.domgarr.UI_Challenge.models.Tag;
import com.domgarr.UI_Challenge.models.TopTagResponse;

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
    private ProgressBar progressBar;

    private OnListFragmentInteractionListener listener;
    private Integer lastCategoryPosition;
    private List<Tag> tags;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TagFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            lastCategoryPosition = bundle.getInt(MainActivity.CATEGORY_SELECTED);
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
        // TODO: Update argument type and name
        void onListFragmentInteraction(String tagName);
    }

    private void requestTopTags() {
        Single<Response<TopTagResponse>> call = LastFm.getInstance().getLastFmService().topTags(LastFm.API_KEY);
        call.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<TopTagResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Response<TopTagResponse> topTagResponseResponse) {
                        tags = topTagResponseResponse.body().getTopTags().getTags();
                        recyclerView.setAdapter(new TagRecyclerViewAdapter(tags, listener, lastCategoryPosition));
                        recyclerView.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        //TODO: Maybe notify user with a Toast
                    }
                });
    }
}
