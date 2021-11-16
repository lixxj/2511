package dungeonmania.entities.collectables;

import java.util.List;

import dungeonmania.entities.Entity;
import dungeonmania.entities.EntityInfo;
import dungeonmania.entities.moving.player.Player;
import dungeonmania.entities.staticitems.FloorSwitch;
import dungeonmania.dungeon.Dungeon;
import dungeonmania.util.Position;



/**
 * a class representing an Bomb
 */
public class Bomb extends Entity {

    /**
     * Constructor for a bomb.
     * @param entityInfo - entityInfo object
     */
    public Bomb(EntityInfo entityInfo){
        super(entityInfo);
    }
    
    /**
     * Checks if a bomb can explode at a given position. If there is a triggered floor switch in its 
     * cardinally adjacent squares, it can explode.
     * @param dungeon - dungeon in which bomb is set to explode
     * @param position - position in which the bomb is supposed to be set
     * @return true if it can explode, false otherwise.
     */
    public boolean canExplodeBomb(Dungeon dungeon, Position position) {
        List<Position>  adjacentBombPositions = position.getAdjacentMovementPositions();
        return adjacentBombPositions.stream().anyMatch(x -> (checkTriggeredFloorSwitch(dungeon, x)));
    }

    /**
     * Checks for a triggered floor switch on the specified position in the specified dungeon.
     * @param dungeon 
     * @param position
     * @return true if a triggered floor switch is on the specified position, false otherwise.
     */
    private boolean checkTriggeredFloorSwitch(Dungeon dungeon, Position position) {
        return dungeon.getEntitiesAtPosition(position).stream().anyMatch(x->x instanceof FloorSwitch && ((FloorSwitch)x).isSwitchOn());
    }

    /**
     * Explodes a bomb.
     * 
     * @pre caller's responsibility to check if bomb can explode.
     * @param dungeon - dungeon in which the bomb must explode
     */
    public void explodeBomb(Dungeon dungeon) {
        // Remove the bomb from the dungeon
        dungeon.removeEntity(this);

        List<Position>  adjacentBombPositions = this.getPosition().getAdjacentPositions();
        for(Position adjacentBombPosition : adjacentBombPositions) {
            List<Entity> adjacentBombEntities = dungeon.getEntitiesAtPosition(adjacentBombPosition);
            for(Entity adjacentBombEntity : adjacentBombEntities){
                // Can trigger another bomb
                if (adjacentBombEntity instanceof Bomb) ((Bomb)adjacentBombEntity).explodeBomb(dungeon);
                if (!(adjacentBombEntity instanceof Player)) dungeon.removeEntity(adjacentBombEntity);
            }
        }
    }
}
