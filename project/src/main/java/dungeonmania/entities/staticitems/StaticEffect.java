package dungeonmania.entities.staticitems;

import dungeonmania.dungeon.Dungeon;
import dungeonmania.util.Direction;

/**
 * returns true if the player can move through the static entitiy.
 *
 * @param dungeon 
 * @param direction

 */
public interface StaticEffect {
    public boolean moveAllowedPlayer(Dungeon dungeon, Direction movementDirection);
}
