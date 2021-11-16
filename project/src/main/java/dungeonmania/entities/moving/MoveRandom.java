package dungeonmania.entities.moving;

import dungeonmania.dungeon.Dungeon;
import java.util.Random;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class MoveRandom extends MovementBehaviour {
    
    private static final int UP = 0;
    private static final int DOWN = 1;
    private static final int LEFT = 2;
    private static final int RIGHT = 3;
    private static Random rand = new Random();

    public MoveRandom(Dungeon dungeon, Position startingPosition) {
        super(dungeon, startingPosition);
    }

    @Override
    public Position move() {

        if (getSwampTime() != 0) {
            decreaseSwampTime();
            return getCurrentPosition();
        }

        Position newPosition = getCurrentPosition();

        int numTries = 0;
        while (newPosition == getCurrentPosition() && numTries < 10) {
            int movement = rand.nextInt(4);
            if (movement == UP) {
                newPosition = moveUp();
            } else if (movement == DOWN) {
                newPosition = moveDown();
            } else if (movement == RIGHT) {
                newPosition = moveRight();
            } else if (movement == LEFT) {
                newPosition = moveLeft();
            }
            numTries++;
        }
        
        setCurrentPosition(newPosition);
        manageTeleport();
        manageSwamp();

        return getCurrentPosition();
    }

    /**
     * Checks if the entity can move up.
     * @return new position based on whether the move can occur or not
     */
    private Position moveUp() {
        if (checkCollision(getCurrentPosition().translateBy(Direction.UP))) return getCurrentPosition();
        return getCurrentPosition().translateBy(Direction.UP);
    }

    /**
     * Checks if the entity can move down.
     * @return new position based on whether the move can occur or not
     */
    private Position moveDown() {
        if (checkCollision(getCurrentPosition().translateBy(Direction.DOWN))) return getCurrentPosition();
        return getCurrentPosition().translateBy(Direction.DOWN);
    }

    /**
     * Checks if the entity can move right.
     * @return new position based on whether the move can occur or not
     */
    private Position moveRight() {
        if (checkCollision(getCurrentPosition().translateBy(Direction.RIGHT))) return getCurrentPosition();
        return getCurrentPosition().translateBy(Direction.RIGHT);
    }

    /**
     * Checks if the entity can move left.
     * @return new position based on whether the move can occur or not
     */
    private Position moveLeft() {
        if (checkCollision(getCurrentPosition().translateBy(Direction.LEFT))) return getCurrentPosition();
        return getCurrentPosition().translateBy(Direction.LEFT);
    }
}
