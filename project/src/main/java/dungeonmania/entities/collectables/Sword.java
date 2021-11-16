package dungeonmania.entities.collectables;

import dungeonmania.entities.DungeonConstants;
import dungeonmania.entities.EntityInfo;
import dungeonmania.entities.moving.enemies.Enemy;
import dungeonmania.entities.moving.player.Player;

/**
 * a class representing an Sword
 */
public class Sword extends Weapon {
    /**
     * Class constructor.
     *
     * @param entityInfo - entityInfo object
     */
    public Sword(EntityInfo entityInfo) {
        super(entityInfo, DungeonConstants.SWORD_DURABILITY, DungeonConstants.SWORD_ATTACK_DAMAGE);
    }

    @Override
    public void attack(Player player, Enemy enemy) {
        enemy.decreaseEnemyHealth(player, player.getAttackDamage() + this.getAttackDamage());
        reduceDurability();        
    }
}
