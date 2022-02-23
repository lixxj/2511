package unsw.blackout;

public interface Connectable {
    
    /**
     * Check if current Connectable instance can connect to the given object
     * (as per project constraints)
     * 
     * @param target - target object that the caller is trying to connect to
     * @param range - range in which the current Connectable object operates
     * @return true if target is a Connectable object AND in range AND visible
     */
    public boolean canConnect(Object target, double range);

    /**
     * Check if current Connectable instance can connect to the given object
     * 
     * @param target - target object that the caller is trying to connect to
     * @return true if target is a Connectable object AND in range AND visible
     */
    public boolean canConnect(Object target);

    /**
     * Determines whether the instantiated Connectable object is allowed to
     * connect to the specified target as per project specifications.
     * @param target - target object that the caller is trying to connect to
     * @return true if allowed to connect else false
     */
    public boolean allowedToConnect(Object target);
}
