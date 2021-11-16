package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import dungeonmania.entities.*;
import dungeonmania.entities.collectables.*;
import dungeonmania.entities.moving.enemies.Assassin;
import dungeonmania.entities.moving.player.Player;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.dungeon.Dungeon;
import dungeonmania.dungeon.DungeonInfo;
import dungeonmania.goals.ExitGoal;

import dungeonmania.util.*;

public class AssassinTest {

    // Testing a successful movement using the MoveTowards movement strategy
    @Test
    public void testMoveTowards() {

        String dungeonId = "dungeon1";
        String name = "maze";
        String assassinId = "Merc";
        String playerId = "Player 1";

        Position playerPosition = new Position(1,0);
        Position assassinPosition = new Position(5,0);

        Dungeon dungeon = new Dungeon(new DungeonInfo(50, 50, dungeonId, name, DungeonConstants.MODE_PEACEFUL, new ExitGoal()));
        Player player = new Player(dungeon, 
                        new EntityInfo(playerId, playerPosition, DungeonConstants.ENTITY_PLAYER, false));
        dungeon.setPlayer(player);
        Assassin assassin = new Assassin(dungeon, 
                            new EntityInfo(assassinId, assassinPosition, DungeonConstants.ENTITY_ASSASSIN, false));
        
        Double distance = Math.sqrt(Math.pow((assassinPosition.getX() - playerPosition.getX()), 2) + 
        Math.pow((assassinPosition.getY() - playerPosition.getY()), 2));

        while (distance != 0) {
            assassin.move();
            assassinPosition = assassin.getPosition();
            distance = Math.sqrt(Math.pow((assassinPosition.getX() - playerPosition.getX()), 2) + 
                        Math.pow((assassinPosition.getY() - playerPosition.getY()), 2));
        }
        assertTrue(distance == 0);
        assertEquals(playerPosition, assassinPosition);
    }

    // Testing assasin bribe exceptions and successful bribe
    @Test
    public void testAssassinBribeExceptions() {
        String dungeonId = "dungeon1";
        String name = "maze";
        String assassinId = "edward_kenway";
        Position initialPosition = new Position(0, 10);
        Dungeon dungeon = new Dungeon(new DungeonInfo(50, 50, dungeonId, name, DungeonConstants.MODE_PEACEFUL, new ExitGoal()));
        Player player = new Player(dungeon, 
                        new EntityInfo("gamer", 
                        new Position(0, 0), DungeonConstants.ENTITY_PLAYER, false));
        dungeon.setPlayer(player);
        Assassin assassin = new Assassin(dungeon, 
                            new EntityInfo(assassinId, initialPosition, DungeonConstants.ENTITY_ASSASSIN, false));
        
        List<Entity> entities = new ArrayList<>();
        entities.add(assassin);
        dungeon.setEntities(entities);
        dungeon.addEnemy(assassin);
       
        dungeon.tick(null, Direction.DOWN); // Player - (0, -9), Mercenary - (0, 1)
        assertEquals(new Position(0, 1), player.getPosition());
        assertEquals(new Position(0, 9), assassin.getPosition());

        // Assassin cannot be bribed since it is not nearby
        assertThrows(InvalidActionException.class, () -> {
            dungeon.interact(assassinId);
        });

        assassin.setPosition(new Position(0, 3));

        // Assassin cannot be bribed since player has no gold
        assertThrows(InvalidActionException.class, () -> {
            dungeon.interact(assassinId);
        });

        
        player.addItemToInventory(new Treasure(new EntityInfo("treasure", null, DungeonConstants.ENTITY_TREASURE, true)));

        // Assassin cannot be bribed since player does not have the one ring
        assertThrows(InvalidActionException.class, () -> {
            dungeon.interact(assassinId);
        });

        player.addItemToInventory(new OneRing(new EntityInfo("magic_ring", null, DungeonConstants.ENTITY_ONE_RING, true)));

        // Bribe successful
        dungeon.interact(assassinId);
        assertTrue(assassin.isAlly());
    }
}
