package dungeonmania.entities.moving;

import java.util.Objects;
import java.util.UUID;

import dungeonmania.entities.*;
import dungeonmania.entities.collectables.*;
import dungeonmania.entities.moving.enemies.*;
import dungeonmania.entities.moving.player.Player;
import dungeonmania.dungeon.Dungeon;

public class Battle {

    /**
     * Starts a battle between the specified player and enemy. Once the battle is over,
     * either the player or the enemy is dead.
     * 
     * @pre assumes that the provided player and enemy can fight
     * @param player
     * @param enemy
     */
    public static void fight(Dungeon dungeon, Player player, Enemy enemy) {
        
        // Enemy is attacked first
        if (player.isInvincible()) enemy.setHealth(0);
        else player.attackEnemy(enemy);        

        // Player is attacked next (if in standard or hard mode)
        if (!player.isInvincible() && canFight(player, enemy)
            && !Objects.equals(dungeon.getMode(), DungeonConstants.MODE_PEACEFUL)) {
            enemy.attackPlayer();
        }

        // Can no longer keep fighting
        if (!canFight(player, enemy)) battleEnded(player, enemy);
        
    }

    private static void battleEnded(Player player, Enemy enemy) {
        if (!player.isAlive()) {
            if (player.hasItemOfType(DungeonConstants.ENTITY_ONE_RING)) player.useOneRing();
        }

        if (!enemy.isAlive()) {
            if(enemy instanceof Mercenary){
                if(((Mercenary)enemy).hasProtection()){
                    player.setProtection(new Armour(new EntityInfo(generateRandomId()+ "-" +"armour", null,
                    DungeonConstants.ENTITY_ARMOUR, true)));
                }
            } 
            if(enemy instanceof ZombieToast){
                if(((ZombieToast)enemy).hasProtection()){
                    player.setProtection(new Armour(new EntityInfo(generateRandomId()+ "-" +"armour", null,
                    DungeonConstants.ENTITY_ARMOUR, true)));
                } 
            }
            if (!player.hasItemOfType(DungeonConstants.ENTITY_ONE_RING) && rewardOneRing()) { 
                String id = generateRandomId() + "-" + "one_ring";
                player.addItemToInventory(new OneRing(new EntityInfo(id, player.getPosition(), DungeonConstants.ENTITY_ONE_RING, true)));
            }
        }        
    }

    // Generate random Id
    private static String generateRandomId() {
        return UUID.randomUUID().toString();
    }
    
    /**
     * Rewards the One Ring to the player in every 1 in 10 battles.
     * @return true if one ring is rewarded, false otherwise.
     */
    private static boolean rewardOneRing() {
        return Math.random() * 10 < 1; 
    }

    /**
     * Checks if a battle between the player and specified enemy can occur. False if player
     * or enemy are dead. False if enemy is a mercenary, who is also an ally. False if player
     * and enemy are in different positions. False if player is invisible.
     * 
     * @pre player and enemy are assumed to be in the same dungeon
     * @param player
     * @param enemy
     * @return true if the battle can occur, false otherwise.
     */
    public static boolean canFight(Player player, Enemy enemy) {
        if (!Objects.equals(enemy.getPosition(), player.getPosition())) return false;
        if (enemy instanceof Mercenary && ((Mercenary)enemy).isAlly()) return false;
        if (player.isInvisible()) return false;
        return player.isAlive() && enemy.isAlive();
    }

}
