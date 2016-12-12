package codepath.flicks;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import codepath.flicks.adapters.MoviesAdapter;
import codepath.flicks.api.Movie;
import codepath.flicks.api.MovieService;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.movieListView)
    RecyclerView moviesListView;

    @BindView(R.id.swipeListViewContainer)
    SwipeRefreshLayout swipeContainer;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private List<Movie> movies = new ArrayList<>();
    private MoviesAdapter moviesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        initGui();
    }

    private void initGui() {
        moviesListView.setLayoutManager(new LinearLayoutManager(this));
        moviesAdapter = new MoviesAdapter(movies);
        moviesListView.setAdapter(moviesAdapter);

        fetchMovies();
        swipeContainer.setOnRefreshListener(this::fetchMovies);

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    private void fetchMovies() {
        MovieService.getMovies(new MovieService.MoviesCallback<Movie>() {
            @Override
            public void done(final List<Movie> res) {
                runOnUiThread(() -> {
                    movies.addAll(res);
                    moviesAdapter.notifyDataSetChanged();
                    swipeContainer.setRefreshing(false);
                });
            }

            @Override
            public void error(Exception e) {
                runOnUiThread(() -> {
                    swipeContainer.setRefreshing(false);
                    Toast.makeText(MainActivity.this, "Couldn't load data...", Toast.LENGTH_SHORT).show();
                });

            }
        });
    }
}
