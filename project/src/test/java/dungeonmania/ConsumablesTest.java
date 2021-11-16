package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import dungeonmania.entities.*;
import dungeonmania.entities.collectables.potions.*;
import dungeonmania.entities.moving.Battle;
import dungeonmania.entities.moving.enemies.ZombieToast;
import dungeonmania.entities.moving.player.Player;
import dungeonmania.dungeon.Dungeon;
import dungeonmania.dungeon.DungeonInfo;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.goals.ExitGoal;
import org.junit.jupiter.api.Test;

import dungeonmania.util.Position;

public class ConsumablesTest {

    // Testing health potion consumption
    @Test
    public void testHealthPotion() {
        String dungeonId = "dungeon1";
        String name = "maze";
        String playerId = "Batman";
        Dungeon dungeon = new Dungeon(new DungeonInfo(50, 50, dungeonId, name, DungeonConstants.MODE_STANDARD, new ExitGoal()));

        ZombieToast zombie = new ZombieToast(dungeon, new EntityInfo("dead_robin", new Position(0, 0),
                DungeonConstants.ENTITY_ZOMBIE_TOAST, false));
        Player myPlayer = new Player(dungeon, new EntityInfo(playerId, new Position(0, 0, 3),
                DungeonConstants.ENTITY_PLAYER, false));
        dungeon.setPlayer(myPlayer);

        //in peaceful mode, player spwans with 100 health
        assertEquals(100, myPlayer.getHealth());

        while (Battle.canFight(myPlayer, zombie)) {
            Battle.fight(dungeon, myPlayer, zombie);
        }

        //player should have lost health
        assertEquals(true, myPlayer.getHealth() < 100);

        HealthPotion newHealthPotion = new HealthPotion(new EntityInfo("HP1", new Position(0, 0),
                DungeonConstants.ENTITY_HEALTH_POTION, true));
        myPlayer.addItemToInventory(newHealthPotion);

        //use health potion, should restore health
        myPlayer.useItem("HP1");

        // Battle numero 2
        ZombieToast zombie2 = new ZombieToast(dungeon, new EntityInfo("dead_robin", new Position(0, 0),
                DungeonConstants.ENTITY_ZOMBIE_TOAST, false));
        while (Battle.canFight(myPlayer, zombie2)) {
            Battle.fight(dungeon, myPlayer, zombie2);
        }
        //player should have lost health
        assertEquals(true, myPlayer.getHealth() < 100);

        // cannot use health potion again since it was used once before (discarded from inventory)
        assertThrows(InvalidActionException.class ,() -> myPlayer.useItem("HP1"));


    }

    // Testing invisibility potion consumption
    @Test
    public void testInvisibiltyPotion(){
        String dungeonId = "dungeon1";
        String name = "maze";
        String playerId = "Batman";
        Dungeon dungeon = new Dungeon(new DungeonInfo(50, 50, dungeonId, name, DungeonConstants.MODE_STANDARD, new ExitGoal()));
        
        ZombieToast zombie = new ZombieToast(dungeon, new EntityInfo("dead_robin", new Position(0, 0), 
        DungeonConstants.ENTITY_ZOMBIE_TOAST, false));
        Player myPlayer = new Player(dungeon, new EntityInfo(playerId, new Position(0, 0, 3), 
        DungeonConstants.ENTITY_PLAYER, false));

        InvisibilityPotion invisibilityPotion = new InvisibilityPotion(new EntityInfo("IP1", new Position(0, 0), 
        DungeonConstants.ENTITY_INVISIBILITY_POTION, true));
        
        myPlayer.addItemToInventory(invisibilityPotion);

        //use invisibilty potion, player should be invisible now
        myPlayer.useItem("IP1");
        assertEquals(true, myPlayer.isInvisible());
        
        //player should be invisible, so nothing happens
        assertFalse(Battle.canFight(myPlayer, zombie));        
    }

    // Testing invincibility potion consumption
    @Test
    public void testInvincibilityPotion(){
        String dungeonId = "dungeon1";
        String name = "maze";
        String playerId = "Batman";
        Dungeon dungeon = new Dungeon(new DungeonInfo(50, 50, dungeonId, name, DungeonConstants.MODE_STANDARD, new ExitGoal()));
        
        ZombieToast zombie = new ZombieToast(dungeon, new EntityInfo("dead_robin", new Position(0, 0), 
                                DungeonConstants.ENTITY_ZOMBIE_TOAST, false));
        Player myPlayer = new Player(dungeon, new EntityInfo(playerId, new Position(0, 0, 3), 
        DungeonConstants.ENTITY_PLAYER, false));
         
        InvincibilityPotion invincibilityPotion = new InvincibilityPotion(new EntityInfo("IP1", new Position(0, 0), 
                            DungeonConstants.ENTITY_INVINCIBILITY_POTION, true));
        
        myPlayer.addItemToInventory(invincibilityPotion);

        //use invisibilty potion, player should be Invincible now
        myPlayer.useItem("IP1");
        assertEquals(true, myPlayer.isInvincible());

        while (Battle.canFight(myPlayer, zombie)) {
            Battle.fight(dungeon, myPlayer, zombie);
        }
        
        //player is invincible, so it kills enemy and health stays full
        assertEquals(100, myPlayer.getHealth());
        assertEquals(false, zombie.isAlive());
        assertEquals(true, myPlayer.isInvincible());
    }

}
