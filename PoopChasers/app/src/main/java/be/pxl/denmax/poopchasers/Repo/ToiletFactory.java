package be.pxl.denmax.poopchasers.Repo;

import com.google.android.gms.maps.model.LatLng;

import be.pxl.denmax.poopchasers.Model.Toilet;
import be.pxl.denmax.poopchasers.Model.ToiletCommentsList;
import be.pxl.denmax.poopchasers.Model.ToiletTags;

public class ToiletFactory {
    public int id = 0;
    public String name = "";
    public LatLng latlng  = new LatLng(0,0);
    public ToiletTags tags = new ToiletTags();
    public ToiletCommentsList comments = new ToiletCommentsList();

    public Toilet getToilet() {
        return new Toilet(id, name, latlng.latitude, latlng.longitude, tags, comments);
    }

    public ToiletFactory setId(int id) {
        this.id = id;
        return this;
    }

    public ToiletFactory setName(String name) {
        this.name = name;
        return this;
    }

    public ToiletFactory setLatlng(LatLng latlng) {
        this.latlng = latlng;
        return this;
    }

    public ToiletFactory setTags(ToiletTags tags) {
        this.tags = tags;
        return this;
    }

    public ToiletFactory setComments(ToiletCommentsList comments) {
        this.comments = comments;
        return this;
    }
}
