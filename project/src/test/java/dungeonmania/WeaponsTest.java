package dungeonmania;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import dungeonmania.entities.*;
import dungeonmania.entities.collectables.*;
import dungeonmania.entities.collectables.potions.*;
import dungeonmania.entities.moving.Battle;
import dungeonmania.entities.moving.enemies.Hydra;
import dungeonmania.entities.moving.enemies.Mercenary;
import dungeonmania.entities.moving.enemies.ZombieToast;
import dungeonmania.entities.moving.player.*;
import dungeonmania.entities.staticitems.ZombieToastSpawner;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.dungeon.*;
import dungeonmania.goals.ExitGoal;

import dungeonmania.util.Direction;
import dungeonmania.util.Position;

import org.junit.jupiter.api.Test;

public class WeaponsTest {
    @Test
    public void testUsingSword(){

        String dungeonId = "dungeon1";
        String name = "maze";
        Dungeon dungeon = new Dungeon(new DungeonInfo(50, 50, dungeonId, name, DungeonConstants.MODE_STANDARD, new ExitGoal()));

        Player player = new Player(dungeon, new EntityInfo("batman", new Position(0, 0, 3), 
        DungeonConstants.ENTITY_PLAYER, false));
        ZombieToast zombie = new ZombieToast(dungeon, new EntityInfo("dead_robin", new Position(0, 2), 
        DungeonConstants.ENTITY_ZOMBIE_TOAST, false));
        Sword sword = new Sword(new EntityInfo("s1", new Position(0, 1), DungeonConstants.ENTITY_SWORD, true));


        dungeon.setPlayer(player);
        dungeon.addEntity(zombie);
        dungeon.addEntity(sword);

        dungeon.addEnemy(zombie);
        
        player.move(Direction.DOWN);
        assertEquals(new Position(0, 1), player.getPosition());
        assertEquals(true, player.hasWeapon());
        
        player.move(Direction.DOWN);
        assertEquals(new Position(0, 2), player.getPosition());

        while (Battle.canFight(player, zombie)) {
            Battle.fight(dungeon, player, zombie);
        }
        
        assertEquals(false, zombie.isAlive());
        assertEquals(true, player.getHealth() < 100);
        assertEquals(6, sword.getDurability());
    }

    
    @Test
    public void testBowDamage(){
        String dungeonId = "dungeon1";
        String name = "maze";
        String playerId = "Batman";

        Dungeon dungeon = new Dungeon(new DungeonInfo(50, 50, dungeonId, name, DungeonConstants.MODE_STANDARD, new ExitGoal()));
        Player player = new Player(dungeon, new EntityInfo(playerId, new Position(1, 2), 
        DungeonConstants.ENTITY_PLAYER, false));
        ZombieToast zombie = new ZombieToast(dungeon, new EntityInfo("dead_robin", new Position(1, 2), 
        DungeonConstants.ENTITY_ZOMBIE_TOAST, false));

        dungeon.setPlayer(player);

        //adding 1 wood, 1 key and 1 sunstone to inventory
        player.addItemToInventory(new Wood(new EntityInfo("wood1",
        new Position(1, 2), DungeonConstants.ENTITY_WOOD, true)));

        player.addItemToInventory(new Arrow(new EntityInfo("arrow1",
        new Position(1, 2), DungeonConstants.ENTITY_ARROW, true)));
        player.addItemToInventory(new Arrow(new EntityInfo("arrow2",
        new Position(1, 2), DungeonConstants.ENTITY_ARROW, true)));
        player.addItemToInventory(new Arrow(new EntityInfo("arrow3",
        new Position(1, 2), DungeonConstants.ENTITY_ARROW, true)));

        dungeon.build(DungeonConstants.ENTITY_BOW);



        List<Entity> entities = new ArrayList<>();
        entities.add(zombie);
        
        dungeon.setEntities(entities);
        dungeon.addEnemy(zombie);
    
        while (Battle.canFight(player, zombie)) {
            Battle.fight(dungeon, player, zombie);
        }
        
        assertEquals(false, zombie.isAlive());
        assertEquals(100, player.getHealth());
    }
    
    @Test
    public void testWinArmour(){
        String dungeonId = "dungeon1";
        String name = "maze";
        String playerId = "Batman";
        Dungeon dungeon = new Dungeon(new DungeonInfo(50, 50, dungeonId, name, DungeonConstants.MODE_STANDARD, new ExitGoal()));
        Player player = new Player(dungeon, new EntityInfo(playerId, new Position(1, 2), 
        DungeonConstants.ENTITY_PLAYER, false));
        dungeon.setPlayer(player);
        InvincibilityPotion invincibilityPotion = new InvincibilityPotion(new EntityInfo("strength", null, 
                                                DungeonConstants.ENTITY_INVINCIBILITY_POTION, true));
        player.addItemToInventory(invincibilityPotion);
        player.useItem("strength");
        ZombieToast zombie = new ZombieToast(dungeon, new EntityInfo("Dead Robin", 
                            new Position(1, 2), DungeonConstants.ENTITY_ZOMBIE_TOAST, false));
        Armour armour = new Armour(new EntityInfo("kevlar_armour", new Position(1, 2), 
                            DungeonConstants.ENTITY_SWORD, true));
        
        dungeon.addEnemy(zombie);
        dungeon.addEntity(armour);
        zombie.setProtection(armour);

        while (Battle.canFight(player, zombie)) {
            Battle.fight(dungeon, player, zombie);
        }

        assertFalse(zombie.isAlive());
        assertTrue(player.hasProtection());
    }

    @Test
    public void testDestroyZombieToastSpawner(){
        String dungeonId = "dungeon1";
        String name = "maze";
        String playerId = "Batman";
        Dungeon dungeon = new Dungeon(new DungeonInfo(50, 50, dungeonId, name, DungeonConstants.MODE_STANDARD, new ExitGoal()));
        Player player = new Player(dungeon, new EntityInfo(playerId, new Position(0, 0), 
        DungeonConstants.ENTITY_PLAYER, false));
        dungeon.setPlayer(player);
        ZombieToastSpawner zombieToastSpawner = new ZombieToastSpawner(new EntityInfo("toaster", new Position(2, 0), 
                                                DungeonConstants.ENTITY_ZOMBIE_TOAST_SPAWNER, false));
        Sword sword = new Sword(new EntityInfo("sword", new Position(2, -1), DungeonConstants.ENTITY_SWORD, true));

        List<Entity> entities = new ArrayList<>();
        entities.add(zombieToastSpawner);
        entities.add(sword);

        dungeon.setEntities(entities);
        
        // Not cardinally adjacent to toaster
        assertThrows(InvalidActionException.class, () -> {
            dungeon.interact("toaster");
        });

        dungeon.tick(null, Direction.RIGHT);

        // Cannot destroy toaster since no weapon is equipped
        assertThrows(InvalidActionException.class, () -> {
            dungeon.interact("toaster");
        });

        dungeon.tick(null, Direction.UP);
        dungeon.tick(null, Direction.RIGHT); // Pick Up Sword
        // Player consumes the potion
        assertTrue(player.checkItemExists("sword"));
        

        // Removed from dungeon
        assertFalse(dungeon.getEntities().stream().anyMatch(x->Objects.equals(x.getType(), "sword")));

        // Player has a weapon (sword)
        assertTrue(player.hasWeapon());

        // Destroy Zombie Toast Spawner 
        dungeon.interact("toaster");
        
        // Removed from dungeon
        assertFalse(dungeon.getEntities().stream().anyMatch(x->Objects.equals(x.getType(), "zombie_toast_spawner")));
    }


    @Test
    public void testUsingAnduril(){
        String dungeonId = "dungeon1";
        String name = "maze";

        Dungeon dungeon = new Dungeon(new DungeonInfo(50, 50, dungeonId, name, DungeonConstants.MODE_STANDARD, new ExitGoal()));

        Player player = new Player(dungeon, new EntityInfo("batman", new Position(0, 0, 3), 
        DungeonConstants.ENTITY_PLAYER, false));
        Hydra hydra = new Hydra(dungeon, new EntityInfo("lernaean_hydra", new Position(0, 2), 
        DungeonConstants.ENTITY_HYDRA, false));
        Sword sword = new Anduril(new EntityInfo("s1", new Position(0, 1), DungeonConstants.ENTITY_ANDURIL, true));

        dungeon.setPlayer(player);
        dungeon.addEntity(hydra);
        dungeon.addEntity(sword);

        dungeon.addEnemy(hydra);
        
        player.move(Direction.DOWN);
        assertEquals(new Position(0, 1), player.getPosition());
        assertEquals(true, player.hasWeapon());
        
        player.move(Direction.DOWN);
        assertEquals(new Position(0, 2), player.getPosition());

        while (Battle.canFight(player, hydra)) {
            Battle.fight(dungeon, player, hydra);
        }
        
        assertEquals(false, hydra.isAlive());
        assertEquals(false, player.getHealth() < 100);
        assertEquals(7, sword.getDurability());
    }

    @Test
    public void testAndurilZombie() {
        String dungeonId = "dungeon1";
        String name = "maze";
        Dungeon dungeon = new Dungeon(new DungeonInfo(50, 50, dungeonId, name, DungeonConstants.MODE_STANDARD, new ExitGoal()));

        Player player = new Player(dungeon, new EntityInfo("batman", new Position(0, 0, 3), 
        DungeonConstants.ENTITY_PLAYER, false));
        ZombieToast zombie = new ZombieToast(dungeon, new EntityInfo("dead_robin", new Position(0, 2), 
        DungeonConstants.ENTITY_ZOMBIE_TOAST, false));
        Sword sword = new Anduril(new EntityInfo("s1", new Position(0, 1), DungeonConstants.ENTITY_ANDURIL, true));

        dungeon.setPlayer(player);
        dungeon.addEntity(zombie);
        dungeon.addEntity(sword);
        
        dungeon.addEnemy(zombie);
        
        player.move(Direction.DOWN);
        assertEquals(new Position(0, 1), player.getPosition());
        assertEquals(true, player.hasWeapon());
        
        player.move(Direction.DOWN);
        assertEquals(new Position(0, 2), player.getPosition());

        while (Battle.canFight(player, zombie)) {
            Battle.fight(dungeon, player, zombie);
        }
        
        assertFalse(zombie.isAlive());
        // Player is damaged a bit since anduril does not offer triple damage for normal enemies
        assertTrue(player.getHealth() < 100);

    }

    @Test
    public void testBuildingSceptre(){
        String dungeonId = "dungeon1";
        String name = "maze";
        String playerId = "Batman";
        Dungeon dungeon = new Dungeon(new DungeonInfo(50, 50, dungeonId, name, DungeonConstants.MODE_STANDARD, new ExitGoal()));
        Player player = new Player(dungeon, new EntityInfo(playerId, new Position(1, 2), 
        DungeonConstants.ENTITY_PLAYER, false));

        dungeon.setPlayer(player);

        //adding 1 wood, 1 key and 1 sunstone to inventory
        player.addItemToInventory(new Wood(new EntityInfo("wood1",
        new Position(1, 2), DungeonConstants.ENTITY_WOOD, true)));
        player.addItemToInventory(new Key(new EntityInfo("key1",
        new Position(1, 2), DungeonConstants.ENTITY_KEY, true), 0));
        player.addItemToInventory(new SunStone(new EntityInfo("stone1",
        new Position(1, 2), DungeonConstants.ENTITY_SUN_STONE, true)));

        dungeon.build(DungeonConstants.ENTITY_SCEPTRE);

        //all the items should be used to build one sceptre
        assertEquals(1,player.getInventoryItems().size());
        player.getInventoryItems().clear();

        
        player.addItemToInventory(new Arrow(new EntityInfo("arrow1",
        new Position(1, 2), DungeonConstants.ENTITY_ARROW, true)));
        player.addItemToInventory(new Arrow(new EntityInfo("arrow2",
        new Position(1, 2), DungeonConstants.ENTITY_ARROW, true)));
        player.addItemToInventory(new Key(new EntityInfo("key1",
        new Position(1, 2), DungeonConstants.ENTITY_KEY, true), 0));
        player.addItemToInventory(new SunStone(new EntityInfo("stone1",
        new Position(1, 2), DungeonConstants.ENTITY_SUN_STONE, true)));
        
        dungeon.build(DungeonConstants.ENTITY_SCEPTRE);
        
        //all the items should be used to build one sceptre
        assertEquals(1,player.getInventoryItems().size());
        player.getInventoryItems().clear();

        player.addItemToInventory(new Arrow(new EntityInfo("arrow1",
        new Position(1, 2), DungeonConstants.ENTITY_ARROW, true)));
        player.addItemToInventory(new Arrow(new EntityInfo("arrow2",
        new Position(1, 2), DungeonConstants.ENTITY_ARROW, true)));
        player.addItemToInventory(new Key(new EntityInfo("key1",
        new Position(1, 2), DungeonConstants.ENTITY_KEY, true), 0));
        
        assertThrows(InvalidActionException.class, () -> {
            dungeon.build(DungeonConstants.ENTITY_SCEPTRE);
        });
        
    }

    @Test
    public void testMidnightArmourBattle(){
        String dungeonId = "dungeon1";
        String name = "maze";
        String playerId = "Batman";
        Dungeon dungeon = new Dungeon(new DungeonInfo(50, 50, dungeonId, name, DungeonConstants.MODE_STANDARD, new ExitGoal()));
        Player player = new Player(dungeon, new EntityInfo(playerId, new Position(0, 3), 
        DungeonConstants.ENTITY_PLAYER, false));

        dungeon.setPlayer(player);

        //adding 1 wood, 1 key and one sunstone to inventory
        player.addItemToInventory(new Armour(new EntityInfo("armour", null,
        DungeonConstants.ENTITY_ARMOUR, true)));
        player.addItemToInventory(new SunStone(new EntityInfo("stone", null,
        DungeonConstants.ENTITY_SUN_STONE, true)));

        
        dungeon.build(DungeonConstants.ENTITY_MIDNIGHT_ARMOUR);
        
        //checking if player midnight armour is built (armour + SunStone)
        assertEquals(true, player.hasProtection());

        Mercenary mercenary = new Mercenary(dungeon, new EntityInfo("merc", new Position(0, 3), DungeonConstants.ENTITY_MERCENARY, false));
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
    
    @Test
    public void testCannotBuildMidnightArmour(){
        String dungeonId = "dungeon1";
        String name = "maze";
        String playerId = "Batman";
        Dungeon dungeon = new Dungeon(new DungeonInfo(50, 50, dungeonId, name, DungeonConstants.MODE_STANDARD, new ExitGoal()));
        Player player = new Player(dungeon, new EntityInfo(playerId, new Position(0, 3), 
        DungeonConstants.ENTITY_PLAYER, false));
        ZombieToast zombieToast = new ZombieToast(dungeon, new EntityInfo("zombie",
        new Position(1, 4), DungeonConstants.ENTITY_ZOMBIE_TOAST, false));

        dungeon.setPlayer(player);
        dungeon.addEntity(player);
        dungeon.addEntity(zombieToast);
        
        //adding 1 wood, 1 key and one sunstone to inventory
        player.addItemToInventory(new Armour(new EntityInfo("armour", new Position(1, 2),
        DungeonConstants.ENTITY_ARMOUR, true)));
        player.addItemToInventory(new SunStone(new EntityInfo("stone",new Position(1, 2),
        DungeonConstants.ENTITY_SUN_STONE, true)));

        
        dungeon.build(DungeonConstants.ENTITY_MIDNIGHT_ARMOUR);
        
        //checking if player midnight armour is built (armour + SunStone)
        // should not be built and inventory should stay of size 2 since there is zombies
        assertEquals(false, player.hasProtection());


    }

    @Test
    public void testSwordAndArmourDurability() {
        String dungeonId = "dungeon1";
        String name = "maze";
        String playerId = "Batman";
        Dungeon dungeon = new Dungeon(new DungeonInfo(50, 50, dungeonId, name, DungeonConstants.MODE_STANDARD, new ExitGoal()));
        Player player = new Player(dungeon, new EntityInfo(playerId, new Position(1, 2), 
        DungeonConstants.ENTITY_PLAYER, false));
        dungeon.setPlayer(player);

        // Give the player a sword and armour
        Sword sword = new Sword(new EntityInfo("king_slayer", null, DungeonConstants.ENTITY_SWORD, true));
        player.addItemToInventory(sword);
        player.setWeapon(sword);
        Armour armour = new Armour(new EntityInfo("bat_suit", null, DungeonConstants.ENTITY_ARMOUR, true));
        player.addItemToInventory(armour);
        player.setProtection(armour);

        // Simulate 20 battles and exit if both weapon and armour are lost
        for (int i = 0; i < 20 && (player.hasWeapon() || player.hasProtection()); i++) {
            player.setHealth(100);
            ZombieToast zombie = new ZombieToast(dungeon, new EntityInfo("Dead Robin", 
            new Position(1, 2), DungeonConstants.ENTITY_ZOMBIE_TOAST, false));
            dungeon.addEnemy(zombie);
            while (Battle.canFight(player, zombie)) {
                Battle.fight(dungeon, player, zombie);
            }
            assertFalse(zombie.isAlive());
        }
        
        // Sword durability has lapsed so weapon is lost
        assertFalse(player.hasWeapon());

        // Armour durability has also lapsed so armour is lost
        assertFalse(player.hasProtection());
        
    }
}
