package com.example.android.trailfinder.ui.alltrails;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AllTrailsRecyclerViewMargin extends RecyclerView.ItemDecoration {
    private final int columns;
    private final int margin;

    public AllTrailsRecyclerViewMargin(int columns, int margin) {
        this.columns = columns;
        this.margin = margin;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view,
                               @NonNull RecyclerView parent,
                               @NonNull RecyclerView.State state) {

        int position = parent.getChildAdapterPosition(view);

        // Set right and bottom margin for all.
        outRect.bottom = margin;
        outRect.right = margin;
        // Only add top margin to the first row. Only add left margin to the first column.
        if(position < columns) outRect.top = margin;
        if(position % columns == 0) outRect.left = margin;
    }
}
