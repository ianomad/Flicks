package codepath.flicks.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieApiClient {
    String MOVIE_API_KEY = "a07e22bc18f5cb106bfe4cc1f83ad8ed";

    @GET("movie/now_playing")
    Call<MoviesResponse> getMovies(@Query("api_key") String key, @Query("page") int page);

    @GET("movie/{movieId}/trailers")
    Call<TrailerResponse> getTrailer(@Path("movieId") String id, @Query("api_key") String key, @Query("page") int page);
}
