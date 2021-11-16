package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


import java.util.ArrayList;
import java.util.List;

import dungeonmania.entities.moving.enemies.Mercenary;
import dungeonmania.entities.moving.enemies.Spider;
import dungeonmania.entities.moving.enemies.ZombieToast;
import org.junit.jupiter.api.Test;

import dungeonmania.entities.DungeonConstants;
import dungeonmania.entities.Entity;
import dungeonmania.entities.EntityInfo;
import dungeonmania.entities.moving.player.Player;
import dungeonmania.entities.staticitems.SwampTile;
import dungeonmania.dungeon.Dungeon;
import dungeonmania.dungeon.DungeonInfo;
import dungeonmania.goals.EnemiesGoal;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class SwampTest {
       
    // Spider
    @Test
    public void testSpiderSwampMovement() {
        String dungeonId = "dungeon1";
        String name = "maze";

        Dungeon dungeon = new Dungeon(new DungeonInfo(50, 50, dungeonId, name, DungeonConstants.MODE_STANDARD, new EnemiesGoal()));
        Spider mySpider = new Spider(dungeon, new EntityInfo("Spider1", new Position(5, 5),
            DungeonConstants.ENTITY_SPIDER, false));
        SwampTile swamp = new SwampTile(new EntityInfo("shrek_swamp", new Position(6, 4), DungeonConstants.ENTITY_SWAMP_TILE, false), 4);
    
        List<Entity> entities = new ArrayList<>();
        entities.add(swamp);
        dungeon.setEntities(entities);
    
        // Spider moves up
        mySpider.move();
        assertEquals(mySpider.getPosition(), new Position(5, 4));
    
        // Spider moves onto swamp
        mySpider.move();
        assertEquals(mySpider.getPosition(), new Position(6, 4));
    
        // Spider stays on swamp for 3 ticks (it takes 3 extra ticks to cross swamp)
        mySpider.move();
        mySpider.move();
        mySpider.move();
        assertEquals(mySpider.getPosition(), new Position(6, 4));

        mySpider.move();
        assertEquals(mySpider.getPosition(), new Position(6, 5));
    }
    
    // Mercenary
    @Test
    public void testMercenarySwampMovement() {
        String dungeonId = "dungeon1";
        String name = "maze";

        Dungeon dungeon = new Dungeon(new DungeonInfo(50, 50, dungeonId, name, DungeonConstants.MODE_STANDARD, new EnemiesGoal()));
                
        Player player = new Player(dungeon, new EntityInfo("player", new Position(1, 3), 
        DungeonConstants.ENTITY_PLAYER, false));
        dungeon.setPlayer(player);
        Mercenary mercenary = new Mercenary(dungeon, new EntityInfo("merc", new Position(1, 2),
            DungeonConstants.ENTITY_MERCENARY, false));
        SwampTile swamp = new SwampTile(new EntityInfo("shrek_swamp", new Position(1, 4), DungeonConstants.ENTITY_SWAMP_TILE, false), 2);

        List<Entity> entities = new ArrayList<>();
        entities.add(swamp);
        dungeon.setEntities(entities);

        // Player moves onto swamp
        player.move(Direction.DOWN);
        assertEquals(player.getPosition(), new Position(1, 4));

        // Player is unaffected by swamp
        player.move(Direction.DOWN);
        assertEquals(player.getPosition(), new Position(1, 5));

        // Mercenary moves onto swamp
        mercenary.move();
        mercenary.move();
        assertEquals(mercenary.getPosition(), new Position(1, 4));

        // Mercenary stays for another tick (takes 2 ticks to cross swamp)
        mercenary.move();
        assertEquals(mercenary.getPosition(), new Position(1, 4));

        // Mercenary can cross swamp
        mercenary.move();
        assertEquals(mercenary.getPosition(), new Position(1, 5));
    }
    
    // ZombieToast
    @Test
    public void testZombieToastSwampMovement() {
        String dungeonId = "dungeon1";
        String name = "maze";

        Dungeon dungeon = new Dungeon(new DungeonInfo(50, 50, dungeonId, name, DungeonConstants.MODE_STANDARD, new EnemiesGoal()));
        ZombieToast myZombie = new ZombieToast(dungeon, new EntityInfo("zombie1", new Position(1, 4),
        DungeonConstants.ENTITY_ZOMBIE_TOAST, false));
        SwampTile swamp1 = new SwampTile(new EntityInfo("shrek_swamp", new Position(0, 4), DungeonConstants.ENTITY_SWAMP_TILE, false), 2);
        SwampTile swamp2 = new SwampTile(new EntityInfo("shrek_swamp", new Position(2, 4), DungeonConstants.ENTITY_SWAMP_TILE, false), 2);
        SwampTile swamp3 = new SwampTile(new EntityInfo("shrek_swamp", new Position(1, 5), DungeonConstants.ENTITY_SWAMP_TILE, false), 2);
        SwampTile swamp4 = new SwampTile(new EntityInfo("shrek_swamp", new Position(1, 3), DungeonConstants.ENTITY_SWAMP_TILE, false), 2);

        List<Entity> entities = new ArrayList<>();
        entities.add(swamp1);
        entities.add(swamp2);
        entities.add(swamp3);
        entities.add(swamp4);

        dungeon.setEntities(entities);
    
        // Zombie is already on swamp and stays on it for 1 extra tick (movement factor is 2)
        myZombie.move();
        Position zombiePosition = new Position(myZombie.getPosition().getX(), myZombie.getPosition().getY()) ;

        myZombie.move();
        assertEquals(myZombie.getPosition(), zombiePosition);
        // Zombie can now move again
        myZombie.move();
        assertNotEquals(myZombie.getPosition(), zombiePosition);
    }

}