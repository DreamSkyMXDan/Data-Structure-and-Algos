package com.mystanford.algo;

import java.io.DataOutputStream;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Created by dshen on 3/19/2017.
 */

public class Huffman {

    // size of hashMap, total of 256 ASCII
    public static int SIZE = 256;
    public Huffman(){
    }

    // define code
    private static class Node{
        private Node left;
        private Node right;
        private char chr;
        private int freq;
        public Node(char chr, int freq, Node l, Node r){
            this.chr = chr;
            this.freq = freq;
            this.left = l;
            this.right = r;
        }

        private boolean isLeaf(){
            return left == null && right == null;
        }

    }

    private static class NodeComparator implements Comparator<Node> {
        public int compare(Node a, Node b){
            return a.freq - b.freq;
        }
    }

    public static void compress(byte[] inputData, BinaryOut binaryOutput){
        // read data content and build frequency table for each char in the inputData
        int[] freqMap = new int[SIZE];
        for(int i = 0; i<inputData.length; i++){
            freqMap[inputData[i]]++;
        }

        // 2. build Huffman Tree with the freqMap
        Node root = buildTree(freqMap);

        // 3. build codeTable with Huffman Tree
        // map 256 ASCII to code string
        String[] codeTable = new String[SIZE];
        buildCodeTable(codeTable, root, "");

        //4. write huffman tree to outputStream
        writeTree(root, binaryOutput);
        binaryOutput.write(inputData.length);
        // 5. use Huffman Tree to encode the inputData
        for(int i = 0; i<inputData.length; i++){
            String bStr = codeTable[inputData[i]];
            for(int j = 0; j<bStr.length(); j++){
                if(bStr.charAt(j) == '0'){
                    binaryOutput.write(false);
                }else{
                    binaryOutput.write(true);
                }
            }
        }
    }

    public static byte[] decompress(BinaryIn binaryInput){
        // read Huffman Tree
        Node root = readTree(binaryInput);
        // read the length of the input, number of bytes
        int num = binaryInput.readInt();
        byte[] out = new byte[num];
        for(int i = 0; i<num; i++){
            Node node = root;
            while(!node.isLeaf()){
                boolean bit = binaryInput.readBoolean();
                if(bit){
                    node = node.right;
                }else{
                    node = node.left;
                }
            }
            out[i] = (byte)node.chr;
        }
        return out;
    }

    /**
     * reverse preorder traversal
     * @param binaryInput
     * @return
     */
    private static Node readTree(BinaryIn binaryInput){
        if(binaryInput.readBoolean()){
            Node node = new Node(binaryInput.readChar(), 0, null, null);
            return node;
        }
        Node left = readTree(binaryInput);
        Node right = readTree(binaryInput);
        return new Node('0', 0, left, right);
    }

    /**
     * preorder traversal
     * if it a leaf, write 1 following with the ascii for the character
     * otherwise, write 0, traverse its left and right.
     * @param root
     */
    private static void writeTree(Node root, BinaryOut binaryOutPut){

        if(root.isLeaf()){
            binaryOutPut.write(true);
            binaryOutPut.write(root.chr, 8);
            return;
        }
        binaryOutPut.write(false);
        writeTree(root.left, binaryOutPut);
        writeTree(root.right, binaryOutPut);
    }

    // preorder traversal for the Huffman Tree
    private static void buildCodeTable(String[] codeTable, Node root, String str){

        if(root.isLeaf()){
            codeTable[root.chr] = str;
            return;
        }
        buildCodeTable(codeTable, root.left, str + '0');
        buildCodeTable(codeTable, root.right, str + '1');
    }

    private static Node buildTree(int[] freqMap){

        // Each distinct char is a node now, put them into a minHeap
        Queue<Node> minHeap = new PriorityQueue<>(SIZE, new NodeComparator());
        for(char i = 0; i<SIZE; i++){
            if(freqMap[i] > 0){
                minHeap.offer(new Node(i, freqMap[i], null, null));
            }
        }

        while(minHeap.size() > 1){
            Node left = minHeap.poll();
            Node right = minHeap.poll();
            int freq = left.freq + right.freq;
            Node newNode = new Node('\0', freq, left, right);
            minHeap.offer(newNode);
        }
        return minHeap.poll();
    }

}
