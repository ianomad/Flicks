package codepath.flicks.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.util.List;

import codepath.flicks.DetailsActivity;
import codepath.flicks.R;
import codepath.flicks.api.Movie;
import codepath.flicks.view.holders.PopularMovieViewHolder;
import codepath.flicks.view.holders.RegularMovieViewHolder;
import codepath.flicks.view.holders.ViewHolder;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class MoviesAdapter extends RecyclerView.Adapter<ViewHolder> {

    private int VIEW_TYPE_REGULAR = 0;
    private int VIEW_TYPE_POPULAR = 1;

    private List<Movie> movies;

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
    public void onBindViewHolder(ViewHolder vh, int pos) {

        Context context = vh.rootView.getContext();
        Movie movie = movies.get(pos);

        if (getItemViewType(pos) == VIEW_TYPE_REGULAR) {
            RegularMovieViewHolder regularVH = (RegularMovieViewHolder) vh;

            regularVH.title.setText(movie.getTitle());
            regularVH.desc.setText(movie.getDescription());
            regularVH.releaseDate.setText(movie.getFormattedReleaseDate());
            regularVH.ratingBar.setRating(movie.getRating());

            String imageURL = isLandscape(context) ? movie.getFullBackgropImageURL() : movie.getFullPosterImageURL();
            fetchRoundedImage(context, regularVH.imageView, imageURL, true);
        } else {
            PopularMovieViewHolder popularViewHolder = (PopularMovieViewHolder) vh;
            fetchRoundedImage(context, popularViewHolder.imageView, movie.getFullBackgropImageURL(), false);
        }

        vh.rootView.setOnClickListener(view -> openMovieDetails(context, movie));
    }

    @Override
    public int getItemViewType(int position) {
        return movies.get(position).getRating() > 5.0d ? VIEW_TYPE_POPULAR : VIEW_TYPE_REGULAR;
    }

    private boolean isLandscape(Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    private void fetchRoundedImage(Context context, ImageView imageView, String imageURL, boolean round) {
        RequestCreator requestCreator = Picasso.with(context)
                .load(imageURL)
                .placeholder(R.drawable.placeholder);

        if (round) {
            RoundedCornersTransformation cornersTransformation =
                    new RoundedCornersTransformation(5, 0, RoundedCornersTransformation.CornerType.ALL);
            requestCreator = requestCreator.transform(cornersTransformation);
        }

        requestCreator.into(imageView);
    }

    private void openMovieDetails(Context context, Movie movie) {
        Intent intent = new Intent(context, DetailsActivity.class);

        intent.putExtra("id", movie.getId());
        intent.putExtra("title", movie.getTitle());
        intent.putExtra("description", movie.getDescription());
        intent.putExtra("release_date", movie.getReleaseDate());
        intent.putExtra("rating", movie.getRating());
        intent.putExtra("video", movie.isVideo());
        intent.putExtra("backdrop", movie.getBackdropPath());

        context.startActivity(intent);
    }
}
