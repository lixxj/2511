package dungeonmania.goals;

import java.util.List;

import dungeonmania.entities.Entity;
import dungeonmania.entities.DungeonConstants;
import org.json.JSONObject;

/**
 * a class that represents goal of reaching the exit, implements Goal(used in composite pattern)
 */
public class ExitGoal implements Goal {

    @Override
    public boolean isComplete(List<Entity> entities) {
        Entity exit = entities.stream().
                filter(entity -> entity.getType().equals(DungeonConstants.ENTITY_EXIT)).
                findFirst().orElse(null);
        if (exit == null) {
            return true;
        }
        Entity player = entities.stream().
                filter(entity -> entity.getType().equals(DungeonConstants.ENTITY_PLAYER)).
                findFirst().orElse(null);
        if (player == null) {
            return false;
        }
        return exit.getPosition().equals(player.getPosition());
    }

    @Override
    public String getNameFrontend() {
        return ":" + DungeonConstants.ENTITY_EXIT;
    }

    @Override
    public String getName() {
        return DungeonConstants.ENTITY_EXIT;
    }

    @Override
    public JSONObject getJSONObject() {
        JSONObject goalJson = new JSONObject();
        goalJson.put("goal", getName());
        return goalJson;
    }
}
