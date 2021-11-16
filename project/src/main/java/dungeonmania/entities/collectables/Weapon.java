package dungeonmania.entities.collectables;

import dungeonmania.entities.*;
import dungeonmania.entities.moving.player.Player;
import dungeonmania.entities.moving.enemies.Enemy;

/**
 * an abstract class representing an Weapon
 */
public abstract class Weapon extends Entity {
    
    private int currentDurability;
    private int attackDamage;

    /**
     * Class constructor.
     *
     * @param entityInfo - entityInfo object
     */
    public Weapon(EntityInfo entityInfo, int durability, int attackDamage) {
        super(entityInfo);
        this.currentDurability = durability;
        this.attackDamage = attackDamage;
    }

    /**
     * Durability, in a weapon, is a unique number which specifies the number of times it can be used.
     * @return durability of the weapon (int)
     */
    public int getDurability() {
        return this.currentDurability;
    }

    /**
     * Reduces the durability of the weapon by one point. Every time a weapon is used, this method must
     * be called to reflect deterioration of the weapon.
     */
    public void reduceDurability() {
        this.currentDurability -= 1;
    }

    /**
     * Gets the amount of extra attack damage this weapon offers.
     * @return attack damage
     */
    public int getAttackDamage() {
        return attackDamage;
    }

    /**
     * Simulates an attack from the player on the enemy.
     * @param player
     * @param enemy
     */
    public abstract void attack(Player player, Enemy enemy);

}
