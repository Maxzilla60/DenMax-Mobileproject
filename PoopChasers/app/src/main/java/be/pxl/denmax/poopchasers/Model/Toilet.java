package be.pxl.denmax.poopchasers.Model;

import android.location.Location;
import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// Class representing a Toilet (location)
public class Toilet implements Comparable<LatLng> {
    private int id;
    private String name;
    private LatLng latlng;
    private ToiletTags tags;
    private ToiletCommentsList comments;

    // Constructor
    public Toilet(int id, String name, double latitude, double longitude) {
        this(id, name, latitude, longitude, new ToiletTags(), new ToiletCommentsList());
    }
    public Toilet(int id, String name, double latitude, double longitude, ToiletTags tags) {
        this(id, name, latitude, longitude, tags, new ToiletCommentsList());
    }
    public Toilet(int id, String name, double latitude, double longitude, ToiletTags tags, ToiletCommentsList comments) {
        this.id = id;
        this.name = name;
        this.latlng = new LatLng(latitude, longitude);
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
        this.latlng = new LatLng(otherToilet.latlng.latitude, otherToilet.latlng.longitude);
        this.tags = otherToilet.tags;
        this.comments = otherToilet.comments;
    }

    public void addTags(ToiletTag... newTags) {
        for (ToiletTag tag : newTags) {
            tags.addTag(tag);
        }
    }

    public float distanceTo(Toilet otherToiler) {
        return distanceTo(otherToiler.getLatLng());
    }
    public float distanceTo(LatLng otherLL) {
        // Create respective location classes:
        // This:
        Location locationThis = new Location("");
        locationThis.setLatitude(this.getLatLng().latitude);
        locationThis.setLongitude(this.getLatLng().longitude);
        // Other:
        Location locationOther = new Location("");
        locationOther.setLatitude(otherLL.latitude);
        locationOther.setLongitude(otherLL.longitude);

        return locationThis.distanceTo(locationOther);
    }

    public void removeTag(ToiletTag tag) {
        tags.removeTag(tag);
    }
    public boolean hasTag(ToiletTag tag) {
        return tags.hasTag(tag);
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

    public ToiletTags getToiletTags() {return this.tags;}

    public int getId() {
        return id;
    }

    public LatLng getLatLng(){
        return this.latlng;
    }

    /*public void setTags(ToiletTags tags) {
        this.tags = tags;
    }*/

    // Getting tags as a list
    public List<ToiletTag> getTagsAsList() {
        return tags.getTagsAsList();
    }

    public ArrayList<ToiletComment> getComments() {
        return comments.getCommentsList();
    }

    @Override
    public int compareTo(@NonNull LatLng other) {
        return (int) this.distanceTo(other);
    }
}