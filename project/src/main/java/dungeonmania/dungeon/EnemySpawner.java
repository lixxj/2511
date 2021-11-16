package dungeonmania.dungeon;

import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import dungeonmania.entities.DungeonConstants;
import dungeonmania.entities.Entity;
import dungeonmania.entities.EntityInfo;
import dungeonmania.entities.collectables.Armour;
import dungeonmania.entities.moving.enemies.*;
import dungeonmania.entities.staticitems.ZombieToastSpawner;
import dungeonmania.util.Position;

public class EnemySpawner {

    private Dungeon dungeon;
    private Random rand = new Random();
    
    public EnemySpawner(Dungeon dungeon) {
        this.dungeon = dungeon;
    }

    /**
     * Spawns new enemies if they can be spawned.
     */
    public void spawnEnemies() {

        spawnZombie();
        spawnHydra(getEntrySpawnPoint());
        spawnMercenary(getEntrySpawnPoint());
        spawnSpider(getRandomSpawnPoint());

    }

    /**
     * Uses any zombie toast spawner currently in the dungeon to spawn zombie toasts.
     */
    private void spawnZombie() {
        if (!canZombieSpawn()) return;
        
        List<Entity> zombieToastSpawners = this.dungeon.getEntities().stream().
        filter(entity -> entity instanceof ZombieToastSpawner).
        collect(Collectors.toList());

        zombieToastSpawners.forEach(entity -> {
            ((ZombieToastSpawner)entity).spawnToast(this.dungeon);
        });
    }

    /**
     * Zombies spawn every 15 ticks in hard mode and every 20 ticks in other modes.
     * This method checks if the method can spawn in the current dungeon state.
     * @return true if zombie can spawn, false otherwise.
     */
    private boolean canZombieSpawn() {
        int tick = this.dungeon.getCurrentTick();
        if (Objects.equals(DungeonConstants.MODE_HARD, this.dungeon.getMode())) return tick % 15 == 0 && tick >= 15;
        return tick % 20 == 0 && tick >= 20;
    }

    /**
     * Manages hydra spawning behaviour.
     * 
     * @param spawnPosition - position in which 
     */
    private void spawnHydra(Position spawnPosition) {
                
        if (!Objects.equals(this.dungeon.getMode(), DungeonConstants.MODE_HARD)) return;
        if (!canHydraSpawn()) return;  

        addHydra(spawnPosition);

    }

    private boolean canHydraSpawn() {
        int tick = this.dungeon.getCurrentTick();
        return tick % 50 == 0 && tick > 0;
    }

    private void spawnMercenary(Position spawnPosition) {

        if (!canMercenarySpawn()) return;

        if (canAssassinSpawn()) {
            addAssassin(spawnPosition);
        } else {
            addMercenary(spawnPosition);
        }

    }

    private boolean canMercenarySpawn() {
        int tick = this.dungeon.getCurrentTick();
        return tick % 21 == 0 && tick > 0;
    }

    /**
     * Checks probability of an assassin spawning (30% chance)
     * 
     * @pre can spawn in current dungeon state
     * @return true if assassin can spawn instead of mercenary 
     */
    private boolean canAssassinSpawn(){
        return Math.random() < 0.3;
    }

    private void spawnSpider(Position spawnPosition) {
        if (!canSpiderSpawn()) return;  
        addSpider(spawnPosition);
    }

    /**
     * Checks if a spider can spawn into the current dungeon.
     * @return true if the number of spiders in the dungeon is less than 4, false otherwise.
     */
    private boolean canSpiderSpawn() {
        return this.dungeon.getEnemies().stream()
        .filter(x -> Objects.equals(x.getType(), DungeonConstants.ENTITY_SPIDER))
        .collect(Collectors.toList()).size() < 4;
    }

    private Position getRandomSpawnPoint() {
        int x = rand.nextInt(this.dungeon.getWidth());
        int y = rand.nextInt(this.dungeon.getHeight());
        return new Position(x, y);
    }

    private Position getEntrySpawnPoint() {
        Position spawnPosition = new Position(1, 1);
        if (!this.dungeon.getEntitiesAtPosition(spawnPosition).isEmpty()) {
            List<Position> adjPositions = spawnPosition.getAdjacentPositions();
            spawnPosition = adjPositions.stream().filter(x -> this.dungeon.getEntitiesAtPosition(x).isEmpty()).findAny().orElse(null);
        }
        return spawnPosition;
    }
    
    /**
     * Checks if an armour can be added to an enemy. 20% chance.
     * @return
     */
    private boolean canAddArmour() {
        return (Math.random() * 10) < 20;
    }

    /**
     * generates a random id.
     * @return entity id
     */
    private String generateRandomId() {
        return UUID.randomUUID().toString();
 
    }

    /**
     * Adds hydra to entities list and enemies list
     * @param spawnPosition
     */
    private void addHydra(Position spawnPosition){
        Entity newHydra = new Hydra(dungeon, 
                                new EntityInfo(generateRandomId() + "-" + DungeonConstants.ENTITY_HYDRA, spawnPosition,
                                DungeonConstants.ENTITY_HYDRA, false));
        this.dungeon.addEntity(newHydra);
        this.dungeon.addEnemy((Enemy) newHydra);
    }

    /**
     * Adds mercenary to entities list and enemies list
     * @param spawnPosition
     */
    private void addMercenary(Position spawnPosition) {
        Mercenary newMercenary = new Mercenary(dungeon, 
                                new EntityInfo(generateRandomId() + "-" + DungeonConstants.ENTITY_MERCENARY, spawnPosition,
                                DungeonConstants.ENTITY_MERCENARY, false));
        
        if (canAddArmour()) {
            Armour newArmour = new Armour(new EntityInfo(generateRandomId() + "-" + DungeonConstants.ENTITY_ARMOUR,
                                null, DungeonConstants.ENTITY_ARMOUR, true));
            newMercenary.setProtection(newArmour);
        }

        dungeon.addEntity(newMercenary);
        dungeon.addEnemy((Enemy) newMercenary);
    }
    
    /**
     * Adds assassin to entities list and enemies list
     * @param spawnPosition
     */ 
    private void addAssassin(Position spawnPosition){
        Assassin newAssassin = new Assassin(dungeon, 
                                new EntityInfo(generateRandomId() + "-" + DungeonConstants.ENTITY_ASSASSIN, spawnPosition,
                                DungeonConstants.ENTITY_ASSASSIN, false));

        if (canAddArmour()) {
            Armour newArmour = new Armour(new EntityInfo(generateRandomId() + "-" + DungeonConstants.ENTITY_ARMOUR,
                                null, DungeonConstants.ENTITY_ARMOUR, true));
            newAssassin.setProtection(newArmour);
        }

        dungeon.addEntity(newAssassin);
        dungeon.addEnemy((Enemy) newAssassin);
    }
    
    /**
     * Adds hydra to entities list and enemies list
     * @param spawnPosition
     */
    private void addSpider(Position spawnPosition){
        Entity newSpider = new Spider(dungeon, 
                                new EntityInfo(generateRandomId() + "-" + DungeonConstants.ENTITY_SPIDER, spawnPosition,
                                DungeonConstants.ENTITY_SPIDER, false));
        this.dungeon.addEntity(newSpider);
        this.dungeon.addEnemy((Enemy) newSpider);
    }
}
