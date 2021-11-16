package dungeonmania.entities.staticitems;

import dungeonmania.entities.collectables.Key;
import dungeonmania.entities.moving.player.Player;

import java.util.Objects;

import dungeonmania.entities.*;
import dungeonmania.dungeon.*;
import dungeonmania.util.Direction;
import org.json.JSONObject;

public class Door extends Entity implements StaticEffect {

    private boolean doorClosed;
    private final int keyId;

    /**
     * Class Constructor.
     * @param entityInfo     entityInfo object
     * @param keyId          id of key that will open this door
     */
    public Door(EntityInfo entityInfo, int keyId){
        super(entityInfo);
        this.keyId = keyId;  
        this.doorClosed = true;
    }

    public Door(EntityInfo entityInfo, int keyId, boolean doorClosed){
        super(entityInfo);
        this.keyId = keyId;
        this.doorClosed = doorClosed;
    }

    @Override
    public boolean moveAllowedPlayer(Dungeon dungeon, Direction movementDirection) {
        Player player = dungeon.getPlayer();
        
        // Door is open so player can move
        if (!isDoorClosed()) return true;
        
        // Sunstone takes priority over any keys
        if(player.countItems(DungeonConstants.ENTITY_SUN_STONE) != 0){
             openDoor();
        } else {
            Key key = (Key) player.getItemByType(DungeonConstants.ENTITY_KEY);
            if (key != null && Objects.equals(key.getKeyId(), this.getKeyId())) {
                player.removeItemFromInventory(key.getId()); // Remove key from player inventory
                openDoor();
            }
        }
        
        return !isDoorClosed();
    }

    /**
     * Checks if the door is open
     * @return true if door is open, false otherwise.
     */
    public boolean isDoorClosed() {
        return this.doorClosed;
    }

    /**
     * Opens the instantiated door.
     */
    public void openDoor() {
        this.doorClosed = false;
    }

    /**
     * Gets the key id of the key that will open this instantiated door.
     * @return key id
     */
    public int getKeyId() {
        return keyId;
    }

    @Override
    public JSONObject getEntityStateAsJSON() {
        JSONObject entityObject = super.getEntityStateAsJSON();
        entityObject.put("doorClosed", doorClosed);
        entityObject.put("key", keyId);
        return entityObject;
    }
}