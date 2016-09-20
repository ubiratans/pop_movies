package com.ubiratans.android.popmoviesapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.ubiratans.android.popmoviesapp.data.MovieData;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, MoviesListFragment.Listener {
    private DrawerLayout mDrawerLayout;
    private MoviesListFragment mMoviesListFragment;
    private NavigationView mNavigationView;
    private ActionBarDrawerToggle mDrawerToggle;
    private MovieDetailFragment mMovieDetailFragment = null;
    private MovieData mSelectedMovie = null;
    private static final String SELECTED_MOVIE_KEY = "SELECTED_MOVIE_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.layout_drawer);
        mNavigationView = (NavigationView)findViewById(R.id.navigation_view);
        mMoviesListFragment = (MoviesListFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_movies_list);
        mMovieDetailFragment = (MovieDetailFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_movie_detail);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open,
                R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };

        mNavigationView.setNavigationItemSelectedListener(this);
        mMoviesListFragment.setListener(this);

        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        getSupportActionBar().setIcon(R.drawable.icon_drawer);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (mMovieDetailFragment != null) {
            if (savedInstanceState != null) {
                mSelectedMovie =
                        (MovieData)savedInstanceState.getSerializable(SELECTED_MOVIE_KEY);
            }

            if (mSelectedMovie != null) {
                mMovieDetailFragment.updateView(mSelectedMovie);
            } else {
                getSupportFragmentManager().beginTransaction().hide(mMovieDetailFragment).commit();
            }
        }

        //getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        MoviesListFragment.SortByParameter param = mMoviesListFragment.getSortParameter();

        if (param == MoviesListFragment.SortByParameter.POPULARITY) {
            mNavigationView.setCheckedItem(R.id.menu_sort_by_popularity);
        } else if (param == MoviesListFragment.SortByParameter.RATE) {
            mNavigationView.setCheckedItem(R.id.menu_sort_by_rate);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        boolean ret = false;

        switch (item.getItemId()) {
            case R.id.menu_sort_by_popularity:
                mMoviesListFragment
                        .updateMovies(MoviesListFragment.SortByParameter.POPULARITY);
                mDrawerLayout.closeDrawer(mNavigationView);
                ret = true;
                break;
            case R.id.menu_sort_by_rate:
                mMoviesListFragment.updateMovies(MoviesListFragment.SortByParameter.RATE);
                mDrawerLayout.closeDrawer(mNavigationView);
                ret = true;
                break;
        }

        if (ret) {
            mSelectedMovie = null;

            if (mMovieDetailFragment != null)
                getSupportFragmentManager().beginTransaction().hide(mMovieDetailFragment).commit();
        }

        return false;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(SELECTED_MOVIE_KEY, mSelectedMovie);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onItemClick(MovieData movie) {
        if (mMovieDetailFragment == null) {
            Intent intent = new Intent(getApplicationContext(), MovieDetailActivity.class);
            intent.putExtra(MovieDetailActivity.MOVIE_DATA_KEY, movie);
            startActivity(intent);
            mMoviesListFragment.clearSelection();
        } else {
            getSupportFragmentManager().beginTransaction().show(mMovieDetailFragment).commit();
            mMovieDetailFragment.updateView(movie);
        }

        mSelectedMovie = movie;
    }
}
