package dungeonmania.entities.staticitems;

import java.util.Objects;

import dungeonmania.entities.DungeonConstants;
import dungeonmania.entities.Entity;
import dungeonmania.entities.EntityInfo;
import dungeonmania.entities.moving.player.Player;
import dungeonmania.dungeon.Dungeon;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import org.json.JSONObject;

public class Portal extends Entity implements StaticEffect{

    private String colour;
    /**
     * Class constructor.
     *
     * @param entityInfo     entityInfo object
     * @param colour         colour of portal (only 2 portals can exist of the same colour)
     */
    public Portal(EntityInfo entityInfo, String colour){
        super(entityInfo);
        this.colour = colour;
    }

    @Override
    public boolean moveAllowedPlayer(Dungeon dungeon, Direction movementDirection) {
        Portal correspondingPortal = getCorrespondingPortal(dungeon);
        Position newSpawnPoint = correspondingPortal.getSpawnPoint(movementDirection);
        Player player = dungeon.getPlayer();
        player.setPosition(newSpawnPoint);
        return false;
    }

    /**
     * Gets another portal (of different id) that has the same colour as the current portal.
     * 
     * @pre only one corresponding portal exists of the same colour
     * @param dungeon
     * @return the corresponding portal for the current instantiated portal
     */
    public Portal getCorrespondingPortal(Dungeon dungeon) {
        Portal correspondingPortal = (Portal) dungeon.getEntities().stream().filter(entity -> {
            return (Objects.equals(entity.getType(), DungeonConstants.ENTITY_PORTAL) 
                && !Objects.equals(entity.getId(), ((Entity)this).getId()) 
                && Objects.equals(((Portal)entity).getColour(), this.colour)); 
        }).findFirst().get();
        return correspondingPortal;
    }

    /**
     * Gets the current portal's default spawn point.
     * @param dungeon
     * @return the current portal spawn point where the entity is teleported to
     */
    public Position getCorrespondingSpawnPoint(Dungeon dungeon) {
        return getCorrespondingPortal(dungeon).getSpawnPoint();
    }

    /**
     * Gets the spawn point of the current portal in the specified movement direction.
     * @param movementDirection - specified direction in which the spawn point should be
     *                             relative to the corresponding portal
     * @return the default position where an entity entering the current instantiated 
     *         portal is teleported to
     */
    public Position getSpawnPoint(Direction movementDirection) {
        return ((Entity)this).getPosition().translateBy(movementDirection);
    }

    /**
     * Gets the default spawn point of the corresponding portal.
     * @return the default position where an entity entering the current instantiated 
     *         portal is teleported to
     */
    public Position getSpawnPoint() {
        return getSpawnPoint(Direction.UP);
    }

    /**
     * @return colour of current instantiated portal
     */
    public String getColour() {
        return colour;
    }

    @Override
    public JSONObject getEntityStateAsJSON() {
        JSONObject entityObject = super.getEntityStateAsJSON();
        entityObject.put("colour", colour);
        return entityObject;
    }
}
