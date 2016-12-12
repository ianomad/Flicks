package codepath.flicks.modules;


import javax.inject.Singleton;

import codepath.flicks.api.MovieApiClient;
import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class CommonModule {

    @Provides
    @Singleton
    Retrofit getMovieRetrofit() {
        return new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    MovieApiClient getMovieApiClient(Retrofit retrofit) {
        return retrofit.create(MovieApiClient.class);
    }
}
