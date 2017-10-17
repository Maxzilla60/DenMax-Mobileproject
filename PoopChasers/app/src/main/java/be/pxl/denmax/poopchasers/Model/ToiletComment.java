package be.pxl.denmax.poopchasers.Model;

import be.pxl.denmax.poopchasers.Exceptions.InvalidRatingException;

/**
 * Created by 11502759 on 17/10/2017.
 */

class ToiletComment {
    private String content;
    private String username;
    private int rating;

    public ToiletComment(String content, String username, int rating) throws InvalidRatingException {
        this.content = content;
        this.username = username;
        if (rating > 5 || rating < 1) {
            throw new InvalidRatingException();
        }
        this.rating = rating;
    }

    public String getContent() {
        return content;
    }

    public String getUsername() {
        return username;
    }

    public int getRating() {
        return rating;
    }
}
