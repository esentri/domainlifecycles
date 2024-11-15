package io.domainlifecycles.persistence.repository.order.graph;

import io.domainlifecycles.persistence.repository.order.graph.DirectedGraph;
import org.junit.jupiter.api.Test;

import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class GraphTests {

    @Test
    public void testTopologicalOrder() {
        DirectedGraph<Integer> g = new DirectedGraph<Integer>();
        g.addEdge(new DirectedGraph.Edge<>(5, 2));
        g.addEdge(new DirectedGraph.Edge<>(5, 0));
        g.addEdge(new DirectedGraph.Edge<>(4, 0));
        g.addEdge(new DirectedGraph.Edge<>(4, 1));
        g.addEdge(new DirectedGraph.Edge<>(2, 3));
        g.addEdge(new DirectedGraph.Edge<>(3, 1));
        var topolog = g.topologicalSort();
        assertThat(topolog.stream().map(Object::toString).collect(Collectors.joining(" "))).isEqualTo("5 4 2 3 1 0");
    }

    @Test
    public void testTopologicalOrderCycleDetectionFalse() {
        DirectedGraph<Integer> g = new DirectedGraph<Integer>();
        g.addEdge(new DirectedGraph.Edge<>(5, 2));
        g.addEdge(new DirectedGraph.Edge<>(5, 0));
        g.addEdge(new DirectedGraph.Edge<>(4, 0));
        g.addEdge(new DirectedGraph.Edge<>(4, 1));
        g.addEdge(new DirectedGraph.Edge<>(2, 3));
        g.addEdge(new DirectedGraph.Edge<>(3, 1));

        assertThat(g.checkCycle()).isFalse();
    }

    @Test
    public void testTopologicalOrderWithCycle() {
        DirectedGraph<Integer> g = new DirectedGraph<Integer>();
        g.addEdge(new DirectedGraph.Edge<>(5, 2));
        g.addEdge(new DirectedGraph.Edge<>(5, 0));
        g.addEdge(new DirectedGraph.Edge<>(4, 0));
        g.addEdge(new DirectedGraph.Edge<>(4, 1));
        g.addEdge(new DirectedGraph.Edge<>(2, 3));
        g.addEdge(new DirectedGraph.Edge<>(3, 1));
        g.addEdge(new DirectedGraph.Edge<>(3, 5));
        var topolog = g.topologicalSort();
        assertThat(topolog.stream().map(Object::toString).collect(Collectors.joining(" "))).isNotEmpty();
    }

    @Test
    public void testTopologicalOrderCycleDetectionTrue() {
        DirectedGraph<Integer> g = new DirectedGraph<Integer>();
        g.addEdge(new DirectedGraph.Edge<>(5, 2));
        g.addEdge(new DirectedGraph.Edge<>(5, 0));
        g.addEdge(new DirectedGraph.Edge<>(4, 0));
        g.addEdge(new DirectedGraph.Edge<>(4, 1));
        g.addEdge(new DirectedGraph.Edge<>(2, 3));
        g.addEdge(new DirectedGraph.Edge<>(3, 1));
        g.addEdge(new DirectedGraph.Edge<>(3, 5));
        assertThat(g.checkCycle()).isTrue();
    }

    @Test
    public void testTopologicalOrderCycleDetectionTrue2() {
        DirectedGraph<Integer> g = new DirectedGraph<Integer>();
        g.addEdge(new DirectedGraph.Edge<>(5, 2));
        g.addEdge(new DirectedGraph.Edge<>(5, 0));
        g.addEdge(new DirectedGraph.Edge<>(4, 0));
        g.addEdge(new DirectedGraph.Edge<>(4, 1));
        g.addEdge(new DirectedGraph.Edge<>(2, 3));
        g.addEdge(new DirectedGraph.Edge<>(3, 1));
        g.addEdge(new DirectedGraph.Edge<>(2, 5));
        assertThat(g.checkCycle()).isTrue();
    }

    @Test
    public void testTopologicalOrderCycleDetectionTrue3() {
        DirectedGraph<Integer> g = new DirectedGraph<Integer>();
        g.addEdge(new DirectedGraph.Edge<>(5, 2));
        g.addEdge(new DirectedGraph.Edge<>(5, 0));
        g.addEdge(new DirectedGraph.Edge<>(4, 0));
        g.addEdge(new DirectedGraph.Edge<>(4, 1));
        g.addEdge(new DirectedGraph.Edge<>(2, 3));
        g.addEdge(new DirectedGraph.Edge<>(3, 1));
        g.addEdge(new DirectedGraph.Edge<>(0, 5));
        assertThat(g.checkCycle()).isTrue();
    }
}
