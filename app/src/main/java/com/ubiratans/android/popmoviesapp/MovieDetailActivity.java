package com.ubiratans.android.popmoviesapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ubiratans.android.popmoviesapp.data.MovieData;

public class MovieDetailActivity extends AppCompatActivity {
    static public final String MOVIE_DATA_KEY = "movie_data";
    private MovieDetailFragment mMovieDetailFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        mMovieDetailFragment = (MovieDetailFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_movie_detail);

        if (getIntent().hasExtra(MOVIE_DATA_KEY)) {
            MovieData movieData = (MovieData)getIntent().getSerializableExtra(MOVIE_DATA_KEY);
            mMovieDetailFragment.updateView(movieData);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
