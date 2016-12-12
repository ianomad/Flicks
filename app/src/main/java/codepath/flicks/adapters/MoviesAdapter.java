package codepath.flicks.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;

import org.parceler.Parcels;

import java.util.List;

import codepath.flicks.DetailsActivity;
import codepath.flicks.R;
import codepath.flicks.api.Movie;
import codepath.flicks.view.holders.PopularMovieViewHolder;
import codepath.flicks.view.holders.RegularMovieViewHolder;
import codepath.flicks.view.holders.ViewHolder;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MoviesAdapter extends RecyclerView.Adapter<ViewHolder> {

    private int VIEW_TYPE_REGULAR = 0;
    private int VIEW_TYPE_POPULAR = 1;

    private final List<Movie> movies;

    public MoviesAdapter(List<Movie> movies) {
        this.movies = movies;
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == VIEW_TYPE_POPULAR) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_movie_popular, parent, false);
            return new PopularMovieViewHolder(view);
        }

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_movie, parent, false);
        return new RegularMovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int pos) {
        Context context = viewHolder.rootView.getContext();
        Movie movie = movies.get(pos);

        if (getItemViewType(pos) == VIEW_TYPE_REGULAR) {
            RegularMovieViewHolder vh = (RegularMovieViewHolder) viewHolder;

            vh.title.setText(movie.getTitle());
            vh.desc.setText(movie.getDescription());
            vh.releaseDate.setText(movie.getFormattedReleaseDate());
            vh.ratingBar.setRating(movie.getRating());

            String imageURL = isLandscape(context) ? movie.getFullBackgropImageURL() : movie.getFullPosterImageURL();
            fetchRoundedImage(context, vh.imageView, imageURL, true);
        } else {
            PopularMovieViewHolder vh = (PopularMovieViewHolder) viewHolder;
            fetchRoundedImage(context, vh.imageView, movie.getFullBackgropImageURL(), false);
        }

        viewHolder.rootView.setOnClickListener(view -> openMovieDetails(context, movie));
    }

    @Override
    public int getItemViewType(int position) {
        return movies.get(position).getRating() > 5.0d ? VIEW_TYPE_POPULAR : VIEW_TYPE_REGULAR;
    }

    private boolean isLandscape(Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    private void fetchRoundedImage(Context context, ImageView imageView, String imageURL, boolean round) {
        DrawableRequestBuilder<String> requestCreator = Glide.with(context)
                .load(imageURL)
                .placeholder(R.drawable.camera)
                .fitCenter();

        if (round) {
            RoundedCornersTransformation cornersTransformation = new RoundedCornersTransformation(context, 5, 0);
            requestCreator = requestCreator.bitmapTransform(cornersTransformation);
        }

        requestCreator.into(imageView);
    }

    private void openMovieDetails(Context context, Movie movie) {
        Intent intent = new Intent(context, DetailsActivity.class);
        intent.putExtra(DetailsActivity.EXTRA_MOVIE, Parcels.wrap(movie));
        context.startActivity(intent);
    }
}
