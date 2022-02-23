package unsw.piazza;

import java.util.List;
import java.util.ArrayList;

/**
 * A post in the Piazza forum
 */
public class Post {

    private String content;
    private User author;
    private int upvotes;
    private List<User> upvoteUsers = new ArrayList<>();
    
    /**
     * Creates a new post by the author with the given content.
     * @param content
     * @param author
     */
    public Post(String content, User author) {
        this.content = content;
        this.author = author;
    }

    /**
     * @return Author of the post
     */
    public User getAuthor() {
        return this.author;
    }

    /**
     * @return The content of the post
     */
    public String getContent() {
        return this.content;
    }

    /**
     * @return A non-negative integer representing the total number of upvotes
     */
    public int getUpvotes() {
        return this.upvotes;
    }

    /**
     *  Called when the given user wants to update the content
     *  @param content
     *  @throws PermissionDeniedException if the given user is not the author
     */
    public void setContent(String content, User by) throws PermissionDeniedException {
        if (!by.equals(this.author)) throw new PermissionDeniedException("Unauthorised attempt to update post content!");
        this.content = content;
    }

    /**
     * Called when the given user wants to upvote this post.
     * A user can only perform an upvote once. If they try more than once, nothing happens.
     * Users can upvote their own posts.
     * @param by
     */
    public void upvote(User by) {
        if (upvoteUsers.contains(by)) return;
        upvoteUsers.add(by);
        this.upvotes += 1;
    }
}