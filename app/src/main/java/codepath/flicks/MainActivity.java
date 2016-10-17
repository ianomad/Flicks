package codepath.flicks;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import codepath.flicks.api.Movie;
import codepath.flicks.api.MovieService;
import cz.msebera.android.httpclient.Header;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.movieListView)
    ListView moviesListView;

    @BindView(R.id.swipeListViewContainer)
    SwipeRefreshLayout swipeContainer;

    private List<Movie> movies = new ArrayList<>();
    private MoviesAdapter moviesAdapter = new MoviesAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        initGui();
    }

    private void initGui() {
        moviesListView.setAdapter(moviesAdapter);
        moviesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                openMovieDetails(pos);
            }
        });

        loadMovies();
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadMovies();
            }
        });

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    private void openMovieDetails(int pos) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra("id", movies.get(pos).getId());
        intent.putExtra("title", movies.get(pos).getTitle());
        intent.putExtra("description", movies.get(pos).getDescription());
        intent.putExtra("release_date", movies.get(pos).getReleaseDate());
        intent.putExtra("rating", movies.get(pos).getRating());
        intent.putExtra("video", movies.get(pos).isVideo());
        intent.putExtra("backdrop", movies.get(pos).getBackdropPath());

        startActivity(intent);
    }

    private void loadMovies() {
        MovieService.getMovies(new MovieService.MoviesCallback<Movie>() {
            @Override
            public void done(final List<Movie> res) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        movies = res;
                        moviesAdapter.notifyDataSetChanged();
                        swipeContainer.setRefreshing(false);
                    }
                });
            }

            @Override
            public void error(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        swipeContainer.setRefreshing(false);
                        Toast.makeText(MainActivity.this, "Couldn't load data...", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }

    private class MoviesAdapter extends BaseAdapter {

        int VIEW_TYPE_REGULAR = 0;
        int VIEW_TYPE_POPULAR = 1;

        private class ViewHolder {
            TextView title;
            TextView desc;
            ImageView imageView;
            TextView releaseDate;
            RatingBar ratingBar;
            ImageView playIcon;
            int viewType;

            ViewHolder() {
            }
        }

        @Override
        public int getCount() {
            return movies.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public int getItemViewType(int position) {
            return movies.get(position).getRating() > 5.0d ? VIEW_TYPE_POPULAR : VIEW_TYPE_REGULAR;
        }

        @Override
        public View getView(int pos, View view, ViewGroup viewGroup) {

            ViewHolder vh;

            if (null == view || null == view.getTag() || ((ViewHolder) view.getTag()).viewType != getItemViewType(pos)) {

                if (getItemViewType(pos) == VIEW_TYPE_POPULAR) {
                    view = LayoutInflater.from(MainActivity.this).inflate(R.layout.list_item_movie_popular, viewGroup, false);
                } else {
                    view = LayoutInflater.from(MainActivity.this).inflate(R.layout.list_item_movie, viewGroup, false);
                }

                vh = new ViewHolder();
                vh.title = (TextView) view.findViewById(R.id.title);
                vh.desc = (TextView) view.findViewById(R.id.description);
                vh.imageView = (ImageView) view.findViewById(R.id.imageView);
                vh.releaseDate = (TextView) view.findViewById(R.id.releaseDate);
                vh.ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
                vh.playIcon = (ImageView) view.findViewById(R.id.playIcon);
                vh.viewType = getItemViewType(pos);

                view.setTag(vh);
            } else {
                vh = (ViewHolder) view.getTag();
            }


            String imageUrl;
            if (vh.viewType == VIEW_TYPE_REGULAR) {
                vh.title.setText(movies.get(pos).getTitle());
                vh.desc.setText(movies.get(pos).getDescription());
                String date = "Release: " + movies.get(pos).getReleaseDate();
                vh.releaseDate.setText(date);
                vh.ratingBar.setRating(movies.get(pos).getRating());
                String image = movies.get(pos).getPosterPath();
                if (isLandscape()) {
                    image = movies.get(pos).getBackdropPath();
                }

                imageUrl = "https://image.tmdb.org/t/p/w300/" + image;
            } else {
                String image = movies.get(pos).getBackdropPath();
                imageUrl = "https://image.tmdb.org/t/p/w300/" + image;
            }

            vh.playIcon.setVisibility(View.VISIBLE); //video field is not reliable

            Picasso.with(MainActivity.this)
                    .load(imageUrl)
                    .placeholder(R.drawable.placeholder)
                    .transform(new RoundedCornersTransformation(5, 0, RoundedCornersTransformation.CornerType.ALL))
                    .into(vh.imageView);

            return view;
        }

        private boolean isLandscape() {
            return getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        }
    }
}
