package unsw.sso;

public abstract class Page {
    public abstract Page interact(Object o);
    public abstract Page back();
    public abstract String getName();
}
