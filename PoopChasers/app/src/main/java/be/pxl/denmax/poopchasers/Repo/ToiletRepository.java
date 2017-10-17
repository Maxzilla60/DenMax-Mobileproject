package be.pxl.denmax.poopchasers.Repo;

import java.util.ArrayList;
import java.util.List;

import be.pxl.denmax.poopchasers.Exceptions.ToiletLocationIDAlreadyExists;
import be.pxl.denmax.poopchasers.Exceptions.ToiletLocationIDNotFoundException;
import be.pxl.denmax.poopchasers.Model.*;

public class ToiletRepository {
    private List<Toilet> toiletLocations;
    private int idIncrementor = 0;

    public ToiletRepository() {

    }

    public final List<Toilet> getAllToiletLocations() {
        return new ArrayList<Toilet>(toiletLocations);
    }

    public Toilet getToiletLocationByID(int id) throws ToiletLocationIDNotFoundException {
        for (Toilet t : toiletLocations) {
            if (t.getId() == id) {
                return new Toilet(t);
            }
        }
        throw new ToiletLocationIDNotFoundException();
    }

    public void addCommentToToiletLocation(ToiletComment comment, int id) throws ToiletLocationIDNotFoundException {
        getToiletLocationByID(id).addComment(comment);
    }

    public void addToiletLocation(Toilet toilet) throws ToiletLocationIDAlreadyExists {
        if (idExists(toilet.getId())) {
            throw new ToiletLocationIDAlreadyExists();
        }
        toiletLocations.add(toilet, idIncrementor++);
    }

    private boolean idExists(int id) {
        for (Toilet t : toiletLocations) {
            if (t.getId() == id) {
                return true;
            }
        }
        return false;
    }
}
