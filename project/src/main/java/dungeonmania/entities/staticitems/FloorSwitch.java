package dungeonmania.entities.staticitems;

import java.util.List;

import dungeonmania.entities.Entity;
import dungeonmania.entities.EntityInfo;
import dungeonmania.entities.collectables.Bomb;
import dungeonmania.dungeon.Dungeon;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import org.json.JSONObject;


public class FloorSwitch extends Entity implements StaticEffect {

    private boolean switchOn = false;
    /**
     * Class constructor.
     *
     * @param entityInfo     entityInfo object
     */
    public FloorSwitch(EntityInfo entityInfo) {
        super(entityInfo);
    }
    public FloorSwitch(EntityInfo entityInfo, boolean switchOn) {
        super(entityInfo);
        this.switchOn = switchOn;
    }

    @Override
    public boolean moveAllowedPlayer(Dungeon dungeon, Direction movementDirection) {
        List<Entity> entities = dungeon.getEntitiesAtPosition(((Entity)this).getPosition());
        for (Entity entity: entities) {
            if (entity instanceof Boulder) {
                return ((StaticEffect) entity).moveAllowedPlayer(dungeon, movementDirection);
            }
        }
        return true;
    }

    /**
     * Sets switchOn to true when called.
     * Also checks if a bomb is cardinally adjacent to the switch.
     * If true, calls explodeBomb.
     * @param dungeon
     */
    public void triggerOn(Dungeon dungeon) {
        switchOn = true;

        List<Position>  adjacentSwitchPositions = this.getPosition().getAdjacentMovementPositions();
        List<Entity> adjacentSwitchEntities;
        for(Position adjacentSwitchPosition : adjacentSwitchPositions){
            adjacentSwitchEntities = dungeon.getEntitiesAtPosition(adjacentSwitchPosition);
            for(Entity adjacentSwitchEntity : adjacentSwitchEntities){
                if(adjacentSwitchEntity instanceof Bomb){
                    ((Bomb)adjacentSwitchEntity).explodeBomb(dungeon);
                    break;
                }
            }
        }
    }

    /**
     * Sets switchOn to false.
     */
    public void triggerOff() {
        switchOn = false;
    }

    /**
     * Returns switchOn boolean.
     */
    public boolean isSwitchOn() {
        return switchOn;
    }

    @Override
    public JSONObject getEntityStateAsJSON() {
        JSONObject entityObject = super.getEntityStateAsJSON();
        entityObject.put("switchOn", switchOn);
        return entityObject;
    }

}
