package be.pxl.denmax.poopchasers.Model;

import java.util.List;

/**
 * Created by 11502759 on 17/10/2017.
 */

class ToiletCommentsList {
    private List<ToiletComment> commentsList;

    public int getAvarageRating() {
        int a = 0;
        for (ToiletComment comment : commentsList) {
            a += comment.getRating();
        }
        return (a / commentsList.size());
    }

    public void add(ToiletComment comment) {
        commentsList.add(comment);
    }
}
