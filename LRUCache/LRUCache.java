import java.util.HashMap;

/**
 * Created by dshen on 1/18/2017.
 */


public class LRUCache {
    /*
    doubly linkedlist
     */
    class DListNode{
        private DListNode prev;
        private DListNode next;
        int key;
        int val; // value could a string
        public DListNode(int k, int v){
            val = v;
            key = k;
        }
    }

    private int cap;
    HashMap<Integer, DListNode> hm = null;
    private DListNode head;
    private DListNode tail;

    public LRUCache(int capacity) {
        cap = capacity;
        hm = new HashMap<Integer, DListNode>();
        head = new DListNode(-1,-1);
        tail = new DListNode(-1,-1);
        head.next = tail;
        tail.prev = head;
    }

    public int getCapacity(){
        return cap;
    }

    /**
     * 
     * @param key the hashKey
     * @return the value corresponding to the key
     */
    public int get(int key) {
        int ret = Integer.MIN_VALUE;
        if(hm.containsKey(key)){
            DListNode node = hm.get(key);
            ret = node.val;
            removeNode(node);
            insertToTail(node);
        }
        return ret;
    }

    /**
     * 
     * @param node the node to remove from the doubly linkedlist and hashmap
     */
    private void removeNode(DListNode node){
        hm.remove(node.key);
        node.next.prev = node.prev;
        node.prev.next = node.next;
    }

    /**
     * 
     * @param node the node to insert to the tail end of the list
     */
    private void insertToTail(DListNode node){
        hm.put(node.key, node);
        tail.prev.next = node;
        node.prev = tail.prev;
        tail.prev = node;
        node.next = tail;
    }

    /**
     * 
     * @param key the hashKey
     * @param value the content 
     */
    public void set(int key, int value) {
        if(hm.containsKey(key)){
            DListNode node = hm.get(key);
            removeNode(node);
            node.val = value;
            insertToTail(node);
        }else{
            DListNode newNode = new DListNode(key, value);
            if(hm.size() == cap){
                removeNode(head.next);
            }
            insertToTail(newNode);
        }
    }
}
