package q13;

import java.time.LocalDateTime;
import java.util.List;

public class LiveArticle {
    LocalDateTime timeCreated;
    List<Post> posts; // composition 1..*

    public LiveArticle(LocalDateTime timeCreated) {
        this.timeCreated = timeCreated;
    }

    public String createPost(LocalDateTime timeCreated, String postTitle, String postContent) {
        return null;
    }

    public void updatePost(String postID, String newContent) {
        
    }

    public void deletePost(String postID) {

    }

    public void addComment(String postID, String comment) {
        
    }

}
