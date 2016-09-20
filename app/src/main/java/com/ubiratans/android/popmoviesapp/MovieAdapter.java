package com.ubiratans.android.popmoviesapp;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.ubiratans.android.popmoviesapp.data.MovieData;

/**
 * Created by ubiratans on 09/09/16.
 */

public class MovieAdapter extends ArrayAdapter<MovieData> {
    private static final String URL_PREFIX = "http://image.tmdb.org/t/p/w185/";
    private static final int IMAGE_WIDTH = 185;
    private static final int IMAGE_HEIGHT = 277;
    private int mSelectedItemPos = -1;

    public MovieAdapter(Context context, int resource) {
        super(context, resource);
    }

    public void setSelectedItem(int pos) {
        mSelectedItemPos = pos;
    }

    public void clearSelectedItem() {
        mSelectedItemPos = -1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MovieData movieData = getItem(position);
        GridView gridView = (GridView)parent;
        int colWidth = gridView.getColumnWidth();

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.grid_view_item, parent, false);

            ImageView imageView =
                    (ImageView)convertView.findViewById(R.id.image_view_movie_poster);
            Double dHeight = 1.6678 * colWidth;
            int height = dHeight.intValue();
            imageView.setMaxHeight(height);
            imageView.setMinimumHeight(height);
        }

        TextView titleTv = (TextView)convertView.findViewById(R.id.text_view_title);
        titleTv.setText(movieData.getTitle());
        ImageView imageView = (ImageView)convertView.findViewById(R.id.image_view_movie_poster);

        Drawable loadingImage =
                ContextCompat.getDrawable(getContext(), R.drawable.icon_loading_image);
        Drawable errorImage =
                ContextCompat.getDrawable(getContext(), R.drawable.icon_error_loading_image);

        if (mSelectedItemPos == position) {
            convertView.setBackground(ContextCompat.getDrawable(getContext(),
                    R.drawable.background_border_hover));
            titleTv.setBackgroundColor(
                    ContextCompat.getColor(getContext(),
                            R.color.colorMovieTitleSelectedBackground));
        } else {
            convertView.setBackground(ContextCompat.getDrawable(getContext(),
                    R.drawable.background_border));
            titleTv.setBackgroundColor(
                    ContextCompat.getColor(getContext(), R.color.colorMovieTitleBackground));
        }

        // TODO: put this code below in a public method on utils
        Picasso.with(getContext()).cancelRequest(imageView);
        Picasso.with(getContext()).load(URL_PREFIX + movieData.getPosterPath()).fit()
                .placeholder(loadingImage).error(errorImage).noFade().into(imageView);

        return convertView;
    }
}
