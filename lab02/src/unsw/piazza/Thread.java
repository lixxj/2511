package unsw.piazza;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * A thread in the Piazza forum.
 */
public class Thread {

    private String title;
    private User author;
    private List<Post> posts = new ArrayList<>();
    private List<String> tags = new ArrayList<>();

    /**
     * Creates a new thread with a title and an initial first post.
     * The author of the first post at the time of thread creation is the owner of the thread.
     * The owner cannot change once the thread is created.
     * @param title
     * @param firstPost
     */
    public Thread(String title, Post firstPost) {
        this.title = title;
        this.author = firstPost.getAuthor();
        this.posts.add(firstPost);
    }

    /**
     * @return The owner of the thread
     */
    public User getOwner() {
        return this.author;
    }

    /**
     * @return The title of the thread
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * @return A SORTED list of unique tags
     */
    public List<String> getTags() {
        Collections.sort(this.tags);
        return this.tags;
    }

    /**
     * @return A list of posts in this thread, in the order that they were published
     */
    public List<Post> getPosts() {
        return this.posts;
    }

    /**
     * Adds the given post object into the list of posts in the thread.
     * @param post
     */
    public void publishPost(Post post) {
        this.posts.add(post);
    }

    /**
     * Allows the given user to remove the Post from the thread.
     * Does nothing if the post is not in the thread.
     * @param post
     * @throws PermissionDeniedException if the given user is not an author of the post
     */
    public void removePost(Post post, User by) throws PermissionDeniedException {
        if (!by.equals(this.author)) throw new PermissionDeniedException("Unauthorised attempt to remove post!");
        this.posts.remove(post);
    }

    /**
     * Allows the given uer to edit the thread title.
     * @param title
     * @param by
     * @throws PermissionDeniedException if the given user is not the owner of the thread.
     */
    public void setTitle(String title, User by) throws PermissionDeniedException { 
        if (!by.equals(this.author)) throw new PermissionDeniedException("Unauthorised attempt to set title!");
        this.title = title; 
    }

    /**
     * Allows the given user to replace the thread tags (list of strings)
     * @param tags
     * @param by
     * @throws PermissionDeniedException if the given user is not the owner of the thread.
     */
    public void setTags(String[] tags, User by) throws PermissionDeniedException {
        if (!by.equals(this.author)) throw new PermissionDeniedException("Unauthorised attempt to set tags!");
        this.tags = Arrays.asList(tags);
    }
}
