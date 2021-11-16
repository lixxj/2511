package dungeonmania.entities.buildables;

public interface Protection {

    /**
     * Decreases the durability of the implemented protection object.
     */
    public void decreaseDurability();

    /**
     * Checks if the protection is still usable (and still durable).
     * @return true if usable, false otherwise.
     */
    public boolean hasProtection();

    /**
     * Gets the factor by which the protection factor reduces the damage inflicted upon
     * the wearing entity.
     * @return number specifying the protection factor
     */
    public double getProtectionFactor();

    /**
     * Returns the current durability of the implemented protection object.
     * @return durability
     */
    public int getDurability();

}
