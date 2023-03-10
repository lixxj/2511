package unsw.graph;

import java.util.Comparator;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class DepthFirstGraphIterator<N extends Comparable<N>> implements Iterator<N> {
    private Graph<N> graph;
    Set<N> visited;
    Deque<N> queue;

    public DepthFirstGraphIterator(Graph<N> graph, N first) {
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

        N vertex = queue.pollLast();
        visited.add(vertex);

        List<N> adjacentNodes = graph.getAdjacentNodes(vertex);
        adjacentNodes.sort(new Comparator<N>() {
            @Override
            public int compare(N o1, N o2) {
                return -o1.compareTo(o2);
            }
        });

        for (N adjacentNode : adjacentNodes) {
            if (!visited.contains(adjacentNode) && !queue.contains(adjacentNode)) queue.addLast(adjacentNode);
        }

        return vertex;
    }
}
