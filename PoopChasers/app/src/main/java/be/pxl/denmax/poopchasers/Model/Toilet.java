package be.pxl.denmax.poopchasers.Model;

import android.location.Address;

import java.util.AbstractMap;
import java.util.List;

/**
 * Created by 11502759 on 17/10/2017.
 */

public class Toilet {
    private String name;
    private Address location;
    private ToiletTags tags;
    private ToiletCommentsList comments;

    public Toilet(String name, Address location, int rating, ToiletTags tags, ToiletCommentsList comments) {
        this.name = name;
        this.location = location;
        this.tags = tags;
        this.comments = comments;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getLocation() {
        return location;
    }

    public void setLocation(Address location) {
        this.location = location;
    }

    public int getRating() {
        return comments.getAvarageRating();
    }

    public ToiletTags getTags() {
        return tags;
    }

    public void setTags(ToiletTags tags) {
        this.tags = tags;
    }

    public ToiletCommentsList getComments() {
        return comments;
    }

    public void addComment (ToiletComment comment) {
        comments.add(comment);
    }
}
