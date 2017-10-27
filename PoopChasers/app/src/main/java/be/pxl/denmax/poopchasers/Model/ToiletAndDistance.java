package be.pxl.denmax.poopchasers.Model;

import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

// Class containing a Toilet and its distance to a certain latlong
public class ToiletAndDistance implements Comparable<ToiletAndDistance> {
    private Toilet toilet;
    private float distance;

    public ToiletAndDistance(Toilet toilet, LatLng location) {
        this.toilet = toilet;
        this.distance = toilet.distanceTo(location);
    }
    public ToiletAndDistance(Toilet toilet, Toilet location) {
        this(toilet, location.getLatLng());
    }

    public Toilet getToilet() {
        return toilet;
    }

    public float getDistance() {
        return distance;
    }

    @Override
    public int compareTo(@NonNull ToiletAndDistance toiletAndDistance) {
        return (int) (this.distance - toiletAndDistance.distance);
    }
}
