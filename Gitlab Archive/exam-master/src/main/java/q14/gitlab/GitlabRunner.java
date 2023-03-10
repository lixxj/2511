package q14.gitlab;

public class GitlabRunner {
    public void run(Runnable runnable) {
        try {
            runnable.run();
        } catch (Throwable t) {}
    }
}