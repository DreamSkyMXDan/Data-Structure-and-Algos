package com.mystanford.algo;

import com.mystanford.algo.Graph.Edge;

/**
 * Created by daniel shen on 3/16/2017.
 */

public class DijkstraAlgo {
    private Graph graph;
    private BinaryMinHeap bMinHeap;
    private int singleSrc;
    private int[] distances;
    private int[] parents;

    /**
     * it calculates the single source shortest path from the passed in single source to all other vertexes in the graph.
     * Assume V is the number of vertices, E is the number of edges. Time complexity is O((V+E)logV)
     * @param g it is a DAG(Directed Acyclic Graph) where each edge's weight is no less than 0.
     * @param src the single source
     */
    public DijkstraAlgo(Graph g, int src){
        graph = g;
        singleSrc = src;
        int size = graph.getNodes().size();
        bMinHeap = new BinaryMinHeap(size);
        distances = new int[size];
        parents = new int[size];
    }

    public void doDijkstra(){
        initialize();
        // it will loop V times
        while(!bMinHeap.isEmpty()){
            // extract the min of the minHeap O(lgV)
            int minId = bMinHeap.extractMin();
            // each edge will be visited once and total is E times
            for(Edge edge : graph.getNodes().get(minId).getAllEdges()){
                int neighborId = edge.getEnd();
                int edgeWeight = edge.getWeight();
                int newDist = distances[minId] + edgeWeight;
                // insert and decreaseKey operation both cost O(lgV)
                if(distances[neighborId] == -1){
                    distances[neighborId] = newDist;
                    bMinHeap.insert(neighborId, newDist);
                    parents[neighborId] = minId;
                }else if(newDist < distances[neighborId]){
                    distances[neighborId] = newDist;
                    bMinHeap.decreaseKey(neighborId, newDist);
                    parents[neighborId] = minId;
                }
            }
        }
    }

    private void initialize(){
        // init distances
        for(int i = 0; i<distances.length; i++){
            distances[i] = -1;
        }
        // init parents
        for(int i = 0; i<parents.length; i++){
            parents[i] = -1;
        }
        // init singleSource
        distances[singleSrc] = 0;
        bMinHeap.insert(singleSrc, 0);
    }
}
