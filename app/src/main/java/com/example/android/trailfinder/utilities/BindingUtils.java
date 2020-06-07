package com.example.android.trailfinder.utilities;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;

import com.example.android.trailfinder.R;
import com.squareup.picasso.Picasso;

import timber.log.Timber;

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
                case "greenBlue":
                    view.setTextColor(view.getResources().getColor(R.color.colorGreen));
                    break;
                case "blue":
                case "blueBlack":
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
                case "greenBlue":
                    view.setText(R.string.trail_difficulty_easy);
                    break;
                case "blue":
                case "blueBlack":
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

    @BindingAdapter({"urlLink"})
    public static void setTrailLink(TextView view, String url) {
        if(url != null) {
            String text = view.getResources().getString(R.string.trail_see_more);
            SpannableString spannableString = new SpannableString(text);
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(@NonNull View widget) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    view.getContext().startActivity(intent);
                }
            };
            spannableString.setSpan(clickableSpan,0,text.length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            view.setText(spannableString);
            view.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

}
