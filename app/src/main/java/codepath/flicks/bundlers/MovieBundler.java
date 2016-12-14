package codepath.flicks.bundlers;

import android.os.Bundle;

import org.parceler.Parcels;

import java.util.ArrayList;

import codepath.flicks.api.Movie;
import icepick.Bundler;

public class MovieBundler implements Bundler<ArrayList<Movie>> {
    @Override
    public void put(String key, ArrayList<Movie> movies, Bundle bundle) {
        bundle.putParcelable(key, Parcels.wrap(movies));
    }

    @Override
    public ArrayList<Movie> get(String key, Bundle bundle) {
        return Parcels.unwrap(bundle.getParcelable(key));
    }
}
