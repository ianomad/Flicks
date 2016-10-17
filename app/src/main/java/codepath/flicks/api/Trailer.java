/*
 * Copyright (c) 2016.  Iliiazbek Akhmedov
 */

package codepath.flicks.api;

import com.google.gson.annotations.SerializedName;

public class Trailer {

    @SerializedName("name")
    private String name;

    @SerializedName("source")
    private String source;

    public Trailer() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
