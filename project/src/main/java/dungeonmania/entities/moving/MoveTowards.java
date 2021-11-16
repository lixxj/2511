package dungeonmania.entities.moving;

import java.util.Objects;

import dungeonmania.dungeon.*;
import dungeonmania.util.Position;

public class MoveTowards extends MovementBehaviour {
    
    public MoveTowards(Dungeon dungeon, Position startingPosition) {
        super(dungeon, startingPosition);
    }

    @Override
    public Position move() {

        Position playerPosition = getDungeon().getPlayerPosition();

        // On the player's position --> no need to move
        if (Objects.equals(playerPosition, getCurrentPosition())) return getCurrentPosition();
        if (getSwampTime() != 0) {
            decreaseSwampTime();
            return getCurrentPosition();
        }
        // Surrounded by obstacles --> cannot move
        if (getCurrentPosition().getAdjacentMovementPositions().stream().allMatch(x->checkCollision(x))) return getCurrentPosition();

        Graph g = new Graph(getDungeon());
        Position newPosition = g.nextPathDjikstra(getCurrentPosition(), playerPosition);

        if (newPosition == null) return getCurrentPosition(); // No path exists

        setCurrentPosition(newPosition);
        manageTeleport();
        manageSwamp();

        return getCurrentPosition(); 
    }
    
}
