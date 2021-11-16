package dungeonmania.entities.collectables;

import dungeonmania.entities.Entity;
import dungeonmania.entities.EntityInfo;
import org.json.JSONObject;

/**
 * a class representing an Key
 */
public class Key extends Entity {
    
    private int keyId;

    /**
     * Class constructor.
     * 
     * @param entityInfo - entityInfo object
     * @param keyId - id of key (must correspond to key id of a door in the same dungeon)
     */
    public Key(EntityInfo entityInfo, int keyId){
        super(entityInfo);
        this.keyId = keyId;
    }

    //GETTER
    public int getKeyId() {
        return keyId;
    }

    @Override
    public JSONObject getEntityStateAsJSON() {
        JSONObject entityObject = super.getEntityStateAsJSON();
        entityObject.put("key", keyId);
        return entityObject;
    }
}
