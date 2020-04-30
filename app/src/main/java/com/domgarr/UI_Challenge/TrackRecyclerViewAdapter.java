package com.domgarr.UI_Challenge;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.domgarr.UI_Challenge.TrackFragment.OnListFragmentInteractionListener;
import com.domgarr.UI_Challenge.models.top_track_response.Track;

import java.util.List;

public class TrackRecyclerViewAdapter extends RecyclerView.Adapter<TrackRecyclerViewAdapter.ViewHolder> {
    private final List<Track> tracks;
    private final OnListFragmentInteractionListener listener;
    private int selectedPosition = RecyclerView.NO_POSITION;

    public TrackRecyclerViewAdapter(List<Track> tracks, OnListFragmentInteractionListener listener, Integer restoredSelectedPosition) {
        this.tracks = tracks;
        this.listener = listener;

        //Check to see if a previously selected position exists.
        selectedPosition = RecyclerView.NO_POSITION;
        if (restoredSelectedPosition != null) {
            selectedPosition = restoredSelectedPosition;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_track, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.track = tracks.get(position);
        holder.trackNameView.setText(tracks.get(position).getName());
        holder.trackArtistView.setText(tracks.get(position).getArtist().getName());
        holder.trackRankView.setText(String.valueOf(tracks.get(position).getAttr().getRank()));
        /* Setting itemView to selected state satisfies the selector in res/drawable/song_item
            and will highlight with given color.
         */
        holder.itemView.setSelected(selectedPosition == position);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != listener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    listener.onListFragmentInteraction(holder.track);
                }
                //Highlights selected item.
                notifyItemChanged(selectedPosition);
                selectedPosition = position;
                notifyItemChanged(selectedPosition);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tracks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final TextView trackNameView;
        public final TextView trackArtistView;
        public final TextView trackRankView;

        public Track track;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            trackNameView = (TextView) view.findViewById(R.id.song_name);
            trackArtistView = (TextView) view.findViewById(R.id.artist_name);
            trackRankView = (TextView) view.findViewById(R.id.song_rank);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + trackNameView.getText() + "'";
        }
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }
}
