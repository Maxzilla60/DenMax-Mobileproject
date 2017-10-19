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

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLatlng(LatLng latlng) {
        this.latlng = latlng;
    }

    public void setTags(ToiletTags tags) {
        this.tags = tags;
    }

    public void setComments(ToiletCommentsList comments) {
        this.comments = comments;
    }
}
