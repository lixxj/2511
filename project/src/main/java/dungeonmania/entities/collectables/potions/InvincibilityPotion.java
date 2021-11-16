package dungeonmania.entities.collectables.potions;

import dungeonmania.entities.DungeonConstants;
import dungeonmania.entities.EntityInfo;
import dungeonmania.entities.moving.player.Player;
import org.json.JSONObject;

/**
 * a class representing an InvincibilityPotion
 */
public class InvincibilityPotion extends Potion {

    /**
     * Class constructor.
     *
     * @param entityInfo - entityInfo object
     */
    public InvincibilityPotion(EntityInfo entityInfo) {
        super(entityInfo);
    }

    @Override
    public void consumePotion(Player player) {
        player.consumeInvincibilityPotion();
        player.setInvincibilityTime(DungeonConstants.INVINCIBILITY_TIME);
    }

}
