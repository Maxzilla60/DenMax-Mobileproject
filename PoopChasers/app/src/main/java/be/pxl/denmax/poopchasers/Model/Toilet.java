package be.pxl.denmax.poopchasers.Model;

import java.util.HashMap;
import java.util.List;

// Class representing a Toilet (location)
public class Toilet {
    private int id;
    private String name;
    private double latitude;
    private double longitude;
    private ToiletTags tags;
    private ToiletCommentsList comments;

    // Constructor
    public Toilet(int id, String name, double latitude, double longitude) {
        this(id, name, latitude, longitude, new ToiletTags(), new ToiletCommentsList());
    }
    public Toilet(int id, String name, double latitude, double longitude, ToiletTags tags, ToiletCommentsList comments) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.tags = tags;
        this.comments = comments;
    }

    // Copy Constructor
    public Toilet(Toilet otherToilet) {
        this(otherToilet, otherToilet.getId());
    }
    public Toilet(Toilet otherToilet, int id) {
        this.id = id;
        this.name = otherToilet.name;
        this.latitude = otherToilet.latitude;
        this.longitude = otherToilet.longitude;
        this.tags = otherToilet.tags;
        this.comments = otherToilet.comments;
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

    // Rating will be dynamically calculated
    public int getRating() {
        return comments.getAverageRating();
    }

    public HashMap<ToiletTag, Boolean> getTags() {
        return tags.getTags();
    }

    public int getId() {
        return id;
    }

    /*public void setTags(ToiletTags tags) {
        this.tags = tags;
    }*/

    // Getting tags as a list
    public List<ToiletTag> getTagsAsList() {
        return tags.getTagsAsList();
    }

    public List<ToiletComment> getComments() {
        return comments.getCommentsList();
    }
}