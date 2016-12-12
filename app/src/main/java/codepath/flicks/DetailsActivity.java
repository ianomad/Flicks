package codepath.flicks;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;

import org.parceler.Parcels;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import codepath.flicks.api.Movie;
import codepath.flicks.api.MovieApiClient;
import codepath.flicks.api.Trailer;
import codepath.flicks.api.TrailerResponse;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsActivity extends AppCompatActivity {

    public static final String EXTRA_MOVIE = "MOVIE";
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

    @Inject
    MovieApiClient movieApiClient;

    YouTubePlayerFragment videoFr;

    private YouTubePlayer youTubePlayer;
    private Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        ButterKnife.bind(this);
        ((FlicksApplication) getApplication()).getAppComponent().inject(this);

        movie = Parcels.unwrap(getIntent().getExtras().getParcelable(EXTRA_MOVIE));
        videoFr = (YouTubePlayerFragment) getFragmentManager().findFragmentById(R.id.youtubeFragment);

        initGui();
    }

    private void initGui() {

        title.setText(movie.getTitle());
        ratingBar.setRating(movie.getRating());
        releaseDate.setText(movie.getFormattedReleaseDate());
        description.setText(movie.getDescription());

        YouTubePlayer.OnInitializedListener videoHandler = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean b) {
                youTubePlayer = player;

                fetchTrailer();
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult res) {

            }
        };

        videoFr.initialize(YOUTUBE_KEY, videoHandler);
    }

    private void showImage() {
        Glide.with(this)
                .load(movie.getFullBackgropImageURL())
                .bitmapTransform(new RoundedCornersTransformation(this, 5, 0))
                .into(imageView);
    }

    private void fetchTrailer() {
        movieApiClient.getTrailer(movie.getId(), MovieApiClient.MOVIE_API_KEY, 1).enqueue(new Callback<TrailerResponse>() {
            @Override
            public void onResponse(Call<TrailerResponse> call, Response<TrailerResponse> response) {
                if (response.isSuccessful()) {
                    List<Trailer> trailers = response.body().getTrailers();
                    if (!trailers.isEmpty() && null != videoFr.getView()) {
                        youTubePlayer.cueVideo(trailers.get(0).getSource());
                        return;
                    }
                }

                showImage();
            }

            @Override
            public void onFailure(Call<TrailerResponse> call, Throwable t) {
                showImage();
            }
        });
    }
}
