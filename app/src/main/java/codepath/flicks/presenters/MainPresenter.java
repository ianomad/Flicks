package codepath.flicks.presenters;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import codepath.flicks.FlicksApplication;
import codepath.flicks.MainActivity;
import codepath.flicks.api.Movie;
import codepath.flicks.api.MovieApiClient;
import codepath.flicks.bundlers.MovieBundler;
import icepick.Icepick;
import icepick.State;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainPresenter {

    private static final String TAG = MainPresenter.class.getSimpleName();

    @State(MovieBundler.class)
    ArrayList<Movie> movieList;

    @Inject
    MovieApiClient movieApiClient;

    private MainActivity activity;

    public MainPresenter(Bundle savedState, MainActivity activity) {
        Icepick.restoreInstanceState(this, savedState);

        this.activity = activity;
        FlicksApplication application = (FlicksApplication) activity.getApplication();
        application.getAppComponent().inject(this);

        if(null == movieList) {
            fetchMovies();
        } else {
            activity.showMovies(movieList);
        }
    }

    public void onSave(@NonNull Bundle state) {
        Icepick.saveInstanceState(this, state);
    }

    public void fetchMovies() {
        movieApiClient.getMoviesRx(MovieApiClient.MOVIE_API_KEY, 1)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .delaySubscription(3, TimeUnit.SECONDS)
                .subscribe(moviesResponseResponse -> {

                    if (moviesResponseResponse.isSuccessful()) {
                        movieList = new ArrayList<>(moviesResponseResponse.body().getMovies());
                        activity.showMovies(movieList);
                    } else {
                        activity.showError();
                    }

                }, throwable -> {

                    Log.e(TAG, throwable.getMessage(), throwable);

                });
    }
}
