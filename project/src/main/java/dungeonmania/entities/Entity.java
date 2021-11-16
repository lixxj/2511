package dungeonmania.entities;

import dungeonmania.entities.buildables.Sceptre;
import dungeonmania.entities.moving.enemies.Mercenary;
import dungeonmania.entities.staticitems.ZombieToastSpawner;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Position;
import org.json.JSONObject;

public abstract class Entity {

    private EntityInfo entityInfo;

    /**
     * Class constructor.
     * @param entityInfo
     */
    public Entity(EntityInfo entityInfo) {
        this.entityInfo = entityInfo;
    }

    /**
     * Simple getter to retrieve entity id
     */
    public String getId() {
        return entityInfo.getId();
    }

    /**
     * Simple getter to retrieve position
     */
    public Position getPosition() {
        return entityInfo.getPosition();
    }

    /**
     * Simple getter to retrieve entity's type
     */
    public String getType() {
        return entityInfo.getType();
    }

    /**
     * Simple setter to update position
     *
     * @param position new position of entity
     */
    public void setPosition(Position position) {
        entityInfo.setPosition(position);;
    }

    public boolean isInteractable() {
        return (this instanceof Mercenary) || (this instanceof ZombieToastSpawner) || (this instanceof Sceptre);
    }

    /**
     * Simple getter to build and get Entity Response
     *
     */
    public EntityResponse getEntityResponse() {
        return new EntityResponse(entityInfo.getId(), entityInfo.getType(), entityInfo.getPosition(), this.isInteractable());
    }

    public boolean isCollectible() {
        return entityInfo.isCollectible();
    }

    public JSONObject getEntityStateAsJSON() {
        JSONObject entityObject = new JSONObject();
        entityObject.put("id", this.getId());
        entityObject.put("x", this.getPosition().getX());
        entityObject.put("y", this.getPosition().getY());
        entityObject.put("type", this.getType());
        entityObject.put("isCollectible", this.isCollectible());
        return entityObject;
    }

}
