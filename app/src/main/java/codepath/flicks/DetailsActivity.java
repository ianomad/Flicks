/*
 * Copyright (c) 2016.  Iliiazbek Akhmedov
 */

package codepath.flicks;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import codepath.flicks.api.MovieService;
import codepath.flicks.api.Trailer;
import cz.msebera.android.httpclient.Header;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class DetailsActivity extends AppCompatActivity {

    private static final String YOUTUBE_KEY = "AIzaSyDKNbjeQHUgdqOcekntKtrYZw6q7Ek4ZOg";

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.ratingBar)
    RatingBar ratingBar;
    @BindView(R.id.releaseDate)
    TextView releaseDate;
    @BindView(R.id.description)
    TextView description;
    @BindView(R.id.imageView)
    ImageView imageView;

    YouTubePlayerFragment videoFr;
    private String movieId;
    private String backdropImage;
    private YouTubePlayer youTubePlayer;
    private float rating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        videoFr = (YouTubePlayerFragment)
                getFragmentManager().findFragmentById(R.id.youtubeFragment);

        ButterKnife.bind(this);
        initGui();
    }

    private void initGui() {
        Bundle extras = getIntent().getExtras();

        movieId = extras.getString("id");
        backdropImage = extras.getString("backdrop");
        title.setText(extras.getString("title"));
        rating = extras.getFloat("rating");
        ratingBar.setRating(rating);
        String releaseD = "Release: " + extras.getString("release_date");
        releaseDate.setText(releaseD);
        description.setText(extras.getString("description"));

        videoFr.initialize(YOUTUBE_KEY,
                new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                        YouTubePlayer player, boolean b) {
                        youTubePlayer = player;
                        loadTrailer();
                    }

                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                        YouTubeInitializationResult youTubeInitializationResult) {
                        //TODO: wrap null and log
                        if (null != videoFr.getView()) {
                            videoFr.getView().setVisibility(View.GONE);
                        }
                        showImage();
                    }
                });
    }

    void showImage() {
        String imageUrl = "https://image.tmdb.org/t/p/w300/" + backdropImage;

        Picasso.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.placeholder)
                .transform(new RoundedCornersTransformation(5, 0, RoundedCornersTransformation.CornerType.ALL))
                .into(imageView);
    }

    private void loadTrailer() {
        MovieService.getTrailers(movieId, new MovieService.MoviesCallback<Trailer>() {
            @Override
            public void done(final List<Trailer> res) {
                runOnUiThread(() -> {
                    if (res.isEmpty()) {
                        if (null != videoFr.getView()) {
                            videoFr.getView().setVisibility(View.GONE);
                        }
                        showImage();
                        return;
                    }

                    youTubePlayer.cueVideo(res.get(0).getSource());

                    if (rating > 5) {
                        youTubePlayer.play();
                        youTubePlayer.setFullscreen(true);
                    }
                });

            }

            @Override
            public void error(Exception e) {
                //TODO: wrap null and log
            }
        });
    }
}
