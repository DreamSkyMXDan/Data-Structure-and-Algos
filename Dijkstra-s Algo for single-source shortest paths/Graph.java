package com.mystanford.algo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by daniel shen on 3/16/2017.
 */

public class Graph {

    static class Node{
        private int id;
        private List<Edge> edges;
        public Node(int nId){
            id = nId;
            edges = new ArrayList<Edge>();
        }

        public List<Edge> getAllEdges(){
            return edges;
        }

        public void addEdge(Edge edge){
            edges.add(edge);
        }

    }

    static class Edge{
        private int start;
        private int end;
        private int weight;
        public Edge(int s, int e, int w){
            start = s;
            end = e;
            weight = w;
        }
        public int getWeight(){
            return weight;
        }
        public int getEnd(){
            return end;
        }
    }

    private HashMap<Integer, Node> nodes;
    public Graph(){
        nodes = new HashMap<Integer, Node>();
    }
    // construct the graph that has n vertices named from 0 to n-1 and a list of nodes
    public Graph(int n, List<Node> nodeList){
        nodes = new HashMap<Integer, Node>();
        for(int i = 0; i<n; i++){
            nodes.put(i, new Node(i));
        }

        for(Node node : nodeList) {
            nodes.put(node.id, node);
        }
    }

    public HashMap<Integer, Node> getNodes(){
        return nodes;
    }

    public void addNewNode(int id, Node node){
        nodes.put(id, node);
    }

}
