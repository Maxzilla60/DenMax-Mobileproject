package be.pxl.denmax.poopchasers.Model;

import java.util.ArrayList;
import java.util.List;

// Class for keeping a list of comments
public class ToiletCommentsList {
    private List<ToiletComment> commentsList;

    // Constructor
    public ToiletCommentsList() {
        commentsList = new ArrayList<ToiletComment>();
    }

    // Dynamically calculates average rating
    public int getAverageRating() {
        if (commentsList.size() <= 0) {
        	return 0;
        }
        
        int a = 0;
        for (ToiletComment comment : commentsList) {
            a += comment.getRating();
        }
        return (Math.round((float) a / commentsList.size()));
    }

    // Adding a comment
    public void add(ToiletComment comment) {
        commentsList.add(comment);
    }

    public List<ToiletComment> getCommentsList() {
        return commentsList;
    }
}
