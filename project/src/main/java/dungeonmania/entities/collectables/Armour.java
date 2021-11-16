package dungeonmania.entities.collectables;

import dungeonmania.entities.DungeonConstants;
import dungeonmania.entities.Entity;
import dungeonmania.entities.EntityInfo;
import dungeonmania.entities.buildables.Protection;

/**
 * a class representing an armour
 */
public class Armour extends Entity implements Protection  {
    private int durability;
    /**
     * Class constructor.
     *
     * @param entityInfo - entityInfo object
     */
    public Armour(EntityInfo entityInfo) {
        super(entityInfo);
        this.durability = DungeonConstants.SHIELD_DURABILITY;
    }


    @Override
    public void decreaseDurability() {
        this.durability -= 1;
    }

    @Override
    public boolean hasProtection() {
        return getDurability() > 0;
    }

    @Override
    public double getProtectionFactor() {
        return DungeonConstants.ARMOUR_PROTECTION_FACTOR;
    }

    @Override
    public int getDurability() {
        return this.durability;
    }
}
