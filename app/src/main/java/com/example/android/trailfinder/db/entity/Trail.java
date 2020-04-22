package com.example.android.trailfinder.db.entity;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.android.trailfinder.R;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.squareup.picasso.Picasso;

@Entity(tableName = "trail_table")
public class Trail {

    @PrimaryKey(autoGenerate = false)
    @Expose
    @SerializedName("id")
    private int id;
    @Expose
    @SerializedName("name")
    private String name;
    @Expose
    @SerializedName("summary")
    private String summary;
    @Expose
    @SerializedName("difficulty")
    private String difficulty;
    @Expose
    @SerializedName("location")
    private String location;
    @Expose
    @SerializedName("imgMedium")
    private String image;
    @Expose
    @SerializedName("length")
    private double length;
    @Expose
    @SerializedName("ascent")
    private int ascent;
    @Expose
    @SerializedName("descent")
    private int descent;
    @Expose
    @SerializedName("high")
    private int high;
    @Expose
    @SerializedName("low")
    private int low;
    @Expose
    @SerializedName("longitude")
    private double longitude;
    @Expose
    @SerializedName("latitude")
    private double latitude;

    private long lastRefresh;

    public Trail(int id, String name, String summary, String difficulty,
                 String location, String image, double length, int ascent,
                 int descent, int high, int low, double longitude, double latitude,
                 long lastRefresh) {

        this.id = id;
        this.name = name;
        this.summary = summary;
        this.difficulty = difficulty;
        this.location = location;
        this.image = image;
        this.length = length;
        this.ascent = ascent;
        this.descent = descent;
        this.high = high;
        this.low = low;
        this.longitude = longitude;
        this.latitude = latitude;
        this.lastRefresh = lastRefresh;
    }

    @BindingAdapter({"imageUrl"})
    public static void loadImage(ImageView view, String url) {
        Picasso.get()
                .load(url)
                .fit()
                .placeholder(R.drawable.image_loading_white_24dp)
                .error(R.drawable.image_error_white_24dp)
                .into(view);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public int getAscent() {
        return ascent;
    }

    public void setAscent(int ascent) {
        this.ascent = ascent;
    }

    public int getDescent() {
        return descent;
    }

    public void setDescent(int descent) {
        this.descent = descent;
    }

    public int getHigh() {
        return high;
    }

    public void setHigh(int high) {
        this.high = high;
    }

    public int getLow() {
        return low;
    }

    public void setLow(int low) {
        this.low = low;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public long getLastRefresh() {
        return lastRefresh;
    }

    public void setLastRefresh(long time) {
        lastRefresh = time;
    }

}
