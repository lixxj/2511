package dungeonmania;

import dungeonmania.entities.DungeonConstants;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Direction;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class IntegrationTest {

    @Test
    public void testAdvanced2PlayDungeon() {

        DungeonManiaController dungeonManiaController = new DungeonManiaController();
        dungeonManiaController.newGame("advanced-2", DungeonConstants.MODE_STANDARD);
        DungeonResponse dungeonResponse;
        for (int i = 0; i < 10; i++) {
            dungeonManiaController.tick(null, Direction.RIGHT);
        }
        for (int i = 0; i < 3; i++) {
            dungeonManiaController.tick(null, Direction.DOWN);
        }
        for (int i = 0; i < 5; i++) {
            dungeonManiaController.tick(null, Direction.RIGHT);
        }
        for (int i = 0; i < 6; i++) {
            dungeonManiaController.tick(null, Direction.DOWN);
        }
        for (int i = 0; i < 9; i++) {
            dungeonManiaController.tick(null, Direction.LEFT);
        }
        for (int i = 0; i < 1; i++) {
            dungeonManiaController.tick(null, Direction.UP);
        }
        for (int i = 0; i < 3; i++) {
            dungeonManiaController.tick(null, Direction.LEFT);
        }
        for (int i = 0; i < 3; i++) {
            dungeonManiaController.tick(null, Direction.DOWN);
        }
        for (int i = 0; i < 4; i++) {
            dungeonManiaController.tick(null, Direction.RIGHT);
        }
        for (int i = 0; i < 1; i++) {
            dungeonManiaController.tick(null, Direction.DOWN);
        }
        for (int i = 0; i < 3; i++) {
            dungeonManiaController.tick(null, Direction.RIGHT);
        }
        for (int i = 0; i < 2; i++) {
            dungeonManiaController.tick(null, Direction.UP);
        }
        for (int i = 0; i < 3; i++) {
            dungeonManiaController.tick(null, Direction.DOWN);
        }
        for (int i = 0; i < 4; i++) {
            dungeonManiaController.tick(null, Direction.RIGHT);
        }
        dungeonResponse = dungeonManiaController.tick(null, Direction.RIGHT);
        for (EntityResponse entityResponse: dungeonResponse.getEntities()) {
            assertNotEquals(DungeonConstants.ENTITY_BOMB, entityResponse.getType());
            assertNotEquals(DungeonConstants.ENTITY_SWORD, entityResponse.getType());
            assertNotEquals(DungeonConstants.ENTITY_TREASURE, entityResponse.getType());
            assertNotEquals(DungeonConstants.ENTITY_ARROW, entityResponse.getType());
            assertNotEquals(DungeonConstants.ENTITY_WOOD, entityResponse.getType());
        }

        dungeonResponse = dungeonManiaController.build(DungeonConstants.ENTITY_BOW);
        dungeonResponse = dungeonManiaController.build(DungeonConstants.ENTITY_SHIELD);

        for (ItemResponse itemResponse: dungeonResponse.getInventory()) {
            assertNotEquals(DungeonConstants.ENTITY_ARROW, itemResponse.getType());
            assertNotEquals(DungeonConstants.ENTITY_WOOD, itemResponse.getType());
        }

    }
}
