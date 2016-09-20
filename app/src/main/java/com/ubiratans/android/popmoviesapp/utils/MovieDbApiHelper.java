package com.ubiratans.android.popmoviesapp.utils;

import android.net.Uri;

import com.ubiratans.android.popmoviesapp.data.MovieData;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ubiratans on 09/09/16.
 */

public class MovieDbApiHelper {
    private static final String BASE_URL = "http://api.themoviedb.org/3/";
    // TODO: Add your API key value below
    private static final String VALUE_API_KEY = "YOUR_KEY!";
    private static final String PARAM_API_KEY = "api_key";
    public static final String PARAM_SORT_BY = "sort_by";
    public static final String VALUE_SORT_BY_POPULARITY = "popularity.desc";
    public static final String VALUE_SORT_BY_RATE = "vote_average.desc";
    public static final String HTTP_REQUEST_METHOD = "GET";
    public static final String PARAM_RESULTS_ARRAY = "results";
    public static final String PARAM_ID = "id";
    public static final String PARAM_OVERVIEW = "overview";
    public static final String PARAM_POSTER_PATH = "poster_path";
    public static final String PARAM_ADULT = "adult";
    public static final String PARAM_RELEASE_DATE = "release_date";
    public static final String PARAM_GENRE_IDS = "genre_ids";
    public static final String PARAM_ORIGINAL_TITLE = "original_title";
    public static final String PARAM_ORIGINAL_LANGUAGE = "original_language";
    public static final String PARAM_TITLE = "title";
    public static final String PARAM_BACKDROP_PATH = "backdrop_path";
    public static final String PARAM_POPULARITY = "popularity";
    public static final String PARAM_VIDEO = "video";
    public static final String PARAM_VOTE_AVERAGE = "vote_average";
    public static final String PARAM_VOTE_COUNT = "vote_count";
    private static final int RESPONSE_CODE_SUCCESS = 200;

    public static String Query(String service, String[] parameters, String[] values) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String ret = "";

        Uri.Builder uriBuilder = Uri.parse(BASE_URL).buildUpon();
        uriBuilder.appendEncodedPath(service);

        if (null != parameters && null != values && values.length == parameters.length) {
            for (int i = 0; i < values.length; ++i) {
                uriBuilder.appendQueryParameter(parameters[i], values[i]);
            }
        }

        uriBuilder.appendQueryParameter(PARAM_API_KEY, VALUE_API_KEY);
        URL url = null;

        try {
            url = new URL(uriBuilder.toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(HTTP_REQUEST_METHOD);
            urlConnection.connect();

            if (RESPONSE_CODE_SUCCESS != urlConnection.getResponseCode()) {
                return null;
            }

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            ret = buffer.toString();


        } catch (IOException e) {
            return null;
        } finally {
            if (urlConnection != null) urlConnection.disconnect();
        }

        return ret;
    }

    public static MovieData buildMovieFromJson(JSONObject jsonObj) {
        MovieData movie = new MovieData();

        try {
            movie.setId(jsonObj.getString(PARAM_ID));
            movie.setPosterPath(jsonObj.getString(PARAM_POSTER_PATH));
            movie.setTitle(jsonObj.getString(PARAM_TITLE));
            movie.setOriginalTitle(jsonObj.getString(PARAM_ORIGINAL_TITLE));
            movie.setOverview(jsonObj.getString(PARAM_OVERVIEW));
            movie.setPopularity(jsonObj.getDouble(PARAM_POPULARITY));

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String dateStr = jsonObj.getString(PARAM_RELEASE_DATE);
            try {
                movie.setReleaseDate(dateFormat.parse(dateStr));
            } catch (ParseException e) {
                return null;
            }

            movie.setVoteAverage(jsonObj.getDouble(PARAM_VOTE_AVERAGE));
            movie.setVoteCount(jsonObj.getLong(PARAM_VOTE_COUNT));

        } catch (JSONException e) {
            return null;
        }

        return movie;
    }
}
