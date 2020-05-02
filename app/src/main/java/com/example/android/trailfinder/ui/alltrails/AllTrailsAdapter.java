package com.example.android.trailfinder.ui.alltrails;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.trailfinder.databinding.CardTrailBinding;
import com.example.android.trailfinder.data.database.model.Trail;

import java.util.List;

public class AllTrailsAdapter extends RecyclerView.Adapter<AllTrailsAdapter.TrailViewHolder> {

    public static class TrailViewHolder extends RecyclerView.ViewHolder {

        private final CardTrailBinding binding;

        public TrailViewHolder(CardTrailBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Trail trail, OnItemClickListener listener) {
            binding.setTrail(trail);
            binding.executePendingBindings();
            binding.getRoot().setOnClickListener(view -> {
                if (listener != null) {
                    listener.onItemClick(view, trail);
                }
            });
        }

    }

    private final LayoutInflater inflater;
    private List<Trail> allTrails;

    private OnItemClickListener listener;

    public AllTrailsAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public TrailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final CardTrailBinding itemBinding
                = CardTrailBinding.inflate(inflater, parent, false);
        return new TrailViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailViewHolder holder, int position) {
        if(allTrails != null) {
            Trail trail = allTrails.get(position);
            holder.bind(trail, listener);
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

    public interface OnItemClickListener {
        void onItemClick(View view, Trail item);
    }

}