package com.ubiratans.android.popmoviesapp;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.ubiratans.android.popmoviesapp.data.MovieData;

import java.util.Calendar;

public class MovieDetailFragment extends Fragment {
    private static final String URL_PREFIX = "http://image.tmdb.org/t/p/w185/";
    private static final int IMAGE_WIDTH = 185;
    private static final int IMAGE_HEIGHT = 277;

    public MovieDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void updateView(MovieData data) {
        if (data == null) {
            return;
        }

        TextView tvTitle = (TextView)getActivity().findViewById(R.id.text_view_movie_title);
        TextView tvDuration = (TextView)getActivity().findViewById(R.id.text_view_movie_duration);
        TextView tvRate = (TextView)getActivity().findViewById(R.id.text_view_movie_rate);
        TextView tvYear = (TextView)getActivity().findViewById(R.id.text_view_movie_release_year);
        TextView tvDescription =
                (TextView)getActivity().findViewById(R.id.text_view_movie_description);
        ImageView ivPoster =
                (ImageView)getActivity().findViewById(R.id.image_view_detail_movie_poster);

        tvTitle.setText(data.getOriginalTitle());
        tvRate.setText(String.valueOf(data.getVoteAverage()) + "/10.0");

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(data.getReleaseDate());

        tvYear.setText(String.valueOf(calendar.get(Calendar.YEAR)));
        tvDescription.setText(data.getOverview());

        // TODO: put this code below in a public method on utils
        Drawable loadingImage =
                ContextCompat.getDrawable(getContext(), R.drawable.icon_loading_image);
        Drawable errorImage =
                ContextCompat.getDrawable(getContext(), R.drawable.icon_error_loading_image);
        Picasso.with(getContext()).cancelRequest(ivPoster);
        Picasso.with(getContext()).load(URL_PREFIX + data.getPosterPath())
                .resize(IMAGE_WIDTH, IMAGE_HEIGHT).placeholder(loadingImage).error(errorImage)
                .into(ivPoster);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie_detail, container, false);
    }
}
