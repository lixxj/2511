package dungeonmania.dungeon;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import dungeonmania.entities.*;
import dungeonmania.entities.buildables.*;
import dungeonmania.entities.moving.Battle;
import dungeonmania.entities.moving.enemies.*;
import dungeonmania.entities.moving.player.Player;
import dungeonmania.exceptions.InvalidActionException;

import dungeonmania.response.models.*;
import dungeonmania.util.*;


public class Dungeon {

    private DungeonInfo dungeonInfo;
    private EnemySpawner enemySpawner;
    private List<Enemy> enemies;
    private List<Entity> entities;
    private Player player;
    private int currentTick;

    /**
     * Class Constructor. 
     * Class the contains information about the dungeon.
     * 
     * @param dungeonInfo
     */
    public Dungeon(DungeonInfo dungeonInfo) {
        this.dungeonInfo = dungeonInfo;
        this.enemies = new ArrayList<>();
        this.entities = new ArrayList<>();
        this.currentTick = 0;
        this.enemySpawner = new EnemySpawner(this);
    }

    public Dungeon(Dungeon dungeon) {
        this.dungeonInfo = dungeon.dungeonInfo;
        this.enemies = dungeon.getEnemies();
        this.entities = dungeon.getEntities();
        this.currentTick = dungeon.getCurrentTick();
        this.player = dungeon.getPlayer();
    }

    public Dungeon cloneObject()  {
        try {
            return (Dungeon) super.clone();
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public int getHeight() {
        return this.dungeonInfo.getHeight();
    }

    public int getWidth() {
        return this.dungeonInfo.getWidth();
    }

    public String getMode() {
        return this.dungeonInfo.getMode();
    }

    public DungeonInfo getDungeonInfo() {
        return this.dungeonInfo;
    }

    public List<Entity> getEntities() {
        return this.entities;
    }

    public String getGoalsAsString() {
        if (this.dungeonInfo.getGoals() == null) {
            return "No Goals";
        }
        if (this.dungeonInfo.getGoals().isComplete(entities)) {
            return "";
        } else {
            return this.dungeonInfo.getGoals().getNameFrontend();
        }
    }

    public void setEntities(List<Entity> entities) {
        this.entities = entities;
    }

    /**
     * Gets the player in the current dungeon.
     * @return current player
     */
    public Player getPlayer() {
        if (this.player == null) 
            this.player = (Player) entities.stream().filter(entity -> (entity instanceof Player)).findFirst().get();
        return this.player;
    }

    /**
     * Gets the position of the player in the current dungeon.
     * @return player position
     */
    public Position getPlayerPosition() {
        return new Position(player.getPosition().getX(), player.getPosition().getY());
    }

    /**
     * Gets all the enemies that exist in the current dungeon.
     * @return list of enemies
     */
    public List<Enemy> getEnemies() {
        return enemies;
    }

    /**
     * Sets the player for the current instantiated dungeon.
     * @param player - current player in the dungeon (there can only be one)
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * Adds an enemy to the current instantiated dungeon.
     * @param enemy - enemy that is added to the dungeon
     */
    public void addEnemy(Enemy enemy) {
        if (!this.enemies.contains(enemy))
            this.enemies.add(enemy);
    }

    /**
     * Gets a list of all entities that are in the specified position in the current
     * instantiated dungeon.
     * 
     * @param position - position in which entities are being checked
     * @return list of all entities in the specified position
     */
    public List<Entity> getEntitiesAtPosition(Position position) {
        return this.entities.stream().
                filter(e -> Objects.equals(e.getPosition(), position)).
                collect(Collectors.toList());
    }

    /**
     * @return list of all entities in the current instantiated dungeon as EntityResponse Objects
     */
    public List<EntityResponse> getEntityResponses() {
        return this.entities.stream().
                map(Entity::getEntityResponse).
                collect(Collectors.toList());
    }

    public List<ItemResponse> getItemResponses() {
        List<Entity> inventoryItems = player.getInventoryItems();
        return inventoryItems.stream().
                map(entity -> new ItemResponse(entity.getId(), entity.getType())).
                collect(Collectors.toList());
    }


    /**
     * Removes an entity from the current instantiated dungeon.
     * @param entity - entity that is removed from the dungeon
     */
    public void removeEntity(Entity entity) {
        this.entities.remove(entity);
        if (entity instanceof Enemy) this.enemies.remove(entity);
    }

    public List<String> getBuildables() {
        return BuildableObjectFactory.getBuildables(player.getPlayerInventory());
    }

    /**
     * Checks for a zombieToastSpawner, if true, calls spawnToast from ZombieToastSpawner.
     * Starts a battle between the player and an enemy. Checks if the enemy is a mercenary
     * if true, battle occurs. if mercenary is bribed, mercenary fights other enemies.
     * Increments currentTick
     * 
     * @param itemUsed
     * @param movementDirection
     */
    public void tick(String itemUsed, Direction movementDirection) {

        // Move the player / Use any items that must be used
        this.player.move(movementDirection);
        this.player.useItem(itemUsed);

        // Update any current enemies of the player's status
        this.enemies.forEach(x->x.update(this.player));

        // Move all enemies
        this.enemies.forEach(Enemy::move);
        manageBattles();


        // Spawn any new enemies
        enemySpawner.spawnEnemies();
        
        // Increment dungeon tick counter
        this.currentTick += 1;
    }

    /**
     * Manages any possible battles that can occur in the current tick.
     */
    private void manageBattles() {
        // Start new battle round
        Enemy battleEnemy = this.enemies.stream().filter(enemy -> Battle.canFight(this.player, enemy)).findAny().orElse(null);

        // No battle enemy found
        if (battleEnemy == null) return;

        Boolean battle_occurred = false;
        while (Battle.canFight(this.player, battleEnemy)) {
            battle_occurred = true;
            Battle.fight(this, this.player, battleEnemy);
            allyAttack(battleEnemy);
        } 

        // Remove the dead parties
        if (!battleEnemy.isAlive()) removeEntity(battleEnemy);
        if (!player.isAlive()) removeEntity(player);

        if (battle_occurred) mercenaryDoubleMovement();        
    }

    /**
     * Battle implementation for mercenary. If an enemy mercenary is in battle radius of player,
     * it moves twice in a single tick. However, if the mercenary has been bribed to become an ally,
     * it attacks the enemy that the player is fighting.
     * 
     * @param enemy - enemy to attack
    */
    private void allyAttack(Enemy enemy) {                
       this.enemies.forEach(otherEnemy -> {
           // check if there is an ally in battle radius
           if (allyInRadius(otherEnemy, enemy)) {
                // Attack only if the battle is still going and the player and enemy are not dead.
                if (Battle.canFight(this.player, enemy)){
                    ((Mercenary)otherEnemy).attackEnemyAsAlly(enemy);
                    //enemy.decreaseEnemyHealth(otherEnemy, enemy.getAttackDamage());
                } //Battle.attack(otherEnemy, enemy);
           }
       });
    }
    
    /**
     * Checks if specified possibleAlly is an ally mercenary in battle radius.
     * 
     * @param possibleAlly - possible ally that can attack enemy
     * @param enemy - enemy under attack
     * @return true if ally and enemy is in battle radius, false otherwise
     */
    private boolean allyInRadius(Enemy possibleAlly, Enemy enemy) {
        return !Objects.equals(possibleAlly.getId(), enemy.getId()) 
               && (possibleAlly instanceof Mercenary)
               && (((Mercenary)possibleAlly).isAlly())
               && (((Mercenary)possibleAlly).inBattleRadius());
    }

    /**
     * Manages double movement for mercenary (occurs if player has a battle)
     */
    private void mercenaryDoubleMovement() {
        this.enemies.forEach(enemy -> {
            if (enemy instanceof Mercenary
                && ((Mercenary)enemy).inBattleRadius()){
                    enemy.move();   
            }
        });
    }

    public int getCurrentTick() {
        return this.currentTick;
    }


    /**
     * Helps player interact with an entity in the dungeon.
     * 
     * @param entityId
     */
    public void interact(String entityId) {
        Entity entity = this.entities.stream().filter(e -> e.getId().equals(entityId)).findFirst().orElse(null);
        if (entity == null) {
            throw new IllegalArgumentException("No entity is specified!");
        } else if (!entity.isInteractable()) {
            throw new IllegalArgumentException("Entity is not interactable!");
        }

        this.player.interact(entity);        
    }
    
    /**
     * @return dungeonResponse for the current dungeon.
     */
    public DungeonResponse getDungeonResponse() {
        return new DungeonResponse(
                this.dungeonInfo.getId(),
                this.dungeonInfo.getName(),
                getEntityResponses(),
                getItemResponses(),
                getBuildables(),
                getGoalsAsString());
    }

    /**
     * adds an entity to the current dungeon
     * @param entity
     */
    public void addEntity(Entity entity) {
        this.entities.add(entity);
    }

    /**
     * Builds an item of specified type.
     * @param type - type of entity (constant that must be provided in DungeonConstants file)
     * @return entity object of the item that is built
     * @throws IllegalArgumentException - if item type is not a buildable entity (see spec.)
     * @throws InvalidActionException - if player does not have sufficient items to craft the buildable
     */
    public Entity build(String type) throws IllegalArgumentException, InvalidActionException {
        if(Objects.equals(type, DungeonConstants.ENTITY_MIDNIGHT_ARMOUR) && !MidnightArmour.canExist(this)){
            return null;
        }

        return BuildableObjectFactory.build(type, this.getPlayer());
    }
    
    /*
     * Checks if the specified position is on the current dungeon
     * @param position
     * @return true if it is a valid cell, false otherwise.
     */
    public boolean validCell(Position position) {
        return position.getX() >= 0 && position.getX() < this.getWidth() && 
                position.getY() >= 0 && position.getY() < this.getHeight();
    }

    public void setCurrentTick(int currentTick) {
        this.currentTick = currentTick;
    }
}
