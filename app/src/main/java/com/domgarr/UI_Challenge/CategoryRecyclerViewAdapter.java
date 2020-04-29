package com.domgarr.UI_Challenge;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.domgarr.UI_Challenge.CategoryFragment.OnListFragmentInteractionListener;
import com.domgarr.UI_Challenge.models.Category;

import java.util.List;

public class CategoryRecyclerViewAdapter extends RecyclerView.Adapter<CategoryRecyclerViewAdapter.ViewHolder> {

    private final List<Category> categories;
    private final OnListFragmentInteractionListener listener;

    private int selectedPosition = RecyclerView.NO_POSITION;

    public CategoryRecyclerViewAdapter(List<Category> categories, OnListFragmentInteractionListener listener, Integer lastCategoryPosition) {
        this.categories = categories;
        this.listener = listener;
        if(lastCategoryPosition != null){
            selectedPosition = lastCategoryPosition;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.category = categories.get(position);
        holder.categoryNameView.setText(categories.get(position).getName());
        holder.itemView.setSelected(selectedPosition == position );
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final View view;
        public final TextView categoryNameView;
        public Category category;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            categoryNameView = view.findViewById(R.id.category_name);
            categoryNameView.setOnClickListener(this);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + categoryNameView.getText() + "'";
        }

        @Override
        public void onClick(View v) {
            notifyItemChanged(selectedPosition);
            selectedPosition = getLayoutPosition();
            notifyItemChanged(selectedPosition);
            listener.onListFragmentInteraction(selectedPosition);
        }
    }
}
