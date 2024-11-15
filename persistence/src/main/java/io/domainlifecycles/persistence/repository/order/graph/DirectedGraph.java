/*
 *     ___
 *     │   ╲                 _
 *     │    ╲ ___ _ __  __ _(_)_ _
 *     |     ╲ _ ╲ '  ╲╱ _` │ │ ' ╲
 *     |_____╱___╱_│_│_╲__,_│_│_||_|
 *     │ │  (_)╱ _│___ __ _  _ __│ |___ ___
 *     │ │__│ │  _╱ -_) _│ ││ ╱ _│ ╱ -_|_-<
 *     │____│_│_│ ╲___╲__│╲_, ╲__│_╲___╱__╱
 *                      |__╱
 *
 *  Copyright 2019-2024 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package io.domainlifecycles.persistence.repository.order.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

/**
 * Simple directed graph implementation, that enables for topological sorting of vertices and cycle detection.
 *
 * @param <T> the type of vertices used in the graph
 * @author Mario Herb
 */
public class DirectedGraph<T> {

    private final Map<T, LinkedList<T>> adjacency = new HashMap<>();
    private final Set<T> vertices = new HashSet<>();


    /**
     * Add an edge to the graph. If the edge contains any new vertices, they are added.
     *
     * @param edge the edge to be added
     */
    public void addEdge(Edge<T> edge) {
        var list = adjacency.computeIfAbsent(edge.source(), k -> new LinkedList<>());
        list.add(edge.target());
        vertices.add(edge.source());
        vertices.add(edge.target());
    }

    /**
     * Add a vertex to the graph.
     *
     * @param v vertex to be added
     */
    public void addVertex(T v) {
        vertices.add(v);
    }

    // A recursive function used by topologicalSort
    private void topologicalSortRecursive(T v, Map<T, Boolean> visited,
                                          Stack<T> stack) {
        // Mark the current node as visited.
        visited.put(v, true);
        T i;

        // Recur for all the vertices adjacent to this
        // vertex
        var adjForVertex = adjacency.get(v);
        if (adjForVertex != null) {
            for (T forVertex : adjForVertex) {
                i = forVertex;
                var visitedI = visited.get(i);
                if (visitedI != null && !visitedI)
                    topologicalSortRecursive(i, visited, stack);
            }
        }

        // Push current vertex to stack which stores result
        stack.push(v);
    }

    /**
     * Return a topologically ordered list of the vertices in the graph.
     *
     * @return ordered list of vertices
     */
    public List<T> topologicalSort() {
        Stack<T> stack = new Stack<>();

        // Mark all the vertices as not visited
        Map<T, Boolean> visited = new HashMap<>();
        for (T n : vertices) {
            visited.put(n, false);
        }


        // Call the recursive helper function to store
        // Topological Sort starting from all vertices
        // one by one
        for (T v : vertices) {
            if (!visited.get(v)) {
                topologicalSortRecursive(v, visited, stack);
            }

        }
        var sorted = new ArrayList<T>();
        while (!stack.isEmpty()) {
            sorted.add(stack.pop());
        }
        return sorted;
    }

    /**
     * Checks the graph for cycles
     *
     * @return true, if a cycle is detected
     */
    public boolean checkCycle() {
        var sorted = topologicalSort();
        for (T v : vertices) {
            var adj = adjacency.get(v);
            if (adj != null) {
                for (T it : adj) {
                    // If parent vertex
                    // does not appear first
                    if (sorted.indexOf(v) > sorted.indexOf(it)) {
                        // Cycle exists
                        return true;
                    }
                }
            }
        }
        // Return false if cycle
        // does not exist
        return false;
    }

    /**
     * Edge container
     *
     * @param <T>    type of vertices which make up the edge
     * @param source source vertex
     * @param target target vertex
     */
    public record Edge<T>(T source, T target) {
    }
}
