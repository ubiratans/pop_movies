package com.ubiratans.android.popmoviesapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.ubiratans.android.popmoviesapp.data.MovieData;
import com.ubiratans.android.popmoviesapp.utils.MovieDbApiHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MoviesListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private MovieAdapter mMovieAdapter;
    private MovieDbAsyncTask mQueryTask;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private SortByParameter mSortParameter = SortByParameter.POPULARITY;
    private TextView mTextViewTitle;
    public static String SORT_KEY = "sort_parameter";
    private Listener mListener = null;

    public enum SortByParameter {
        POPULARITY,
        RATE
    }


    public MoviesListFragment() {
        // Required empty public constructor
    }

    public void setListener(Listener listener) {
        mListener = listener;
    }

    public void clearSelection() {
        mMovieAdapter.clearSelectedItem();
    }

    public SortByParameter getSortParameter() {
        return mSortParameter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedBundle) {
        mMovieAdapter = new MovieAdapter(getContext(), R.layout.fragment_movies_list);

        View rootView = inflater.inflate(R.layout.fragment_movies_list, container, false);

        GridView gridView = (GridView) rootView.findViewById(R.id.grid_view_movies_list);
        gridView.setAdapter(mMovieAdapter);
        mSwipeRefreshLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.layout_swipe_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mTextViewTitle = (TextView)rootView.findViewById(R.id.text_view_list_title);

        if (savedBundle != null) {
            mSortParameter = SortByParameter.values()
                    [savedBundle.getInt(SORT_KEY, SortByParameter.POPULARITY.ordinal())];
        }

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mMovieAdapter.setSelectedItem(position);
                mMovieAdapter.notifyDataSetChanged();

                if (mListener != null) {
                    mListener.onItemClick(mMovieAdapter.getItem(position));
                }
            }
        });

        onRefresh();
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mQueryTask.cancel(true);
    }

    @Override
    public void onRefresh() {
        updateMovies(mSortParameter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(SORT_KEY, mSortParameter.ordinal());
        super.onSaveInstanceState(outState);
    }

    public void updateMovies(SortByParameter parameter, int page, boolean clearOldList) {
        if (null != mQueryTask) {
            mQueryTask.cancel(true);
        }

        mQueryTask = new MovieDbAsyncTask();

        String[] params = { MovieDbApiHelper.PARAM_SORT_BY };
        String[] values = { MovieDbApiHelper.VALUE_SORT_BY_POPULARITY };

        SortByParameter newParam = SortByParameter.POPULARITY;
        String title = "";

        if (parameter == SortByParameter.RATE) {
            title = getString(R.string.top_rated);
            newParam = SortByParameter.RATE;
            values[0] = MovieDbApiHelper.VALUE_SORT_BY_RATE;
        } else if (parameter == SortByParameter.POPULARITY) {
            title = getString(R.string.most_popular);
        }

        mSortParameter = newParam;
        mTextViewTitle.setText(title);

        mQueryTask.setClearOldList(clearOldList);
        mQueryTask.execute(params, values);
    }

    public void updateMovies(SortByParameter parameter) {
        updateMovies(parameter, 1, true);
    }

    private class MovieDbAsyncTask extends AsyncTask<String[], MovieData, List<MovieData>> {
        private boolean mFirstUpdate;
        private boolean mClearOldList = true;

        public void setClearOldList(boolean clearOldList) {
            this.mClearOldList = clearOldList;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mFirstUpdate = true;
            mMovieAdapter.clearSelectedItem();
            mSwipeRefreshLayout.setRefreshing(true);
        }

        @Override
        protected List<MovieData> doInBackground(String[]... params) {
            String jsonStr = MovieDbApiHelper.Query("discover/movie", params[0], params[1]);

            if (null != jsonStr) {
                return parseJson(jsonStr);
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(MovieData... values) {
            if (mClearOldList && mFirstUpdate) {
                mMovieAdapter.clear();
                mFirstUpdate = false;
            }

            mMovieAdapter.add(values[0]);
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(List<MovieData> movies) {
            mSwipeRefreshLayout.setRefreshing(false);

            if (mClearOldList) {
                mMovieAdapter.clear();
            }

            mMovieAdapter.addAll(movies);
            //mMovieAdapter.notifyDataSetChanged();
        }

        private List< MovieData > parseJson(String jsonStr) {
            JSONObject json = null;
            JSONArray moviesArr = null;
            List< MovieData > movieList = new ArrayList<>();

            try {

                json = new JSONObject(jsonStr);
                moviesArr = json.getJSONArray(MovieDbApiHelper.PARAM_RESULTS_ARRAY);

                for (int i = 0; i < moviesArr.length(); ++i) {
                    MovieData movieData =
                            MovieDbApiHelper.buildMovieFromJson(moviesArr.getJSONObject(i));

                    if (null == movieData) {
                        continue;
                    }

                    movieList.add(movieData);
                }
            } catch (JSONException e) {
                return null;
            }

            return movieList;
        }

        // TODO: remove this method
        private boolean updateView(String jsonStr) {
            JSONObject json = null;
            JSONArray moviesArr = null;

            try {
                json = new JSONObject(jsonStr);
                moviesArr = json.getJSONArray(MovieDbApiHelper.PARAM_RESULTS_ARRAY);

                for (int i = 0; i < moviesArr.length(); ++i) {
                    MovieData movieData =
                            MovieDbApiHelper.buildMovieFromJson(moviesArr.getJSONObject(i));

                    if (null == movieData) {
                        continue;
                    }

                    publishProgress(movieData);
                }
            } catch (JSONException e) {
                return false;
            }

            return true;
        }
    }

    public interface Listener {
        public void onItemClick(MovieData movie);
    }
}
