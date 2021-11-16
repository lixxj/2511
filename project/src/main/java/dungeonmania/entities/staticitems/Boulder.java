package dungeonmania.entities.staticitems;

import java.util.List;

import dungeonmania.entities.*;
import dungeonmania.dungeon.*;
import dungeonmania.util.*;

public class Boulder extends Entity implements StaticEffect {

    /**
     * Class constructor.
     *
     * @param entityInfo     entityInfo object
     */
    public Boulder(EntityInfo entityInfo) {
        super(entityInfo);
    }


    @Override
    public boolean moveAllowedPlayer(Dungeon dungeon, Direction movementDirection) {

        boolean boulderMoved = false;

        // Move boulder in direction
        Position newPosition = getPosition().
                translateBy(movementDirection);

        // Check obstacles in the position that the boulder would be moved to
        List<Entity> possibleObstacles = dungeon.getEntitiesAtPosition(newPosition);

        // No obstacles, boulder can move
        if (possibleObstacles.isEmpty()) {
            boulderMoved = true;
        }

        // Check if any obstacle is a floor switch
        for (Entity possibleObstacle : possibleObstacles) {
            if (possibleObstacle instanceof FloorSwitch) {
                ((FloorSwitch) possibleObstacle).triggerOn(dungeon);
                boulderMoved = true;
                break;
            }
        }

        if (boulderMoved) {
            // trigger off all floor switches that were on at the previous location
            for (Entity oldPosition : dungeon.getEntitiesAtPosition(this.getPosition())) {
                if (oldPosition instanceof FloorSwitch) {
                    ((FloorSwitch) oldPosition).triggerOff();
                }
            }
        }
        
        if (!boulderMoved) return false;
        setPosition(newPosition);      
        return true;
    }



}