package dungeonmania;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.junit.jupiter.api.Test;

import dungeonmania.entities.*;
import dungeonmania.entities.buildables.Shield;
import dungeonmania.entities.collectables.*;
import dungeonmania.entities.moving.Battle;
import dungeonmania.entities.moving.enemies.*;
import dungeonmania.util.*;

import dungeonmania.entities.moving.player.Player;
import dungeonmania.dungeon.Dungeon;
import dungeonmania.dungeon.DungeonInfo;
import dungeonmania.goals.ExitGoal;

public class BattleTest {

    // Testing simple battle simulation and enemy death
    @Test
    public void testSimulateBattle() {

        String dungeonId = "dungeon1";
        String name = "maze";
        Dungeon dungeon = new Dungeon(new DungeonInfo(50, 50, dungeonId, name, DungeonConstants.MODE_STANDARD, new ExitGoal()));

        Player player = new Player(dungeon, new EntityInfo("batman", new Position(0, 0, 3), 
        DungeonConstants.ENTITY_PLAYER, false));
        ZombieToast zombie = new ZombieToast(dungeon, new EntityInfo("dead_robin", new Position(0, 0), 
        DungeonConstants.ENTITY_ZOMBIE_TOAST, false));
        
        dungeon.setPlayer(player);
        List<Entity> entities = new ArrayList<>();
        entities.add(zombie);
        
        while (Battle.canFight(player, zombie)) {
            Battle.fight(dungeon, player, zombie);
        }

        assertEquals(true, zombie.isAlive());
        assertEquals(true, player.getHealth() < 100);
        assertEquals(true, zombie.getHealth() < 100);
        assertEquals(false, player.isAlive());
    }

    @Test
    public void testSimulatePeacefulBattle() {

        String dungeonId = "dungeon1";
        String name = "maze";
        Dungeon dungeon = new Dungeon(new DungeonInfo(50, 50, dungeonId, name, DungeonConstants.MODE_PEACEFUL, new ExitGoal()));

        Player player = new Player(dungeon, new EntityInfo("batman", new Position(0, 0, 3), 
        DungeonConstants.ENTITY_PLAYER, false));
        ZombieToast zombie = new ZombieToast(dungeon, new EntityInfo("dead_robin", new Position(0, 0), 
        DungeonConstants.ENTITY_ZOMBIE_TOAST, false));
        
        while (Battle.canFight(player, zombie)) {
            Battle.fight(dungeon, player, zombie);
        }

        assertEquals(false, zombie.isAlive());
        assertEquals(true, player.getHealth() == 100); // player health does not change

    }

    // Testing simple battle with armour
    @Test
    public void testBattleWithArmour() {

        String dungeonId = "dungeon1";
        String name = "maze";
        Dungeon dungeon = new Dungeon(new DungeonInfo(50, 50, dungeonId, name, DungeonConstants.MODE_STANDARD, new ExitGoal()));

        Player player = new Player(dungeon, new EntityInfo("batman", new Position(0, 0, 3), 
                                     DungeonConstants.ENTITY_PLAYER, false));
        dungeon.setPlayer(player);

        Mercenary mercenary = new Mercenary(dungeon, new EntityInfo("deadpool", 
                                new Position(0, 3), DungeonConstants.ENTITY_MERCENARY, false));
        
        Armour armour = new Armour(new EntityInfo("armour", new Position(0, 1), 
                            DungeonConstants.ENTITY_ARMOUR, true));
        
        List<Entity> entities = new ArrayList<>();
        entities.add(mercenary);
        entities.add(armour);

        dungeon.setEntities(entities);
        dungeon.addEnemy(mercenary);

        // Moves and collects armour
        player.move(Direction.DOWN);
        assertEquals(new Position(0, 1), player.getPosition());

        // Player collects armour and uses it
        assertTrue(player.checkItemExists("armour"));

        // Removed from dungeon
        assertFalse(dungeon.getEntities().stream().anyMatch(x->Objects.equals(x.getType(), "armour")));
        
        // Player has armour
        assertTrue(player.hasProtection());    

        dungeon.getEnemies().forEach(Enemy::move);
        dungeon.getEnemies().forEach(Enemy::move);
        assertEquals(new Position(0, 1), mercenary.getPosition());

        while (Battle.canFight(player, mercenary)) {
            Battle.fight(dungeon, player, mercenary);
        }

        assertEquals(false, mercenary.isAlive());
    }

    // Testing simple battle with shield
    @Test
    public void testBattleWithShield() {

        String dungeonId = "dungeon1";
        String name = "maze";
        Dungeon dungeon = new Dungeon(new DungeonInfo(50, 50, dungeonId, name, DungeonConstants.MODE_STANDARD, new ExitGoal()));
        Player player = new Player(dungeon, new EntityInfo("batman", new Position(0, 0, 3), 
        DungeonConstants.ENTITY_PLAYER, false));
        dungeon.setPlayer(player);

        Mercenary mercenary = new Mercenary(dungeon, new EntityInfo("deadpool", 
                new Position(0, 3), DungeonConstants.ENTITY_MERCENARY, false));
      
        List<Entity> entities = new ArrayList<>();
        entities.add(mercenary);

        dungeon.setEntities(entities);
        dungeon.addEnemy(mercenary);

        // Moves
        dungeon.tick(null, Direction.DOWN);
        assertEquals(new Position(0, 1), player.getPosition());
        assertEquals(new Position(0, 2), mercenary.getPosition());

        // Add ingredients of shield to player's inventory (recipe 1)
        player.addItemToInventory(new Wood(new EntityInfo("wood0", new Position(1, 2), DungeonConstants.ENTITY_WOOD, true)));
        player.addItemToInventory(new Wood(new EntityInfo("wood1", new Position(1, 2), DungeonConstants.ENTITY_WOOD, true)));
        player.addItemToInventory(new Treasure(new EntityInfo("treasure0", new Position(1, 2), DungeonConstants.ENTITY_TREASURE, true)));

        Entity shield = dungeon.build("shield");
        assertEquals(true, shield instanceof Shield);

        
        // Player has shield
        assertTrue(player.hasProtection());    

        dungeon.getEnemies().forEach(Enemy::move);
        assertEquals(new Position(0, 1), mercenary.getPosition());

        while (Battle.canFight(player, mercenary)) {
            Battle.fight(dungeon, player, mercenary);
        }

        // player survives because of shield
        assertEquals(true, player.isAlive());
    }

    // Testing simple battle with armoured zombie
    @Test
    public void testBattleArmouredZombie() {
        String dungeonId = "dungeon1";
        String name = "maze";
        Dungeon dungeon = new Dungeon(new DungeonInfo(50, 50, dungeonId, name, DungeonConstants.MODE_STANDARD, new ExitGoal()));

        Player player = new Player(dungeon, new EntityInfo("batman", new Position(0, 0, 3), 
        DungeonConstants.ENTITY_PLAYER, false));
        Weapon weapon = new Sword(new EntityInfo("sharp_sword", null, DungeonConstants.ENTITY_SWORD, true));
        player.addItemToInventory(weapon);
        player.setWeapon(weapon);
        Armour armour = new Armour(new EntityInfo("bat_suit", null, DungeonConstants.ENTITY_ARMOUR, true));
        player.addItemToInventory(armour);
        player.setProtection(armour);

        dungeon.setPlayer(player);
        ZombieToast zombie = new ZombieToast(dungeon, new EntityInfo("dead_joker", new Position(0, 0), 
        DungeonConstants.ENTITY_ZOMBIE_TOAST, false));
        zombie.setProtection(new Armour(new EntityInfo("thorny_suit", null, DungeonConstants.ENTITY_ARMOUR, true)));
        
        dungeon.addEntity(zombie);
        dungeon.addEnemy(zombie);

        while (Battle.canFight(player, zombie)) {
            Battle.fight(dungeon, player, zombie);
        }

        assertFalse(zombie.isAlive());
        assertTrue(player.getHealth() < 100);
        assertTrue(player.isAlive());
    }

    // Testing simple battle with armoured zombie
    @Test
    public void testBattleArmouredMercenary() {
        String dungeonId = "dungeon1";
        String name = "maze";
        Dungeon dungeon = new Dungeon(new DungeonInfo(50, 50, dungeonId, name, DungeonConstants.MODE_STANDARD, new ExitGoal()));

        Player player = new Player(dungeon, new EntityInfo("batman", new Position(0, 0, 3), 
        DungeonConstants.ENTITY_PLAYER, false));
        Weapon weapon = new Sword(new EntityInfo("sharp_sword", null, DungeonConstants.ENTITY_SWORD, true));
        player.addItemToInventory(weapon);
        player.setWeapon(weapon);
        Armour armour = new Armour(new EntityInfo("bat_suit", null, DungeonConstants.ENTITY_ARMOUR, true));
        player.addItemToInventory(armour);
        player.setProtection(armour);

        dungeon.setPlayer(player);
        Mercenary mercenary = new Mercenary(dungeon, new EntityInfo("cat_woman", new Position(0, 0), 
        DungeonConstants.ENTITY_MERCENARY, false));
        mercenary.setProtection(new Armour(new EntityInfo("power_suit", null, DungeonConstants.ENTITY_ARMOUR, true)));
        
        dungeon.addEntity(mercenary);
        dungeon.addEnemy(mercenary);

        while (Battle.canFight(player, mercenary)) {
            Battle.fight(dungeon, player, mercenary);
        }

        assertFalse(mercenary.isAlive());
        assertTrue(player.getHealth() < 100);
        assertTrue(player.isAlive());
    }
}
