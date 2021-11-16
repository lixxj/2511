package dungeonmania.goals;

import java.util.List;

import dungeonmania.entities.Entity;
import dungeonmania.entities.collectables.SunStone;
import dungeonmania.entities.collectables.Treasure;
import dungeonmania.entities.DungeonConstants;
import org.json.JSONObject;

/**
 * a class that represents a goal as the collection of a treasure, implements Goal(used in composite pattern)
 */
public class TreasureGoal implements Goal {

    @Override
    public boolean isComplete(List<Entity> entities) {
        for (Entity entity: entities) {
            if (entity instanceof Treasure) {
                return false;
            }
            if (entity instanceof SunStone) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String getNameFrontend() {
        return ":" + DungeonConstants.ENTITY_TREASURE;
    }

    @Override
    public String getName() {
        return DungeonConstants.ENTITY_TREASURE;
    }

    @Override
    public JSONObject getJSONObject() {
        JSONObject goalJson = new JSONObject();
        goalJson.put("goal", getName());
        return goalJson;
    }
}
