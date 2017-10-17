package be.pxl.denmax.poopchasers.Model;

import android.location.Address;

import java.util.HashMap;
import java.util.List;


// Class representing a Toilet (location)
public class Toilet {
    private String name;
    private Address location;
    private ToiletTags tags;
    private ToiletCommentsList comments;

    // Constructor
    public Toilet(String name, Address location, int rating, ToiletTags tags, ToiletCommentsList comments) {
        this.name = name;
        this.location = location;
        this.tags = tags;
        this.comments = comments;
    }

    public void addTag(ToiletTag tag) {
        tags.addTag(tag);
    }
    public void removeTag(ToiletTag tag) {
        tags.removeTag(tag);
    }

    public void addComment (ToiletComment comment) {
        comments.add(comment);
    }

    // Getters & Setters:
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

    // Rating will be dynamically calculated
    public int getRating() {
        return comments.getAverageRating();
    }

    public HashMap<ToiletTag, Boolean> getTags() {
        return tags.getTags();
    }

    // Getting tags as a list
    public List<ToiletTag> getTagsAsList() {
        return tags.getTagsAsList();
    }

    /*public void setTags(ToiletTags tags) {
        this.tags = tags;
    }*/

    public List<ToiletComment> getComments() {
        return comments.getCommentsList();
    }
}
