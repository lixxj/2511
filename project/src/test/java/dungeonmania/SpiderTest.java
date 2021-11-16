package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.entities.*;
import dungeonmania.entities.moving.enemies.*;
import dungeonmania.entities.staticitems.*;
import dungeonmania.dungeon.*;
import dungeonmania.goals.ExitGoal;
import org.junit.jupiter.api.Test;

import dungeonmania.util.Position;

public class SpiderTest {


    @Test
    public void testClockwiseMovement() {

        String dungeonId = "dungeon1";
        String name = "maze";
        String spiderId = "First";
        Dungeon dungeon = new Dungeon(new DungeonInfo(50, 50, dungeonId, name, DungeonConstants.MODE_PEACEFUL, new ExitGoal()));
        Spider mySpider = new Spider(dungeon, new EntityInfo(spiderId, new Position(5, 5), 
            DungeonConstants.ENTITY_SPIDER, false));

        mySpider.move(); // Move Up
        assertEquals(new Position(5, 4), mySpider.getPosition());

        mySpider.move(); // Move Right
        assertEquals(new Position(6, 4), mySpider.getPosition());
        
        mySpider.move(); // Move Down
        assertEquals(new Position(6, 5), mySpider.getPosition());

        mySpider.move(); // Move Down
        assertEquals(new Position(6, 6), mySpider.getPosition());
               
        mySpider.move(); // Move Left
        assertEquals(new Position(5, 6), mySpider.getPosition());  
        
        mySpider.move(); // Move Left
        assertEquals(new Position(4, 6), mySpider.getPosition());
        
        mySpider.move(); // Move Up
        assertEquals(new Position(4, 5), mySpider.getPosition());

        mySpider.move(); // Move Up
        assertEquals(new Position(4, 4), mySpider.getPosition());

        mySpider.move(); // Move Right
        assertEquals(new Position(5, 4), mySpider.getPosition());    
        
    }

    @Test
    public void testCannotMoveCircle() {
        String dungeonId = "dungeon1";
        String name = "maze";
        String spiderId = "tarantula";
        Position initialPosition = new Position(2, 2);

        Dungeon dungeon = new Dungeon(new DungeonInfo(50, 50, dungeonId, name, DungeonConstants.MODE_PEACEFUL, new ExitGoal()));
        Spider spider = new Spider(dungeon, new EntityInfo(spiderId, initialPosition, 
        DungeonConstants.ENTITY_SPIDER, false));

        Boulder boulder1 = new Boulder(new EntityInfo("bouldy0", 
                            new Position(1, 2), DungeonConstants.ENTITY_BOULDER, false));
        Boulder boulder2 = new Boulder(new EntityInfo("bouldy1", 
                            new Position(3, 2), DungeonConstants.ENTITY_BOULDER, false));
        Boulder boulder3 = new Boulder(new EntityInfo("bouldy2", 
                            new Position(2, 3), DungeonConstants.ENTITY_BOULDER, false));
        Boulder boulder4 = new Boulder(new EntityInfo("bouldy3", 
                            new Position(2, 1), DungeonConstants.ENTITY_BOULDER, false));

        List<Entity> entities = new ArrayList<>();
        entities.add(spider);
        entities.add(boulder1);
        entities.add(boulder2);
        entities.add(boulder3);
        entities.add(boulder4);

        dungeon.setEntities(entities);
        dungeon.addEnemy(spider);
        
        // Spider cannot move
        dungeon.getEnemies().forEach(Enemy::move);

        assertEquals(initialPosition, spider.getPosition());

    }

    @Test
    public void testTeleport() {
        String dungeonId = "dungeon1";
        String name = "maze";
        String spiderId = "First";
        Dungeon dungeon = new Dungeon(new DungeonInfo(50, 50, dungeonId, name, DungeonConstants.MODE_PEACEFUL, new ExitGoal()));
        Spider mySpider = new Spider(dungeon, new EntityInfo(spiderId, new Position(0, 5), 
            DungeonConstants.ENTITY_SPIDER, false));

        Portal portal1 = new Portal(new EntityInfo("portal1", new Position(0, 4), 
                        DungeonConstants.ENTITY_PORTAL, false), "BLUE");
        Portal portal2 = new Portal(new EntityInfo("portal2", new Position(0, 2), 
        DungeonConstants.ENTITY_PORTAL, false), "BLUE");
        
        List<Entity> entities = new ArrayList<>();
        entities.add(mySpider);
        entities.add(portal1);
        entities.add(portal2);

        dungeon.setEntities(entities);
        dungeon.addEnemy(mySpider);

        // Spider should be teleported
        dungeon.getEnemies().forEach(Enemy::move);
        assertEquals(new Position(0, 1), mySpider.getPosition());

        dungeon.getEnemies().forEach(Enemy::move);
        assertEquals(new Position(0, 0), mySpider.getPosition());

        dungeon.getEnemies().forEach(Enemy::move);
        assertEquals(new Position(1, 0), mySpider.getPosition());
    }

    @Test
    public void testBoulderReverse() {
        String dungeonId = "dungeon1";
        String name = "maze";
        String spiderId = "First";
        Dungeon dungeon = new Dungeon(new DungeonInfo(50, 50, dungeonId, name, DungeonConstants.MODE_PEACEFUL, new ExitGoal()));
        Spider mySpider = new Spider(dungeon, new EntityInfo(spiderId, new Position(2, 5), 
        DungeonConstants.ENTITY_SPIDER, false));

        Boulder boulder = new Boulder(new EntityInfo("bouldy", 
                            new Position(3, 4), DungeonConstants.ENTITY_BOULDER, false));
        
        List<Entity> entities = new ArrayList<>();
        entities.add(mySpider);
        entities.add(boulder);

        dungeon.setEntities(entities);

        // Spider should be teleported
        mySpider.move();
        assertEquals(new Position(2, 4), mySpider.getPosition());

        // Hits a boulder so it does not move
        mySpider.move();
        assertEquals(new Position(2, 4), mySpider.getPosition());

        // Reverses direction
        mySpider.move(); // Move Left
        assertEquals(new Position(1, 4), mySpider.getPosition());

        mySpider.move(); // Move Down
        assertEquals(new Position(1, 5), mySpider.getPosition());

        mySpider.move(); // Move Down
        assertEquals(new Position(1, 6), mySpider.getPosition());

        mySpider.move(); // Move Right
        assertEquals(new Position(2, 6), mySpider.getPosition());

        mySpider.move(); // Move Right
        assertEquals(new Position(3, 6), mySpider.getPosition());

        mySpider.move(); // Move Up
        assertEquals(new Position(3, 5), mySpider.getPosition());

        mySpider.move(); // Hits a boulder so it does not move
        assertEquals(new Position(3, 5), mySpider.getPosition());
    }
}
