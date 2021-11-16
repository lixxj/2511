package dungeonmania.entities.collectables;

import dungeonmania.entities.EntityInfo;
import dungeonmania.entities.moving.enemies.*;
import dungeonmania.entities.moving.player.Player;

public class Anduril extends Sword {
    
    /**
     * Class constructor.
     *
     * @param entityInfo - entityInfo object
     */
    public Anduril(EntityInfo entityInfo) {
        super(entityInfo);
    }

    @Override
    public void attack(Player player, Enemy enemy) {
        if (enemy instanceof Assassin || enemy instanceof Hydra) {
            enemy.decreaseEnemyHealth(player, player.getAttackDamage() + this.getAttackDamage() * 3);
        } else {
            enemy.decreaseEnemyHealth(player, player.getAttackDamage() + this.getAttackDamage()); 
        }
        reduceDurability();
    }
}
