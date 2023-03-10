package q13;

import java.time.LocalDateTime;
import java.util.List;

public class Post {
    String postTitle;
    String postContent;
    LocalDateTime timeCreated;
    List<Comment> comments; // composition 1..*

    public Post(LocalDateTime timeCreated, String postTitle, String postContent) {
        this.timeCreated = timeCreated;
        this.postTitle = postTitle;
        this.postContent = postContent;
    }

    public void updatePost(String newContent) {
        this.postContent = newContent;
    }

    public void addComment(String comment) {
        
    }
}
