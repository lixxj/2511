package dungeonmania.entities.buildables;

import dungeonmania.entities.*;

public class Sceptre extends Entity {

    /**
     * Class constructor.
     *
     * @param entityInfo - entityInfo object
     */
    public Sceptre(EntityInfo entityInfo){
        super(entityInfo);
    }

    /**
     * Gets the amount of ticks a sceptre can control an entity for.
     * @return time
     */
    public static int getMindControlTime() {
        return DungeonConstants.SCEPTRE_MIND_CONTROL_TIME;
    }
}
