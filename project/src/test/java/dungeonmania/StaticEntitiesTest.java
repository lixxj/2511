package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import dungeonmania.entities.*;
import dungeonmania.entities.collectables.*;
import dungeonmania.entities.moving.player.Player;
import dungeonmania.entities.staticitems.*;
import dungeonmania.dungeon.*;
import dungeonmania.goals.*;
import dungeonmania.response.models.*;
import dungeonmania.util.*;
import org.junit.jupiter.api.Test;


public class StaticEntitiesTest {
    
    /**
     * WALL - Unit Tests
     */
    @Test
    public void testPlayerIntoWall(){
        String dungeonId = "dungeon1";
        String name = "maze";
        String wallId = "W1";


        Position wallPosition = new Position(1, 1);

        Dungeon dungeon = new Dungeon(new DungeonInfo(50, 50, dungeonId, name, DungeonConstants.MODE_PEACEFUL, new ExitGoal()));
        Player P1 = new Player(dungeon, new EntityInfo("player", new Position(1, 2), 
        DungeonConstants.ENTITY_PLAYER, false));
        dungeon.setPlayer(P1);
        Wall wall = new Wall(new EntityInfo(wallId, wallPosition, 
        DungeonConstants.ENTITY_WALL, false));

        List<Entity> entities = new ArrayList<>();
        entities.add(wall);
        entities.add(P1);

        dungeon.setEntities(entities);

        Direction moveDirection = Direction.UP;
        P1.move(moveDirection);

        assertEquals(new Position(1, 2), P1.getPosition());
    }

    /**
     * BOULDER - Unit Tests
     */
    @Test
    public void testBoulderCanMove(){
        String dungeonId = "dungeon1";
        String name = "boulders";
        String boulderId = "B1";


        Position boulderPosition = new Position(1, 2);

        Dungeon dungeon = new Dungeon(new DungeonInfo(50, 50, dungeonId, name, DungeonConstants.MODE_PEACEFUL, new BoulderGoal()));
        Boulder boulder1 = new Boulder(new EntityInfo(boulderId, boulderPosition, 
        DungeonConstants.ENTITY_BOULDER, false));
        Player P1 = new Player(dungeon, new EntityInfo("player", new Position(1, 3), 
        DungeonConstants.ENTITY_PLAYER, false));
        dungeon.setPlayer(P1);
        
        List<Entity> entities = new ArrayList<>();
        entities.add(boulder1);
        entities.add(P1);

        dungeon.setEntities(entities);
        Direction moveDirection = Direction.UP;
        P1.move(moveDirection);

        assertEquals(new Position(1, 2), P1.getPosition());
        assertEquals(new Position(1, 1), boulder1.getPosition());
    }
    
    @Test
    public void testBoulderIntoBoulder(){
        String dungeonId = "dungeon1";
        String name = "boulders";
        String boulder1Id = "B1";
        String boulder2Id = "B2";


        Position boulder1Position = new Position(1, 3);
        Position boulder2Position = new Position(1, 2);

        Dungeon dungeon = new Dungeon(new DungeonInfo(50, 50, dungeonId, name, DungeonConstants.MODE_PEACEFUL, new BoulderGoal()));
        Boulder boulder1 = new Boulder(new EntityInfo(boulder1Id, boulder1Position, 
        DungeonConstants.ENTITY_BOULDER, false));
        Boulder boulder2 = new Boulder(new EntityInfo(boulder2Id, boulder2Position, 
        DungeonConstants.ENTITY_BOULDER, false));
        Player P1 = new Player(dungeon, new EntityInfo("player", new Position(1, 4), 
        DungeonConstants.ENTITY_PLAYER, false));
        dungeon.setPlayer(P1);

        List<Entity> entities = new ArrayList<>();
        entities.add(boulder1);
        entities.add(boulder2);
        entities.add(P1);

        dungeon.setEntities(entities);
        Direction moveDirection = Direction.UP;
        P1.move(moveDirection);

        assertEquals(new Position(1, 4), P1.getPosition());
        assertEquals(new Position(1, 3), boulder1.getPosition());
        assertEquals(new Position(1, 2), boulder2.getPosition());
    }
    
    @Test
    public void testBoulderOntoSwitch(){
        String dungeonId = "dungeon1";
        String name = "boulders";
        String boulder1Id = "B1";
        String switchId = "S1";


        Position switchPosition = new Position(1, 2);
        Position boulder1Position = new Position(1, 3);

        Dungeon dungeon = new Dungeon(new DungeonInfo(50, 50, dungeonId, name, DungeonConstants.MODE_PEACEFUL, new BoulderGoal()));
        Boulder boulder1 = new Boulder(new EntityInfo(boulder1Id, boulder1Position, 
                            DungeonConstants.ENTITY_BOULDER, false));
        FloorSwitch switch1 = new FloorSwitch(new EntityInfo(switchId, switchPosition, 
                            DungeonConstants.ENTITY_FLOOR_SWITCH, false));
        Player P1 = new Player(dungeon, new EntityInfo("player", new Position(1, 4), 
        DungeonConstants.ENTITY_PLAYER, false));
        dungeon.setPlayer(P1);
        
        List<Entity> entities = new ArrayList<>();
        entities.add(boulder1);
        entities.add(switch1);
        entities.add(P1);

        dungeon.setEntities(entities);
        Direction moveDirection = Direction.UP;
        P1.move(moveDirection);

        assertEquals(new Position(1, 3), P1.getPosition());
        assertEquals(new Position(1, 2), boulder1.getPosition());
        //assertEquals(true, switch1.isSwitchOn());
    }
    
    /**
     * SWITCH - Unit Tests
     */
    @Test
    public void testSwitchOff(){
        String dungeonId = "dungeon1";
        String name = "boulders";
        String switchId = "S1";


        Position switchPosition = new Position(1, 2);

        Dungeon dungeon = new Dungeon(new DungeonInfo(50, 50, dungeonId, name, DungeonConstants.MODE_PEACEFUL, new BoulderGoal()));
        FloorSwitch switch1 = new FloorSwitch(new EntityInfo(switchId, switchPosition, DungeonConstants.ENTITY_FLOOR_SWITCH, false));
        
        List<Entity> entities = new ArrayList<>();
        entities.add(switch1);

        dungeon.setEntities(entities);

        assertEquals(false, switch1.isSwitchOn());
    }
    
    @Test
    public void testSwitchTriggeredOnOff(){
        String dungeonId = "dungeon1";
        String name = "boulders";
        String boulder1Id = "B1";
        String switchId = "S1";


        Position switchPosition = new Position(1, 2);
        Position boulder1Position = new Position(1, 3);

        Dungeon dungeon = new Dungeon(new DungeonInfo(50, 50, dungeonId, name, DungeonConstants.MODE_PEACEFUL, new BoulderGoal()));
        Boulder boulder1 = new Boulder(new EntityInfo(boulder1Id, boulder1Position, DungeonConstants.ENTITY_BOULDER, false));
        FloorSwitch switch1 = new FloorSwitch(new EntityInfo(switchId, switchPosition, DungeonConstants.ENTITY_FLOOR_SWITCH, false));
        Player P1 = new Player(dungeon, new EntityInfo("player", new Position(1, 4), 
        DungeonConstants.ENTITY_PLAYER, false));
        dungeon.setPlayer(P1);
        
        List<Entity> entities = new ArrayList<>();
        entities.add(boulder1);
        entities.add(switch1);
        entities.add(P1);

        dungeon.setEntities(entities);

        Direction moveDirection = Direction.UP;
        P1.move(moveDirection);

        assertEquals(new Position(1, 3), P1.getPosition());
        assertEquals(new Position(1, 2), boulder1.getPosition());
        assertEquals(true, switch1.isSwitchOn());

        moveDirection = Direction.UP;
        P1.move(moveDirection);

        assertEquals(new Position(1, 2), P1.getPosition());
        assertEquals(new Position(1, 1), boulder1.getPosition());
        assertEquals(false, switch1.isSwitchOn());
    }
    
    @Test
    public void testSwitchTriggeredFromPlayer(){
        String dungeonId = "dungeon1";
        String name = "boulders";
        String switchId = "S1";


        Position switchPosition = new Position(1, 2);

        Dungeon dungeon = new Dungeon(new DungeonInfo(50, 50, dungeonId, name, DungeonConstants.MODE_PEACEFUL, new BoulderGoal()));
        FloorSwitch switch1 = new FloorSwitch(new EntityInfo(switchId, switchPosition, DungeonConstants.ENTITY_FLOOR_SWITCH, false));
        Player P1 = new Player(dungeon, new EntityInfo("player", new Position(1, 3), 
        DungeonConstants.ENTITY_PLAYER, false));
        dungeon.setPlayer(P1);
        
        List<Entity> entities = new ArrayList<>();
        entities.add(switch1);
        entities.add(P1);

        dungeon.setEntities(entities);
        
        Direction moveDirection = Direction.UP;
        P1.move(moveDirection);

        assertEquals(new Position(1, 2), P1.getPosition());
        assertEquals(false, switch1.isSwitchOn());
    }

    /**
     * PORTAL - Unit Tests
     */
    @Test
    public void testPortal(){
        String dungeonId = "dungeon1";
        String name = "portals";
        
        Dungeon dungeon = new Dungeon(new DungeonInfo(50, 50, dungeonId, name, DungeonConstants.MODE_PEACEFUL, new ExitGoal()));
        Player P1 = new Player(dungeon, new EntityInfo("player", new Position(1, 3), 
        DungeonConstants.ENTITY_PLAYER, false));
        dungeon.setPlayer(P1);

        // Create two blue portals
        Portal portal1 = new Portal(new EntityInfo("portal1", new Position(1, 2), 
                        DungeonConstants.ENTITY_PORTAL, false), "BLUE");
        Portal portal2 = new Portal(new EntityInfo("portal2", new Position(3, 7), 
        DungeonConstants.ENTITY_PORTAL, false), "BLUE");
        
        List<Entity> entities = new ArrayList<>();
        entities.add(portal1);
        entities.add(portal2);
        entities.add(P1);

        dungeon.setEntities(entities);
        
        Direction moveDirection = Direction.UP;
        P1.move(moveDirection);

        assertEquals(new Position(3, 6), P1.getPosition());
    }

    /**
     * DOOR - Unit Tests
     */
    @Test
    public void testDoorStatusClosed(){
        String dungeonId = "dungeon1";
        String name = "maze";
        int doorKeyId = 1;
        
        Dungeon dungeon = new Dungeon(new DungeonInfo(50, 50, dungeonId, name, DungeonConstants.MODE_PEACEFUL, new ExitGoal()));
        Player P1 = new Player(dungeon, new EntityInfo("player", new Position(1, 3), 
        DungeonConstants.ENTITY_PLAYER, false));
        dungeon.setPlayer(P1);
        Door door1 = new Door(new EntityInfo("D1", new Position(1, 2), DungeonConstants.ENTITY_DOOR, false), doorKeyId);

        dungeon.setPlayer(P1);
        
        List<Entity> entities = new ArrayList<>();
        entities.add(door1);
        entities.add(P1);

        dungeon.setEntities(entities);
        
        Direction moveDirection = Direction.UP;
        P1.move(moveDirection);

        assertEquals(new Position(1, 3), P1.getPosition());
        assertEquals(true, door1.isDoorClosed());
    }
    
    @Test
    public void testDoorStatusWrongKey(){
        String dungeonId = "dungeon1";
        String name = "maze";
        int doorKeyId = 1;
        int keyKeyId = 2;


        Dungeon dungeon = new Dungeon(new DungeonInfo(50, 50, dungeonId, name, DungeonConstants.MODE_PEACEFUL, new ExitGoal()));
        Player P1 = new Player(dungeon, new EntityInfo("player", new Position(1, 3), 
        DungeonConstants.ENTITY_PLAYER, false));
        dungeon.setPlayer(P1);
        Door door1 = new Door(new EntityInfo("D1", new Position(1, 1), DungeonConstants.ENTITY_DOOR, false), doorKeyId);
        Key key1 = new Key(new EntityInfo("K1", new Position(1, 2), DungeonConstants.ENTITY_KEY, true), keyKeyId);

        dungeon.setPlayer(P1);
        
        List<Entity> entities = new ArrayList<>();
        entities.add(door1);
        entities.add(key1);
        entities.add(P1);

        dungeon.setEntities(entities);
        
        Direction moveDirection = Direction.UP;
        P1.move(moveDirection);

        P1.addItemToInventory(key1);

        assertEquals(new Position(1, 2), P1.getPosition());
        
        P1.move(moveDirection);

        assertEquals(new Position(1, 2), P1.getPosition());
        assertEquals(true, door1.isDoorClosed());
    }
    
    @Test
    public void testDoorStatusCorrectKey(){
        String dungeonId = "dungeon1";
        String name = "maze";
        int doorKeyId = 1;
        int keyKeyId = 1;


        Dungeon dungeon = new Dungeon(new DungeonInfo(50, 50, dungeonId, name, DungeonConstants.MODE_PEACEFUL, new ExitGoal()));
        Player P1 = new Player(dungeon, new EntityInfo("player", new Position(1, 3), 
        DungeonConstants.ENTITY_PLAYER, false));
        dungeon.setPlayer(P1);
        Door door1 = new Door(new EntityInfo("D1", new Position(1, 1), DungeonConstants.ENTITY_DOOR, false), doorKeyId);
        Key key1 = new Key(new EntityInfo("K1", new Position(1, 2), DungeonConstants.ENTITY_KEY, true), keyKeyId);
        
        List<Entity> entities = new ArrayList<>();
        entities.add(door1);
        entities.add(key1);
        entities.add(P1);

        dungeon.setEntities(entities);
        
        P1.move(Direction.UP);
        assertEquals(new Position(1, 2), P1.getPosition());
        assertEquals(true, P1.checkItemExists("K1"));
        
        P1.move(Direction.UP);
        assertEquals(new Position(1, 1), P1.getPosition());
        assertEquals(false, door1.isDoorClosed());
    }

    /**
     * ZOMBIE_TOAST_SPAWNER - Unit Tests
     */
    @Test
    public void testZombieToastSpawnerPeaceful() {

        DungeonManiaController dungeonManiaController = new DungeonManiaController();
        dungeonManiaController.newGame("zombie-spawner", DungeonConstants.MODE_PEACEFUL);
        DungeonResponse dungeonResponse;
        for (int i = 0; i < 9; i++) {
            dungeonManiaController.tick(null, Direction.RIGHT);
        }
        for (int i = 0; i < 9; i++) {
            dungeonManiaController.tick(null, Direction.DOWN);
        }
        dungeonResponse = dungeonManiaController.tick(null, Direction.UP);
        dungeonResponse = dungeonManiaController.tick(null, Direction.UP);

        for (EntityResponse entityResponse: dungeonResponse.getEntities()) {
            assertNotEquals(DungeonConstants.ENTITY_ZOMBIE_TOAST, entityResponse.getType());
        }
        boolean zombieToastFound = false;
        dungeonResponse = dungeonManiaController.tick(null, Direction.UP);
        for(EntityResponse entityResponse : dungeonResponse.getEntities()){
            if(entityResponse.getType().equals(DungeonConstants.ENTITY_ZOMBIE_TOAST)){
                zombieToastFound = true;
                break;
            }
        }
        assertEquals(true, zombieToastFound);
    }

    @Test
    public void testZombieToastSpawnerHard(){

        DungeonManiaController dungeonManiaController = new DungeonManiaController();
        dungeonManiaController.newGame("zombie-spawner", DungeonConstants.MODE_HARD);
        DungeonResponse dungeonResponse;
        for (int i = 0; i < 9; i++) {
            dungeonManiaController.tick(null, Direction.RIGHT);
        }
        for (int i = 0; i < 5; i++) {
            dungeonManiaController.tick(null, Direction.DOWN);
        }
        dungeonResponse = dungeonManiaController.tick(null, Direction.UP);

        for (EntityResponse entityResponse: dungeonResponse.getEntities()) {
            assertNotEquals(DungeonConstants.ENTITY_ZOMBIE_TOAST, entityResponse.getType());
        }
        boolean zombieToastFound = false;
        dungeonResponse = dungeonManiaController.tick(null, Direction.UP);
        for(EntityResponse entityResponse : dungeonResponse.getEntities()){
            if(entityResponse.getType().equals(DungeonConstants.ENTITY_ZOMBIE_TOAST)){
                zombieToastFound = true;
                break;
            }
        }
        assertEquals(true, zombieToastFound);
    }

    @Test
    public void testCannotSpawnZombie() {

        String dungeonId = "dungeon1";
        String name = "maze";
        Dungeon dungeon = new Dungeon(new DungeonInfo(50, 50, dungeonId, name, DungeonConstants.MODE_HARD, new ExitGoal()));
        Player P1 = new Player(dungeon, new EntityInfo("player", new Position(0, 4), 
        DungeonConstants.ENTITY_PLAYER, false));
        dungeon.setPlayer(P1);
        ZombieToastSpawner zombieSpawner = new ZombieToastSpawner(new EntityInfo("dead_boy_spawner", new Position(0, 0), 
        DungeonConstants.ENTITY_ZOMBIE_TOAST_SPAWNER, false));

        // Create obstacles
        Door door = new Door(new EntityInfo("door", new Position(1, 0), DungeonConstants.ENTITY_DOOR, false), 1);
        Boulder boulder = new Boulder(new EntityInfo("bouldy", 
                            new Position(1, 1), DungeonConstants.ENTITY_BOULDER, false));
        Wall wall = new Wall(new EntityInfo("wally", 
                    new Position(0, 1), DungeonConstants.ENTITY_WALL, false));

        List<Entity> entities = new ArrayList<>();
        entities.add(zombieSpawner);
        entities.add(door);
        entities.add(boulder);
        entities.add(wall);

        dungeon.setEntities(entities);

        // 20 ticks later...
        for (int i = 0; i < 20; i++) {
            dungeon.tick(null, Direction.RIGHT);
        }

        // Still no zombie toast since zombie spawner is blocked (adjacent positions are not free)
        assertFalse(dungeon.getEntities().stream().anyMatch(x -> Objects.equals(x.getType(), DungeonConstants.ENTITY_ZOMBIE_TOAST)));
    
    }
    
    /**
     * BOMB - Unit Tests
     */
    @Test
    public void testBombExplosion(){
        String dungeonId = "dungeon1";
        String name = "maze";
        String bombId = "bomb1";

        Dungeon dungeon = new Dungeon(new DungeonInfo(50, 50, dungeonId, name, DungeonConstants.MODE_STANDARD, new ExitGoal()));
                
        Player P1 = new Player(dungeon, new EntityInfo("player", new Position(0, 4), 
        DungeonConstants.ENTITY_PLAYER, false));
        dungeon.setPlayer(P1);
        Bomb bomb = new Bomb(new EntityInfo(bombId, new Position(0, 3), DungeonConstants.ENTITY_BOMB, true));
        FloorSwitch switch1 = new FloorSwitch(new EntityInfo("S1", new Position(0, 1), DungeonConstants.ENTITY_FLOOR_SWITCH, false));
        Boulder b1 = new Boulder(new EntityInfo("B1", new Position(0, 2), DungeonConstants.ENTITY_BOULDER, false));

        List<Entity> entities = new ArrayList<>();
        
        entities.add(P1);
        entities.add(bomb);
        entities.add(switch1);
        entities.add(b1);

        dungeon.setEntities(entities);
        
        // Moves and collects a Bomb
        P1.move(Direction.UP);
        assertEquals(new Position(0, 3), P1.getPosition());
        
        // Removed from dungeon
        assertFalse(dungeon.getEntities().stream().anyMatch(x->Objects.equals(x.getType(), "bomb")));

        // Moves a boulder onto a switch
        P1.move(Direction.UP);
        assertEquals(new Position(0, 2), P1.getPosition());
        assertEquals(switch1.getPosition(), b1.getPosition());
        assertTrue(switch1.isSwitchOn());

        // Player uses bomb and it explodes
        assertTrue(P1.checkItemExists(bombId));
        assertDoesNotThrow(() -> P1.useItem(bombId)); 

        // Only player is left standing
        assertEquals(1, dungeon.getEntitiesAtPosition(new Position(0, 2)).size());
        assertEquals(new Position(0, 2), P1.getPosition());
        // Nearby entities are destroyed
        assertEquals(0, dungeon.getEntitiesAtPosition(new Position(0, 1)).size());
    }

    @Test
    public void testBombChaining() {
        String dungeonId = "dungeon1";
        String name = "maze";

        Dungeon dungeon = new Dungeon(new DungeonInfo(50, 50, dungeonId, name, DungeonConstants.MODE_STANDARD, new ExitGoal()));
                
        Player P1 = new Player(dungeon, new EntityInfo("player", new Position(1, 2), 
        DungeonConstants.ENTITY_PLAYER, false));
        dungeon.setPlayer(P1);
        Bomb bomb1 = new Bomb(new EntityInfo("bomb1", null, DungeonConstants.ENTITY_BOMB, true));
        P1.addItemToInventory(bomb1);
        assertDoesNotThrow(() -> P1.useItem("bomb1")); // Bomb is placed on (1, 1) next to an untriggered switch
        Bomb bomb2 = new Bomb(new EntityInfo("bomb2", null, DungeonConstants.ENTITY_BOMB, true));
        P1.addItemToInventory(bomb2);

        Wall wall = new Wall(new EntityInfo("wally", new Position(1, 1), DungeonConstants.ENTITY_WALL, false));
        FloorSwitch switch1 = new FloorSwitch(new EntityInfo("S1", new Position(1, 2), DungeonConstants.ENTITY_FLOOR_SWITCH, false));
        FloorSwitch switch2 = new FloorSwitch(new EntityInfo("S2", new Position(1, 4), DungeonConstants.ENTITY_FLOOR_SWITCH, false));
        Boulder b1 = new Boulder(new EntityInfo("B1", new Position(1, 3), DungeonConstants.ENTITY_BOULDER, false));
      
        dungeon.addEntity(P1);
        dungeon.addEntity(wall);
        dungeon.addEntity(switch1);
        dungeon.addEntity(switch2);
        dungeon.addEntity(b1);

        // Moves a boulder onto a switch
        P1.move(Direction.DOWN);
        assertEquals(new Position(1, 3), P1.getPosition());
        assertEquals(switch2.getPosition(), b1.getPosition());
        assertFalse(switch1.isSwitchOn());
        assertTrue(switch2.isSwitchOn());

        // Player uses bomb 2 and it explodes
        assertDoesNotThrow(() -> P1.useItem("bomb2")); 

        assertTrue(dungeon.getEntitiesAtPosition(new Position(1, 4)).isEmpty());

        // Only player is left standing
        assertTrue(dungeon.getEntities().stream().allMatch(x-> Objects.equals(x.getType(), DungeonConstants.ENTITY_PLAYER)));

        // Ensure that bomb 1 exploded as well
        assertTrue(dungeon.getEntitiesAtPosition(new Position(1, 1)).isEmpty());
        assertTrue(dungeon.getEntitiesAtPosition(new Position(1, 2)).isEmpty());
    }

    @Test
    public void testDestroyZombieSpawner() {
        DungeonManiaController dungeonManiaController = new DungeonManiaController();
        dungeonManiaController.newGame("zombie-spawner", DungeonConstants.MODE_STANDARD);
        DungeonResponse dungeonResponse;

        dungeonResponse = dungeonManiaController.tick(null, Direction.RIGHT);
        dungeonResponse = dungeonManiaController.tick(null, Direction.RIGHT);
        dungeonResponse = dungeonManiaController.tick(null, Direction.RIGHT);
        dungeonResponse = dungeonManiaController.tick(null, Direction.RIGHT);

        for (EntityResponse entityResponse: dungeonResponse.getEntities()) {
            if (entityResponse.getType().equals(DungeonConstants.ENTITY_ZOMBIE_TOAST_SPAWNER)) {
                dungeonManiaController.interact(entityResponse.getId());
            }
        }
        dungeonResponse = dungeonManiaController.tick(null, Direction.RIGHT);

        for (EntityResponse entityResponse: dungeonResponse.getEntities()) {
            assertNotEquals(entityResponse.getType(), DungeonConstants.ENTITY_ZOMBIE_TOAST_SPAWNER);
        }
    }
}
