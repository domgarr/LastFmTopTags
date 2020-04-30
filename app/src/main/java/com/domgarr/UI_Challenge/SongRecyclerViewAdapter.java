package com.domgarr.UI_Challenge;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.domgarr.UI_Challenge.SongFragment.OnListFragmentInteractionListener;
import com.domgarr.UI_Challenge.models.Song;
import com.domgarr.UI_Challenge.models.Track;

import org.w3c.dom.Text;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Song} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class SongRecyclerViewAdapter extends RecyclerView.Adapter<SongRecyclerViewAdapter.ViewHolder> {

    private final List<Track> tracks;
    private final OnListFragmentInteractionListener listener;
    private int selectedPosition = RecyclerView.NO_POSITION;

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public SongRecyclerViewAdapter(List<Track> tracks, OnListFragmentInteractionListener listener, Integer restoredSelectedPosition) {
        this.tracks = tracks;
        this.listener = listener;

        //Check to see if a previously selected position exists.
        selectedPosition = RecyclerView.NO_POSITION;
        if(restoredSelectedPosition != null){
            selectedPosition = restoredSelectedPosition;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_song, parent, false);
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
            }
        });
    }

    @Override
    public int getItemCount() {
        return tracks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
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
            trackNameView.setOnClickListener(this);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + trackNameView.getText() + "'";
        }

        @Override
        public void onClick(View v) {
            //Selected song is highlighted here.
            //TODO: Understand this better.
            notifyItemChanged(selectedPosition);
            selectedPosition = getLayoutPosition();
            notifyItemChanged(selectedPosition);
        }
    }
}
