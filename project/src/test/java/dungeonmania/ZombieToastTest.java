package dungeonmania;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import dungeonmania.entities.*;
import dungeonmania.entities.moving.enemies.*;
import dungeonmania.entities.staticitems.*;
import dungeonmania.dungeon.*;
import dungeonmania.goals.ExitGoal;
import org.junit.jupiter.api.Test;

import dungeonmania.util.Position;

public class ZombieToastTest {

    @Test
    public void testMovement() {

        String dungeonId = "dungeon1";
        String name = "maze";
        String zombieId = "First";

        Dungeon dungeon = new Dungeon(new DungeonInfo(50, 50, dungeonId, name, DungeonConstants.MODE_PEACEFUL, new ExitGoal()));

        Position initialPosition = new Position(0,0);
        ZombieToast myZombie = new ZombieToast(dungeon, new EntityInfo(zombieId, initialPosition, 
        DungeonConstants.ENTITY_ZOMBIE_TOAST, false));

        myZombie.move();
        assertTrue(initialPosition.getAdjacentPositions()
                    .stream()
                    .anyMatch(x -> Objects.equals(x, myZombie.getPosition())));

        int movesRemaining = 10;
        while (movesRemaining >= 0) {
            initialPosition = myZombie.getPosition();
            myZombie.move();
        
            assertTrue(initialPosition.getAdjacentPositions()
                        .stream()
                        .anyMatch(x -> Objects.equals(x, myZombie.getPosition())));

            movesRemaining--;
        }
    }   

    @Test
    public void testCannotMoveRandom() {
        String dungeonId = "dungeon1";
        String name = "maze";
        String zombieId = "dead_boy";
        Position initialPosition = new Position(0, 0);
        Dungeon dungeon = new Dungeon(new DungeonInfo(50, 50, dungeonId, name, DungeonConstants.MODE_PEACEFUL, new ExitGoal()));
        ZombieToast zombie = new ZombieToast(dungeon, new EntityInfo(zombieId, initialPosition, 
        DungeonConstants.ENTITY_ZOMBIE_TOAST, false));

        // Create obstacles
        Door door = new Door(new EntityInfo("door", new Position(1, 0), DungeonConstants.ENTITY_DOOR, false), 1);
        ZombieToastSpawner zombieToastSpawner = new ZombieToastSpawner(new EntityInfo("toaster", 
                            new Position(-1, 0), DungeonConstants.ENTITY_ZOMBIE_TOAST_SPAWNER, false));
        Boulder boulder = new Boulder(new EntityInfo("bouldy", 
                            new Position(0, -1), DungeonConstants.ENTITY_BOULDER, false));
        Wall wall = new Wall(new EntityInfo("wally", 
                    new Position(0, 1), DungeonConstants.ENTITY_WALL, false));

        List<Entity> entities = new ArrayList<>();
        entities.add(zombie);
        entities.add(door);
        entities.add(zombieToastSpawner);
        entities.add(boulder);
        entities.add(wall);

        dungeon.setEntities(entities);
        dungeon.addEnemy(zombie);
        
        // Zombie cannot move
        dungeon.getEnemies().forEach(Enemy::move);

        assertEquals(initialPosition, zombie.getPosition());

    }

    @Test
    public void testTeleport() {
        String dungeonId = "dungeon1";
        String name = "maze";
        String zombieId = "badboy";
        Position initialPosition = new Position(0, 0);
        Dungeon dungeon = new Dungeon(new DungeonInfo(50, 50, dungeonId, name, DungeonConstants.MODE_PEACEFUL, new ExitGoal()));
        ZombieToast myZombie = new ZombieToast(dungeon, new EntityInfo(zombieId, initialPosition, 
        DungeonConstants.ENTITY_ZOMBIE_TOAST, false));
        
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
        entities.add(myZombie);
        entities.add(portal1);
        entities.add(portal2);
        entities.add(portal3);
        entities.add(portal4);
        entities.add(portal5);
        entities.add(portal6);
        entities.add(portal7);
        entities.add(portal8);

        dungeon.setEntities(entities);
        dungeon.addEnemy(myZombie);

        List<Position> possibleTeleportedLocations = new ArrayList<>();
        possibleTeleportedLocations.add(portal1.getCorrespondingSpawnPoint(dungeon));
        possibleTeleportedLocations.add(portal3.getCorrespondingSpawnPoint(dungeon));
        possibleTeleportedLocations.add(portal5.getCorrespondingSpawnPoint(dungeon));
        possibleTeleportedLocations.add(portal7.getCorrespondingSpawnPoint(dungeon));
        
        // Zombie is teleported
        dungeon.getEnemies().forEach(Enemy::move);
        // assertFalse(initialPosition.getAdjacentPositions().stream().anyMatch(x -> Objects.equals(x, myZombie.getPosition())));
        assertTrue(possibleTeleportedLocations.stream().anyMatch(x->Objects.equals(x, myZombie.getPosition())));

    }
}
