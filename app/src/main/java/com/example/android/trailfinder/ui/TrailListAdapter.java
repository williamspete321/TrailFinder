package com.example.android.trailfinder.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.trailfinder.databinding.CardTrailBinding;
import com.example.android.trailfinder.db.entity.Trail;

import java.util.List;

public class TrailListAdapter extends RecyclerView.Adapter<TrailListAdapter.TrailViewHolder> {

    public static class TrailViewHolder extends RecyclerView.ViewHolder {

        private CardTrailBinding binding;

        private TrailViewHolder(CardTrailBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Trail trail) {
            binding.setTrail(trail);
            binding.executePendingBindings();
        }
    }

    private final LayoutInflater inflater;
    private List<Trail> allTrails;

    public TrailListAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public TrailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        CardTrailBinding itemBinding
                = CardTrailBinding.inflate(inflater, parent, false);

        return new TrailViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailViewHolder holder, int position) {
        if(allTrails != null) {
            Trail trail = allTrails.get(position);
            holder.bind(trail);
        }
    }

    @Override
    public int getItemCount() {
        if(allTrails != null) return allTrails.size();
        return 0;
    }

    public void setData(List<Trail> trails) {
        allTrails = trails;
        notifyDataSetChanged();
    }

}
