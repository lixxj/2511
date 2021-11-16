package dungeonmania;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.entities.*;
import dungeonmania.entities.moving.Battle;
import dungeonmania.entities.moving.enemies.*;
import dungeonmania.entities.moving.player.Player;
import dungeonmania.dungeon.*;
import dungeonmania.goals.ExitGoal;

import dungeonmania.util.Position;

public class OneRingTest {
    @Test
    public void testOneRing() {
        String dungeonId = "dungeon1";
        String name = "maze";
        Dungeon dungeon = new Dungeon(new DungeonInfo(50, 50, dungeonId, name, DungeonConstants.MODE_STANDARD, new ExitGoal()));

        Player player = new Player(dungeon, new EntityInfo("batman", new Position(1, 2), 
        DungeonConstants.ENTITY_PLAYER, false));

        dungeon.setPlayer(player);
        boolean hasOneRing = false;

        while (!hasOneRing) { // Make sure that the player has the One Ring
            player.setHealth(100);
            Spider spidey = new Spider(dungeon, new EntityInfo("dead_robin", new Position(1, 2), 
            DungeonConstants.ENTITY_SPIDER, false));
            List<Entity> entities = new ArrayList<>();
            entities.add(spidey);
            dungeon.setEntities(entities);
            dungeon.addEnemy(spidey);

            while (Battle.canFight(player, spidey)) {
                Battle.fight(dungeon, player, spidey);
            }
            if(!spidey.isAlive()){
                dungeon.removeEntity(spidey);
            }
            hasOneRing = player.hasItemOfType(DungeonConstants.ENTITY_ONE_RING);
        }
        assertTrue(player.hasItemOfType(DungeonConstants.ENTITY_ONE_RING));

        // Player can now die and respawn
        player.setHealth(1);
        Spider spidey = new Spider(dungeon, new EntityInfo("dead_robin", new Position(1, 2), 
        DungeonConstants.ENTITY_SPIDER, false));
        List<Entity> entities = new ArrayList<>();
        entities.add(spidey);
        dungeon.setEntities(entities);
        dungeon.addEnemy(spidey);
        while (Battle.canFight(player, spidey)) {
            Battle.fight(dungeon, player, spidey);
        }
        assertTrue(player.isAlive()); // Player respawned in the battle and killed the zombie
        assertFalse(spidey.isAlive());

        assertEquals(true, player.getHealth() < 100);

    }
}