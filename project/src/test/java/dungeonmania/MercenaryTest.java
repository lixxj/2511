package dungeonmania;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import dungeonmania.entities.*;
import dungeonmania.entities.collectables.Key;
import dungeonmania.entities.collectables.SunStone;
import dungeonmania.entities.collectables.Treasure;
import dungeonmania.entities.collectables.Wood;
import dungeonmania.entities.collectables.potions.*;
import dungeonmania.entities.moving.MoveAway;
import dungeonmania.entities.moving.enemies.*;
import dungeonmania.dungeon.*;
import dungeonmania.goals.ExitGoal;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.*;
import dungeonmania.entities.moving.player.Player;
import dungeonmania.entities.staticitems.*;
import dungeonmania.exceptions.InvalidActionException;

import org.junit.jupiter.api.Test;



public class MercenaryTest {

    @Test
    public void testMercenaryPeacefulTest() {
        DungeonManiaController dungeonManiaController = new DungeonManiaController();
        dungeonManiaController.newGame("advanced-2", DungeonConstants.MODE_PEACEFUL);
        DungeonResponse dungeonResponse;
        for (int i = 0; i < 3; i++) {
            dungeonResponse = dungeonManiaController.tick(null, Direction.DOWN);
        }
        dungeonResponse = dungeonManiaController.tick(null, Direction.DOWN);
        for (EntityResponse entityResponse: dungeonResponse.getEntities()) {
            assertNotEquals(DungeonConstants.ENTITY_MERCENARY, entityResponse.getType());
        }
    }


    // Tests if a mercenary can be bribed
    @Test
    public void testMercenaryBribe() {
        DungeonManiaController dungeonManiaController = new DungeonManiaController();
        dungeonManiaController.newGame("advanced-2", DungeonConstants.MODE_STANDARD);
        DungeonResponse dungeonResponse;

        for (int i = 0; i < 10; i++) {
            dungeonResponse = dungeonManiaController.tick(null, Direction.RIGHT);
        }
        for (int i = 0; i < 9; i++) {
            dungeonResponse = dungeonManiaController.tick(null, Direction.DOWN);
        }
        for (int i = 0; i < 4; i++) {
            dungeonResponse = dungeonManiaController.tick(null, Direction.LEFT);
        }
        dungeonResponse = dungeonManiaController.tick(null, Direction.RIGHT);

        for (EntityResponse entityResponse: dungeonResponse.getEntities()) {
            if (entityResponse.getType().equals(DungeonConstants.ENTITY_MERCENARY)) {
                dungeonManiaController.interact(entityResponse.getId());
                break;
            }
        }

        dungeonResponse = dungeonManiaController.tick(null, Direction.LEFT);

        for (ItemResponse itemResponse: dungeonResponse.getInventory()) {
            assertNotEquals(DungeonConstants.ENTITY_TREASURE, itemResponse.getType());
        }
    }


    @Test
    public void testMercenaryHardTest() {
        DungeonManiaController dungeonManiaController = new DungeonManiaController();
        dungeonManiaController.newGame("advanced-2", DungeonConstants.MODE_HARD);
        DungeonResponse dungeonResponse;
        for (int i = 0; i < 2; i++) {
            dungeonResponse = dungeonManiaController.tick(null, Direction.DOWN);
        }
        dungeonResponse = dungeonManiaController.tick(null, Direction.DOWN);
        for (EntityResponse entityResponse: dungeonResponse.getEntities()) {
            assertNotEquals(DungeonConstants.ENTITY_PLAYER, entityResponse.getType());
        }
    }

    // MERCENARY SPAWNS EVERY 11 TICKS NOW
    @Test
    public void testMercenaryStandardWithWeaponTest() {
        DungeonManiaController dungeonManiaController = new DungeonManiaController();
        dungeonManiaController.newGame("advanced-2", DungeonConstants.MODE_STANDARD);
        DungeonResponse dungeonResponse;

        dungeonResponse = dungeonManiaController.tick(null, Direction.RIGHT);
        dungeonResponse = dungeonManiaController.tick(null, Direction.RIGHT);
        dungeonResponse = dungeonManiaController.tick(null, Direction.RIGHT);
        dungeonResponse = dungeonManiaController.tick(null, Direction.RIGHT);
        dungeonResponse = dungeonManiaController.tick(null, Direction.RIGHT);


        dungeonResponse = dungeonManiaController.tick(null, Direction.LEFT);
        dungeonResponse = dungeonManiaController.tick(null, Direction.LEFT);

        for (EntityResponse entityResponse: dungeonResponse.getEntities()) {
            assertNotEquals(DungeonConstants.ENTITY_MERCENARY, entityResponse.getType());
        }
    }

    // Testing a successful movement using the MoveTowards movement strategy
    @Test
    public void testMoveTowards() {

        String dungeonId = "dungeon1";
        String name = "maze";
        String mercenaryId = "Merc";
        String playerId = "Player 1";

        Position playerPosition = new Position(1,0);
        Position mercenaryPosition = new Position(5,0);

        Dungeon dungeon = new Dungeon(new DungeonInfo(8, 8, dungeonId, name, DungeonConstants.MODE_PEACEFUL, new ExitGoal()));
        Player myPlayer = new Player(dungeon, new EntityInfo(playerId, playerPosition, 
        DungeonConstants.ENTITY_PLAYER, false));
        dungeon.setPlayer(myPlayer);
        Mercenary mercenary = new Mercenary(dungeon, new EntityInfo(mercenaryId, mercenaryPosition, 
        DungeonConstants.ENTITY_MERCENARY, false));
        
        Double distance = Math.sqrt(Math.pow((mercenaryPosition.getX() - playerPosition.getX()), 2) + 
        Math.pow((mercenaryPosition.getY() - playerPosition.getY()), 2));

        while (distance != 0) {
            mercenary.move();
            mercenaryPosition = mercenary.getPosition();
            distance = Math.sqrt(Math.pow((mercenaryPosition.getX() - playerPosition.getX()), 2) + 
                        Math.pow((mercenaryPosition.getY() - playerPosition.getY()), 2));
        }
        
        assertTrue(distance == 0);
        assertEquals(playerPosition, mercenaryPosition);

    }

    // Testing a successful movement using the MoveAway movement strategy
    @Test
    public void testMoveAway() {

        String dungeonId = "dungeon1";
        String name = "maze";
        String mercenaryId = "Merc";
        String playerId = "Player 1";

        Position playerPosition = new Position(1,0);
        Position mercenaryPosition = new Position(5,0);

        Dungeon dungeon = new Dungeon(new DungeonInfo(50, 50, dungeonId, name, DungeonConstants.MODE_PEACEFUL, new ExitGoal()));
        Player myPlayer = new Player(dungeon, new EntityInfo(playerId, playerPosition, 
        DungeonConstants.ENTITY_PLAYER, false));
        dungeon.setPlayer(myPlayer);
        Mercenary mercenary = new Mercenary(dungeon, new EntityInfo(mercenaryId, mercenaryPosition, 
        DungeonConstants.ENTITY_MERCENARY, false));
        mercenary.setMovableBehaviour(new MoveAway(dungeon, mercenaryPosition));

        Double initialDistance = Math.sqrt(Math.pow((mercenaryPosition.getX() - playerPosition.getX()), 2) + 
        Math.pow((mercenaryPosition.getY() - playerPosition.getY()), 2));

        int i = 0;
        while (i < 15) {
            mercenary.move();
            Position newMercenaryPosition = mercenary.getPosition();
            Double newDistance = Math.sqrt(Math.pow((newMercenaryPosition.getX() - playerPosition.getX()), 2) + 
            Math.pow((newMercenaryPosition.getY() - playerPosition.getY()), 2));
            assertTrue(newDistance > initialDistance);
            i++;
        }
    }

    // Testing if the object does not move if it cannot (surrounded by obstacles)
    @Test
    public void testCannotMoveTowards() {
        String dungeonId = "dungeon1";
        String name = "maze";
        String mercenaryId = "bigguns";
        Position initialPosition = new Position(1, 1);
        Dungeon dungeon = new Dungeon(new DungeonInfo(50, 50, dungeonId, name, DungeonConstants.MODE_PEACEFUL, new ExitGoal()));
        Player myPlayer = new Player(dungeon, new EntityInfo("gamer", new Position(10, 10), 
        DungeonConstants.ENTITY_PLAYER, false));
        dungeon.setPlayer(myPlayer);
        Mercenary mercenary = new Mercenary(dungeon, new EntityInfo(mercenaryId, initialPosition, 
        DungeonConstants.ENTITY_MERCENARY, false));

        // Create obstacles
        Door door = new Door(new EntityInfo("door", new Position(2, 1), DungeonConstants.ENTITY_DOOR, false), 1);
        ZombieToastSpawner zombieToastSpawner = new ZombieToastSpawner(new EntityInfo("toaster", 
                            new Position(0, 1), DungeonConstants.ENTITY_ZOMBIE_TOAST_SPAWNER, false));
        Boulder boulder = new Boulder(new EntityInfo("bouldy", 
                            new Position(1, 0), DungeonConstants.ENTITY_BOULDER, false));
        Wall wall = new Wall(new EntityInfo("wally", 
                    new Position(1, 2), DungeonConstants.ENTITY_WALL, false));

        List<Entity> entities = new ArrayList<>();
        entities.add(mercenary);
        entities.add(door);
        entities.add(zombieToastSpawner);
        entities.add(boulder);
        entities.add(wall);

        dungeon.setEntities(entities);
        dungeon.addEnemy(mercenary);
        
        // Mercenary cannot move
        dungeon.getEnemies().forEach(Enemy::move);

        assertEquals(initialPosition, mercenary.getPosition());

    }

    // Testing if the object does not move if it cannot (surrounded by obstacles)
    @Test
    public void testCannotMoveAway() {
        String dungeonId = "dungeon1";
        String name = "maze";
        String mercenaryId = "bigguns";
        Position initialPosition = new Position(0, 0);
        Dungeon dungeon = new Dungeon(new DungeonInfo(50, 50, dungeonId, name, DungeonConstants.MODE_PEACEFUL, new ExitGoal()));
        Player myPlayer = new Player(dungeon, new EntityInfo("gamer", new Position(10, 10), 
        DungeonConstants.ENTITY_PLAYER, false));
        dungeon.setPlayer(myPlayer);
        Mercenary mercenary = new Mercenary(dungeon, new EntityInfo(mercenaryId, initialPosition, 
        DungeonConstants.ENTITY_MERCENARY, false));
        mercenary.setMovableBehaviour(new MoveAway(dungeon, initialPosition));

        // Create obstacles
        Door door = new Door(new EntityInfo("door", new Position(1, 0), DungeonConstants.ENTITY_DOOR, false), 1);
        ZombieToastSpawner zombieToastSpawner = new ZombieToastSpawner(new EntityInfo("toaster", 
                            new Position(-1, 0), DungeonConstants.ENTITY_ZOMBIE_TOAST_SPAWNER, false));
        Boulder boulder = new Boulder(new EntityInfo("bouldy", 
                            new Position(0, -1), DungeonConstants.ENTITY_BOULDER, false));
        Wall wall = new Wall(new EntityInfo("wally", 
                    new Position(0, 1), DungeonConstants.ENTITY_WALL, false));

        List<Entity> entities = new ArrayList<>();
        entities.add(mercenary);
        entities.add(door);
        entities.add(zombieToastSpawner);
        entities.add(boulder);
        entities.add(wall);

        dungeon.setEntities(entities);
        dungeon.addEnemy(mercenary);
        
        // Mercenary cannot move
        dungeon.getEnemies().forEach(Enemy::move);

        assertEquals(initialPosition, mercenary.getPosition());

    }

    // Testing teleportation using the MoveTowards movement strategy
    @Test
    public void testTeleportTowards() {
        String dungeonId = "dungeon1";
        String name = "maze";
        String mercenaryId = "bigguns";
        Position initialPosition = new Position(0, 0);
        Dungeon dungeon = new Dungeon(new DungeonInfo(50, 50, dungeonId, name, DungeonConstants.MODE_PEACEFUL, new ExitGoal()));
        Player myPlayer = new Player(dungeon, new EntityInfo("gamer", new Position(10, 10), 
        DungeonConstants.ENTITY_PLAYER, false));
        dungeon.setPlayer(myPlayer);
        Mercenary mercenary = new Mercenary(dungeon, new EntityInfo(mercenaryId, initialPosition, 
        DungeonConstants.ENTITY_MERCENARY, false));
        
        Portal portal1 = new Portal(new EntityInfo("portal1", new Position(0, 1), 
                        DungeonConstants.ENTITY_PORTAL, false), "BLUE");
        Portal portal2 = new Portal(new EntityInfo("portal2", new Position(0, 3), 
        DungeonConstants.ENTITY_PORTAL, false), "BLUE");

        Portal portal3 = new Portal(new EntityInfo("portal3", new Position(0, -1), 
                        DungeonConstants.ENTITY_PORTAL, false), "RED");
        Portal portal4 = new Portal(new EntityInfo("portal4", new Position(0, -3), 
        DungeonConstants.ENTITY_PORTAL, false), "RED");

        Portal portal5 = new Portal(new EntityInfo("portal5", new Position(-1, 0), 
                        DungeonConstants.ENTITY_PORTAL, false), "CYAN");
        Portal portal6 = new Portal(new EntityInfo("portal6", new Position(-3, 0), 
        DungeonConstants.ENTITY_PORTAL, false), "CYAN");

        Portal portal7 = new Portal(new EntityInfo("portal7", new Position(1, 0), 
                        DungeonConstants.ENTITY_PORTAL, false), "VIOLET");
        Portal portal8 = new Portal(new EntityInfo("portal8", new Position(3, 0), 
        DungeonConstants.ENTITY_PORTAL, false), "VIOLET");

        List<Entity> entities = new ArrayList<>();
        entities.add(mercenary);
        entities.add(portal1);
        entities.add(portal2);
        entities.add(portal3);
        entities.add(portal4);
        entities.add(portal5);
        entities.add(portal6);
        entities.add(portal7);
        entities.add(portal8);

        dungeon.setEntities(entities);
        dungeon.addEnemy(mercenary);

        List<Position> possibleTeleportedLocations = new ArrayList<>();
        possibleTeleportedLocations.add(portal1.getCorrespondingSpawnPoint(dungeon));
        possibleTeleportedLocations.add(portal3.getCorrespondingSpawnPoint(dungeon));
        possibleTeleportedLocations.add(portal5.getCorrespondingSpawnPoint(dungeon));
        possibleTeleportedLocations.add(portal7.getCorrespondingSpawnPoint(dungeon));
        
        // Mercenary is teleported
        dungeon.getEnemies().forEach(Enemy::move);

        assertTrue(possibleTeleportedLocations.stream().anyMatch(x->Objects.equals(x, mercenary.getPosition())));

    }

    // Testing teleportation using the MoveAway movement strategy
    @Test
    public void testTeleportAway() {
        String dungeonId = "dungeon1";
        String name = "maze";
        String mercenaryId = "bigguns";
        Position initialPosition = new Position(0, 0);
        Dungeon dungeon = new Dungeon(new DungeonInfo(50, 50, dungeonId, name, DungeonConstants.MODE_STANDARD, new ExitGoal()));
        Player myPlayer = new Player(dungeon, new EntityInfo("gamer", new Position(-1, -10), 
        DungeonConstants.ENTITY_PLAYER, false));
        dungeon.setPlayer(myPlayer);
        Mercenary mercenary = new Mercenary(dungeon, new EntityInfo(mercenaryId, initialPosition, 
        DungeonConstants.ENTITY_MERCENARY, false));

        Portal portal1 = new Portal(new EntityInfo("portal1", new Position(0, 1), 
                        DungeonConstants.ENTITY_PORTAL, false), "BLUE");
        Portal portal2 = new Portal(new EntityInfo("portal2", new Position(0, 3), 
        DungeonConstants.ENTITY_PORTAL, false), "BLUE");

        Portal portal3 = new Portal(new EntityInfo("portal3", new Position(1, 0), 
                        DungeonConstants.ENTITY_PORTAL, false), "RED");
        Portal portal4 = new Portal(new EntityInfo("portal4", new Position(3, 0), 
        DungeonConstants.ENTITY_PORTAL, false), "RED");

        Portal portal5 = new Portal(new EntityInfo("portal5", new Position(-1, 0), 
                        DungeonConstants.ENTITY_PORTAL, false), "CYAN");
        Portal portal6 = new Portal(new EntityInfo("portal6", new Position(-3, 0), 
        DungeonConstants.ENTITY_PORTAL, false), "CYAN");
        
        InvincibilityPotion invincibilityPotion = new InvincibilityPotion(new EntityInfo("IP1", new Position(0, -10), 
        DungeonConstants.ENTITY_INVINCIBILITY_POTION, true));

        List<Entity> entities = new ArrayList<>();
        entities.add(mercenary);
        entities.add(portal1);
        entities.add(portal2);
        entities.add(portal3);
        entities.add(portal4);
        entities.add(portal5);
        entities.add(portal6);
        entities.add(invincibilityPotion);

        dungeon.setEntities(entities);
        dungeon.addEnemy(mercenary);

        // Moves and collects an invincibility potion
        myPlayer.move(Direction.RIGHT);
        assertEquals(new Position(0, -10), myPlayer.getPosition());

        // Player consumes the potion
        assertTrue(myPlayer.checkItemExists("IP1"));
        assertDoesNotThrow(() -> myPlayer.useItem("IP1")); 

        // Removed from dungeon
        assertFalse(dungeon.getEntities().stream().anyMatch(x->Objects.equals(x.getType(), "invincibility_potion")));
        
        // Player is invincible
        assertTrue(myPlayer.isInvincible());

        List<Position> possibleTeleportedLocations = new ArrayList<>();
        possibleTeleportedLocations.add(portal1.getCorrespondingSpawnPoint(dungeon));
        possibleTeleportedLocations.add(portal3.getCorrespondingSpawnPoint(dungeon));
        possibleTeleportedLocations.add(portal5.getCorrespondingSpawnPoint(dungeon));
        
        // Mercenary is teleported
        dungeon.getEnemies().forEach(Enemy::move);
        assertTrue(possibleTeleportedLocations.stream().anyMatch(x->Objects.equals(x, mercenary.getPosition())));
    }

    // Testing if the mercenary changes its movement behaviour once the player consumes
    // an invisibility potion
    @Test
    public void testMercenaryMovementInvinciblePlayer() {
        String dungeonId = "dungeon1";
        String name = "maze";
        String mercenaryId = "bigguns";
        Position initialPosition = new Position(1, 15);
        Dungeon dungeon = new Dungeon(new DungeonInfo(50, 50, dungeonId, name, DungeonConstants.MODE_PEACEFUL, new ExitGoal()));
        Player myPlayer = new Player(dungeon, new EntityInfo("gamer", new Position(0, 5), 
        DungeonConstants.ENTITY_PLAYER, false));
        dungeon.setPlayer(myPlayer);
        Mercenary mercenary = new Mercenary(dungeon, new EntityInfo(mercenaryId, initialPosition, 
        DungeonConstants.ENTITY_MERCENARY, false));
        
        InvincibilityPotion invincibilityPotion = new InvincibilityPotion(new EntityInfo("IP1", new Position(1, 5), 
        DungeonConstants.ENTITY_INVINCIBILITY_POTION, true));

        List<Entity> entities = new ArrayList<>();
        entities.add(mercenary);
        entities.add(invincibilityPotion);

        dungeon.setEntities(entities);
        dungeon.addEnemy(mercenary);

        // Moves and collects an invincibility potion
        myPlayer.move(Direction.RIGHT);
        assertEquals(new Position(1, 5), myPlayer.getPosition());

        // Player consumes the potion
        assertTrue(myPlayer.checkItemExists("IP1"));
        assertDoesNotThrow(() -> myPlayer.useItem("IP1")); 

        // Removed from dungeon
        assertFalse(dungeon.getEntities().stream().anyMatch(x->Objects.equals(x.getType(), "invincibility_potion")));
        
        // Player is invincible
        assertTrue(myPlayer.isInvincible());
       
        // Mercenary moves away since player is invincible
        dungeon.tick(null, Direction.DOWN); // Player - (1, 6), Mercenary - (1, 16)
        assertEquals(new Position(1, 16), mercenary.getPosition());

        dungeon.tick(null, Direction.DOWN); // Player - (1, 7), Mercenary - (1, 17)
        assertEquals(new Position(1, 17), mercenary.getPosition());

        dungeon.tick(null, Direction.DOWN); // Player - (1, 8), Mercenary - (1, 18)
        assertEquals(new Position(1, 18), mercenary.getPosition());

        dungeon.tick(null, Direction.DOWN); // Player - (1, 9), Mercenary - (1, 19)
        assertEquals(new Position(1, 19), mercenary.getPosition());
        
        dungeon.tick(null, Direction.DOWN); // Player - (1, 10), Mercenary - (1, 20)
        assertEquals(new Position(1, 20), mercenary.getPosition());

        dungeon.tick(null, Direction.DOWN); // Player - (1, 11), Mercenary - (1, 21)
        assertEquals(new Position(1, 21), mercenary.getPosition());

        // Movement Behaviour is initialised again for mercenary (since invincibility has run out!)
        dungeon.tick(null, Direction.DOWN); // Player - (1, 12), Mercenary - (1, 20)
        assertEquals(new Position(1, 20), mercenary.getPosition());
    }

    @Test
    public void testMercenaryMovementInvisiblePlayer() {
        boolean success = false;
        // Since there is no guarantee that the mercenary does indeed end up at the player's position.
        // it is repeated up to 5 times until a successful test is made
        for (int i = 0; i < 5; i++) {
            String dungeonId = "dungeon1";
            String name = "maze";
            String mercenaryId = "bigguns";
            Position initialPosition = new Position(1, 15);
            Dungeon dungeon = new Dungeon(new DungeonInfo(50, 50, dungeonId, name, DungeonConstants.MODE_STANDARD, new ExitGoal()));
            Player myPlayer = new Player(dungeon, new EntityInfo("gamer", new Position(0, 5), 
            DungeonConstants.ENTITY_PLAYER, false));
            dungeon.setPlayer(myPlayer);
            Mercenary myMercenary = new Mercenary(dungeon, new EntityInfo(mercenaryId, initialPosition, 
            DungeonConstants.ENTITY_MERCENARY, false));
            
            InvisibilityPotion invisibilityPotion = new InvisibilityPotion(new EntityInfo("IP1", new Position(1, 5), 
            DungeonConstants.ENTITY_INVISIBILITY_POTION, true));

            List<Entity> entities = new ArrayList<>();
            entities.add(myMercenary);
            entities.add(invisibilityPotion);

            dungeon.setEntities(entities);
            dungeon.addEnemy(myMercenary);

            // Moves and collects an invisibility potion
            myPlayer.move(Direction.RIGHT);
            assertEquals(new Position(1, 5), myPlayer.getPosition());

            // Player consumes the potion
            assertTrue(myPlayer.checkItemExists("IP1"));
            assertDoesNotThrow(() -> myPlayer.useItem("IP1")); 

            // Removed from dungeon
            assertFalse(dungeon.getEntities().stream().anyMatch(x->Objects.equals(x.getType(), "invincibility_potion")));
            
            // Player is invisible
            assertTrue(myPlayer.isInvisible());
        
            // Mercenary moves randomly since player is invincible
            for (int j = 0; j < 5; j++) {
                dungeon.tick(null, Direction.DOWN); // Player - (1, 6) 
                dungeon.tick(null, Direction.UP); // Player - (1, 5)
            }

            // Traditional movement would mean that the mercenary moves in a straight line
            // Mercenary - (1, 14)
            // Mercenary - (1, 13)
            // Mercenary - (1, 12)
            // Mercenary - (1, 11)       
            // Mercenary - (1, 10)
            // Mercenary - (1, 9)
            // Mercenary - (1, 8)
            // Mercenary - (1, 7)
            // Mercenary - (1, 6)
            // Mercenary - (1, 5)

            success = (myMercenary.getPosition() != new Position(1, 5));
            if (success) break;
        }
        assertTrue(success);
    }

    // Testing mercenary bribing and ally attacking - System test
    @Test
    public void testMercenarySimulationOne() {

        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse newGame = controller.newGame("battle", "Standard");
        
        String mercenaryId = newGame.getEntities().stream()
                        .filter(x -> Objects.equals(x.getType(), "mercenary"))
                        .findFirst().get().getId();

        assertEquals(newGame.getEntities().stream()
                    .filter(x -> Objects.equals(x.getType(), "player"))
                    .findFirst().get().getPosition(), new Position(2, 1));
        assertEquals(newGame.getEntities().stream()
                    .filter(x -> Objects.equals(x.getType(), "spider"))
                    .findFirst().get().getPosition(), new Position(1, 2));
        assertEquals(newGame.getEntities().stream()
                    .filter(x -> Objects.equals(x.getType(), "mercenary"))
                    .findFirst().get().getPosition(), new Position(4, 1));

        DungeonResponse move = controller.tick(null, Direction.LEFT);
       
        // Player and Spider are now on the same square, battle round --> spider is killed
        assertEquals(move.getEntities().stream()
                    .filter(x -> Objects.equals(x.getType(), "player"))
                    .findFirst().get().getPosition(), new Position(1, 1));
        // Mercenary moves twice since player was in battle
        assertEquals(move.getEntities().stream()
                    .filter(x -> Objects.equals(x.getType(), "mercenary"))
                    .findFirst().get().getPosition(), new Position(2, 1));

        // Player does not have any treasure to bribe the mercenary
        assertThrows(InvalidActionException.class, () -> {
            controller.interact(mercenaryId);
        });
       
        // Treasure at position
        assertEquals(move.getEntities().stream()
                    .filter(x -> Objects.equals(x.getType(), "treasure"))
                    .findFirst().get().getPosition(), new Position(0, 1));

        // Player collects treasure
        move = controller.tick(null, Direction.LEFT);        

        assertEquals(move.getEntities().stream()
                    .filter(x -> Objects.equals(x.getType(), "player"))
                    .findFirst().get().getPosition(), new Position(0, 1));
        assertEquals(move.getEntities().stream()
                    .filter(x -> Objects.equals(x.getType(), "mercenary"))
                    .findFirst().get().getPosition(), new Position(1, 1));
        

        // Mercenary is bribed
        controller.interact(mercenaryId);
        move = controller.tick(null, Direction.RIGHT);

        assertEquals(move.getEntities().stream()
                    .filter(x -> Objects.equals(x.getType(), "player"))
                    .findFirst().get().getPosition(), new Position(1, 1));
        assertEquals(move.getEntities().stream()
                    .filter(x -> Objects.equals(x.getType(), "mercenary"))
                    .findFirst().get().getPosition(), new Position(1, 1));    
        
        // Mercenary moves along with player
        move = controller.tick(null, Direction.UP);        

        assertEquals(move.getEntities().stream()
                    .filter(x -> Objects.equals(x.getType(), "player"))
                    .findFirst().get().getPosition(), new Position(1, 0));
        assertEquals(move.getEntities().stream()
                    .filter(x -> Objects.equals(x.getType(), "mercenary"))
                    .findFirst().get().getPosition(), new Position(1, 1));
    }

    // Testing for mercernary bribe exceptions
    @Test
    public void testMercenaryCannotBribe() {
        String dungeonId = "dungeon1";
        String name = "maze";
        String mercenaryId = "bigguns";
        Position initialPosition = new Position(0, 15);
        Dungeon dungeon = new Dungeon(new DungeonInfo(50, 50, dungeonId, name, DungeonConstants.MODE_PEACEFUL, new ExitGoal()));
        Player myPlayer = new Player(dungeon, new EntityInfo("gamer", new Position(0, 5), 
        DungeonConstants.ENTITY_PLAYER, false));
        dungeon.setPlayer(myPlayer);
        Mercenary mercenary = new Mercenary(dungeon, new EntityInfo(mercenaryId, initialPosition, 
        DungeonConstants.ENTITY_MERCENARY, false));
        
        List<Entity> entities = new ArrayList<>();
        entities.add(mercenary);
        dungeon.setEntities(entities);
        dungeon.addEnemy(mercenary);
       
        dungeon.tick(null, Direction.DOWN); // Player - (0, -9), Mercenary - (0, 1)
        assertEquals(new Position(0, 6), myPlayer.getPosition());
        assertEquals(new Position(0, 14), mercenary.getPosition());

        // Mercenary cannot be bribed since it is not nearby
        assertThrows(InvalidActionException.class, () -> {
            dungeon.interact(mercenaryId);
        });
    }

    // Testing for mercenary successful bribe and ally attacking other enemies
    @Test
    public void testMercenaryAlly() {
        String dungeonId = "dungeon1";
        String name = "maze";
        String mercenary1Id = "mercenary1";
        String mercenary2Id = "mercenary2";
        Dungeon dungeon = new Dungeon(new DungeonInfo(50, 50, dungeonId, name, DungeonConstants.MODE_PEACEFUL, new ExitGoal()));
        Player myPlayer = new Player(dungeon, new EntityInfo("gamer", new Position(0, 9), 
        DungeonConstants.ENTITY_PLAYER, false));
        dungeon.setPlayer(myPlayer);
        Treasure treasure = new Treasure(new EntityInfo("treasure", new Position(0, 10), DungeonConstants.ENTITY_TREASURE, true));
        Mercenary mercenary1 = new Mercenary(dungeon, new EntityInfo(mercenary1Id, new Position(0, 12), 
        DungeonConstants.ENTITY_MERCENARY, false));
        Mercenary mercenary2 = new Mercenary(dungeon, new EntityInfo(mercenary2Id, new Position(0, 14), 
        DungeonConstants.ENTITY_MERCENARY, false));
        
        List<Entity> entities = new ArrayList<>();
        entities.add(treasure);
        entities.add(mercenary1);
        entities.add(mercenary2);
        dungeon.setEntities(entities);
        dungeon.addEnemy(mercenary1);
        dungeon.addEnemy(mercenary2);

        // Moves and collects a treasure
        dungeon.tick(null, Direction.DOWN); 
        assertEquals(new Position(0, 10), myPlayer.getPosition());
        assertEquals(new Position(0, 11), mercenary1.getPosition());
        assertEquals(new Position(0, 13), mercenary2.getPosition());

        // Check treasure in inventory
        assertTrue(myPlayer.checkItemExists("treasure"));

        // Removed from dungeon
        assertFalse(dungeon.getEntities().stream().anyMatch(x->Objects.equals(x.getType(), "treasure")));
        
        // Bribe the mercenary with new treasure
        dungeon.interact(mercenary1Id);
        assertTrue(mercenary1.isAlly());

        // Moves - no battle since mercenary 1 is an ally
        dungeon.tick(null, Direction.DOWN); 
        assertTrue(myPlayer.isAlive());
        assertEquals(new Position(0, 11), myPlayer.getPosition());
        assertEquals(new Position(0, 11), mercenary1.getPosition());
        assertEquals(new Position(0, 12), mercenary2.getPosition());
        
        // Moves - battle with mercenary 2 (player wins with help of ally)
        dungeon.tick(null, Direction.DOWN); 
        assertTrue(myPlayer.isAlive());
        assertFalse(mercenary2.isAlive());
        assertEquals(new Position(0, 12), myPlayer.getPosition());
        assertEquals(new Position(0, 11), mercenary1.getPosition());
        assertEquals(new Position(0, 12), mercenary2.getPosition());

    }

    @Test
    public void testShortestPath() {
        String dungeonId = "dungeon1";
        String name = "maze";
        String mercenaryId = "bigguns";
        Dungeon dungeon = new Dungeon(new DungeonInfo(50, 50, dungeonId, name, DungeonConstants.MODE_STANDARD, new ExitGoal()));
        Player myPlayer = new Player(dungeon, new EntityInfo("gamer", new Position(4, 2), 
        DungeonConstants.ENTITY_PLAYER, false));
        dungeon.setPlayer(myPlayer);
        Mercenary myMercenary = new Mercenary(dungeon, new EntityInfo(mercenaryId, new Position(2, 2), 
        DungeonConstants.ENTITY_MERCENARY, false));
        SwampTile swamp = new SwampTile(new EntityInfo("lernaean_hydra_swamp", new Position(3, 2), DungeonConstants.ENTITY_SWAMP_TILE, false), 10);
        
        List<Entity> entities = new ArrayList<>();
        entities.add(myMercenary);
        entities.add(swamp);
        dungeon.setEntities(entities);
        dungeon.addEnemy(myMercenary);
       
        dungeon.tick(null, Direction.RIGHT); // Player - (5, 2), Mercenary - (3, 2) if swamp tile is not considered in shortest path
        assertEquals(new Position(5, 2), myPlayer.getPosition());
        assertNotEquals(new Position(3, 2), myMercenary.getPosition());
    }

    @Test
    public void testArmouredMercenarySpawn() {
        String dungeonId = "dungeon1";
        String name = "maze";
        Dungeon dungeon = new Dungeon(new DungeonInfo(50, 50, dungeonId, name, DungeonConstants.MODE_STANDARD, new ExitGoal()));
        Player myPlayer = new Player(dungeon, new EntityInfo("gamer", new Position(0, 0), 
        DungeonConstants.ENTITY_PLAYER, false));
        dungeon.setPlayer(myPlayer);
        List<Entity> mercList = new ArrayList<>();

        // Statistically, this method allows for approximately 238 mercenary spawns. With a 20% chance of spawning with
        // armour, it can be expected that the list an average of 47 mercenaries (if the loop is allowed to continue).
        // For simplicity, the loop breaks if any mercenary is spawned with armour, in which case the test specification
        // is fulfilled.

        for (int i = 0; i < 10; i++) {
            dungeon.tick(null, Direction.RIGHT);
            mercList = dungeon.getEntities().stream().filter(x -> x instanceof Mercenary).collect(Collectors.toList());
            mercList.forEach(x -> {
                if (!((Mercenary)x).hasProtection()) dungeon.removeEntity(x);
            });
            if (mercList.size() != 0) break;
        }
        
        for (int i = 0; i < 5000; i++) {
            dungeon.tick(null, Direction.RIGHT);
            dungeon.tick(null, Direction.LEFT);
            mercList = dungeon.getEntities().stream().filter(x -> x instanceof Mercenary).collect(Collectors.toList());
            mercList.forEach(x -> {
                if (!((Mercenary)x).hasProtection()) dungeon.removeEntity(x);
            });
            if (mercList.size() != 0) break;
        }
    
        assertTrue(mercList.size() > 0);
        assertTrue(mercList.stream().allMatch(x -> ((Mercenary)x).hasProtection()));

    }

    @Test
    public void testSceptreMindControl(){
        String dungeonId = "dungeon1";
        String name = "maze";
        Dungeon dungeon = new Dungeon(new DungeonInfo(50, 50, dungeonId, name, DungeonConstants.MODE_STANDARD, new ExitGoal()));
        Player player =  new Player(dungeon, new EntityInfo("gamer", new Position(0, 9), 
        DungeonConstants.ENTITY_PLAYER, false));
        Mercenary mercenary = new Mercenary(dungeon, new EntityInfo("mercenary1Id", new Position(0, 17), 
        DungeonConstants.ENTITY_MERCENARY, false));
        
        dungeon.setPlayer(player);
        dungeon.addEnemy(mercenary);
        ArrayList<Entity> entities = new ArrayList<>();
        entities.add(mercenary);

        //collecting 1 wood, 1 key and 1 sunstone
        Wood wood = new Wood(new EntityInfo("wood1",
        new Position(0, 10), DungeonConstants.ENTITY_WOOD, true));
        Key key = new Key(new EntityInfo("key1",
        new Position(0, 11), DungeonConstants.ENTITY_KEY, true), 0); 
        SunStone stone = new SunStone(new EntityInfo("stone1",
        new Position(0, 12), DungeonConstants.ENTITY_SUN_STONE, true));

        entities.add(wood);
        entities.add(key);
        entities.add(stone);
        dungeon.setEntities(entities);

        dungeon.tick(null, Direction.DOWN); 
        dungeon.tick(null, Direction.DOWN);
        dungeon.tick(null, Direction.DOWN); 
        dungeon.build(DungeonConstants.ENTITY_SCEPTRE);

        dungeon.interact("mercenary1Id");
        assertTrue(mercenary.isAlly());

    }



}
