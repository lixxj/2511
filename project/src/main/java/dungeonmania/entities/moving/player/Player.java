package dungeonmania.entities.moving.player;

import java.util.List;
import java.util.Objects;

import dungeonmania.entities.collectables.*;
import dungeonmania.entities.collectables.potions.*;
import dungeonmania.entities.staticitems.*;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.entities.buildables.*;
import dungeonmania.entities.*;
import dungeonmania.entities.moving.Character;
import dungeonmania.entities.moving.enemies.*;
import dungeonmania.dungeon.*;
import dungeonmania.util.*;
import org.json.JSONArray;
import org.json.JSONObject;

public class Player extends Character {

    private Dungeon dungeon;
    private Inventory inventory;
    private boolean invincible;
    private boolean invisible;
    private Weapon weapon;
    private Protection protection;
    private int invincibilityTime = 0;
    private int maxHealth;

    /**
     * Creates an instance of a player
     * 
     * @param dungeon - current dungeon that the player is in
     * @param entityInfo - entityInfo object
     */
    public Player(Dungeon dungeon, EntityInfo entityInfo) {
        super(entityInfo);
        this.dungeon = dungeon;
        this.inventory = new Inventory();

        if (Objects.equals(dungeon.getMode(), DungeonConstants.MODE_HARD)) {
            this.maxHealth = DungeonConstants.DEFAULT_HEALTH_HARD;
            this.setAttackDamage(DungeonConstants.PLAYER_ATTACK_DAMAGE_HARD);
        } else {
            this.maxHealth = DungeonConstants.DEFAULT_HEALTH;
            this.setAttackDamage(DungeonConstants.PLAYER_ATTACK_DAMAGE);
        }
        
        this.setHealth(maxHealth);

        this.invincible = false;
        this.invisible = false;
    }

    /**
     * Moves the player in the specified direction.
     * @param movementDirection - direction in which player should be moved
     */
    public void move(Direction movementDirection) {
        Position currentPosition = this.getPosition();
        if (isMoveAllowed(currentPosition, movementDirection)) {
            currentPosition = currentPosition.translateBy(movementDirection);
            this.setPosition(currentPosition);
        }
    }

    /**
     * Actions usage/interaction of the specified item type as per specification.
     * 
     * @param usedItemId - unique entity id corresponding to the item that is being used
     * @throws IllegalArgumentException - if item is not a bomb, potion or null (not specified)
     * @throws InvalidActionException - if item does not exist in player's inventory
     */
    public void useItem(String usedItemId) throws IllegalArgumentException, InvalidActionException {
        if (usedItemId == null) {
            return;
        }

        Entity item = this.inventory.getItem(usedItemId);

        if (item == null) {
            throw new InvalidActionException("Item does not exist in inventory!");
        }

        if (item instanceof Bomb) {
            Bomb bomb = ((Bomb)item);
            bomb.setPosition(getPosition());
            this.dungeon.addEntity(bomb);
            // Explode bomb if it can
            if (bomb.canExplodeBomb(this.dungeon, this.getPosition()))
                bomb.explodeBomb(dungeon);
        } else if (item instanceof Potion) {
            ((Potion)item).consumePotion(this);
        } else {
            throw new IllegalArgumentException("Item cannot be used!");
        }

        // Remove item from inventory
        this.inventory.removeItemWithId(usedItemId);
    }

    /**
     * Checks if a move can occur in the specified direction.
     * 
     * @param currentPosition - position from which player is trying to move
     * @param movementDirection - direction in which player wants to move
     * @return true if move is allowed, false otherwise.
     */
    private boolean isMoveAllowed(Position currentPosition, Direction movementDirection) {
        Position movementPosition = currentPosition.translateBy(movementDirection);
        List<Entity> entities = this.dungeon.getEntitiesAtPosition(movementPosition);

        if (entities.isEmpty()) {
            return true;
        }

        for (Entity entity: entities) {
             if (entity instanceof StaticEffect) {
                 return ((StaticEffect) entity).moveAllowedPlayer(this.dungeon, movementDirection);
             }
             if (entity.isCollectible()) {
                if (!(entity instanceof Key && this.inventory.countItemsByType(DungeonConstants.ENTITY_KEY) > 0)) {
                    this.inventory.add(entity);
                    this.dungeon.removeEntity(entity);
                }
                if (entity instanceof Weapon){
                    this.weapon = (Weapon)entity;
                } 
                if (entity instanceof Protection){
                    this.protection = (Protection)entity;
                }
                return true;
             } else if (entity instanceof Enemy) return true;
         }
        return false;
    }

    /**
     * Adds the specified entity to the player's inventory
     * 
     * @pre item must be an Entity and cannot be null
     * @param item - item that must be added to the inventory
     */
    public void addItemToInventory(Entity item) {
        this.inventory.add(item);
    }

    /**
     * Checks if the itemId corresponds to an item in the player's inventory
     * @param itemId - unique entity id of the item
     * @return true if item exists in inventory, false otherwise
     */
    public boolean checkItemExists(String itemId) {
        return this.inventory.checkItemExists(itemId);
    }

    /**
     * Checks if the item type corresponds to an item in the player's inventory
     * @param itemType - type of entity (constant that must be provided in DungeonConstants file)
     * @return true if item exists in inventory, false otherwise
     */
    public boolean hasItemOfType(String itemType) {
        return this.inventory.countItemsByType(itemType) > 0;
    }


    /**
     * Checks if the player has consumed an invincibility potion and is invincible.
     * @return true if invincible, false otherwise.
     */
    public boolean isInvincible() {
        if (this.invincible && (dungeon.getCurrentTick() > invincibilityTime)) {
            invincibilityTime = 0;
            makeNotInvincible();
        }
        return this.invincible;
    }

    public void setInvincibilityTime(int time) {
        this.invincibilityTime = dungeon.getCurrentTick() + time;
    }

    /**
     * Checks if the player has consumed an invisibility potion and is invisible.
     * @return true if invisible, false otherwise.
     */
    public boolean isInvisible() {
        return this.invisible;
    }

    public Entity getItemByType(String type){
        return inventory.getItemByType(type);
    }

    public void removeItemFromInventory(String itemId){
        inventory.removeItemWithId(itemId);
    }

    /**
     * Resets the character's health to their max default health.
     */
    public void consumeHealthPotion() {
        this.setHealth(this.maxHealth);
    }

    public void consumeInvisibilityPotion() {
        if(dungeon.getMode().equals(DungeonConstants.MODE_HARD)) {
            return;
        }
        dungeon.getCurrentTick();
        this.invisible = true;
    }
    
    public void consumeInvincibilityPotion() {
        this.invincible = true;
        notifyObservers();
    }

    private void makeNotInvincible() {
        this.invincible = false;
        notifyObservers();
    }
    
    private void notifyObservers() {
        this.dungeon.getEnemies().forEach(e -> e.update(this));
    }

    /**
     * Checks if the player holds a weapon.
     * @return true if a weapon is equipped, false otherwise.
     */
    public boolean hasWeapon() {
        return (this.weapon != null);
    }

    /**
     * Gets the weapon that is currently equipped by the player.
     * @return weapon that is currently equipped, null if no weapon is equipped
     */
    public Weapon getWeapon() {
        return this.weapon;
    }

    /**
     * Uses the one ring.
     * @pre method called when the user is dead
     * @pre player must have the one ring
     */
    public void useOneRing() {
        this.setHealth(this.maxHealth);
        this.inventory.removeItemsByType(DungeonConstants.ENTITY_ONE_RING, 1);
    }


    public void interact(Entity entity) {

        if (entity instanceof Mercenary && !((Mercenary)entity).isAlly()) {
            bribe((Mercenary) entity);
        } else if (entity instanceof ZombieToastSpawner) {
            destroyZombieToastSpawner((ZombieToastSpawner) entity);
        }
        
    }

    private void bribe(Mercenary mercenary) throws InvalidActionException {
        List<Position> adjacentPositions = this.getPosition().getAdjacentPositions();
        boolean nearby = adjacentPositions.stream().anyMatch(position -> {
            List<Position> positions = position.getAdjacentPositions();
            return positions.stream().anyMatch(x -> Objects.equals(x, mercenary.getPosition()));
        });

        if (!nearby) {
            throw new InvalidActionException("Player is not within 2 cardinal tiles of mercenary");
        }

        if (this.hasItemOfType(DungeonConstants.ENTITY_SCEPTRE)) {
            mercenary.bribe(Sceptre.getMindControlTime());
            return;
        }

        int sunStoneCount = inventory.countItemsByType(DungeonConstants.ENTITY_SUN_STONE);
        int goldAmount =  inventory.countItemsByType(DungeonConstants.ENTITY_TREASURE);
        if ((goldAmount  + sunStoneCount) < mercenary.getBribeAmount()) {
            throw new InvalidActionException("Player does not have enough gold or Sunstones to bribe mercenary");
        }

        if (mercenary instanceof Assassin) {
            if (!hasItemOfType(DungeonConstants.ENTITY_ONE_RING)) {
                throw new InvalidActionException("Player does not have the One Ring to bribe Assassin with");
            } else {
                inventory.removeItemsByType(DungeonConstants.ENTITY_ONE_RING, 1);
            }
        }

        mercenary.bribe();
        if (sunStoneCount > 0) {
            if (sunStoneCount > mercenary.getBribeAmount()) {
                inventory.removeItemsByType(DungeonConstants.ENTITY_SUN_STONE, mercenary.getBribeAmount());
                return;
            } else {
                inventory.removeItemsByType(DungeonConstants.ENTITY_SUN_STONE, sunStoneCount);
            }
        }
        inventory.removeItemsByType(DungeonConstants.ENTITY_TREASURE, mercenary.getBribeAmount() - sunStoneCount);
    }

    private void destroyZombieToastSpawner(ZombieToastSpawner zombieToastSpawner) throws InvalidActionException {
        List<Position> adjacentPositions = zombieToastSpawner.getPosition().getAdjacentPositions();
        boolean inAdjacentPosition = adjacentPositions.stream().anyMatch(a -> a.equals(this.getPosition()));

        if (!inAdjacentPosition) {
            throw new InvalidActionException("Player is not cardinally adjacent to the spawner");
        }

        if (!hasWeapon()) {
            throw new InvalidActionException("Player does not have a weapon");
        }
        
        dungeon.removeEntity(zombieToastSpawner);
    }

    public List<Entity> getInventoryItems() {
        return inventory.getItems();
    }

    @Override
    public JSONObject getEntityStateAsJSON() {
        JSONObject entityObject = super.getEntityStateAsJSON();
        entityObject.put("invincible", invincible);
        entityObject.put("invisible", invisible);
        entityObject.put("invincibilityTime", invincibilityTime);
        entityObject.put("invisible", invisible);
        entityObject.put("invisible", invisible);
        JSONArray inventoryJson = new JSONArray();
        for (Entity entity: getInventoryItems()) {
            inventoryJson.put(entity.getEntityStateAsJSON());
        }
        entityObject.put("inventory", inventoryJson);
        return entityObject;
    }

    public int countItems(String type){
        return inventory.countItemsByType(type);
    }

    public void attackEnemy(Enemy enemy){
        if (this.hasWeapon()) {
            Weapon weapon = this.getWeapon();
            weapon.attack(this, enemy);
            if(weapon.getDurability() <= 0){
                removeItemFromInventory(weapon.getId());
                this.weapon = null;
            }
        } else {
            enemy.decreaseEnemyHealth(this, getAttackDamage());
        }
    }

    public boolean hasProtection(){
        return protection != null && protection.hasProtection();
    }

    public void decreasePlayerHealth(Enemy enemy, int totalAttackDamage) {
        if (!hasProtection()) {
            //int totalAttackDamage = player.getAttackDamage() + attackDamage;
            this.setHealth(getHealth() - ((enemy.getHealth() * totalAttackDamage) / 10));
        } else {
            this.setHealth(getHealth() - (enemy.getHealth() * totalAttackDamage) / (int)(10 * this.protection.getProtectionFactor()));
            protection.decreaseDurability();
            if(protection.getDurability() <= 0){
                removeItemFromInventory(((Entity)protection).getId());
                this.protection = null;
            }
        }
    }

    public void setProtection(Protection protection){
        this.protection = protection;
    }

    public Inventory getPlayerInventory(){
        return this.inventory;
    }

    public void setWeapon(Weapon weapon){
        this.weapon = weapon;
    }
}

