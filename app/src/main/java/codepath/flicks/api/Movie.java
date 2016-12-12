package codepath.flicks.api;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class Movie {

    @SerializedName("poster_path")
    String posterPath;

    @SerializedName("backdrop_path")
    String backdropPath;

    @SerializedName("title")
    String title;

    @SerializedName("overview")
    String description;

    @SerializedName("release_date")
    String releaseDate;

    @SerializedName("vote_average")
    Float rating;

    @SerializedName("id")
    String id;

    @SerializedName("video")
    boolean video;

    public Movie() {
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getFormattedReleaseDate() {
        return "Release: " + releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isVideo() {
        return video;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

    public String getFullBackgropImageURL() {
        return "https://image.tmdb.org/t/p/w300/" + backdropPath;
    }

    public String getFullPosterImageURL() {
        return "https://image.tmdb.org/t/p/w300/" + posterPath;
    }
}
