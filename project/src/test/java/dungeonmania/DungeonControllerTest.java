package dungeonmania;

import dungeonmania.entities.DungeonConstants;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.*;
import dungeonmania.util.FileLoader;
import org.junit.jupiter.api.Test;

import dungeonmania.util.Direction;
import dungeonmania.util.Position;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DungeonControllerTest {

    @Test
    public void testNewGameInvalidGameMode() {
        DungeonManiaController dungeonManiaController = new DungeonManiaController();
        assertThrows(IllegalArgumentException.class ,() -> {
            dungeonManiaController.newGame("advanced-2", "Invalid Game Mode");
        });
    }

    @Test
    public void testNewGameInvalidDungeonName() {
        DungeonManiaController dungeonManiaController = new DungeonManiaController();
        assertThrows(IllegalArgumentException.class ,() -> {
            dungeonManiaController.newGame("Invalid dungeon name", "Invalid Game Mode");
        });
    }

    @Test
    public void testLoadSaveGame() {
        DungeonManiaController dungeonManiaController = new DungeonManiaController();
        DungeonResponse newGameResp = dungeonManiaController.newGame("advanced-2", DungeonConstants.MODE_PEACEFUL);
        dungeonManiaController.saveGame("saveTest");
        dungeonManiaController.saveGame("saveTest");
        DungeonResponse saveGameResp = dungeonManiaController.loadGame("saveTest");
                assertEquals(newGameResp.getEntities().size(), saveGameResp.getEntities().size());

        int count = newGameResp.getEntities().size();

        for (int i = 0; i< count; i++) {
            EntityResponse e1 = newGameResp.getEntities().get(i);
            EntityResponse e2 = saveGameResp.getEntities().get(i);
            assert e1.getId().equals(e2.getId());
            assert e1.getType().equals(e2.getType());
            assert e1.getPosition().equals(e2.getPosition());
            assert e1.isInteractable() == e2.isInteractable();
        }


    }

    @Test
    public void testTickItemUsedNotInInventory() {
        DungeonManiaController dungeonManiaController = new DungeonManiaController();
        dungeonManiaController.newGame("advanced-2", DungeonConstants.MODE_PEACEFUL);
        assertThrows(InvalidActionException.class ,() -> {
            dungeonManiaController.tick("Invalid item", Direction.NONE);
        });
    }

    @Test
    public void testTickIllegalItemUsedSword() {
        DungeonManiaController dungeonManiaController = new DungeonManiaController();
        dungeonManiaController.newGame("advanced-2", DungeonConstants.MODE_PEACEFUL);
        assertThrows(InvalidActionException.class ,() -> {
            dungeonManiaController.tick(DungeonConstants.ENTITY_SWORD, Direction.NONE);
        });
    }

    @Test
    public void testBuildIllegalArgument() {
        DungeonManiaController dungeonManiaController = new DungeonManiaController();
        dungeonManiaController.newGame("advanced-2", DungeonConstants.MODE_PEACEFUL);
        assertThrows(IllegalArgumentException.class ,() -> {
            dungeonManiaController.build(DungeonConstants.ENTITY_SWORD);
        });
    }

    @Test
    public void testBuildInsufficientItemsForCrafting() {
        DungeonManiaController dungeonManiaController = new DungeonManiaController();
        dungeonManiaController.newGame("advanced-2", DungeonConstants.MODE_PEACEFUL);
        assertThrows(InvalidActionException.class ,() -> {
            dungeonManiaController.build(DungeonConstants.ENTITY_BOW);
        });
    }

    @Test
    public void testInteractInvalidId() {
        DungeonManiaController dungeonManiaController = new DungeonManiaController();
        dungeonManiaController.newGame("advanced-2", DungeonConstants.MODE_PEACEFUL);
        assertThrows(IllegalArgumentException.class ,() -> {
            dungeonManiaController.interact("Invalid ID");
        });
    }

    @Test
    public void testInteractTooFarToBribe() {
        DungeonManiaController dungeonManiaController = new DungeonManiaController();
        DungeonResponse response =  dungeonManiaController.newGame("advanced-2", DungeonConstants.MODE_PEACEFUL);
        EntityResponse mercenary = response.getEntities().stream().filter(entityResponse -> entityResponse.getType().equals(DungeonConstants.ENTITY_MERCENARY)).findFirst().orElse(null);
        assertThrows(InvalidActionException.class ,() -> {
            dungeonManiaController.interact(mercenary.getId());
        });
    }

    @Test
    public void testInteractTooFarToDestroy() {
        DungeonManiaController dungeonManiaController = new DungeonManiaController();
        DungeonResponse response =  dungeonManiaController.newGame("zombie-spawner", DungeonConstants.MODE_PEACEFUL);
        EntityResponse zombieToastSpawner = response.getEntities().stream().filter(entityResponse -> entityResponse.getType().equals(DungeonConstants.ENTITY_ZOMBIE_TOAST_SPAWNER)).findFirst().orElse(null);
        assertThrows(InvalidActionException.class ,() -> {
            dungeonManiaController.interact(zombieToastSpawner.getId());
        });
    }

    @Test
    public void testInteractNoWeaponZombieToastSpawner() {
        DungeonManiaController dungeonManiaController = new DungeonManiaController();
        DungeonResponse response =  dungeonManiaController.newGame("zombie-spawner", DungeonConstants.MODE_PEACEFUL);
        EntityResponse zombieToastSpawner = response.getEntities().stream().filter(entityResponse -> entityResponse.getType().equals(DungeonConstants.ENTITY_ZOMBIE_TOAST_SPAWNER)).findFirst().orElse(null);

        dungeonManiaController.tick(null, Direction.DOWN);
        for (int i = 0; i < 5; i++) {
            dungeonManiaController.tick(null, Direction.RIGHT);
        }
        assertThrows(InvalidActionException.class ,() -> {
            dungeonManiaController.interact(zombieToastSpawner.getId());
        });
    }

}
