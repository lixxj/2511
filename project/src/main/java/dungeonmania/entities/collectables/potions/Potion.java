package dungeonmania.entities.collectables.potions;

import dungeonmania.entities.Entity;
import dungeonmania.entities.EntityInfo;
import dungeonmania.entities.moving.player.Player;

public abstract class Potion extends Entity {

    /**
     * Class constructor.
     *
     * @param entityInfo - entityInfo object
     */
    public Potion(EntityInfo entityInfo){
        super(entityInfo);
    }
    
    /**
     * a method that allows a player to consume a potion
     * @param player
     */
    public abstract void consumePotion(Player player);
}
