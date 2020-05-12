package com.example.android.trailfinder.utilities;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.example.android.trailfinder.R;
import com.squareup.picasso.Picasso;

public class BindingUtils {

    @BindingAdapter({"imageUrl"})
    public static void loadImage(ImageView view, String url) {
        if(url == null || url.isEmpty()) {
            Picasso.get()
                    .load(R.drawable.image_error_white_24dp)
                    .centerCrop()
                    .fit()
                    .error(R.drawable.image_error_white_24dp)
                    .into(view);
        } else {
            Picasso.get()
                    .load(url)
                    .centerCrop()
                    .fit()
                    .placeholder(R.drawable.image_loading_white_24dp)
                    .error(R.drawable.image_error_white_24dp)
                    .into(view);
        }
    }

    @BindingAdapter({"difficultyTextColor"})
    public static void setTrailDifficultyTextColor(TextView view, String difficulty) {
        if(difficulty != null) {
            switch (difficulty) {
                case "green":
                    view.setTextColor(view.getResources().getColor(R.color.colorGreen));
                    break;
                case "blue":
                    view.setTextColor(view.getResources().getColor(R.color.colorBlue));
                    break;
                case "black":
                    view.setTextColor(view.getResources().getColor(R.color.colorRed));
                    break;
                default:
                    break;
            }
        }
    }

    @BindingAdapter({"difficultyText"})
    public static void setTrailDifficulty(TextView view, String difficulty) {
        if(difficulty != null) {
            switch (difficulty) {
                case "green":
                    view.setText(R.string.trail_difficulty_easy);
                    break;
                case "blue":
                    view.setText(R.string.trail_difficulty_moderate);
                    break;
                case "black":
                    view.setText(R.string.trail_difficulty_difficult);
                    break;
                default:
                    break;
            }
        }
    }

    @BindingAdapter({"summaryText"})
    public static void setTrailSummary(TextView view, String summary) {
        if(summary != null) {
            String summaryLowerCase = summary.toLowerCase();
            if(summaryLowerCase.contains(view.getResources()
                    .getString(R.string.trail_needs_summary)))
            {
                view.setText(R.string.trail_no_description_available);
            } else {
                view.setText(summary);
            }
        }
    }

}
