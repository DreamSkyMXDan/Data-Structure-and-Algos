package com.mystanford.algo;

/**
 * Created by daniel shen on 3/16/2017.
 */

public class BinaryMinHeap {

    public static final int MAX_SIZE = Integer.MAX_VALUE;
    private int capacity = 0;
    private int heapSize = 0;
    private int[] heap;
    private int[] objId2pos;
    private int[] pos2objId;

    public BinaryMinHeap(int cap){
        capacity = cap;
        heap = new int[capacity];
        objId2pos = new int[capacity];
        pos2objId = new int[capacity];
    }

    /**
     * 
     * @param objId id of the vertex
     * @param val the value which will be compared
     */
    public void insert(int objId, int val){
        heap[heapSize] = val;
        objId2pos[objId] = heapSize;
        pos2objId[heapSize] = objId;
        siftUp(heapSize);
        heapSize++;
    }

    public boolean isEmpty(){
        return heapSize == 0;
    }

    /**
     * extract the id the vertex that holds the min value
     * @return the id of the vertex that is removed
     */
    public int extractMin(){
        int minId = getMinId();
        heapSize--;
        hswap(0, heapSize);
        siftDown(0);
        return minId;
    }

    /**
     * 
     * @param objId id of the vertex
     * @param val   new value
     */
    public void decreaseKey(int objId, int val){
        int index = objId2pos[objId];
        heap[index] = val;
        siftUp(index);
    }

    public int getMinId(){
        return pos2objId[0];
    }

    private void siftDown(int index){
        while(index < heapSize){
            int left = leftIdx(index);
            int right = rightIdx(index);
            int min = index;
            if(left < heapSize && heap[left] < heap[min]){
                min = left;
            }
            if(right < heapSize && heap[right] < heap[min]){
                min = right;
            }
            if(min == index){
                break;
            }
            hswap(min, index);
            index = min;
        }
    }

    private void siftUp(int index){
        if(index == 0){
            return;
        }
        while(index > 0){
            int pIdx = parentIdx(index);
            if(heap[index] < heap[pIdx]){
                hswap(index, pIdx);
                index = pIdx;
            }else{
                break;
            }
        }
    }

    private void hswap(int a, int b){
        swap(heap, a, b);
        swap(pos2objId, a, b);
        swap(objId2pos, pos2objId[a], pos2objId[b]);
    }

    private void swap(int[] array, int a, int b){

        int temp = array[a];
        array[a] = array[b];
        array[b] = temp;
    }

    private int parentIdx(int index){
        return (index - 1)/2;
    }

    private int leftIdx(int index){
        return 2*index + 1;
    }

    private int rightIdx(int index){
        return 2*index + 2;
    }

    public int getCurrentHeapSize(){
        return heapSize;
    }

}
