package dungeonmania.entities.moving;

import java.util.List;

import dungeonmania.entities.Entity;
import dungeonmania.entities.staticitems.Boulder;
import dungeonmania.entities.staticitems.Portal;
import dungeonmania.dungeon.Dungeon;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class MoveCircle extends MovementBehaviour {
    
    private Position spawnPosition;
    private boolean clockwise;

    public MoveCircle(Dungeon dungeon, Position startingPosition, boolean clockwise) {
        super(dungeon, startingPosition);
        this.spawnPosition = new Position(startingPosition.getX(), startingPosition.getY());
        this.clockwise = clockwise;
    }

    /**
     * Initialises the current position of the entity using this movement strategy. This
     * is done when the current position is equal to the spawn point.
     * @return new position after entity movement initialisation
     */
    private Position initialiseCurrentPosition() {
        if (!checkCollision(getCurrentPosition().translateBy(Direction.UP))) {
            return getCurrentPosition().translateBy(Direction.UP);
        } else if (!checkCollision(getCurrentPosition().translateBy(Direction.RIGHT))) {
            return getCurrentPosition().translateBy(Direction.RIGHT);
        } else if (!checkCollision(getCurrentPosition().translateBy(Direction.DOWN))) {
            return getCurrentPosition().translateBy(Direction.DOWN);
        } else if (!checkCollision(getCurrentPosition().translateBy(Direction.LEFT))){
            return getCurrentPosition().translateBy(Direction.LEFT);
        }

        return getCurrentPosition();
    }

    @Override
    public Position move() {

        if (getSwampTime() != 0) {
            decreaseSwampTime();
            return getCurrentPosition();
        }

        Position newPosition;

        if (getCurrentPosition().equals(this.spawnPosition)) {
            newPosition = initialiseCurrentPosition();
        } else {
            if (this.clockwise) {
                newPosition = moveClockwise();
                if (newPosition == null) return getCurrentPosition(); // Cannot Move
                if (checkCollision(newPosition)) { // Hits Boulder
                    this.clockwise = false; // Reverse direction
                    return getCurrentPosition();
                }
            } else {
                newPosition = moveAntiClockwise();
                if (newPosition == null) return getCurrentPosition(); // Cannot Move
                if (checkCollision(newPosition)) { // Hits Boulder
                    this.clockwise = true; // Reverse direction
                    return getCurrentPosition();
                }
            }
        }

        setCurrentPosition(newPosition);      
        manageTeleport();
        manageSwamp();
        
        return getCurrentPosition();
    }

    @Override
    public void manageTeleport() {
        List<Entity> dungeonEntities = getDungeon().getEntitiesAtPosition(getCurrentPosition());
        Portal portal = (Portal) dungeonEntities.stream()
                        .filter(entity -> entity instanceof Portal)
                        .findFirst().orElse(null);
        if (portal == null) return;
        setCurrentPosition(portal.getCorrespondingSpawnPoint(getDungeon()));
        this.spawnPosition = getCurrentPosition();
    }

    @Override
    public boolean checkCollision(Position position) {
        return (getDungeon().getEntitiesAtPosition(position).stream().anyMatch(entity -> entity instanceof Boulder)) || !getDungeon().validCell(position);
    }
    
    /**
     * Moves the entity in a clockwise direction and returns the new position.
     * NOTE: this method does NOT modify the current position of the entity
     * using this movement strategy.
     * @return new position if the entity was moved.
     */
    private Position moveClockwise() {
        Position diff = Position.calculatePositionBetween(this.spawnPosition, getCurrentPosition());

        if (diff.equals(new Position(0,-1)) || diff.equals(new Position(-1,-1))) { 
            return getCurrentPosition().translateBy(Direction.RIGHT);
        } else if (diff.equals(new Position(1,-1)) || diff.equals(new Position(1,0))) {
            return getCurrentPosition().translateBy(Direction.DOWN);
        } else if (diff.equals(new Position(1,1)) || diff.equals(new Position(0,1))) {
            return getCurrentPosition().translateBy(Direction.LEFT);
        } else if (diff.equals(new Position(-1,1)) || diff.equals(new Position(-1,0))) {
            return getCurrentPosition().translateBy(Direction.UP);
        }

        return null;
    }

    /**
     * Moves the entity in an anti-clockwise direction and returns the new position.
     * NOTE: this method does NOT modify the current position of the entity
     * using this movement strategy.
     * @return new position if the entity was moved.
     */
    private Position moveAntiClockwise() {
        Position diff = Position.calculatePositionBetween(this.spawnPosition, getCurrentPosition());

        if (diff.equals(new Position(0,-1)) || diff.equals(new Position(1,-1))) {
            return getCurrentPosition().translateBy(Direction.LEFT);
        } else if (diff.equals(new Position(-1,-1)) || diff.equals(new Position(-1,0))) {
            return getCurrentPosition().translateBy(Direction.DOWN);
        } else if (diff.equals(new Position(-1,1)) || diff.equals(new Position(0,1))) {
            return getCurrentPosition().translateBy(Direction.RIGHT);
        } else if (diff.equals(new Position(1,1)) || diff.equals(new Position(1,0))) {
            return getCurrentPosition().translateBy(Direction.UP);
        }

        return null;
    }
}
