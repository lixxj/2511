package dungeonmania.entities.collectables.potions;

import dungeonmania.entities.EntityInfo;
import dungeonmania.entities.moving.player.Player;
import org.json.JSONObject;

/**
 * a class representing an HealthPotion
 */
public class HealthPotion extends Potion  {

    private static boolean potionAllowed = true;
    
    /**
     * Class constructor.
     *
     * @param entityInfo - entityInfo object
     */
    public HealthPotion(EntityInfo entityInfo) {
        super(entityInfo);
    }

    @Override
    public void consumePotion(Player player) {
        if(potionAllowed){
            player.consumeHealthPotion();
            potionAllowed = false;
        }
        return;
    }

}

