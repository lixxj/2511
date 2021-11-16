package dungeonmania.entities.collectables.potions;

import dungeonmania.entities.EntityInfo;
import dungeonmania.entities.moving.player.Player;

/**
 * a class representing an InvisibilityPotion
 */
public class InvisibilityPotion extends Potion {

    /**
     * Class constructor.
     *
     * @param entityInfo - entityInfo object
     */
    public InvisibilityPotion(EntityInfo entityInfo) {
        super(entityInfo);
    }

    @Override
    public void consumePotion(Player player) {
        player.consumeInvisibilityPotion();
    }
}
