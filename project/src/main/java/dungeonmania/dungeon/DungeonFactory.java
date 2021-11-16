package dungeonmania.dungeon;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import com.google.gson.*;

import dungeonmania.entities.*;
import dungeonmania.entities.collectables.*;
import dungeonmania.entities.collectables.potions.*;
import dungeonmania.entities.staticitems.*;
import dungeonmania.goals.*;
import dungeonmania.entities.moving.enemies.*;
import dungeonmania.entities.moving.player.Player;
import dungeonmania.memory.Memory;
import dungeonmania.util.Position;


public class DungeonFactory {


    /**
     * creates a new game.
     * @param gameMode
     * @return dungeon.
     */
    public static Dungeon createNewGame(String dungeonName, String gameMode) {
        JsonObject gameFile = Memory.readNewDungeon(dungeonName);
        return createDungeon(gameFile, dungeonName, gameMode);
    }

    /**
     * creates a game from a saved json file.
     * @param dungeonName
     * @return dungeon.
     */
    public static Dungeon createSavedGame(String dungeonName) {
        JsonObject gameFile = Memory.readSavedDungeon(dungeonName);
        String gameMode = gameFile.get("mode").getAsString();
        return createDungeon(gameFile, dungeonName, gameMode);
    }

    /**
     * creates a game from a JSON string (used for time turner).
     * @param gameMode
     * @return dungeon.
     */

    public static Dungeon createGameFromJSON(String dungeonJson, String dungeonName, String gameMode) {
        JsonObject gameFile = JsonParser.parseString(dungeonJson).getAsJsonObject();
        return createDungeon(gameFile, dungeonName, gameMode);
    }

    /**
     * Creates a new Dungeon
     * @param gameFile
     * @param dungeonName
     * @param gameMode
     * @return
     */
    private static Dungeon createDungeon(JsonObject gameFile, String dungeonName, String gameMode) {
        int height = gameFile.has("height") ? gameFile.get("height").getAsInt() : DungeonConstants.DEFAULT_DUNGEON_HEIGHT;
        int width = gameFile.has("width") ? gameFile.get("width").getAsInt() : DungeonConstants.DEFAULT_DUNGEON_WIDTH;
        Goal goals = createGoals(getGoalCondition(gameFile));
        String gameId = getGameId(dungeonName);
        Dungeon newDungeon = new Dungeon(new DungeonInfo(height, width, gameId, dungeonName, gameMode, goals));
        createEntities(newDungeon, gameFile);
        return newDungeon;
    }
    
    /**
     * Gets goal conditions from the game file.
     * 
     * @param gameFile
     * @return condition if any, else null.
     */
    private static JsonObject getGoalCondition(JsonObject gameFile) {
        if (gameFile.has("goal-condition")) {
            return gameFile.get("goal-condition").getAsJsonObject();
        }
        return null;
    }

    /**
     * Creates a list of new entities and add entities to the list and 
     * sets the list in the dungeon.
     * @param newDungeon
     * @param gameFile
     */
    private static void createEntities(Dungeon newDungeon, JsonObject gameFile) {
        List<Entity> newEntities = new ArrayList<>();
        JsonArray gameEntities = gameFile.get("entities").getAsJsonArray();
        for (JsonElement gameEntity: gameEntities) {
            Entity newEntity = createEntity(gameEntity.getAsJsonObject(), newDungeon);
            // Just to see what entities we may have missed
            assert newEntity != null;
            if (Objects.equals(newEntity.getType(), DungeonConstants.ENTITY_PLAYER)) {
                newDungeon.setPlayer((Player) newEntity);
            } else if (newEntity instanceof Enemy) {
                newDungeon.addEnemy((Enemy) newEntity);
            }
            newEntities.add(newEntity);
        }
        newDungeon.setEntities(newEntities);
    }

    /**
     * Creates a new entity in the dungeon depending on the type of entity specified as a JsonObject.
     * 
     * @param entityObject
     * @param newDungeon
     * @return entity object if any, else null.
     */
    private static Entity createEntity(JsonObject entityObject, Dungeon newDungeon) {
        int x = entityObject.get("x").getAsInt();
        int y = entityObject.get("y").getAsInt();
        // Lets leave it as layer 0 for now
        Position position = new Position(x, y, 2);
        String type = entityObject.get("type").getAsString();
        String id;
        if (entityObject.has("id")) {
            id = entityObject.get("id").getAsString();
        } else {
            id = x + "-" + y + "-" + type;
        }

        switch (type){
            case DungeonConstants.ENTITY_PLAYER:
                Player player = new Player(newDungeon, new EntityInfo(id, new Position(x, y, 3), type, false));
                if (entityObject.has("inventory")) {
                    JsonArray inventory = entityObject.getAsJsonArray("inventory");
                    inventory.forEach(i -> player.addItemToInventory(createEntity(i.getAsJsonObject(), newDungeon)));
                }
                return player;
            case DungeonConstants.ENTITY_WALL:
                return new Wall(new EntityInfo(id, position, type, false));
            case DungeonConstants.ENTITY_EXIT:
                return new Exit(new EntityInfo(id, position, type, false));
            case DungeonConstants.ENTITY_BOULDER:
                return new Boulder(new EntityInfo(id, position, type, false));
            case DungeonConstants.ENTITY_FLOOR_SWITCH:
                if (entityObject.has("switchOn")) {
                    return new FloorSwitch(new EntityInfo(id, position, type, false), entityObject.get("switchOn").getAsBoolean());
                }
                return new FloorSwitch(new EntityInfo(id, position, type, false));
            case DungeonConstants.ENTITY_TIME_TURNER:
                return new TimeTurner(new EntityInfo(id, position, type, true));
            case DungeonConstants.ENTITY_DOOR:
                if (entityObject.has("doorClosed")) {
                    return new Door(new EntityInfo(id, position, type, false),
                            entityObject.get(DungeonConstants.ENTITY_KEY).getAsInt(), entityObject.get("doorClosed").getAsBoolean());
                }
                return new Door(new EntityInfo(id, position, type, false),
                                            entityObject.get(DungeonConstants.ENTITY_KEY).getAsInt());
            case DungeonConstants.ENTITY_PORTAL:
                return new Portal(new EntityInfo(id, position, type, false), 
                                                    entityObject.get("colour").getAsString());
            case DungeonConstants.ENTITY_ZOMBIE_TOAST_SPAWNER:
                return new ZombieToastSpawner(new EntityInfo(id, position, type, false));
            case DungeonConstants.ENTITY_SWAMP_TILE:
                return new SwampTile(new EntityInfo(id, position, type, false), entityObject.get("movement_factor").getAsInt());
            case DungeonConstants.ENTITY_SPIDER:
                return new Spider(newDungeon, new EntityInfo(id, position, type, false));
            case DungeonConstants.ENTITY_ZOMBIE_TOAST:
                return new ZombieToast(newDungeon, new EntityInfo(id, position, type, false));
            case DungeonConstants.ENTITY_MERCENARY:
                if (entityObject.has("ally")) {
                    return new Mercenary(newDungeon, new EntityInfo(id, position, type, false),
                            DungeonConstants.MERCENARY_ATTACK_DAMAGE, entityObject.get("ally").getAsBoolean());
                }
                return new Mercenary(newDungeon, new EntityInfo(id, position, type, false));
            case DungeonConstants.ENTITY_HYDRA:
                return new Hydra(newDungeon, new EntityInfo(id, position, type, false));
            case DungeonConstants.ENTITY_ASSASSIN:
                if (entityObject.has("ally")) {
                    return new Assassin(newDungeon, new EntityInfo(id, position, type, false),
                            entityObject.get("ally").getAsBoolean());
                }
                return new Assassin(newDungeon, new EntityInfo(id, position, type, false));
            case DungeonConstants.ENTITY_TREASURE:
                return new Treasure(new EntityInfo(id, position, type, true));
            case DungeonConstants.ENTITY_KEY:
                return new Key(new EntityInfo(id, position, type, true), entityObject.get(DungeonConstants.ENTITY_KEY).getAsInt());
            case DungeonConstants.ENTITY_HEALTH_POTION:
                return new HealthPotion(new EntityInfo(id, position, type, true));
            case DungeonConstants.ENTITY_INVINCIBILITY_POTION:
                return new InvincibilityPotion(new EntityInfo(id, position, type, true));
            case DungeonConstants.ENTITY_INVISIBILITY_POTION:
                return new InvisibilityPotion(new EntityInfo(id, position, type, true));
            case DungeonConstants.ENTITY_WOOD:
                return new Wood(new EntityInfo(id, position, type, true));
            case DungeonConstants.ENTITY_ARROW:
                return new Arrow(new EntityInfo(id, position, type, true));
            case DungeonConstants.ENTITY_BOMB:
                return new Bomb(new EntityInfo(id, position, type, true));
            case DungeonConstants.ENTITY_SWORD:
                return new Sword(new EntityInfo(id, position, type, true));
            case DungeonConstants.ENTITY_ARMOUR:
                return new Armour(new EntityInfo(id, position, type, true));
            case DungeonConstants.ENTITY_ONE_RING:
                return new OneRing(new EntityInfo(id, position, type, true));
            case DungeonConstants.ENTITY_ANDURIL:
                return new Anduril(new EntityInfo(id, position, type, true));
            case DungeonConstants.ENTITY_SUN_STONE:
                return new SunStone(new EntityInfo(id, position, type, true));
        }
        return null;
    }

    /**
     * Creates a goal for the dungeon from what is given in the json file
     * for a single goal objective.
     * 
     * @param jsonObject
     * @return goal object if any, else null.
     */
    private static Goal createGoal(JsonObject jsonObject) {

        String goal = jsonObject.get("goal").getAsString();

        switch (goal) {
            case DungeonConstants.GOAL_EXIT:
                return new ExitGoal();
            case DungeonConstants.GOAL_ENEMIES:
                return new EnemiesGoal();
            case DungeonConstants.GOAL_BOULDERS:
                return new BoulderGoal();
            case DungeonConstants.GOAL_TREASURE:
                return new TreasureGoal();
        }
        return null;
    }

    /**
     * Creates goals for the dungeon from what is given in the json file
     * for multiple goals.
     * 
     * @param goalCondition
     * @return goalContainer object with the goals if any, else null.
     */
    private static Goal createGoals(JsonObject goalCondition) {

        if (goalCondition == null) {
            return null;
        }

        if (goalCondition.has("goal") &&
                !goalCondition.has("subgoals")) {
            return createGoal(goalCondition);
        }

        if (goalCondition.has("subgoals")) {
            JsonArray jsonSubGoals = goalCondition.get("subgoals").getAsJsonArray();
            Goal subGoal1 = createGoals(jsonSubGoals.get(0).getAsJsonObject());
            Goal subGoal2 = createGoals(jsonSubGoals.get(1).getAsJsonObject());
            String condition = goalCondition.get("goal").getAsString();
            if (condition.equals("AND")) {
                return new AndGoal(subGoal1, subGoal2);
            }
            if (condition.equals("OR")) {
                return new OrGoal(subGoal1, subGoal2);
            }
        }

        return null;

    }

    /**
     * Gets the gameId for the dungeon.
     * @param dungeonName
     * @return  returns the gameId.
     */
    private static String getGameId(String dungeonName) {
        return dungeonName + "-" + UUID.randomUUID();
    }
}
