package hr.fer.zemris.java.custom.scripting.exec;

import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Map;

/**
 * Class representing ObjectMultistack collection.
 * Collection uses internally map whose key is String,
 * and value is MultistackEntry which contains pointer to next one
 * and value which is of type ValueWrapper.
 * Idea is that for each key we have value that represents stack.
 * Collection implements push,pop, peek and isEmpty methods with
 * complexity of O(1).
 * 
 * @author Martin Sršen
 *
 */
public class ObjectMultistack {

	/**
	 * Internal storage of collection.
	 */
	private Map<String, MultistackEntry> mapa;
	
	/**
	 * Default constructor that initializes map.
	 */
	public ObjectMultistack() {
		mapa = new HashMap<>();
	}
	
	/**
	 * Adds given ValueWrapper object into stack with given key.
	 * 
	 * @param name	Key of the stack where to enter value.
	 * @param valueWrapper	Object to add on top of the stack.
	 * @throws NullPointerException if name or valueWrapper is null.
	 */
	public void push(String name, ValueWrapper valueWrapper) {
		if(name == null || valueWrapper == null) {
			throw new NullPointerException("Key/value value can't be null");
		}
		
		MultistackEntry toEnter = new MultistackEntry(valueWrapper);
		
		if(mapa.containsKey(name)) {
			toEnter.next = mapa.get(name);
			mapa.replace(name, toEnter);
		}else {
			mapa.put(name, toEnter);
		}
	}
	
	/**
	 * Returns ValueWrapper from top of the stack for given key and removes it.
	 * 
	 * @param name	Key of the stack where to pop value from.
	 * @return	ValueWrapper from top of the stack for given key.
	 * @throws NullPointerException if given key is null.
	 * @throws EmptyStackException if given key doesn't exist.
	 */
	public ValueWrapper pop(String name) {
		if(name == null) {
			throw new NullPointerException("Can't pop null value");
		}
		
		MultistackEntry head = getHeadEntry(name);
		
		if(head.next == null) {
			mapa.remove(name);
		}else {
			mapa.replace(name, head.next);
		}
		
		return head.getValue();
	}
	
	/**
	 * Returns ValueWrapper from top of the stack for given key.
	 * 
	 * @param name	Key of the stack where to peek value from.
	 * @return	ValueWrapper from top of the stack for given key.
	 * @throws NullPointerException if given key is null.
	 * @throws EmptyStackException if given key doesn't exist.
	 */
	public ValueWrapper peek(String name) {
		if(name == null) {
			throw new NullPointerException("Can't pop null value");
		}
		
		return getHeadEntry(name).getValue();
	}
	
	/**
	 * Returns true if given key doesn't exist in map.
	 * 
	 * @param name	Key to check existence.
	 * @return	true if key doesn't exist, false otherwise.
	 */
	public boolean isEmpty(String name) {
		return !mapa.containsKey(name);
	}
	
	/**
	 * Returns MultistackEntry from top of the stack from given key,
	 * or if key doesn't exist throws exception.
	 * 
	 * @param name	key of the stack to return MultistackEntry from.
	 * @return	MultistackEntry on top of the stack with given key.
	 * @throws	EmptyStackException if given key doesn't exist in map.
	 */
	private MultistackEntry getHeadEntry(String name) {
		if(!mapa.containsKey(name)) {
			throw new EmptyStackException();
		}
		
		return mapa.get(name);
	}
	
	/**
	 * Inner class that represents one MultistackEntry, node of the stack.
	 * Contains value of type ValueWrapper and pointer to next MultistackEntry in stack.
	 */
	private static class MultistackEntry {
		/**
		 * MultistackEntry value of type ValueWrapper.
		 */
		private ValueWrapper value;
		/**
		 * Pointer to next MultistackEntry in stack.
		 */
		private MultistackEntry next;
		
		/**
		 * Constructor that creates new MultistackEntry with given value.
		 * 
		 * @param value	Value to set new MultistackEntry object to.
		 */
		public MultistackEntry(ValueWrapper value) {
			this.value = value;
		}
		
		/**
		 * Getter method for value of current MultistackEntry object.
		 * 
		 * @return	Value of MultistackEntry object.
		 */
		public ValueWrapper getValue() {
			return value;
		}
		
	}
}
