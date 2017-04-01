
import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdRandom;

/**
 * Created by daniel shen on 3/25/2017.
 */

public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] q;
    private int size; // number of elements in the queue
    private int tail;
    private int head;

    public RandomizedQueue(){
        q = (Item[]) new Object[2];
        size = 0;
        tail = 0;
        head = 0;
    }

    public boolean isEmpty(){
        return size == 0;
    }

    public int size(){
        return size;
    }

    /**
     * double size if necessary then add item
     * @param item
     */
    public void enqueue(Item item){
        if(item == null) {
            throw new NullPointerException("input is null");
        }
        if(size == q.length){
            // double the size of the array and recopy to the front of the array
            resizeArray(size*2);
        }
        q[tail++] = item;
        // wrap around the tail
        if(tail == q.length){
            tail = 0;
        }
        size++;
    }

    private void resizeArray(int capacity){
        Item[] copy = (Item[]) new Object[capacity];
        for(int i = 0; i<size; i++){
            copy[i] = q[(head + i)%q.length];
        }
        q = copy;
        head = 0;
        tail = size;
    }

    /**
     * remove a random item, shrink the size if necessary
     * @return item
     */
    public Item dequeue(){
        if(isEmpty()){
            throw new NoSuchElementException("empty queue");
        }

        int randInt = StdRandom.uniform(size); // 0 to size - 1
        int rdmIdx = (randInt + head)%q.length;

        Item item = q[rdmIdx];
        swap(head, rdmIdx);
        q[head] = null; // avoid loitering
        size--;
        head++;
        if(head == q.length){
            head = 0;
        }
        if(size > 0 && size == q.length/4){
            resizeArray(q.length/2);
        }
        return item;
    }

    private void swap(int a, int b){
        Item temp = q[a];
        q[a] = q[b];
        q[b] = temp;
    }

    /**
     * return a random item
     * @return item
     */
    public Item sample(){
        if(isEmpty()){
            throw new NoSuchElementException("empty queue");
        }
        int randInt = StdRandom.uniform(size); // 0 to size - 1
        int rdmIdx = (randInt + head)%q.length;
        return q[rdmIdx];
    }

    public Iterator<Item> iterator(){
        return new ListIterator();
    }

    private class ListIterator implements Iterator<Item>{
        private int i = size;
        private Item[] array;
        public ListIterator() {
            array = (Item[]) new Object[size];
            for(int j = 0; j<size; j++){
                array[j] = q[(j+head)%q.length];
            }
        }
        public boolean hasNext(){
            return i > 0;
        }
        public Item next(){
            if(!hasNext()){
                throw new NoSuchElementException("empty queue");
            }
            int randIdx = StdRandom.uniform(i);
            Item item = array[randIdx];
            swap(randIdx, i - 1);
            i--;
            return item;
        }
        private void swap(int a, int b){
            Item temp = array[a];
            array[a] = array[b];
            array[b] = temp;
        }
        public void remove(){
            throw new UnsupportedOperationException("Operation not supported");
        }
    }
}
