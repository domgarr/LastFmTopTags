package com.domgarr.UI_Challenge.models.top_tag_response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TopTags {
    @SerializedName("@attr")
    @Expose(serialize = false)
    private Object attr;

    @SerializedName("tag")
    @Expose
    private List<Tag> tag;

    public TopTags() {
    }

    public TopTags(List<Tag> tags) {
        this.tag = tags;
    }

    public List<Tag> getTags() {
        return tag;
    }

    public void setTags(List<Tag> tags) {
        this.tag = tags;
    }

    @Override
    public String toString() {
        return "TopTags{" +
                "tags=" + tag +
                '}';
    }
}
