package com.domgarr.UI_Challenge;

import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.domgarr.UI_Challenge.SongFragment.OnListFragmentInteractionListener;
import com.domgarr.UI_Challenge.models.Song;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Song} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class SongRecyclerViewAdapter extends RecyclerView.Adapter<SongRecyclerViewAdapter.ViewHolder> {

    private final List<Song> songs;
    private final OnListFragmentInteractionListener listener;
    private int selectedPosition = RecyclerView.NO_POSITION;

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public SongRecyclerViewAdapter(List<Song> songs, OnListFragmentInteractionListener listener, Integer restoredSelectedPosition) {
        this.songs = songs;
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
        holder.song = songs.get(position);
        holder.songNameView.setText(songs.get(position).getName());
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
                    listener.onListFragmentInteraction(holder.song);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final View view;

        public final TextView songNameView;
        public Song song;

        public ViewHolder(View view) {
            super(view);
            this.view = view;

            LinearLayout mSongParent = (LinearLayout) view.findViewById(R.id.song_linear_layout);

            songNameView = (TextView) view.findViewById(R.id.song_name);
            songNameView.setOnClickListener(this);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + songNameView.getText() + "'";
        }

        @Override
        public void onClick(View v) {
            //Selected song is highlighted here.
            notifyItemChanged(selectedPosition);
            selectedPosition = getLayoutPosition();
            notifyItemChanged(selectedPosition);
        }
    }
}
