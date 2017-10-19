package be.pxl.denmax.poopchasers.Model;

// Class for representing a comment
public class ToiletComment {
    private String content;
    private String username;
    private int rating;

    // Constructor
    public ToiletComment(String content, String username, int rating) {
        this.content = content;
        this.username = username;
        if (rating > 5) {
            rating = 5;
        }
        else if (rating < 1) {
            rating = 1;
        }
        this.rating = rating;
    }

    // Getters:
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
