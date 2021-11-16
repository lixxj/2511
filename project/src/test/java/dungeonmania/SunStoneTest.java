package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.dungeon.Dungeon;
import dungeonmania.dungeon.DungeonInfo;
import dungeonmania.entities.*;
import dungeonmania.entities.collectables.*;
import dungeonmania.entities.moving.player.Player;
import dungeonmania.entities.staticitems.*;
import dungeonmania.goals.*;
import dungeonmania.util.*;
import org.junit.jupiter.api.Test;

public class SunStoneTest {
    @Test
    public void testSunStoneForDoor(){
        String dungeonId = "dungeon1";
        String name = "maze";
        String playerId = "P1";
        String SunStoneId = "S1";

        Position doorPosition = new Position(1, 1);
        Position SunStonePosition = new Position(1, 2);
        Position playerPosition = new Position(1, 3);

        Dungeon dungeon = new Dungeon(new DungeonInfo(50, 50, dungeonId, name, DungeonConstants.MODE_PEACEFUL, new TreasureGoal()));
        Player P1 = new Player(dungeon, new EntityInfo(playerId, playerPosition, 
        DungeonConstants.ENTITY_PLAYER, false));
        SunStone stone = new SunStone(new EntityInfo(SunStoneId, SunStonePosition,
        DungeonConstants.ENTITY_SUN_STONE, true));
        Door door1 = new Door(new EntityInfo("door1", doorPosition,
        DungeonConstants.ENTITY_DOOR, false), 2);
        dungeon.setPlayer(P1);
        
        List<Entity> entities = new ArrayList<>();
        entities.add(door1);
        entities.add(stone);
        entities.add(P1);

        dungeon.setEntities(entities);
        
        Direction moveDirection = Direction.UP;
        P1.move(moveDirection);


        assertEquals(new Position(1, 2), P1.getPosition());

        P1.move(moveDirection);

        assertEquals(true, P1.checkItemExists(SunStoneId));
        assertEquals(new Position(1, 1), P1.getPosition());
        //door should be open for character and retains the stone
        assertEquals(false, door1.isDoorClosed());
        assertEquals(true, P1.checkItemExists(SunStoneId));
    }
    
}
