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
import codepath.flicks.presenters.MainPresenter;
import icepick.Icepick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.movieListView)
    RecyclerView moviesListView;

    @BindView(R.id.swipeListViewContainer)
    SwipeRefreshLayout swipeContainer;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private List<Movie> movies = new ArrayList<>();
    private MoviesAdapter moviesAdapter;
    private MainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Icepick.restoreInstanceState(this, savedInstanceState);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        initGui();

        this.presenter = new MainPresenter(savedInstanceState, this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        presenter.onSave(outState);
        Icepick.saveInstanceState(this, outState);
    }

    private void initGui() {
        moviesListView.setLayoutManager(new LinearLayoutManager(this));
        moviesAdapter = new MoviesAdapter(movies);
        moviesListView.setAdapter(moviesAdapter);

        swipeContainer.setOnRefreshListener(() -> presenter.fetchMovies());

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    public void showMovies(List<Movie> movieList) {
        swipeContainer.setRefreshing(false);

        movies.addAll(movieList);
        moviesAdapter.notifyDataSetChanged();
    }

    public void showError() {
        Toast.makeText(this, "Error!", Toast.LENGTH_LONG).show();
    }
}
