package dungeonmania.entities.buildables;

import dungeonmania.entities.*;
import org.json.JSONObject;


/**
 * a class representing a shield
 */
public class Shield extends Entity implements Protection {

    private int durability;
    
    /**
     * Constructor for a shield object.
     * 
     * @param entityInfo - entityInfo object
     */
    public Shield(EntityInfo entityInfo) {
        super(entityInfo);
        this.durability = DungeonConstants.SHIELD_DURABILITY;
    }

    @Override
    public void decreaseDurability() {
        durability -= 1;
    }

    @Override
    public boolean hasProtection() {
        return getDurability() > 0;
    }

    @Override
    public double getProtectionFactor() {
        return DungeonConstants.SHIELD_PROTECTION_FACTOR;
    }

    @Override
    public int getDurability() {
        return this.durability;
    }


}
