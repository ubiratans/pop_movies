package com.ubiratans.android.popmoviesapp.data;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by ubiratans on 09/09/16.
 */

public class MovieData implements Serializable {
    private String mPosterPath;
    private double mPopularity;
    private String mId;
    private double mVoteAverage;
    private String mOverview;
    private String mTitle;
    private String mOriginalTitle;
    private long mVoteCount;
    private Date mReleaseDate;

    public String getPosterPath() {
        return mPosterPath;
    }

    public double getPopularity() {
        return mPopularity;
    }

    public String getId() {
        return mId;
    }

    public double getVoteAverage() {
        return mVoteAverage;
    }

    public String getOverview() {
        return mOverview;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getOriginalTitle() {
        return mOriginalTitle;
    }

    public long getVoteCount() {
        return mVoteCount;
    }

    public Date getReleaseDate() {
        return mReleaseDate;
    }

    public void setPosterPath(String posterPath) {
        this.mPosterPath = posterPath;
    }

    public void setPopularity(double popularity) {
        this.mPopularity = popularity;
    }

    public void setId(String Id) {
        this.mId = Id;
    }

    public void setVoteAverage(double voteAverage) {
        this.mVoteAverage = voteAverage;
    }

    public void setOverview(String overview) {
        this.mOverview = overview;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public void setOriginalTitle(String originalName) {
        this.mOriginalTitle = originalName;
    }

    public void setVoteCount(long voteCount) {
        this.mVoteCount = voteCount;
    }

    public void setReleaseDate(Date releaseDate) {
        this.mReleaseDate = releaseDate;
    }
}
