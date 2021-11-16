package dungeonmania;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Objects;

import dungeonmania.entities.*;
import dungeonmania.entities.buildables.*;
import dungeonmania.entities.collectables.*;
import dungeonmania.entities.moving.player.Player;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.dungeon.*;
import dungeonmania.goals.ExitGoal;

import dungeonmania.util.Direction;
import dungeonmania.util.Position;

import org.junit.jupiter.api.Test;

public class PlayerTest {

    @Test
    public void testCreate() {
        String dungeonId = "dungeon1";
        String name = "maze";
        String playerId = "Batman";
        Dungeon dungeon = new Dungeon(new DungeonInfo(50, 50, dungeonId, name, DungeonConstants.MODE_PEACEFUL, new ExitGoal()));

        Player easyPlayer = new Player(dungeon, new EntityInfo(playerId, new Position(0, 0), 
        DungeonConstants.ENTITY_PLAYER, false));
        dungeon.setPlayer(easyPlayer);

        assertEquals(100, easyPlayer.getHealth());

        dungeon = new Dungeon(new DungeonInfo(50, 50, dungeonId, name, DungeonConstants.MODE_HARD, new ExitGoal()));

        Player hardPlayer = new Player(dungeon, new EntityInfo(playerId, new Position(0, 0), 
        DungeonConstants.ENTITY_PLAYER, false));

        assertEquals(80, hardPlayer.getHealth());

        assertEquals(true, easyPlayer.isAlive());
    }

    @Test
    public void testPickUpKey() {
        String dungeonId = "dungeon1";
        String name = "maze";
        String playerId = "Batman";

        Dungeon dungeon = new Dungeon(new DungeonInfo(50, 50, dungeonId, name, DungeonConstants.MODE_PEACEFUL, new ExitGoal()));

        Player myPlayer = new Player(dungeon, new EntityInfo(playerId, new Position(0, 0), 
        DungeonConstants.ENTITY_PLAYER, false));
        dungeon.setPlayer(myPlayer);

        Key key = new Key(new EntityInfo("key0", new Position(0, 0), DungeonConstants.ENTITY_KEY, true), 1);
        myPlayer.addItemToInventory(key);
        assertEquals(true, myPlayer.checkItemExists("key0"));
    }

    @Test
    public void testCannotPickUpTwoKeys() {
        String dungeonId = "dungeon1";
        String name = "maze";
        String playerId = "Batman";

        Dungeon dungeon = new Dungeon(new DungeonInfo(50, 50, dungeonId, name, DungeonConstants.MODE_PEACEFUL, new ExitGoal()));

        Player myPlayer = new Player(dungeon, new EntityInfo(playerId, new Position(0, 0), 
        DungeonConstants.ENTITY_PLAYER, false));
        dungeon.setPlayer(myPlayer);

        Key key1 = new Key(new EntityInfo("key1", new Position(1, 0), DungeonConstants.ENTITY_KEY, true), 1);
        dungeon.addEntity(key1);
        Key key2 = new Key(new EntityInfo("key2", new Position(2, 0), DungeonConstants.ENTITY_KEY, true), 2);
        dungeon.addEntity(key2);

        // First key is collected
        dungeon.tick(null, Direction.RIGHT);
        assertEquals(true, myPlayer.checkItemExists("key1"));
        assertFalse(dungeon.getEntitiesAtPosition(new Position(1, 0)).stream().anyMatch(x -> Objects.equals(x.getType(), DungeonConstants.ENTITY_KEY)));

        // Second key is not collected
        dungeon.tick(null, Direction.RIGHT);
        assertEquals(false, myPlayer.checkItemExists("key2"));
        assertTrue(dungeon.getEntitiesAtPosition(new Position(2, 0)).stream().anyMatch(x -> Objects.equals(x.getType(), DungeonConstants.ENTITY_KEY)));

    }

    @Test
    public void testBuildBow() {

        String dungeonId = "dungeon1";
        String name = "maze";
        String playerId = "Batman";

        Dungeon dungeon = new Dungeon(new DungeonInfo(50, 50, dungeonId, name, DungeonConstants.MODE_PEACEFUL, new ExitGoal()));

        Player myPlayer = new Player(dungeon, new EntityInfo(playerId, new Position(0, 0), 
        DungeonConstants.ENTITY_PLAYER, false));
        dungeon.setPlayer(myPlayer);

        // Add ingredients of bow to player's inventory
        myPlayer.addItemToInventory(new Wood(new EntityInfo("wood0", new Position(0, 0), DungeonConstants.ENTITY_WOOD, false)));
        myPlayer.addItemToInventory(new Arrow(new EntityInfo("arrow0", new Position(0, 0), DungeonConstants.ENTITY_ARROW, false)));
        myPlayer.addItemToInventory(new Arrow(new EntityInfo("arrow1", new Position(0, 0), DungeonConstants.ENTITY_ARROW, false)));
        myPlayer.addItemToInventory(new Arrow(new EntityInfo("arrow2", new Position(0, 0), DungeonConstants.ENTITY_ARROW, false)));

        Entity itemCreated = dungeon.build(DungeonConstants.ENTITY_BOW);
        assertEquals(true, itemCreated instanceof Bow);
    }

    @Test
    public void testBuildShield() {

        String dungeonId = "dungeon1";
        String name = "maze";
        String playerId = "Batman";
        Dungeon dungeon = new Dungeon(new DungeonInfo(50, 50, dungeonId, name, DungeonConstants.MODE_PEACEFUL, new ExitGoal()));

        Player myPlayer = new Player(dungeon, new EntityInfo(playerId, new Position(0, 0), 
        DungeonConstants.ENTITY_PLAYER, false));

        dungeon.setPlayer(myPlayer);

        // Add ingredients of shield to player's inventory (recipe 1)
        myPlayer.addItemToInventory(new Wood(new EntityInfo("wood0", new Position(0, 0), DungeonConstants.ENTITY_WOOD, true)));
        myPlayer.addItemToInventory(new Wood(new EntityInfo("wood1", new Position(0, 0), DungeonConstants.ENTITY_WOOD, true)));
        myPlayer.addItemToInventory(new Treasure(new EntityInfo("treasure0", new Position(0, 0), DungeonConstants.ENTITY_TREASURE, true)));

        Entity itemCreated0 = dungeon.build("shield");
        assertEquals(true, itemCreated0 instanceof Shield);

        // Add ingredients of shield to player's inventory (recipe 2)
        myPlayer.addItemToInventory(new Wood(new EntityInfo("wood2", new Position(0, 0), DungeonConstants.ENTITY_WOOD, true)));
        myPlayer.addItemToInventory(new Wood(new EntityInfo("wood3", new Position(0, 0), DungeonConstants.ENTITY_WOOD, true)));
        myPlayer.addItemToInventory(new Key(new EntityInfo("key0", new Position(0, 0), DungeonConstants.ENTITY_KEY, true), 1));

        Entity itemCreated1 = dungeon.build(DungeonConstants.ENTITY_SHIELD);
        assertEquals(true, itemCreated1 instanceof Shield);
    }

    @Test
    public void testCannotBuild() {

        String dungeonId = "dungeon1";
        String name = "maze";
        String playerId = "Batman";
        Dungeon dungeon = new Dungeon(new DungeonInfo(50, 50, dungeonId, name, DungeonConstants.MODE_PEACEFUL, new ExitGoal()));

        Player myPlayer = new Player(dungeon, new EntityInfo(playerId, new Position(0, 0), 
        DungeonConstants.ENTITY_PLAYER, false));

        dungeon.setPlayer(myPlayer);

        assertThrows(InvalidActionException.class ,() -> {
            dungeon.build("bow");
        });
        assertThrows(InvalidActionException.class ,() -> {
            dungeon.build("shield");
        });
        assertThrows(IllegalArgumentException.class ,() -> {
            dungeon.build("wood");
        });
    }

    @Test
    public void testMovement() {

        String dungeonId = "dungeon1";
        String name = "maze";
        String playerId = "Batman";
        Dungeon dungeon = new Dungeon(new DungeonInfo(50, 50, dungeonId, name, DungeonConstants.MODE_PEACEFUL, new ExitGoal()));

        Player myPlayer = new Player(dungeon, new EntityInfo(playerId, new Position(0, 0), 
        DungeonConstants.ENTITY_PLAYER, false));
        dungeon.setPlayer(myPlayer);

        Direction moveDirection = Direction.UP;
        myPlayer.move(moveDirection);
        assertEquals(new Position(0, -1), myPlayer.getPosition());

        moveDirection = Direction.RIGHT;
        myPlayer.move(moveDirection);
        assertEquals(new Position(1, -1), myPlayer.getPosition());

        moveDirection = Direction.DOWN;
        myPlayer.move(moveDirection);
        assertEquals(new Position(1, 0), myPlayer.getPosition());

        moveDirection = Direction.LEFT;
        myPlayer.move(moveDirection);
        assertEquals(new Position(0, 0), myPlayer.getPosition());
    }
}
