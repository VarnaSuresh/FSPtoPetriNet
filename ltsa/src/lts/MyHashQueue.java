package lts;

import java.util.LinkedList;

/* MyHash is a speciallized Hashtable/Queue for the reachable analyser
* it includes a queue structure through the hash table entries
*  -- assumes no attempt to input duplicate key
*  
*/

class MyHashQueueEntry {
    byte[] key;
    int action;
    int level=0;
    MyHashQueueEntry next;   //for linking buckets in hash table
    MyHashQueueEntry link;   //for queue linked list
    MyHashQueueEntry parent; //pointer to node above in BFS

    MyHashQueueEntry(byte[] l) {
        key =l; action=0; next = null; link = null;
    }

    MyHashQueueEntry(byte[] l, int a, MyHashQueueEntry p) {
        key =l; action =a; next = null; link =null; parent=p;
    }

 }

public class MyHashQueue implements StackCheck {

    private MyHashQueueEntry [] table;
    private int count =0;
    private MyHashQueueEntry head = null;
    private MyHashQueueEntry tail = null;


    public MyHashQueue(int size) {
        table = new MyHashQueueEntry[size];
    }

    public void addPut(byte[] key, int action, MyHashQueueEntry parent) {
        MyHashQueueEntry entry = new MyHashQueueEntry(key, action, parent);
        if (parent!=null) entry.level=parent.level+1;
        //insert in hash table
        int hash = StateCodec.hash(key) % table.length;
        entry.next=table[hash];
        table[hash]=entry;
        ++count;
        //insert in queue
        if (head==null) 
        	head = tail = entry;
        else {
           tail.link = entry;
           tail = entry;
        }
    }
    
    public MyHashQueueEntry peek() {
    	  return head;
    }
    
    public void pop() { //remove from head of queue
    	  head = head.link;
    	  if (head==null) tail = head;
    }


    public boolean empty() {return head==null;}
    
    public boolean containsKey(byte[] key) {
       int hash = StateCodec.hash(key) % table.length;
        MyHashQueueEntry entry = table[hash];
        while (entry!=null) {
            if (StateCodec.equals(entry.key,key)) return true;
            entry = entry.next;
        }
        return false;
    }
    
    // for breadth first search we can only check that we have already visited the state
    public boolean onStack(byte[] key) {
    	   int hash = StateCodec.hash(key) % table.length;
        MyHashQueueEntry entry = table[hash];
        while (entry!=null) {
            if (StateCodec.equals(entry.key,key)) return entry.level<=head.level;
            entry = entry.next;
        }
        return false;
    }

    public int size() {return count;}
    
    public LinkedList getPath(MyHashQueueEntry end, String [] actionNames) {
    	  LinkedList trace = new LinkedList();
    	  while (end!=null) {
    	  	  if (end.parent!=null)
    	  	  	trace.addFirst(actionNames[end.action]);
    	  	  end = end.parent;
    	  }
    	  return trace;
    }
    
    public int depth(MyHashQueueEntry e) {
    	  int d =0;
    	  while (e!=null) {
    	  	  ++d;
    	  	  e = e.parent;
    	  }
    	  return d;
    }

}