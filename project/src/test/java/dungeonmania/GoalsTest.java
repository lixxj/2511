package dungeonmania;

import dungeonmania.entities.*;
import dungeonmania.entities.collectables.*;
import dungeonmania.entities.moving.enemies.*;
import dungeonmania.entities.moving.player.Player;
import dungeonmania.entities.staticitems.*;
import dungeonmania.dungeon.*;
import dungeonmania.goals.*;
import dungeonmania.util.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GoalsTest {

    @Test
    public void testExitGoal() {
        String dungeonId = "dungeon1";
        String name = "maze";
        String playerId = "Batman";
        Dungeon dungeon = new Dungeon(new DungeonInfo(50, 50, dungeonId, name, DungeonConstants.MODE_PEACEFUL, new ExitGoal()));
        Exit exit = new Exit(new EntityInfo("exit", new Position(1, 1), 
        DungeonConstants.ENTITY_EXIT, false));
        Player player = new Player(dungeon, new EntityInfo(playerId, new Position(1, 2), 
        DungeonConstants.ENTITY_PLAYER, false));
        dungeon.setPlayer(player);

        List<Entity> entities = new ArrayList<>();
        entities.add(exit);
        entities.add(player);
        
        dungeon.setEntities(entities);
        dungeon.setPlayer(player);

        assertEquals(":exit", dungeon.getGoalsAsString());
        dungeon.tick(null, Direction.UP);
        assertEquals("", dungeon.getGoalsAsString());
    }

    @Test
    public void testEnemiesGoal() {
        String dungeonId = "dungeon1";
        String name = "maze";
        String playerId = "Batman";
        Dungeon dungeon = new Dungeon(new DungeonInfo(50, 50, dungeonId, name, DungeonConstants.MODE_PEACEFUL, new EnemiesGoal()));

        Player player = new Player(dungeon, new EntityInfo(playerId, new Position(1, 2), 
        DungeonConstants.ENTITY_PLAYER, false));
        dungeon.setPlayer(player);
        Spider spider = new Spider(dungeon, new EntityInfo("spider0", new Position(1, 1), 
        DungeonConstants.ENTITY_SPIDER, false));

        List<Entity> entities = new ArrayList<>();
        entities.add(spider);
        entities.add(player);

        dungeon.setEntities(entities);

        assertEquals(":enemies", dungeon.getGoalsAsString());
        dungeon.removeEntity(spider);

        assertEquals("", dungeon.getGoalsAsString());
    }

    @Test
    public void testBoulderGoal() {
        String dungeonId = "dungeon1";
        String name = "maze";
        String playerId = "Batman";
        Dungeon dungeon = new Dungeon(new DungeonInfo(50, 50, dungeonId, name, DungeonConstants.MODE_PEACEFUL, new BoulderGoal()));
        Player player = new Player(dungeon, new EntityInfo(playerId, new Position(1, 2), 
        DungeonConstants.ENTITY_PLAYER, false));
        dungeon.setPlayer(player);

        FloorSwitch floorSwitch = new FloorSwitch(new EntityInfo("switch", new Position(1, 0), 
                                DungeonConstants.ENTITY_FLOOR_SWITCH, false));
        Boulder boulder = new Boulder(new EntityInfo("boulder", new Position(1, 1), 
        DungeonConstants.ENTITY_BOULDER, false));

        List<Entity> entities = new ArrayList<>();
        entities.add(boulder);
        entities.add(floorSwitch);

        dungeon.setEntities(entities);
        dungeon.setPlayer(player);

        assertEquals(":boulder", dungeon.getGoalsAsString());
        dungeon.tick(null, Direction.UP);
        assertEquals("", dungeon.getGoalsAsString());
    }

    @Test
    public void testTreasureGoal() {
        String dungeonId = "dungeon1";
        String name = "maze";
        String playerId = "Batman";
        Dungeon dungeon = new Dungeon(new DungeonInfo(50, 50, dungeonId, name, DungeonConstants.MODE_PEACEFUL, new TreasureGoal()));
        Treasure treasure = new Treasure(new EntityInfo("treasure", new Position(1, 1), 
        DungeonConstants.ENTITY_FLOOR_SWITCH, true));
        Player player = new Player(dungeon, new EntityInfo(playerId, new Position(1, 2), 
        DungeonConstants.ENTITY_PLAYER, false));
        dungeon.setPlayer(player);

        List<Entity> entities = new ArrayList<>();
        entities.add(treasure);
        entities.add(player);

        dungeon.setEntities(entities);

        assertEquals(":treasure", dungeon.getGoalsAsString());
        dungeon.tick(null, Direction.UP);
        assertEquals("", dungeon.getGoalsAsString());
    }

    @Test
    public void testAndGoal() {
        String dungeonId = "dungeon1";
        String name = "maze";
        String playerId = "Batman";
        AndGoal andGoal = new AndGoal(new TreasureGoal(), new BoulderGoal());
        Dungeon dungeon = new Dungeon(new DungeonInfo(50, 50, dungeonId, name, DungeonConstants.MODE_PEACEFUL, andGoal));

        FloorSwitch floorSwitch = new FloorSwitch(new EntityInfo("switch", new Position(0, 0), 
                                DungeonConstants.ENTITY_FLOOR_SWITCH, false));
        Boulder boulder = new Boulder(new EntityInfo("boulder", new Position(0, 1), 
        DungeonConstants.ENTITY_BOULDER, false));
        Treasure treasure = new Treasure(new EntityInfo("treasure", new Position(0, 2), 
        DungeonConstants.ENTITY_FLOOR_SWITCH, true));

        Player player = new Player(dungeon, new EntityInfo(playerId, new Position(0, 3), 
        DungeonConstants.ENTITY_PLAYER, false));
        dungeon.setPlayer(player);

        List<Entity> entities = new ArrayList<>();
        entities.add(treasure);
        entities.add(player);
        entities.add(boulder);
        entities.add(floorSwitch);

        dungeon.setEntities(entities);

        assertEquals(":treasure AND :boulder", dungeon.getGoalsAsString());
        dungeon.tick(null, Direction.UP);
        assertEquals(":treasure AND :boulder", dungeon.getGoalsAsString());
        dungeon.tick(null, Direction.UP);
        assertEquals("", dungeon.getGoalsAsString());
    }

    @Test
    public void testOrGoal() {
        String dungeonId = "dungeon1";
        String name = "maze";
        String playerId = "Batman";
        OrGoal orGoal = new OrGoal(new TreasureGoal(), new BoulderGoal());

        Dungeon dungeon = new Dungeon(new DungeonInfo(50, 50, dungeonId, name, DungeonConstants.MODE_PEACEFUL, orGoal));

        FloorSwitch floorSwitch = new FloorSwitch(new EntityInfo("switch", new Position(0, 0), 
                                DungeonConstants.ENTITY_FLOOR_SWITCH, false));
        Boulder boulder = new Boulder(new EntityInfo("boulder", new Position(0, 1), 
        DungeonConstants.ENTITY_BOULDER, false));
        Treasure treasure = new Treasure(new EntityInfo("treasure", new Position(0, 2), 
        DungeonConstants.ENTITY_FLOOR_SWITCH, true));

        Player player = new Player(dungeon, new EntityInfo(playerId, new Position(0, 3), 
        DungeonConstants.ENTITY_PLAYER, false));
        dungeon.setPlayer(player);

        List<Entity> entities = new ArrayList<>();
        entities.add(treasure);
        entities.add(player);
        entities.add(boulder);
        entities.add(floorSwitch);

        dungeon.setEntities(entities);

        assertEquals(":treasure OR :boulder", dungeon.getGoalsAsString());
        dungeon.tick(null, Direction.UP);
        assertEquals("", dungeon.getGoalsAsString());
    }

    @Test
    public void testAndWithAndGoals() {
        String dungeonId = "dungeon1";
        String name = "maze";
        String playerId = "Batman";

        AndGoal andGoalChild = new AndGoal(new BoulderGoal(), new ExitGoal());
        AndGoal andGoalParent = new AndGoal(new TreasureGoal(), andGoalChild);

        Dungeon dungeon = new Dungeon(new DungeonInfo(50, 50, dungeonId, name, DungeonConstants.MODE_PEACEFUL, andGoalParent));

        FloorSwitch floorSwitch = new FloorSwitch(new EntityInfo("switch", new Position(0, 0), 
                                DungeonConstants.ENTITY_FLOOR_SWITCH, false));
        Boulder boulder = new Boulder(new EntityInfo("boulder", new Position(0, 1), 
        DungeonConstants.ENTITY_BOULDER, false));
        Treasure treasure = new Treasure(new EntityInfo("treasure", new Position(0, 2), 
        DungeonConstants.ENTITY_FLOOR_SWITCH, true));

        Player player = new Player(dungeon, new EntityInfo(playerId, new Position(0, 3), 
        DungeonConstants.ENTITY_PLAYER, false));
        dungeon.setPlayer(player);

        Exit exit = new Exit(new EntityInfo("exit", new Position(1, 1), 
        DungeonConstants.ENTITY_EXIT, false));

        List<Entity> entities = new ArrayList<>();
        entities.add(treasure);
        entities.add(player);
        entities.add(boulder);
        entities.add(floorSwitch);
        entities.add(exit);

        dungeon.setEntities(entities);

        assertEquals(":treasure AND :boulder AND :exit", dungeon.getGoalsAsString());
        dungeon.tick(null, Direction.UP);
        assertEquals(":treasure AND :boulder AND :exit", dungeon.getGoalsAsString());
        dungeon.tick(null, Direction.UP);
        assertEquals(":treasure AND :boulder AND :exit", dungeon.getGoalsAsString());
        dungeon.tick(null, Direction.RIGHT);

        assertEquals("", dungeon.getGoalsAsString());
    }

    @Test
    public void testAndWithOrGoal() {
        String dungeonId = "dungeon1";
        String name = "maze";
        String playerId = "Batman";

        OrGoal orGoalChild = new OrGoal(new ExitGoal(), new BoulderGoal());
        AndGoal andGoalParent = new AndGoal(new TreasureGoal(), orGoalChild);

        Dungeon dungeon = new Dungeon(new DungeonInfo(50, 50, dungeonId, name, DungeonConstants.MODE_PEACEFUL, andGoalParent));

        FloorSwitch floorSwitch = new FloorSwitch(new EntityInfo("switch", new Position(0, 0), 
                                DungeonConstants.ENTITY_FLOOR_SWITCH, false));
        Boulder boulder = new Boulder(new EntityInfo("boulder", new Position(0, 1), 
        DungeonConstants.ENTITY_BOULDER, false));
        Treasure treasure = new Treasure(new EntityInfo("treasure", new Position(0, 2), 
        DungeonConstants.ENTITY_FLOOR_SWITCH, true));

        Player player = new Player(dungeon, new EntityInfo(playerId, new Position(0, 3), 
        DungeonConstants.ENTITY_PLAYER, false));
        dungeon.setPlayer(player);

        Exit exit = new Exit(new EntityInfo("exit", new Position(1, 2), 
        DungeonConstants.ENTITY_EXIT, false));


        List<Entity> entities = new ArrayList<>();
        entities.add(treasure);
        entities.add(player);
        entities.add(boulder);
        entities.add(floorSwitch);
        entities.add(exit);

        dungeon.setEntities(entities);
        dungeon.setPlayer(player);

        assertEquals(":treasure AND :exit OR :boulder", dungeon.getGoalsAsString());
        dungeon.tick(null, Direction.UP);
        assertEquals(":treasure AND :exit OR :boulder", dungeon.getGoalsAsString());
        dungeon.tick(null, Direction.RIGHT);

        assertEquals("", dungeon.getGoalsAsString());
    }


}
