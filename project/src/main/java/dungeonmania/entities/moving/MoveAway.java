package dungeonmania.entities.moving;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import dungeonmania.dungeon.Dungeon;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class MoveAway extends MovementBehaviour {
    
    public MoveAway(Dungeon dungeon, Position startingPosition) {
        super(dungeon, startingPosition);
    }

    @Override
    public Position move() {

        if (getSwampTime() != 0) {
            decreaseSwampTime();
            return getCurrentPosition();
        }

        Position playerPosition = getDungeon().getPlayerPosition();

        List<Position> possibleMoves = new ArrayList<>();

        if (!checkCollision(getCurrentPosition().translateBy(Direction.UP))) {
            possibleMoves.add(getCurrentPosition().translateBy(Direction.UP));
        }
        if (!checkCollision(getCurrentPosition().translateBy(Direction.DOWN))) {
            possibleMoves.add(getCurrentPosition().translateBy(Direction.DOWN));
        }
        if (!checkCollision(getCurrentPosition().translateBy(Direction.LEFT))) {
            possibleMoves.add(getCurrentPosition().translateBy(Direction.LEFT));
        }
        if (!checkCollision(getCurrentPosition().translateBy(Direction.RIGHT))) {
            possibleMoves.add(getCurrentPosition().translateBy(Direction.RIGHT));
        }

        List<Double> distances = possibleMoves.stream().map(newMove ->
            Math.sqrt(Math.pow((newMove.getX() - playerPosition.getX()), 2) +
            Math.pow((newMove.getY() - playerPosition.getY()), 2))
        ).collect(Collectors.toList());

        if (possibleMoves.size() == 0) return getCurrentPosition();

        setCurrentPosition(possibleMoves.get(distances.indexOf(Collections.max(distances))));
        manageTeleport();
        manageSwamp();

        return getCurrentPosition();
    }

}
