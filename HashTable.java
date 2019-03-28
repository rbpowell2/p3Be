
//////////////////// ALL ASSIGNMENTS INCLUDE THIS SECTION /////////////////////
//
// Title:           (p3a Hash Table)
// Files:           (HashTable.java, DataStructureADT.java
//                   HashTableADT.java, HashTableTests.java)
// Course:          (CS400 Spring 2019)
//
// Author:          (Ryker Powell)
// Email:           (rbpowell2@wisc.edu)
// Lecturer's Name: (Kuemmel)
//
///////////////////////////// CREDIT OUTSIDE HELP /////////////////////////////
//
// Students who get help from sources other than their partner must fully 
// acknowledge and credit those sources of help here.  Instructors and TAs do 
// not need to be credited here, but tutors, friends, relatives, room mates, 
// strangers, and others do.  If you received no outside help from either type
//  of source, then please explicitly indicate NONE.
//
// Persons:         (NONE)
// Online Sources:  (NONE)
//
/////////////////////////////// 80 COLUMNS WIDE ///////////////////////////////
//
// TODO: I chose to use a hash table that uses an array full of linked lists. 
//				
//
// TODO: Each key is hashed to an array index, and is placed into that index's linked
//				list. If there hasn't been a collision at that index, the new key is the head,
//        else inserted at end of list.
//
// NOTE: you are not required to design your own algorithm for hashing,
//       since you do not know the type for K,
//       you must use the hashCode provided by the <K key> object
//       and one of the techniques presented in lecture
//

/**
 * 
 * @author Ryker Powell This class manages a hash table full of key, value
 *         pairs. The table is an array of linked lists and is self-expanding to
 *         maintain efficient operations.
 * @param <K>
 *          Key
 * @param <V>
 *          Value
 */
public class HashTable<K extends Comparable<K>, V> implements HashTableADT<K, V> {

	/**
	 * This inner class manages the linked lists that make up the array.
	 * 
	 * @author Ryker Powell
	 *
	 * @param <K>
	 *          Key
	 * @param <V>
	 *          Value
	 */
	private class resolutionList<K, V> {

		/**
		 * This inner class manages a node within the list.
		 * 
		 * @author Ryker Powell
		 *
		 * @param <K>
		 *          Key
		 * @param <V>
		 *          Value
		 */
		private class ListNode<K, V> {
			// FIELDS
			private Comparable key;
			private Object value;
			private ListNode next; // represents this node's next node

			// CONSTRUCTOR
			/**
			 * Constructor that initializes a nodes key, value, and next fields.
			 * 
			 * @param k
			 *          key
			 * @param v
			 *          value
			 */
			private ListNode(Comparable k, Object v) {
				this.key = k;
				this.value = v;
				this.next = null;
			}

			/**
			 * Mutator method for the next field
			 * 
			 * @param next
			 */
			private void setNext(ListNode next) {
				this.next = next;
			}

			/**
			 * Accessor method for the next field
			 * 
			 * @return next
			 */
			private ListNode getNext() {
				return this.next;
			}

			/**
			 * Returns the key of this node's next. Useful for removing a desired key and
			 * keeping the list intact.
			 * 
			 * @return the next node's key
			 */
			private Comparable getNextsKey() {
				return this.next.key;
			}
		} // end of Node Class

		// LIST FIELDS
		private ListNode head;
		private int numKeys = 0;// head of the list

		// LIST CONSTRUCTOR
		/**
		 * Constructor for this data structure. Initializes head to null
		 */
		private resolutionList() {
			head = null;
		}

		/**
		 * Adds a new node at the front of the list. Must have non-null and unique key.
		 */
		private void insert(Comparable k, Object v) {

			if (k == null) {
				throw new IllegalArgumentException("null key");
			}

			if (contains(k)) {
				throw new RuntimeException("duplicate key");

			}

			ListNode newNode = new ListNode(k, v);
			ListNode curr = head;
			newNode.setNext(curr);
			head = newNode;
			this.numKeys++;

		}

		/**
		 * Removes a node with a desired key from the list if it is in the list. Key
		 * must be non-null. New list size is automatically updated. Returns false if
		 * key is not found.
		 */

		private boolean remove(Comparable k) {
			if (k == null) {
				throw new IllegalArgumentException("null key");
			}

			if (!contains(k)) {
				return false;
			}

			if (head == null) {
				return false;
			}

			if (head.key == k) {
				head = head.getNext();
				this.numKeys--;
				return true;
			}

			ListNode curr = head;
			while (curr != null) {

				// if the next node's key is the target key, update
				// this node's next to skip over(remove) desired node
				if (curr.getNextsKey() == k) {

					curr.setNext(curr.getNext().getNext());
					this.numKeys--;
					return true;
				}
				curr = curr.getNext();
			}

			return false;
		}

		/**
		 * Returns true if a desired key is found within the list.
		 */

		private boolean contains(Comparable k) {
			if (k == null) {
				return false;
			}
			ListNode curr = head;
			while (curr != null) {
				if (curr.key == k) {
					return true;
				}
				curr = curr.getNext();
			}
			return false;

		}

		/**
		 * Returns, but does not remove, the value associated with a desired key.
		 * Returns null if key is not found.
		 */

		private Object get(Comparable k) {
			if (k == null) {
				throw new IllegalArgumentException("null key");
			}
			ListNode curr = head;
			if (head == null) {
				return null;
			}
			while (curr != null) {
				if (curr.key == k) {
					return curr.value;
				}
				curr = curr.getNext();
			}
			return null;
		}

		/**
		 * Returns the current number of keys of the list.
		 */

		private int numKeys() {
			return this.numKeys;
		}

	} // END OF INNER CLASSES

	// HASHTABLE FIELDS
	private final double loadFactorThreshold;
	private int capacity;
	private int numKeys;
	private resolutionList[] hashTable;

	private boolean resize = false; // true if resizing and rehashing conditions apply

	/**
	 * Public no-arg constructor for HashTable. By default, sets capacity to 11 and
	 * threshold to 0.75. Initializes each index to an empty linked list.
	 */
	public HashTable() {
		hashTable = new resolutionList[11];
		for (int i = 0; i < 11; i++) {
			hashTable[i] = new resolutionList();
		}
		this.loadFactorThreshold = 0.75;
		this.capacity = 11;
		this.numKeys = 0;
	}

	/**
	 * Public two-arg constructor for HashTable. Sets initial capacity and load
	 * factor threshold to the desired values. Initializes each index to an empty
	 * linked list.
	 * 
	 * @param initialCapacity
	 * @param loadFactorThreshold
	 */
	public HashTable(int initialCapacity, double loadFactorThreshold) {
		if (initialCapacity <= 0) {
			throw new IllegalArgumentException();
		}
		hashTable = new resolutionList[initialCapacity];
		for (int i = 0; i < initialCapacity; i++) {
			hashTable[i] = new resolutionList();
		}
		this.loadFactorThreshold = loadFactorThreshold;
		this.capacity = initialCapacity;
		this.numKeys = 0;
	}

	/**
	 * Accessor for the loadFactorThreshold field
	 */
	public double getLoadFactorThreshold() {
		return this.loadFactorThreshold;
	}

	/**
	 * Accessor for the capacity field
	 */
	public int getCapacity() {
		return this.capacity;
	}

	/**
	 * Accessor for the loadFactor field. Equals numKeys/table length
	 */
	public double getLoadFactor() {
		// System.out.println(numKeys);
		double loadFactor = (this.numKeys / hashTable.length);
		// System.out.println(this.capacity);
		return loadFactor;
	}

	/**
	 * Returns true if no keys are present in table.
	 * 
	 * @return boolean
	 */
	public boolean isEmpty() {
		if (this.numKeys > 0) {
			return false;
		}
		return true;
	}

	/**
	 * My collision resolution scheme: array of linked lists
	 */
	public int getCollisionResolution() {
		return 5;
	}

	/**
	 * Adds key/value pairs to the table. Increments numKeys. Calls rehash() after
	 * an insertion if loadFactor >= loadFactorThreshold
	 */
	public void insert(K key, V value) throws IllegalNullKeyException, DuplicateKeyException {
		if (key == null) {
			throw new IllegalNullKeyException();
		}

		// some keys will return negative hashCode due to overflow
		// the following line ensures that those codes are still indexed to a positive
		// index
		int hashIndex = Math.floorMod(key.hashCode(), hashTable.length);

		if (hashTable[hashIndex].contains(key)) {
			throw new DuplicateKeyException();
		}

		if (hashTable[hashIndex].numKeys == 0) {
			this.numKeys++;
		}
		hashTable[hashIndex].insert(key, value);

		// if resize conditions are met after an insertion
		if (resize()) {
			rehash();
		}
	}

	/**
	 * Helper method that doubles array capacity and rehashes every key to its new
	 * proper index.
	 */
	private void rehash() {
		this.capacity = (getCapacity() * 2) + 1;
		resolutionList[] doubleTable = new resolutionList[getCapacity()]; // new table

		for (int i = 0; i < doubleTable.length; i++) { // initialize empty lists
			doubleTable[i] = new resolutionList();
			this.numKeys = 0;
		}

		// this.numKeys = 0;
		for (int i = 0; i < hashTable.length; i++) {
			if (hashTable[i].head != null) {
				// reads value in linked list and rehashes
				int newHashCode = Math.floorMod(hashTable[i].head.key.hashCode(), hashTable.length);
				doubleTable[newHashCode].insert(hashTable[i].head.key, hashTable[i].head.value);

				// reads remaining values in that list if present
				while (hashTable[i].head.getNext() != null) {
					hashTable[i].head = hashTable[i].head.getNext();
					newHashCode = Math.floorMod(hashTable[i].head.key.hashCode(), hashTable.length);
					doubleTable[newHashCode].insert(hashTable[i].head.key, hashTable[i].head.value);

				}
			}
		}
		// update reference;
		hashTable = doubleTable;
	}

	/**
	 * Accessor for the numKeys field
	 */
	public int numKeys() {
		return this.numKeys;
	}

	/**
	 * Finds and removes a desired key. Decrements numKeys. Returns true if
	 * successful. Throws IllegalNullKeyException if desired key is null
	 */
	public boolean remove(K key) throws IllegalNullKeyException {
		if (key == null) {
			throw new IllegalNullKeyException();
		}
		// check the index the key would hash to
		int hashIndex = Math.floorMod(key.hashCode(), hashTable.length);
		if (hashTable[hashIndex].contains(key)) {
			if (hashTable[hashIndex].remove(key)) {
				this.numKeys--;
				return true;
			}
		}
		// key not found
		return false;
	}

	/**
	 * Returns the value associated with a desired key. Throws
	 * IllegalNullKeyException if key is null, and KeyNotFoundException if key is
	 * not present
	 */
	public V get(K key) throws IllegalNullKeyException, KeyNotFoundException {
		if (key == null) {
			throw new IllegalNullKeyException();
		}

		int hashIndex = Math.floorMod(key.hashCode(), hashTable.length);

		if (hashTable[hashIndex].numKeys() == 0) {
			throw new KeyNotFoundException();
		}
		// key found
		if (hashTable[hashIndex].contains(key)) {
			return (V) hashTable[hashIndex].get(key);
		} else {
			throw new KeyNotFoundException();
		}

	}

	/**
	 * Returns true if conditions for a resize are met.
	 * 
	 * @return boolean
	 */
	private boolean resize() {
		if (getLoadFactor() >= loadFactorThreshold) {
			resize = true;
			return resize;
		}
		resize = false;
		return resize;
	}
}
