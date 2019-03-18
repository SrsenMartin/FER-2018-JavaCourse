package hr.fer.zemris.java.hw05.collections;

import static java.lang.Math.abs;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Simple hash table implementation that provides storing elements key->value.
 * The rules for storing is:there can't be duplicate or null keys,
 * but can be duplicate or null values.
 * 
 * Provides put, get and remove operations in constant time.
 * Uses hashing for storing elements and getting them out.
 * Have 2 constructors,default one that will initialise size to 16,
 * and other that takes size as argument and rounds it to ceil value of second potention.
 * Internally uses array to store Entries.
 * 
 * @author Martin Sršen
 *
 * @param <K>	Element key, can't be null and is unique.
 * @param <V>	Element value, can be null and is not unique.
 */
public class SimpleHashtable<K, V> implements Iterable<SimpleHashtable.TableEntry<K, V>> {
	
	/**
	 * If no size is given through constructor,this will be default size.
	 */
	private static final int DEFAULT_SIZE = 16;
	/**
	 * Percentage of hashtable allowed filling.
	 */
	private static final double TABLE_FILLING_LIMIT = 0.75;
	/**
	 * Array used to store entries.
	 */
	private TableEntry<K, V>[]	table;
	/**
	 * Current size of hashtable.
	 */
	private int size;
	/**
	 * Current number of modifications on hashtable.
	 */
	private int modificationCount;
	
	/**
	 * Makes empty hashtable with default capacity of 16.
	 */
	public SimpleHashtable() {
		this(DEFAULT_SIZE);
	}
	
	/**
	 * Makes empty hashtable with given capacity.
	 * 
	 * @param capacity	Given capacity for hashtable.
	 */
	@SuppressWarnings("unchecked")
	public SimpleHashtable(int capacity) {
		if(capacity < 1) {
			throw new IllegalArgumentException("Size can't be lower than 1. Was " + capacity + ".");
		}
		
		int tableCapacity = 1;
		while(tableCapacity < capacity) {
			tableCapacity <<= 1;
		}
		
		table = (TableEntry<K, V>[]) (new TableEntry[tableCapacity]);
	}
	
	/**
	 * Adds key-value pair into hashtable, if key is alredy in table,
	 * replaces old value with new one.
	 * 
	 * @param key	Entry key.
	 * @param value	Entry value.
	 */
	public void put(K key, V value) {
		Objects.requireNonNull(key, "Key can't be null value");
		
		int position = getPosition(key);
		
		TableEntry<K, V> head = table[position];
		if(head == null) {
			table[position] = new TableEntry<K, V>(key, value, null);
			size++;
			modificationCount++;
		}else {
			while(true) {
				if(head.key.equals(key)) {
					head.setValue(value);
					break;
				}else if(head.next == null) {
					head.next = new TableEntry<K, V>(key, value, null);
					size++;
					modificationCount++;
					break;
				}
				
				head = head.next;
			}
		}
		
		resizeIfNeeded();
	}
	
	/**
	 * Returns value that is assigned to given key in hashtable.
	 * Returns null if hashtable doesn't contain key or if given key is null.
	 * 
	 * @param key	Key used to find value.
	 * @return	Value assigned to key, or null if key-value doesn't exist.
	 */
	public V get(Object key) {
		TableEntry<K, V> keyEntry = getEntryWithKey(key);
		
		if(keyEntry == null) {
			return	null;
		}
		
		return keyEntry.getValue();
	}
	
	/**
	 * Returns the number of elements in table.
	 * 
	 * @return	number of elements in table.
	 */
	public int size() {
		return size;
	}
	
	/**
	 * Returns true if table contains given key,
	 * false otherwise.
	 * If given key is null, returns false;
	 * 
	 * @param key	Used to check existence of it.
	 * @return	true if table contains given key, false otherwise.
	 */
	public boolean containsKey(Object key) {
		TableEntry<K, V> keyEntry = getEntryWithKey(key);
		
		if(keyEntry == null) {
			return	false;
		}
		
		return true;
	}
	
	/**
	 * Returns true if table contains given value,
	 * false otherwise.
	 * 
	 * @param value	Used to check existence of it.
	 * @return	true if table contains given value, false otherwise.
	 */
	public boolean containsValue(Object value) {		
		Iterator<TableEntry<K, V>> iterator = iterator();
		
		while(iterator.hasNext()) {
			TableEntry<K, V> next = iterator.next();
			
			if(next.getValue() == value ||
					(next.getValue() != null && next.getValue().equals(value))) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Removes entry for given key from table if existing.
	 * 
	 * @param key	Key of entry to remove.
	 */
	public void remove(Object key) {
		if(key == null)		return;
		
		int position = getPosition(key);
		TableEntry<K, V> head = table[position];
		
		if(head == null)	return;
		if(head.getKey().equals(key)) {
			table[position] = head.next;
			size--;
			modificationCount++;
			return;
		}
		
		while(head.next != null) {
			if(head.next.getKey().equals(key)) {
				head.next = head.next.next;
				size--;
				modificationCount++;
				return;
			}
		}
	}
	
	/**
	 * Checks whether table contains no entries.
	 * 
	 * @return	true if table contains no entries,
	 * false otherwise.
	 */
	public boolean isEmpty() {
		return size == 0;
	}
	
	/**
	 * Removes all entries from table.
	 */
	public void clear() {
		for(int i = 0; i < table.length; i++) {
			table[i] = null;
		}
		
		size = 0;
		modificationCount++;
	}
	
	/**
	 * String representation of SimpleHashtable object.
	 * Returns all entries as string.
	 */
	public String toString() {
		StringBuilder builder = new StringBuilder(2*size + 1);
		
		boolean firstComma = true;
		builder.append("[");
		
		Iterator<TableEntry<K, V>> iterator = iterator();
		while(iterator.hasNext()) {
			if(firstComma) {
				firstComma = false;
			}else {
				builder.append(", ");
			}
			
			TableEntry<K, V> entry = iterator.next();
			builder.append(entry.toString());
		}
		
		builder.append("]");
		
		return builder.toString();
	}
	
	/**
	 * Checks whether table passes table filling limit,
	 * if true resizes inner array to double size.
	 */
	@SuppressWarnings("unchecked")
	private void resizeIfNeeded() {
		if(table.length * TABLE_FILLING_LIMIT > size)	return;
		
		TableEntry<K, V>[]	helper = table;
		table = (TableEntry<K, V>[]) (new TableEntry[table.length * 2]);
		size = 0;
		modificationCount++;
		
		for(int i = 0; i < helper.length; i++) {
			TableEntry<K, V> head = helper[i];
			
			transferList(head);
		}
	}
	
	/**
	 * Helper method used to transfer one list row to current table.
	 * 
	 * @param head	Head of list row.
	 */
	private void transferList(TableEntry<K, V> head) {
		while(head != null) {
			put(head.getKey(), head.getValue());
			
			head = head.next;
		}
	}
	
	/**
	 * Returns entry that contains given key,
	 * or null if there is no element with given key.
	 * 
	 * @param key	Key used to return entry.
	 * @return	Entry with given key,or null if key is not in table.
	 */
	private TableEntry<K, V> getEntryWithKey(Object key){
		if(key == null) {
			return null;
		}
		
		int position = getPosition(key);
		TableEntry<K, V> head = table[position];
		
		while(head != null) {
			if(head.getKey().equals(key)) {
				return head;
			}
			
			head = head.next;
		}
		
		return null;
	}
	
	/**
	 * Returns expected position of entry in table.
	 * Position is calculated by key hashValue modulo tableSize.
	 * 
	 * @param key	Used to calculate position of entry.
	 * @return	Expected position of entry with given key.
	 */
	private int getPosition(Object key) {
		int tableSize = table.length;
		return abs(key.hashCode()) % tableSize;
	}

	/**
	 * Returns new entries iterator.
	 */
	@Override
	public Iterator<TableEntry<K, V>> iterator() {
		return new IteratorImpl();
	}
	
	/**
	 * Inner class that represents one table entry.
	 *
	 * @param <K>	Key of entry.
	 * @param <V>	Value of entry.
	 */
	public static class TableEntry<K, V> {
		
		/**
		 * Entry key.
		 */
		private final K key;
		/**
		 * Entry value.
		 */
		private V value;
		/**
		 * Pointer to next table entry with same position in entries list.
		 */
		private TableEntry<K, V> next;
		
		/**
		 * Constructor used to make new entry with given values.
		 *  
		 * @param key	Entry key.
		 * @param value	Entry value.
		 * @param next	Pointer to next element with same position in entries array.
		 */
		public TableEntry(K key, V value, TableEntry<K, V> next) {
			this.key = key;
			this.value = value;
			this.next = next;
		}
		
		/**
		 * Getter method for entry value.
		 * 
		 * @return	Entry value.
		 */
		public V getValue() {
			return value;
		}
		
		/**
		 * Setter method for entry value.
		 * 
		 * @param value	Value to assign to entry.
		 */
		public void setValue(V value) {
			this.value = value;
		}
		
		/**
		 * Getter method for entry key.
		 * 
		 * @return	Entry key.
		 */
		public K getKey() {
			return key;
		}
		
		/**
		 * String representation of given entry.
		 * Returns key=value as string.
		 */
		@Override
		public String toString() {
			return key.toString() + "=" + (value == null ? value : value.toString());
		}
	}
	
	/**
	 * Inner class representation of iterator for hashtable.
	 * Returns entries one by one on the way they are stores in inner array of entries.
	 * 
	 * @author Martin Sršen
	 *
	 */
	private class IteratorImpl implements Iterator<SimpleHashtable.TableEntry<K, V>>{

		/**
		 * Represents current modification of hashtable when iterator was made.
		 */
		private int initialModificationCount;
		/**
		 * Current position in hashtable inner array that store entries.
		 */
		private int currentPosition;
		/**
		 * Current entry that was iterated.
		 */
		private TableEntry<K, V> currentEntry;
		/**
		 * Next entry that will be iterated.
		 */
		private TableEntry<K, V> nextEntry;
		
		/**
		 * Default constructor that sets initialModificationCount to same value as hashtable 
		 * and sets next entry.
		 */
		public IteratorImpl() {
			initialModificationCount = modificationCount;
			setNextEntry();
		}
		
		/**
		 * Returns true if iterator has more elements.
		 */
		@Override
		public boolean hasNext() {
			if(initialModificationCount != modificationCount) {
				throw new ConcurrentModificationException("Hashtable was changed from outside.");
			}
			
			return nextEntry != null;
		}

		/**
		 * Returns next element in iteration.
		 */
		@Override
		public TableEntry<K, V> next() {
			if(!hasNext()) {
				throw new NoSuchElementException("There are no more elements in hashtable.");
			}
			currentEntry = nextEntry;
			
			if(currentEntry.next != null) {
				nextEntry = currentEntry.next;
			}else if(hasNext()){
				currentPosition++;
				setNextEntry();
			}
			
			return currentEntry;
		}
		
		/**
		 * Removes from hashtable current element that was iterated.
		 */
		@Override
		public void remove() {
			if(initialModificationCount != modificationCount) {
				throw new ConcurrentModificationException("Hashtable was changed from outside.");
			}
			
			if(currentEntry == null) {
				throw new IllegalStateException("Current Entry not avaliable to remove.");
			}
			
			SimpleHashtable.this.remove(currentEntry.getKey());
			currentEntry = null;
			initialModificationCount++;
		}
		
		/**
		 * Helper method used to set next entry to correct value.
		 * Or null if there is no more elements after.
		 */
		private void setNextEntry(){
			for(; currentPosition < table.length; currentPosition++) {
				if(table[currentPosition] != null) {
					nextEntry = table[currentPosition];
					return;
				}
			}
			
			nextEntry = null;
		}
	}
}
