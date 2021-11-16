package dungeonmania.goals;

import java.util.List;

import dungeonmania.entities.Entity;
import dungeonmania.entities.staticitems.FloorSwitch;
import dungeonmania.entities.DungeonConstants;
import org.json.JSONObject;

/**
 * a class that represents  goal of triggering all switches using boulders, implements Goal(used in composite pattern)
 */
public class BoulderGoal implements Goal {

    @Override
    public boolean isComplete(List<Entity> entities) {
        for (Entity entity: entities) {
            if (entity instanceof FloorSwitch) {
                if (!((FloorSwitch) entity).isSwitchOn()) {
                    return false;
                }
            }
        }
        return true;
    }
    
    @Override
    public String getNameFrontend() {
        // This is because they have named the goal - boulders
        // but to render on the frontend want you to return
        // :boulder
        return ":" + DungeonConstants.ENTITY_BOULDER;
    }

    @Override
    public String getName() {
        return DungeonConstants.ENTITY_BOULDER;
    }

    @Override
    public JSONObject getJSONObject() {
        JSONObject goalJson = new JSONObject();
        goalJson.put("goal", getName());
        return goalJson;
    }
}
