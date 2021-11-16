package dungeonmania.entities.staticitems;

import dungeonmania.entities.Entity;
import dungeonmania.entities.EntityInfo;
import dungeonmania.dungeon.Dungeon;
import dungeonmania.util.Direction;

public class SwampTile extends Entity implements StaticEffect {

    private int movementFactor;

    /**
     * Class constructor.
     *
     * @param movementFactor    movement factor of swamp tile
     */

    public SwampTile(EntityInfo entityInfo, int movementFactor) {
        super(entityInfo);
        this.movementFactor = movementFactor;
    }

    /**
     * Gets the movement factor of the swamp tile.
     * @return swamptile's movementFactor 
     */
    public int getMovementFactor() {
        return movementFactor;
    }
    /**
     * Boolean to ensure that player can move on SwampTile
     * @return true of player is allowed to move on a swamp tile
     */
    @Override
    public boolean moveAllowedPlayer(Dungeon dungeon, Direction moveDirection) {
        return true;
    }
    
}
