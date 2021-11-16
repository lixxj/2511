package dungeonmania.entities.buildables;

import java.util.HashMap;
import java.util.Map;

import dungeonmania.entities.moving.player.Inventory;

/** 
 * a class representing a recipe used for building craftable objects
 */
public class Recipe {
    private Map<String, Integer> ingredients;

    /**
     * Constructor for a recipe.
     */
    public Recipe() {
        ingredients = new HashMap<>();
    }

    /**
     * Adds an ingredient to the recipe.
     * @param type - type of entity (constant that must be provided in DungeonConstants file)
     * @param amount - number of entites of specified type required to make recipe
     */
    public void add(String type, int amount) {
        ingredients.put(type, amount);
    }

    /**
     * Checks if an inventory can make the current instantiated recipe.
     * @return true if recipe can be made, false otherwise.
     */
    public boolean checkRecipeSatisfied(Inventory inventory) {
        for (Map.Entry<String,Integer> entry : ingredients.entrySet()) {
            if (inventory.countItemsByType(entry.getKey()) < entry.getValue()) return false;
        }
        return true;
    }

    /**
     * Uses an inventory to make a recipe. Removes items from the specified inventory.
     */
    public void make(Inventory inventory) {
        for (Map.Entry<String,Integer> entry : ingredients.entrySet()) {
            inventory.removeItemsByType(entry.getKey(), entry.getValue());
        }
    }
}
