package codepath.flicks;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import codepath.flicks.adapters.MoviesAdapter;
import codepath.flicks.api.Movie;
import codepath.flicks.api.MovieApiClient;
import codepath.flicks.api.MoviesResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.movieListView)
    RecyclerView moviesListView;

    @BindView(R.id.swipeListViewContainer)
    SwipeRefreshLayout swipeContainer;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Inject
    MovieApiClient movieApiClient;

    private List<Movie> movies = new ArrayList<>();
    private MoviesAdapter moviesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        ((FlicksApplication) getApplication()).getAppComponent().inject(this);

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
        movieApiClient.getMovies(MovieApiClient.MOVIE_API_KEY, 1).enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                swipeContainer.setRefreshing(false);

                if (!response.isSuccessful()) {
                    showError();
                    return;
                }

                movies.addAll(response.body().getMovies());
                moviesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                swipeContainer.setRefreshing(false);

                showError();
            }
        });
    }

    private void showError() {
        //TODO:
    }
}
