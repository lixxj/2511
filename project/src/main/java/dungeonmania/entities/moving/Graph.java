package dungeonmania.entities.moving;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import dungeonmania.entities.Entity;
import dungeonmania.entities.staticitems.Boulder;
import dungeonmania.entities.staticitems.Door;
import dungeonmania.entities.staticitems.SwampTile;
import dungeonmania.entities.staticitems.Wall;
import dungeonmania.entities.staticitems.ZombieToastSpawner;
import dungeonmania.dungeon.Dungeon;
import dungeonmania.util.Position;

public class Graph {

    private Dungeon dungeon;
    private Map<Position, List<Position>> adjacencyList;
    
    /**
     * Constructor to create a graph using a dungeon.
     * 
     * @param dungeon - current dungeon
     */
    public Graph(Dungeon dungeon) {
        this.dungeon = dungeon;
        this.adjacencyList = new HashMap<>();
        createGraph();
    }

    /**
     * Creates a graph (stored using an adjacency list representation) using the dungeon grid. 
     * Graph holds all edges for any movement paths and vertices that cannot be reached 
     * (i.e. obstacles) are removed from the graph.
     */
    private void createGraph() {
        int height = dungeon.getHeight();
        int width = dungeon.getWidth();

        // Add each grid cell into the graph
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Position newPosition = new Position(i, j);
                addVertex(newPosition);
            }
        }

        // Add all adjacent edges that can be moved onto
        for (Position position : adjacencyList.keySet()) {
            addAdjacentEdges(position);
        }

        // Remove all vertexes that have no edges
        // List<Position> disjointVertices = adjacencyList.keySet().stream().filter(x -> adjacencyList.get(x).isEmpty()).collect(Collectors.toList());
        // for (Position position : disjointVertices) adjacencyList.remove(position);
        adjacencyList.values().removeIf(list -> list.isEmpty());
    }

    /**
     * Adds the specified position to the adjacency list vertices.
     * @param position
     */
    private void addVertex(Position position) {
        adjacencyList.putIfAbsent(position, new ArrayList<>());
    }
    
    /**
     * Adds edges to any adjacent positions that can be moved onto
     * @param position1
     */
    private void addAdjacentEdges(Position position1) {

        for (Position position2 : position1.getAdjacentMovementPositions()) {
            if (!isMovementObstacle(position1) && !isMovementObstacle(position2) && dungeon.validCell(position2)) {
                adjacencyList.get(position1).add(new Position(position2.getX(), position2.getY()));
            }
        }

    }

    /**
     * Checks if a position holds a movement obstacle that cannot be moved onto.
     * 
     * @param position - position in which the method checks for obstacles
     * @return true if position cannot be moved onto, false otherwise.
     */
    private boolean isMovementObstacle(Position position) {
        return dungeon.getEntitiesAtPosition(position)
        .stream().anyMatch(x -> {
            if (x instanceof Wall || x instanceof Boulder || 
                x instanceof ZombieToastSpawner) {
                return true;
            } else if (x instanceof Door) {
                return (((Door)x).isDoorClosed()); // true if door is closed
            }
            return false;
        });
    }

    /**
     * Implements Djikstra's algorithm to find the shortest path from src to dest,
     * returns the next move that must be made to follow the shortest path.
     *  
     * @pre assume that src and dest are valid positions in the dungeon grid
     * @param src - source node
     * @param dest - destination node
     * @return next movement position 
     */
    public Position nextPathDjikstra(Position src, Position dest) {

        // Make a queue 
        Queue<Position> q = new LinkedList<>();

        // Make maps holding shortest path (from src to dest) and distances 
        // to reach all vertices in graph
        Map<Position, Position> previous = new HashMap<>();
        Map<Position, Integer> distance = new HashMap<>();

        // Initialise all distance values to maximum and set previous vertex to null
        for (Position vertex : this.adjacencyList.keySet()) {
            previous.put(vertex, null);
            // Source node has distance 0, all other nodes have INFINITE distance 
            // initially
            if (vertex.equals(src)) distance.put(vertex, 0);
            else distance.put(vertex, Integer.MAX_VALUE);
            q.add(vertex);
        }

        while (!q.isEmpty()) {
            /* The first vertex in the queue is the one with the minimum distance
             --> extract it from the queue */
            Position next = getLowestDistanceVertex(q, distance);

            // Get all positions adjacent to next
            for (Position adjacentPosition : adjacencyList.get(next)) {
                int newDistance = distance.get(next) + getMovementCost(next);
                // If the shortest path to adjacentPosition is through next
                if (newDistance < distance.get(adjacentPosition)) {
                    // Update distance of adjacentPosition
                    distance.put(adjacentPosition, newDistance);
                    previous.put(adjacentPosition, next);
                }
            }
        }

        Position p = new Position(dest.getX(), dest.getY());
        
        // Traverse backwards until the previous position is the source node
        while (p != null) {
            if (previous.get(p).equals(src)) return dest;
            if (adjacencyList.get(new Position(src.getX(), src.getY())).contains(previous.get(p))) 
                return previous.get(p);
            p = previous.get(p);
        }

        return p;
    }

    /**
     * Gets the vertex with the smallest distance to reach it and removes it from the queue.
     * 
     * @param queue - queue which holds all unvisited positions
     * @param distance - map that informs the cost of reaching all specified positions in the dungeon
     * @return position that has the shortest distance
     */
    private Position getLowestDistanceVertex(Queue<Position> queue, Map<Position, Integer> distance) {
        Position lowestDistanceVertex = null;
        int lowestDistance = Integer.MAX_VALUE;
        for (Position vertex : queue) {
            int vertexDistance = distance.get(vertex);
            if (vertexDistance <= lowestDistance) {
                lowestDistance = vertexDistance;
                lowestDistanceVertex = vertex;
            }
        }
        queue.remove(lowestDistanceVertex);
        return lowestDistanceVertex;
    }

    /**
     * Returns the movement cost between an edge. This is determined by the nature of the
     * entities on the specified position. Swamp tiles increase the cost of movement by
     * their movement factor.
     * 
     * @param position - position in which this method checks the movement cost
     * @return cost of movement 
     */
    private int getMovementCost(Position position) {
        List<Entity> dungeonEntities = this.dungeon.getEntitiesAtPosition(position);

        // Check for any swamps
        SwampTile swamp = (SwampTile) dungeonEntities.stream()
            .filter(entity -> entity instanceof SwampTile)
            .findFirst().orElse(null);

        // No swamp --> cost == 1
        if (swamp == null) return 1;

        return swamp.getMovementFactor();
    }
}