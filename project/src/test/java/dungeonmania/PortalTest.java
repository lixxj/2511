package dungeonmania;

import dungeonmania.entities.DungeonConstants;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PortalTest {
    // Tests if a portal works as expected
    @Test
    public void testPortal() {
        DungeonManiaController dungeonManiaController = new DungeonManiaController();
        dungeonManiaController.newGame("portals", DungeonConstants.MODE_STANDARD);
        DungeonResponse dungeonResponse;

        dungeonResponse = dungeonManiaController.tick(null, Direction.RIGHT);

        for (EntityResponse entityResponse: dungeonResponse.getEntities()) {
            if (entityResponse.getType().equals(DungeonConstants.ENTITY_PLAYER)) {
                assertEquals(entityResponse.getPosition(), new Position(5, 0));
            }
        }
    }
}
