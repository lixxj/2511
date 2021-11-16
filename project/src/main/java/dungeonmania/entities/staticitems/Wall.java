package dungeonmania.entities.staticitems;

import dungeonmania.entities.Entity;
import dungeonmania.entities.EntityInfo;
import dungeonmania.dungeon.Dungeon;
import dungeonmania.util.Direction;

public class Wall extends Entity implements StaticEffect {

    /**
     * Class constructor.
     *
     * @param id             unique id for entity
     * @param position       initial position of entity
     */
    public Wall(EntityInfo entityInfo) {
        super(entityInfo);
    }

    @Override
    public boolean moveAllowedPlayer(Dungeon dungeon, Direction moveDirection) {
        return false;
    }
    

}
