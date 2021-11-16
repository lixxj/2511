package dungeonmania.entities.buildables;

import dungeonmania.entities.collectables.Weapon;
import dungeonmania.entities.moving.enemies.Enemy;
import dungeonmania.entities.moving.player.Player;
import dungeonmania.entities.DungeonConstants;
import dungeonmania.entities.EntityInfo;

/** 
 * a class representing a Bow
 */
public class Bow extends Weapon {

    /**
     * Constructor for a bow.
     * 
     * @param entityInfo - entityInfo object
     */
    public Bow(EntityInfo entityInfo) {
        super(entityInfo, DungeonConstants.BOW_DURABILITY, DungeonConstants.BOW_ATTACK_DAMAGE);
    }

    @Override
    public void attack(Player player, Enemy enemy) {
        for (int i = 0; i < 2; i++) {
            enemy.decreaseEnemyHealth(player, player.getAttackDamage() + this.getAttackDamage());
        }        
        reduceDurability();
    }


}
