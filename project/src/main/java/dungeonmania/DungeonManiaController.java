package dungeonmania;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dungeonmania.entities.DungeonConstants;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.dungeon.Dungeon;
import dungeonmania.dungeon.DungeonFactory;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.memory.Memory;
import dungeonmania.serializer.Serializer;
import dungeonmania.util.Direction;
import dungeonmania.util.FileLoader;


/**
 * an intermediate class between the frontend and the backend
 */
public class DungeonManiaController {

    private Dungeon currentDungeon;
    private Map<Integer, String> currentDungeonState = new HashMap<>();
    
    /** 
     * constructor
     */
    public DungeonManiaController() {
    }

    public String getSkin() {
        return "default";
    }

    public String getLocalisation() {
        return "en_US";
    }

    public List<String> getGameModes() {
        return Arrays.asList(DungeonConstants.MODE_STANDARD, DungeonConstants.MODE_PEACEFUL, DungeonConstants.MODE_HARD);
    }

    /**
     * /dungeons
     * 
     * Done for you.
     */
    public static List<String> dungeons() {
        try {
            return FileLoader.listFileNamesInResourceDirectory("/dungeons");
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    /**
     * creates a new game and returns DungeonResponse describing new game
     * @param dungeonName
     * @param gameMode
     * @return
     * @throws IllegalArgumentException
     */
    public DungeonResponse newGame(String dungeonName, String gameMode) throws IllegalArgumentException {
        gameMode = gameMode.toLowerCase();
        if (!getGameModes().contains(gameMode)) {
            throw new IllegalArgumentException("Invalid Game Mode");
        }
        this.currentDungeon = DungeonFactory.createNewGame(dungeonName, gameMode);
        return this.currentDungeon.getDungeonResponse();
    }
    
    /**
     * saves the current game and returns DungeonResponse describing that game
     * @param name
     * @return
     */
    public DungeonResponse saveGame(String name) {
        String dungeonJson = Serializer.getDungeonAsJSON(this.currentDungeon);
        Memory.saveGame(dungeonJson, name);
        return this.currentDungeon.getDungeonResponse();
    }


    /**
     * loads a saved game and returns DungeonResponse describing that game
     * @param name
     * @return
     * @throws IllegalArgumentException
     */
    public DungeonResponse loadGame(String name) throws IllegalArgumentException {
        List<String> allGames = this.allGames();
        boolean gameExists = allGames.stream().anyMatch(g -> g.equals(name));
        if (gameExists) {
            this.currentDungeon = DungeonFactory.createSavedGame(name);
            return this.currentDungeon.getDungeonResponse();
        } else {
            throw new IllegalArgumentException("Game ID is not a valid game ID");
        }
    }

    public List<String> allGames() {

        // Need to return all the files in that folder
        List<String> allGames = new ArrayList<>();
        try {
           allGames.addAll(FileLoader.listFileNamesInDirectoryOutsideOfResources("savedGames"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return allGames;
    }

    /**
     * allow frontend user to make a tick
     * @param itemUsed
     * @param movementDirection
     * @return
     * @throws IllegalArgumentException
     * @throws InvalidActionException
     */
    public DungeonResponse tick(String itemUsed, Direction movementDirection) throws IllegalArgumentException, InvalidActionException {
        this.currentDungeon.tick(itemUsed, movementDirection);
        currentDungeonState.put(currentDungeon.getCurrentTick() - 1, Serializer.getDungeonAsJSON(this.currentDungeon));
        return this.currentDungeon.getDungeonResponse();
    }

    /**
     * allows a frontend user to interact with an interactable item
     * @param entityId
     * @return
     * @throws IllegalArgumentException
     * @throws InvalidActionException
     */
    public DungeonResponse interact(String entityId) throws IllegalArgumentException, InvalidActionException {
        this.currentDungeon.interact(entityId);
        return this.currentDungeon.getDungeonResponse();
    }

    /**
     * allows the frontend user to build a buildable item
     * @param buildable
     * @return
     * @throws IllegalArgumentException
     * @throws InvalidActionException
     */
    public DungeonResponse build(String buildable) throws IllegalArgumentException, InvalidActionException {
        this.currentDungeon.build(buildable);
        return this.currentDungeon.getDungeonResponse();
    }

    /**
     * Allows frontend to rewind game if time turner picked up
     * @param ticks
     * @return
     * @throws IllegalArgumentException
     */
    public DungeonResponse rewind(int ticks) throws IllegalArgumentException {

        if (ticks <= 0) {
            throw new IllegalArgumentException("Ticks must be greater than 0");
        }

        int rewindTick = currentDungeon.getCurrentTick() - ticks - 1;
        if (rewindTick <= 0) {
            throw new IllegalArgumentException("Ticks must be greater than 0");
        }

        String rewindJson = currentDungeonState.get(rewindTick);
        String name = currentDungeon.getDungeonInfo().getName();
        String mode = currentDungeon.getDungeonInfo().getMode();
        currentDungeon = DungeonFactory.createGameFromJSON(rewindJson, name, mode);
        currentDungeon.setCurrentTick(rewindTick);
        for (int i = rewindTick + 1; i < currentDungeonState.size(); i++) {
            currentDungeonState.remove(i);
        }
        return currentDungeon.getDungeonResponse();
    }
}