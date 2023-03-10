package unsw.blackout;

interface Entity {
    /**
     * Check if Entity in range and is allowedToConnect to the target
     * 
     * @param target - Entity that the caller is trying to connect to
     * @param range - the range of the current Entity
     * @return true if target is a Entity object and in range and is allowedToConnect
     */
    public boolean connectable(Object target);
    public boolean connectable(Object target, double range);

    /**
     * Check if Entity is allowed to connect to the target
     * @param target - target that the caller is trying to connect to
     * @return true if allowed to connect otherwise false
     */
    public boolean allowedToConnect(Object target);
}
