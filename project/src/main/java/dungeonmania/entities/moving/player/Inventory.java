package dungeonmania.entities.moving.player;

import dungeonmania.entities.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Inventory {
    
    private List<Entity> items;

    /**
     * Constructor for Inventory
     */
    public Inventory() {
        items = new ArrayList<>();
    }

    /**
     * Adds an entity to the instantiated inventory
     * 
     * @param item - entity that is to be added
     */
    public void add(Entity item) {
        items.add(item);
    }

    /**
     * Gets all items in inventory
     */
    public List<Entity> getItems() {
        return items;
    }

    /**
     * If item exists in current inventory, removes it from inventory.
     * 
     * @param itemId - unique entity id of item that must be removed
     */
    public void removeItemWithId(String itemId) {
        if (!checkItemExists(itemId)) return;
        Entity entity = items.stream().filter(x -> Objects.equals(x.getId(), itemId)).findFirst().get();
        items.remove(entity);
    }

    /**
     * Gets an item from the current inventory instance
     * 
     * @param itemId - unique entity id of item that must be removed
     * @return item as an Entity object
     */
    public Entity getItem(String itemId) {
        if (!checkItemExists(itemId)) return null;
        Entity item = items.stream().filter(x -> Objects.equals(x.getId(), itemId)).findFirst().get();
        return item;
    }

    /**
     * Gets a random item the inventory that have the matching type.
     * 
     * @param type - type of entity (constant that must be provided in DungeonConstants file)
     * @return item of the specified type
     */
    public Entity getItemByType(String type){
        if (countItemsByType(type) == 0) return null;
        return items.stream().filter(x -> Objects.equals(x.getType(), type)).findFirst().get();
    }

    /**
     * Checks if an item exists in the current inventory
     * 
     * @param itemId - unique entity id of item that must be removed
     * @return
     */
    public boolean checkItemExists(String itemId) {
        return items.stream().anyMatch(x -> Objects.equals(x.getId(), itemId));
    }

    /**
     * Counts the number of items that are of the specified type in the current inventory
     * 
     * @param type - type of entity (constant that must be provided in DungeonConstants file)
     * @return number of items that match specified type
     */
    public int countItemsByType(String type) {
        return items.stream().filter(x -> Objects.equals(x.getType(), type)).collect(Collectors.toList()).size();
    }

    /**
     * Removes items that match the specified type from the current inventory
     * 
     * @param type - type of entity (constant that must be provided in DungeonConstants file)
     * @param number - how many items of this type must be removed from the inventory
     */
    public void removeItemsByType(String type, int number) {
        while (number > 0) {
            removeItemsByType(type);
            number--;
        }
    }

    /**
     * Removes items that match the specified type from the current inventory. 
     * If no item of specified type exists, then nothing happens.
     * 
     * @param type - type of entity (constant that must be provided in DungeonConstants file)
     */
    private void removeItemsByType(String type) {
        if (countItemsByType(type) == 0) return; 
        Entity entity = items.stream().filter(x -> Objects.equals(x.getType(), type)).findFirst().get();
        items.remove(entity);
    }

}
