package dungeonmania.entities.buildables;

import dungeonmania.dungeon.Dungeon;
import dungeonmania.entities.*;
import dungeonmania.entities.collectables.Weapon;
import dungeonmania.entities.moving.enemies.*;
import dungeonmania.entities.moving.player.Player;


public class MidnightArmour extends Weapon implements Protection  {

    private int durability;
    
    /**
     * Class constructor.
     *
     */
    public MidnightArmour(EntityInfo entityinfo){
        super(entityinfo, DungeonConstants.MIDNIGHT_ARMOUR_ATTACK_DAMAGE, DungeonConstants.MIDNIGHT_ARMOUR_DURABILITY);
        this.durability = DungeonConstants.MIDNIGHT_ARMOUR_DURABILITY;
    }

    /**
     * Checks if a Midnight Armour can be added to the specified dungeon.
     * @param dungeon - dungeon in which midnight armour is added
     * @return true if can be added, false otherwise.
     */
    public static boolean canExist(Dungeon dungeon){
        return !dungeon.getEntities().stream().anyMatch(x -> x instanceof ZombieToast);
    }

    @Override
    public void decreaseDurability() {
        this.durability -= 1;
    }

    @Override
    public boolean hasProtection() {
        return getDurability() > 0;
    }

    @Override
    public double getProtectionFactor() {
        return DungeonConstants.MIDNIGHT_ARMOUR_PROTECTION_FACTOR;
    }

    @Override
    public void attack(Player player, Enemy enemy) {
        enemy.decreaseEnemyHealth(player, player.getAttackDamage() + this.getAttackDamage());
        reduceDurability();
    }
    
    @Override
    public int getDurability() {
        return this.durability;
    }
}
