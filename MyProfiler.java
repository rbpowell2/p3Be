/**
 * Filename:   MyProfiler.java
 * Project:    p3b-201901     
 * Authors:    TODO: add your name(s) and lecture numbers here
 *
 * Semester:   Spring 2019
 * Course:     CS400
 * 
 * Due Date:   TODO: add assignment due date and time
 * Version:    1.0
 * 
 * Credits:    TODO: name individuals and sources outside of course staff
 * 
 * Bugs:       TODO: add any known bugs, or unsolved problems here
 */

// Used as the data structure to test our hash table against
import java.util.TreeMap;

public class MyProfiler<K extends Comparable<K>, V> {

    HashTableADT<K, V> hashtable;
    TreeMap<K, V> treemap;
    
    public MyProfiler() {
        TreeMap<Integer, String> treemap = new TreeMap<Integer, String>();
        HashTable<Integer, String> hashtable = new HashTable<Integer, String>();
        
    }
    
    public void insert(K key, V value) {
    	try {
        hashtable.insert(key, value);
    	}
    	catch (Exception e) {
    		
    	}
        treemap.put(key,  value);
    }
    
    public void retrieve(K key) {
    	try {
        hashtable.get(key);
    	}
    	catch (Exception e) {
    		
    	}
        treemap.get(key);
    }
    
    public static void main(String[] args) {
     //   try {
            int numElements = Integer.parseInt(args[0]);
            
            MyProfiler<Integer, Integer> profile = new MyProfiler<Integer, Integer>();
            
            for (int i = 0; i < numElements; i++) {
            	profile.insert(i, i);
            }
            
            for (int i = 0; i < numElements; i++) {
            	profile.retrieve(i);
            }
            
            String msg = String.format("Inserted and retreived %d (key,value) pairs", numElements);
            System.out.println(msg);
     //   }
      //  catch (Exception e) {
          //  System.out.println("Usage: java MyProfiler <number_of_elements>");
          //  System.exit(1);
       // }
    }
}
