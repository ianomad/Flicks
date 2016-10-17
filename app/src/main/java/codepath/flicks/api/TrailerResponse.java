/*
 * Copyright (c) 2016.  Iliiazbek Akhmedov
 */

package codepath.flicks.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TrailerResponse {

    @SerializedName("youtube")
    List<Trailer> trailers;


    public TrailerResponse() {

    }

    public List<Trailer> getTrailers() {
        return trailers;
    }

    public void setTrailers(List<Trailer> trailers) {
        this.trailers = trailers;
    }
}
