package unsw.graph;

import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class BreadthFirstGraphIterator<N extends Comparable<N>> implements Iterator<N> {
    /*
    BFS Pseudocode:
        
        queue = []
        visited = set()

        while queue:
            vertex = queue.dequeue()
            visited.add(vertex)
            queue.extend(graph.get_adjacent(vertex) - visited)
    */

    private Graph<N> graph;
    Set<N> visited;
    Deque<N> queue;

    public BreadthFirstGraphIterator(Graph<N> graph, N first) {
        this.graph = graph;
        this.visited = new HashSet<>();
        
        this.queue = new LinkedList<>();
        queue.add(first);
    }

    @Override
    public boolean hasNext() {
        return !queue.isEmpty();
    }

    @Override
    public N next() {
        if (!hasNext()) return null;
    
        N vertex = queue.pollFirst();
        visited.add(vertex);

        List<N> adjacentNodes = graph.getAdjacentNodes(vertex);
        for (N adjacentNode : adjacentNodes) {
            if (!visited.contains(adjacentNode) && !queue.contains(adjacentNode)) queue.add(adjacentNode);
        }

        return vertex;
    }
}
