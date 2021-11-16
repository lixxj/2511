package dungeonmania.serializer;

import dungeonmania.dungeon.Dungeon;
import dungeonmania.dungeon.DungeonInfo;
import dungeonmania.entities.Entity;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class Serializer {

    /**
     * Converts a dungeon to a json string representation
     * @param dungeon
     * @return
     */
    public static String getDungeonAsJSON(Dungeon dungeon) {

        JSONObject dungeonJson = new JSONObject();
        JSONArray jsonEntities = new JSONArray();

        DungeonInfo dungeonInfo = dungeon.getDungeonInfo();

        dungeonJson.put("height", dungeonInfo.getHeight());
        dungeonJson.put("width", dungeonInfo.getWidth());
        dungeonJson.put("entities", jsonEntities);
        dungeonJson.put("name", dungeonInfo.getName());
        dungeonJson.put("id", dungeonInfo.getId());
        dungeonJson.put("mode", dungeonInfo.getMode());
        dungeonJson.put("tick", dungeon.getCurrentTick());

        List<Entity> entities = dungeon.getEntities();

        for (Entity entity : entities) {
            jsonEntities.put(entity.getEntityStateAsJSON());
        }

        if (dungeonInfo.getGoals() != null) {
            dungeonJson.put("goal-condition", dungeonInfo.getGoals().getJSONObject());
        }

        return dungeonJson.toString(2);
    }


}
