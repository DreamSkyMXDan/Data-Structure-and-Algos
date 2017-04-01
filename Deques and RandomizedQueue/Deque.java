
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by daniel shen on 3/25/2017.
 */

public class Deque<Item> implements Iterable<Item> {

    private Node first, last;
    private int size;
    private class Node{
        private Item item;
        private Node next;
        private Node prev;
    }

    public Deque(){
        first = null;
        last = null;
        size = 0;
    }

    public boolean isEmpty(){
        return first == null;
    }

    public int size(){
        return size;
    }

    /**
     * add the item to the front
     * @param item
     */
    public void addFirst(Item item){
        if(item == null) {
            throw new NullPointerException("input is null");
        }
        Node oldFirst = first;
        first = new Node();
        first.item = item;
        first.prev = null;
        first.next = oldFirst;

        if(size == 0){
            last = first;
        }else{
            oldFirst.prev = first;
        }
        size++;
    }

    /**
     * add the item to the front
     * @param item
     */
    public void addLast(Item item){
        if(item == null) {
            throw new NullPointerException("input is null");
        }
        Node oldLast = last;
        last = new Node();
        last.item = item;
        last.prev = oldLast;
        last.next = null;
        if(size == 0){
            first = last;
        }else{
            oldLast.next = last;
        }
        size++;
    }

    /**
     * remove the item from the front
     * @return removed the item
     */
    public Item removeFirst(){
        if(size == 0){
            throw new NoSuchElementException("queue is empty now!");
        }
        Item item = first.item;
        first = first.next;
        size--;
        if(size == 0){
            last = null;
        }else{
            first.prev = null;
        }
        return item;
    }

    /**
     * remove the item from the end
     * @return  removed item
     */
    public Item removeLast(){
        if(size == 0){
            throw new NoSuchElementException("queue is empty now!");
        }
        Item item = last.item;
        last = last.prev;
        size--;
        if(size == 0){
            first = null;
        }else{
            last.next = null;
        }
        return item;
    }

    public Iterator<Item> iterator(){
        return new ListIterator();
    }

    private class ListIterator implements Iterator<Item>{

        private Node current = first;

        public boolean hasNext(){
            return current != null;
        }

        public Item next(){
            if(!hasNext()){
                throw new NoSuchElementException("empty queue");
            }
            Item item = current.item;
            current = current.next;
            return item;
        }

        public void remove(){
            throw new UnsupportedOperationException("Operation not supported");
        }
    }

}
