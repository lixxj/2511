package dungeonmania.entities;

import dungeonmania.util.Position;

public class EntityInfo {
    
    private String id;
    private Position position;
    private String type;
    private boolean isCollectible;

    /**
     * Class constructor.
     * 
     * @param id - unique entity name (given on time of creation)
     * @param position - initial position of entity
     * @param type - type of entity (constant that must be provided in DungeonConstants file)
     * @param isCollectible - true if entity can be collected by a player
     */
    public EntityInfo(String id, Position position, String type, boolean isCollectible) {
        this.id = id;
        this.position = position;
        this.type = type;
        this.isCollectible = isCollectible;
    }

    public String getId() {
        return id;
    }

    public Position getPosition() {
        return position;
    }

    public String getType() {
        return type;
    }

    public boolean isCollectible() {
        return isCollectible;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

}
