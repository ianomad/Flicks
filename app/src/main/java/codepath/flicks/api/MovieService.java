/*
 * Copyright (c) 2016.  Iliiazbek Akhmedov
 */

package codepath.flicks.api;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MovieService {

    private static final String MOVIE_API_KEY = "a07e22bc18f5cb106bfe4cc1f83ad8ed";

    public static void getMovies(final MoviesCallback<Movie> callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.themoviedb.org/3/movie/now_playing?api_key=" + MOVIE_API_KEY + "&page=1")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.error(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string();
                MoviesResponse moviesResponse = new Gson().fromJson(res, MoviesResponse.class);
                callback.done(moviesResponse.getMovies());
            }
        });
    }

    public static void getTrailers(String id, final MoviesCallback<Trailer> callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.themoviedb.org/3/movie/" + id + "/trailers?api_key=" + MOVIE_API_KEY + "&page=1")
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.error(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string();
                TrailerResponse resp = new Gson().fromJson(res, TrailerResponse.class);
                callback.done(resp.getTrailers());
            }
        });
    }

    public interface MoviesCallback<T> {
        void done(List<T> movies);

        void error(Exception e);
    }
}
