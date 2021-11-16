package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import dungeonmania.entities.*;
import dungeonmania.entities.moving.Battle;
import dungeonmania.entities.moving.enemies.*;
import dungeonmania.entities.moving.player.Player;
import dungeonmania.entities.staticitems.*;
import dungeonmania.dungeon.*;
import dungeonmania.goals.ExitGoal;
import dungeonmania.response.models.*;

import org.junit.jupiter.api.Test;

import dungeonmania.util.*;

public class HydraTest {

    @Test
    public void testMovement() {

        String dungeonId = "dungeon1";
        String name = "maze";
        String hydraId = "Hydra1";
        Dungeon dungeon = new Dungeon(new DungeonInfo(50, 50, dungeonId, name, DungeonConstants.MODE_HARD, new ExitGoal()));

        Position initialPosition = new Position(0,0);
        Hydra newHydra = new Hydra(dungeon, new EntityInfo(hydraId, 
                                    initialPosition, DungeonConstants.ENTITY_HYDRA, false));

        newHydra.move();
        assertTrue(initialPosition.getAdjacentPositions()
                    .stream()
                    .anyMatch(x -> Objects.equals(x, newHydra.getPosition())));

        int movesRemaining = 10;
        while (movesRemaining >= 0) {
            initialPosition = newHydra.getPosition();
            newHydra.move();
        
            assertTrue(initialPosition.getAdjacentPositions()
                        .stream()
                        .anyMatch(x -> Objects.equals(x, newHydra.getPosition())));

            movesRemaining--;
        }
    }   
    @Test
    public void testCannotMoveRandom() {
        String dungeonId = "dungeon1";
        String name = "maze";
        String hydraId = "Hydra1";
        Position initialPosition = new Position(0, 0);
        Dungeon dungeon = new Dungeon(new DungeonInfo(50, 50, dungeonId, name, DungeonConstants.MODE_HARD, new ExitGoal()));
        Hydra hydra = new Hydra(dungeon, new EntityInfo(hydraId, initialPosition, 
        DungeonConstants.ENTITY_HYDRA, false));

        // Create obstacles
        Door door = new Door(new EntityInfo("door", new Position(1, 0), DungeonConstants.ENTITY_DOOR, false), 1);
        Boulder boulder = new Boulder(new EntityInfo("bouldy", 
                            new Position(0, -1), DungeonConstants.ENTITY_BOULDER, false));
        Wall wall1 = new Wall(new EntityInfo("wally", 
                    new Position(0, 1), DungeonConstants.ENTITY_WALL, false));
        Wall wall2 = new Wall(new EntityInfo("wally", 
                    new Position(-1, 0), DungeonConstants.ENTITY_WALL, false));

        List<Entity> entities = new ArrayList<>();
        entities.add(hydra);
        entities.add(door);
        entities.add(boulder);
        entities.add(wall1);
        entities.add(wall2);

        dungeon.setEntities(entities);
        dungeon.addEnemy(hydra);
        
        dungeon.getEnemies().forEach(Enemy::move);

        assertEquals(initialPosition, hydra.getPosition());

    }
    
    @Test
    public void testTeleport() {
        String dungeonId = "dungeon1";
        String name = "maze";
        String hydraId = "hydra1";
        Position initialPosition = new Position(0, 0);
        Dungeon dungeon = new Dungeon(new DungeonInfo(50, 50, dungeonId, name, DungeonConstants.MODE_HARD, new ExitGoal()));
        Hydra hydra = new Hydra(dungeon, new EntityInfo(hydraId, initialPosition, 
        DungeonConstants.ENTITY_HYDRA, false));
        
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
        entities.add(hydra);
        entities.add(portal1);
        entities.add(portal2);
        entities.add(portal3);
        entities.add(portal4);
        entities.add(portal5);
        entities.add(portal6);
        entities.add(portal7);
        entities.add(portal8);

        dungeon.setEntities(entities);
        dungeon.addEnemy(hydra);

        List<Position> possibleTeleportedLocations = new ArrayList<>();
        possibleTeleportedLocations.add(portal1.getCorrespondingSpawnPoint(dungeon));
        possibleTeleportedLocations.add(portal3.getCorrespondingSpawnPoint(dungeon));
        possibleTeleportedLocations.add(portal5.getCorrespondingSpawnPoint(dungeon));
        possibleTeleportedLocations.add(portal7.getCorrespondingSpawnPoint(dungeon));
        
        dungeon.getEnemies().forEach(Enemy::move);
        assertTrue(possibleTeleportedLocations.stream().anyMatch(x->Objects.equals(x, hydra.getPosition())));
    }


    @Test
    public void testHydraSpawning(){
        // 51 ticks
        DungeonManiaController dungeonManiaController = new DungeonManiaController();
        dungeonManiaController.newGame("advanced", DungeonConstants.MODE_HARD);
        DungeonResponse dungeonResponse;
        
        for (int i = 0; i < 17; i++) {
            dungeonManiaController.tick(null, Direction.RIGHT);
        }
        for (int i = 0; i < 15; i++) {
            dungeonManiaController.tick(null, Direction.DOWN);
        }
        for (int i = 0; i < 16; i++) {
            dungeonManiaController.tick(null, Direction.LEFT);
        }
       
        dungeonResponse = dungeonManiaController.tick(null, Direction.LEFT);
        for (EntityResponse entityResponse: dungeonResponse.getEntities()) {
            assertNotEquals(DungeonConstants.ENTITY_HYDRA, entityResponse.getType());
        }
        
        boolean hydraFound = false;
        dungeonManiaController.tick(null, Direction.UP);
        dungeonResponse = dungeonManiaController.tick(null, Direction.UP);
        for(EntityResponse entityResponse : dungeonResponse.getEntities()){
            if(entityResponse.getType().equals(DungeonConstants.ENTITY_HYDRA)){
                hydraFound = true;
                break;
            }
        }
        assertEquals(true, hydraFound);
    }
    
    @Test
    public void testCannotSpawnInPeaceful(){
        DungeonManiaController dungeonManiaController = new DungeonManiaController();
        dungeonManiaController.newGame("advanced", DungeonConstants.MODE_PEACEFUL);
        DungeonResponse dungeonResponse;
        
        for (int i = 0; i < 17; i++) {
            dungeonManiaController.tick(null, Direction.RIGHT);
        }
        for (int i = 0; i < 15; i++) {
            dungeonManiaController.tick(null, Direction.DOWN);
        }
        for (int i = 0; i < 17; i++) {
            dungeonManiaController.tick(null, Direction.LEFT);
        }
       
        dungeonManiaController.tick(null, Direction.UP);
        dungeonResponse = dungeonManiaController.tick(null, Direction.UP);
        
        for (EntityResponse entityResponse: dungeonResponse.getEntities()) {
            assertNotEquals(DungeonConstants.ENTITY_HYDRA, entityResponse.getType());
        }
    }
    
    @Test
    public void testCannotSpawnInStandard(){
        DungeonManiaController dungeonManiaController = new DungeonManiaController();
        dungeonManiaController.newGame("advanced", DungeonConstants.MODE_STANDARD);
        DungeonResponse dungeonResponse;
        
        for (int i = 0; i < 17; i++) {
            dungeonManiaController.tick(null, Direction.RIGHT);
        }
        for (int i = 0; i < 15; i++) {
            dungeonManiaController.tick(null, Direction.DOWN);
        }
        for (int i = 0; i < 17; i++) {
            dungeonManiaController.tick(null, Direction.LEFT);
        }
       
        dungeonManiaController.tick(null, Direction.UP);
        dungeonResponse = dungeonManiaController.tick(null, Direction.UP);
        
        for (EntityResponse entityResponse: dungeonResponse.getEntities()) {
            assertNotEquals(DungeonConstants.ENTITY_HYDRA, entityResponse.getType());
        }
    }

    @Test
    public void testHydraAttacked(){
        String dungeonId = "dungeon1";
        String name = "maze";
        String hydraId = "Hydra1";
        String playerId = "p1";
        Position hydraInitialPosition = new Position(1, 1);
        Position playerInitialPosition = new Position(1, 2);

        Dungeon dungeon = new Dungeon(new DungeonInfo(50, 50, dungeonId, name, DungeonConstants.MODE_HARD, new ExitGoal()));
        Hydra hydra = new Hydra(dungeon, new EntityInfo(hydraId, hydraInitialPosition, 
        DungeonConstants.ENTITY_HYDRA, false));
        Player player= new Player(dungeon, new EntityInfo(playerId, playerInitialPosition,
        DungeonConstants.ENTITY_PLAYER, false));


        List<Entity> entities = new ArrayList<>();
        entities.add(hydra);
        entities.add(player);

        dungeon.setEntities(entities);
        dungeon.addEnemy(hydra);
        dungeon.addEntity(player);

        Direction moveDirection = Direction.UP;
        player.move(moveDirection);

        assertEquals(new Position(1, 1), player.getPosition());
        assertEquals(80, player.getHealth());
        assertEquals(100, hydra.getHealth());

        while (Battle.canFight(player, hydra)) {
            Battle.fight(dungeon, player, hydra);
        }
        
        assertEquals(true, hydra.isAlive());
        assertEquals(false, player.isAlive());

        assertEquals(true, player.getAttackDamage() == 2);
        assertEquals(true, hydra.getAttackDamage() == 15);

        assertEquals(true, player.getHealth() == -22 || player.getHealth() == -118);
        assertEquals(true, hydra.getHealth() == 68 || hydra.getHealth() == 132);

        player.setHealth(80);
        hydra.setHealth(100);
        assertEquals(80, player.getHealth());
        assertEquals(true, player.isAlive());

        player.move(Direction.RIGHT);
        player.move(Direction.LEFT);

        assertEquals(new Position(1, 1), player.getPosition());

        while (Battle.canFight(player, hydra)) {
            Battle.fight(dungeon, player, hydra);
        }

        assertEquals(true, player.getHealth() == -22 || player.getHealth() == -118);
        assertEquals(true, hydra.getHealth() == 68 || hydra.getHealth() == 132);

    }
}
