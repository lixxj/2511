package dungeonmania.goals;

import dungeonmania.entities.Entity;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * Represents two goals with AND condition
 */
public class AndGoal implements Goal {

    private final Goal subGoal1;
    private final Goal subGoal2;

    public AndGoal(Goal subGoal1, Goal subGoal2) {
        this.subGoal1 = subGoal1;
        this.subGoal2 = subGoal2;
    }

    @Override
    public boolean isComplete(List<Entity> entities) {
        return subGoal1.isComplete(entities) && subGoal2.isComplete(entities);
    }

    @Override
    public String getNameFrontend() {
        return subGoal1.getNameFrontend() + " AND " + subGoal2.getNameFrontend();
    }

    @Override
    public String getName() {
        return subGoal1.getName() + " AND " + subGoal2.getName();
    }

    @Override
    public JSONObject getJSONObject() {
        JSONObject goal = new JSONObject();
        goal.put("goal", "AND");
        JSONArray subgoals = new JSONArray();
        subgoals.put(subGoal1.getJSONObject());
        subgoals.put(subGoal2.getJSONObject());
        goal.put("subgoals", subgoals);
        return goal;
    }
}
