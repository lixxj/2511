package unsw.piazza;

import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;

/**
 * A Piazza Forum 
 * 
 * @author Nick Patrikeos
 */
public class PiazzaForum {
    
    private String className;
    private List<Thread> threads = new ArrayList<>();

    /**
     * Initialises the new PiazzaForum with the given group name
     */
    public PiazzaForum(String className) { this.className = className; }

    /**
     * @return The name of the forum
     */
    public String getName() {
        return className;
    }

    /**
     * Sets the name of the group of the Forum
     * @param name
     */
    public void setName(String name) { this.className = name; }

    /**
     * Returns a list of Threads in the Forum, in the order that they were published
     */
    public List<Thread> getThreads() {
        return this.threads;
    }

    /**
     * Creates a new thread with the given title and adds it to the Forum.
     * The content and author are provided to allow you to create the first Post object.
     * Threads are stored in the order that they are published.
     * Returns the new Thread object
     * @param title
     * @param content
     * @param author
     */
    public Thread publish(String title, String content, User author) {
        Post firstPost = new Post(content, author);
        Thread newThread = new Thread(title, firstPost);
        this.threads.add(newThread);
        return newThread;
    }

    /**
     * Searches all forum Threads for any that contain the given tag.
     * Returns a list of all matching Thread objects in the order that they were published.
     * @param tag
     * @return
     */
    public List<Thread> searchByTag(String tag) {
        return this.threads.stream()
                .filter(x -> x.getTags().contains(tag))
                .collect(Collectors.toList());
    }

    /**
     * Searches all forum Threads for Posts by the given author.
     * Returns a list of matching Post objects in the order that they were published.
     * @param author
     * @return
     */
    public List<Post> searchByAuthor(User author) {
        List<Post> allPosts = new ArrayList<>();
        for (Thread t : this.threads) for (Post p : t.getPosts()) 
            if (p.getAuthor().equals(author)) allPosts.add(p);
        return allPosts;
    }

}