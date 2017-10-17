package be.pxl.denmax.poopchasers.Repo;

import java.util.ArrayList;
import java.util.List;

import be.pxl.denmax.poopchasers.Exceptions.ToiletLocationIDNotFoundException;
import be.pxl.denmax.poopchasers.Model.*;

public class ToiletRepository {
    private static List<Toilet> toiletLocations;
    private static int idIncrementer = 0;

    static {
        toiletLocations = new ArrayList<Toilet>();
        toiletLocations.add(new Toilet(idIncrementer++, "Hier", 1,2));
        toiletLocations.add(new Toilet(idIncrementer++, "Daar", 5,4));
        toiletLocations.add(new Toilet(idIncrementer++, "Overal", 50.953270,5.353262));
        toiletLocations.add(new Toilet(idIncrementer++, "Stink Twalet", 50.9382132,5.3461862));
        toiletLocations.add(new Toilet(idIncrementer++, "Dennis' Badkamer Emporium", 50.883887,5.2205358));
    }

    private ToiletRepository() {

    }

    public static List<Toilet> getAllToiletLocations() {
        return new ArrayList<Toilet>(toiletLocations);
    }

    public static Toilet getToiletLocationByID(int id) throws ToiletLocationIDNotFoundException {
        for (Toilet t : toiletLocations) {
            if (t.getId() == id) {
                return new Toilet(t);
            }
        }
        throw new ToiletLocationIDNotFoundException();
    }

    public static void addCommentToToiletLocation(ToiletComment comment, int id) throws ToiletLocationIDNotFoundException {
        getToiletLocationByID(id).addComment(comment);
    }

    public static void addToiletLocation(Toilet toilet) {
        toiletLocations.add(new Toilet(toilet, idIncrementer++));
    }
}
