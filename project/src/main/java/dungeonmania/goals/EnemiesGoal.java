package dungeonmania.goals;

import java.util.List;

import dungeonmania.entities.Entity;
import dungeonmania.entities.moving.enemies.Enemy;
import dungeonmania.entities.moving.enemies.Mercenary;
import dungeonmania.entities.DungeonConstants;
import org.json.JSONObject;

/**
 * a class that represents killing all Enemies as goal, implements Goal(used in composite pattern)
 */
public class EnemiesGoal implements Goal {
    @Override
    public boolean isComplete(List<Entity> entities) {
        for (Entity entity: entities) {
            if (entity instanceof Enemy) {
                if (entity instanceof Mercenary && ((Mercenary)entity).isAlly()) continue;
                return false;
            }
        }
        return true;
    }

    @Override
    public String getNameFrontend() {
        return ":" + DungeonConstants.GOAL_ENEMIES;
    }

    @Override
    public String getName() {
        return DungeonConstants.GOAL_ENEMIES;
    }

    @Override
    public JSONObject getJSONObject() {
        JSONObject goalJson = new JSONObject();
        goalJson.put("goal", getName());
        return goalJson;
    }
}
