package dungeonmania.entities.staticitems;

import dungeonmania.entities.Entity;
import dungeonmania.entities.EntityInfo;
import dungeonmania.dungeon.Dungeon;
import dungeonmania.util.Direction;

public class Exit extends Entity implements StaticEffect {
    /**
     * Class constructor.
     *
     * @param entityInfo     entityInfo object
     */
    public Exit(EntityInfo entityInfo) {
        super(entityInfo);
    }

    @Override
    public boolean moveAllowedPlayer(Dungeon dungeon, Direction movementDirection) {
        return true;
    }

}
