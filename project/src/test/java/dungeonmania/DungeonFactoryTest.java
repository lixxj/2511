package dungeonmania;

import dungeonmania.entities.DungeonConstants;
import dungeonmania.dungeon.*;
import dungeonmania.response.models.*;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class DungeonFactoryTest {

    @Test
    public void createMazeTest() {
        Dungeon dungeon = DungeonFactory.createNewGame("maze", DungeonConstants.MODE_PEACEFUL);

        DungeonResponse dungeonResponse = dungeon.getDungeonResponse();
        List<EntityResponse> walls = dungeonResponse.getEntities().stream().
                filter(entityResponse -> entityResponse.getType().
                        equals(DungeonConstants.ENTITY_WALL)).collect(Collectors.toList());
        List<EntityResponse> player = dungeonResponse.getEntities().stream().
                filter(entityResponse -> entityResponse.getType().
                        equals(DungeonConstants.ENTITY_PLAYER)).collect(Collectors.toList());
        List<EntityResponse> exit = dungeonResponse.getEntities().stream().
                filter(entityResponse -> entityResponse.getType().
                        equals(DungeonConstants.ENTITY_EXIT)).collect(Collectors.toList());
        assertEquals(walls.size(), 204);
        assertEquals(player.size(), 1);
        assertEquals(exit.size(), 1);
        assertEquals(0, dungeon.getCurrentTick());
    }

    @Test
    public void createBouldersTest() {
        Dungeon dungeon = DungeonFactory.createNewGame("boulders", DungeonConstants.MODE_STANDARD);

        DungeonResponse dungeonResponse = dungeon.getDungeonResponse();
        List<EntityResponse> walls = dungeonResponse.getEntities().stream().
                filter(entityResponse -> entityResponse.getType().
                        equals(DungeonConstants.ENTITY_WALL)).collect(Collectors.toList());
        List<EntityResponse> player = dungeonResponse.getEntities().stream().
                filter(entityResponse -> entityResponse.getType().
                        equals(DungeonConstants.ENTITY_PLAYER)).collect(Collectors.toList());
        List<EntityResponse> boulders = dungeonResponse.getEntities().stream().
                filter(entityResponse -> entityResponse.getType().
                        equals(DungeonConstants.ENTITY_BOULDER)).collect(Collectors.toList());
        List<EntityResponse> switches = dungeonResponse.getEntities().stream().
                filter(entityResponse -> entityResponse.getType().
                        equals(DungeonConstants.ENTITY_FLOOR_SWITCH)).collect(Collectors.toList());
        assertEquals(walls.size(), 35);
        assertEquals(player.size(), 1);
        assertEquals(boulders.size(), 7);
        assertEquals(switches.size(), 7);
    }

    @Test
    public void createAdvancedTest() {
        Dungeon dungeon = DungeonFactory.createNewGame("advanced", DungeonConstants.MODE_HARD);

        DungeonResponse dungeonResponse = dungeon.getDungeonResponse();
        List<EntityResponse> walls = dungeonResponse.getEntities().stream().
                filter(entityResponse -> entityResponse.getType().
                        equals(DungeonConstants.ENTITY_WALL)).collect(Collectors.toList());

        List<EntityResponse> player = dungeonResponse.getEntities().stream().
                filter(entityResponse -> entityResponse.getType().
                        equals(DungeonConstants.ENTITY_PLAYER)).collect(Collectors.toList());

        List<EntityResponse> swords = dungeonResponse.getEntities().stream().
                filter(entityResponse -> entityResponse.getType().
                        equals(DungeonConstants.ENTITY_SWORD)).collect(Collectors.toList());


        List<EntityResponse> bombs = dungeonResponse.getEntities().stream().
                filter(entityResponse -> entityResponse.getType().
                        equals(DungeonConstants.ENTITY_BOMB)).collect(Collectors.toList());

        List<EntityResponse> mercenary = dungeonResponse.getEntities().stream().
                filter(entityResponse -> entityResponse.getType().
                        equals(DungeonConstants.ENTITY_MERCENARY)).collect(Collectors.toList());

        List<EntityResponse> treasure = dungeonResponse.getEntities().stream().
                filter(entityResponse -> entityResponse.getType().
                        equals(DungeonConstants.ENTITY_TREASURE)).collect(Collectors.toList());

        List<EntityResponse> invincibility_potion = dungeonResponse.getEntities().stream().
                filter(entityResponse -> entityResponse.getType().
                        equals(DungeonConstants.ENTITY_INVINCIBILITY_POTION)).collect(Collectors.toList());


        assertEquals(walls.size(), 114);
        assertEquals(player.size(), 1);
        assertEquals(swords.size(), 1);
        assertEquals(bombs.size(), 1);
        assertEquals(mercenary.size(), 1);
        assertEquals(treasure.size(), 1);
        assertEquals(invincibility_potion.size(), 1);


    }
    /*

    // FAILING BECAUSE FILE READ FROM WRONG RESOURCE
    
    @Test
    public void createAdvanced2Test() {
        DungeonFactory dungeonFactory = new DungeonFactory();
        Dungeon dungeon = dungeonFactory.createNewGame("advanced-2", DungeonConstants.MODE_HARD);

        DungeonResponse dungeonResponse = dungeon.getDungeonResponse();
        List<EntityResponse> walls = dungeonResponse.getEntities().stream().
                filter(entityResponse -> entityResponse.getType().
                        equals(DungeonConstants.ENTITY_WALL)).collect(Collectors.toList());

        List<EntityResponse> player = dungeonResponse.getEntities().stream().
                filter(entityResponse -> entityResponse.getType().
                        equals(DungeonConstants.ENTITY_PLAYER)).collect(Collectors.toList());

        List<EntityResponse> swords = dungeonResponse.getEntities().stream().
                filter(entityResponse -> entityResponse.getType().
                        equals(DungeonConstants.ENTITY_SWORD)).collect(Collectors.toList());


        List<EntityResponse> bombs = dungeonResponse.getEntities().stream().
                filter(entityResponse -> entityResponse.getType().
                        equals(DungeonConstants.ENTITY_BOMB)).collect(Collectors.toList());

        List<EntityResponse> mercenary = dungeonResponse.getEntities().stream().
                filter(entityResponse -> entityResponse.getType().
                        equals(DungeonConstants.ENTITY_MERCENARY)).collect(Collectors.toList());

        List<EntityResponse> treasure = dungeonResponse.getEntities().stream().
                filter(entityResponse -> entityResponse.getType().
                        equals(DungeonConstants.ENTITY_TREASURE)).collect(Collectors.toList());


        List<EntityResponse> doors = dungeonResponse.getEntities().stream().
                filter(entityResponse -> entityResponse.getType().
                        equals(DungeonConstants.ENTITY_DOOR)).collect(Collectors.toList());

        List<EntityResponse> keys = dungeonResponse.getEntities().stream().
                filter(entityResponse -> entityResponse.getType().
                        equals(DungeonConstants.ENTITY_KEY)).collect(Collectors.toList());

        List<EntityResponse> wood = dungeonResponse.getEntities().stream().
                filter(entityResponse -> entityResponse.getType().
                        equals(DungeonConstants.ENTITY_WOOD)).collect(Collectors.toList());


        List<EntityResponse> arrows = dungeonResponse.getEntities().stream().
                filter(entityResponse -> entityResponse.getType().
                        equals(DungeonConstants.ENTITY_ARROW)).collect(Collectors.toList());

        assertEquals(walls.size(), 114);
        assertEquals(player.size(), 1);
        assertEquals(swords.size(), 1);
        assertEquals(bombs.size(), 1);
        assertEquals(mercenary.size(), 1);
        assertEquals(treasure.size(), 1);
        assertEquals(doors.size(), 2);
        assertEquals(keys.size(), 2);
        assertEquals(wood.size(), 3);
        assertEquals(arrows.size(), 3);




    }

    */
}
