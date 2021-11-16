package dungeonmania.entities.staticitems;

import java.util.List;
import java.util.UUID;

import dungeonmania.entities.DungeonConstants;
import dungeonmania.entities.Entity;
import dungeonmania.entities.EntityInfo;
import dungeonmania.entities.collectables.Armour;
import dungeonmania.dungeon.Dungeon;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import dungeonmania.entities.moving.enemies.Enemy;
import dungeonmania.entities.moving.enemies.ZombieToast;

public class ZombieToastSpawner extends Entity implements StaticEffect {

    /**
     * Class Constructer.
     * @param entityInfo     entityInfo object
     */
    public ZombieToastSpawner(EntityInfo entityInfo){
        super(entityInfo);
    }

    @Override
    public boolean moveAllowedPlayer(Dungeon dungeon, Direction movementDirection) {
        return false;
    }


    /**
     * Finds empty adjacent squares and adds a zombie to any one of the empty adjacent squares.
     * @param dungeon
     */
    public void spawnToast(Dungeon dungeon) {

        List<Position> adjacentPositions = this.getPosition().getAdjacentPositions();
        Position spawnPosition = adjacentPositions.stream()
                                    .filter(position -> dungeon.validCell(position) && dungeon.getEntitiesAtPosition(position).isEmpty())
                                    .findAny().orElse(null);
        if (spawnPosition == null) return; // No free spaces available 

        addZombie(dungeon, spawnPosition);

    }



    private void addZombie(Dungeon dungeon, Position position) {
        ZombieToast newZombie = new ZombieToast(dungeon, 
                            new EntityInfo(generateRandomId() + "-" + DungeonConstants.ENTITY_ZOMBIE_TOAST, position, 
                            DungeonConstants.ENTITY_ZOMBIE_TOAST, false));

        if (canAddArmour()) newZombie.setProtection(new Armour(new EntityInfo(generateRandomId() + "-" + DungeonConstants.ENTITY_ARMOUR, null, DungeonConstants.ENTITY_ARMOUR, true)));

        dungeon.addEntity(newZombie);
        dungeon.addEnemy((Enemy)newZombie);
    }

    /**
     * Checks if an armour can be added to a zombie. 30% chance.
     * @return
     */
    private boolean canAddArmour() {
        return (Math.random() * 10) < 3;
    }

    // Generate random Id
    private String generateRandomId() {
        return UUID.randomUUID().toString();
    }

}
