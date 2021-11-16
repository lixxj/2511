package dungeonmania.goals;

import java.util.List;

import dungeonmania.entities.Entity;
import org.json.JSONObject;

/**
 * an interface that is implemented by the different goal classes used in a composite pattern
 */
public interface Goal {
    /**
     * checks if a certain goal is completed for a grpup of entities
     * @param entities
     * @return boolean that represents the status of goal completion
     */
    public boolean isComplete(List<Entity> entities);

    /**
     * Get the name of the goal for the front end
     * @return String
     */
    public String getNameFrontend();

    /**
     * Get the name of the goal (internal)
     * @return
     */
    public String getName();

    /**
     * Get the goal as a jsonObject
     * @return JSONObject
     */
    public JSONObject getJSONObject();

}
