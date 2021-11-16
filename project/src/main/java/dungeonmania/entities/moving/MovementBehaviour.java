package dungeonmania.entities.moving;

import java.util.List;

import dungeonmania.entities.Entity;
import dungeonmania.entities.staticitems.*;
import dungeonmania.dungeon.*;
import dungeonmania.util.Position;

public abstract class MovementBehaviour {

    private Dungeon dungeon;
    private Position currentPosition;
    private int swampTime;

    /**
     * Constructor.
     * 
     * @param dungeon - current dungeon in which the movement strategy is used
     * @param position - initial position of the entity using this movement strategy
     */
    public MovementBehaviour(Dungeon dungeon, Position position) {
        this.dungeon = dungeon;
        this.currentPosition = position;
        this.swampTime = 0;
    }

    /**
     * Suggests the next position that the movable entity should move to
     * @return new position as per strategy
     */
    public abstract Position move();

    /**
     * Checks if the entity with the movement strategy collides when moved to
     * the specified position
     * @param position - position in which the method checks for obstacles
     */
    public boolean checkCollision(Position position) {
        return getDungeon().getEntitiesAtPosition(position)
        .stream().anyMatch(x -> {
            if (x instanceof Wall || x instanceof Boulder || 
                x instanceof ZombieToastSpawner) {
                return true;
            } else if (x instanceof Door) {
                return (((Door)x).isDoorClosed()); // true if door is closed
            }
            return false;
        }) || !getDungeon().validCell(position);
    }

    /**
     * If a move is into a portal, teleports the entity.
     */
    public void manageTeleport() {
        List<Entity> dungeonEntities = dungeon.getEntitiesAtPosition(this.currentPosition);
        Portal portal = (Portal) dungeonEntities.stream()
                        .filter(entity -> entity instanceof Portal)
                        .findFirst().orElse(null);
        if (portal == null) return;
        setCurrentPosition(portal.getCorrespondingSpawnPoint(dungeon));
    }

    public void manageSwamp() {
        List<Entity> dungeonEntities = getDungeon().getEntitiesAtPosition(getCurrentPosition());
        SwampTile swamp = (SwampTile) dungeonEntities.stream()
            .filter(entity -> entity instanceof SwampTile)
            .findFirst().orElse(null);
        if (swamp == null) return;
        setSwampTime(swamp.getMovementFactor());
        decreaseSwampTime();
    }

    public int getSwampTime() {
        return this.swampTime;
    }

    public void setSwampTime(int time) {
        this.swampTime = time;
    }

    public void decreaseSwampTime() {
        this.swampTime -= 1;
    }

    /**
     * Sets the position in the movement strategy. Used when initialising an
     * existing movement strategy.
     * 
     * @param position - new current position
     */
    public void setCurrentPosition(Position position) {
        this.currentPosition = position;
    }

    // GETTER - current dungeon
    public Dungeon getDungeon() {
        return this.dungeon;
    }

    // GETTER - current position
    public Position getCurrentPosition() {
        return this.currentPosition;
    }

}
