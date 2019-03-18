package hr.fer.zemris.java.custom.collections;

public class Dictionary {

	private static class Entry{
		Object key;
		Object value;
		
		Entry(Object key, Object value) {
			if(key == null) {
				throw new NullPointerException("Key can't be null value.");
			}
			
			this.key = key;
			this.value = value;
		}
		
		@Override
		public int hashCode() {
			return key.hashCode();
		}
		
		@Override
		public boolean equals(Object arg) {
			if(!(arg instanceof Entry)) {
				return false;
			}
			
			Entry other = (Entry) arg;
			return key.equals(other.key);
		}
	}
	
	private ArrayIndexedCollection entryCollection;
	
	public Dictionary() {
		entryCollection = new ArrayIndexedCollection();
	}
	
	public boolean isEmpty() {
		return entryCollection.isEmpty();
	}
	
	public int size() {
		return entryCollection.size();
	}
	
	public void clear() {
		entryCollection.clear();
	}
	
	public void put(Object key, Object value) {
		Entry entry = new Entry(key, value);
		
		if(entryCollection.contains(entry)) {
			int entryIndex = entryCollection.indexOf(entry);
			Entry existingEntry = (Entry) entryCollection.get(entryIndex);
			
			existingEntry.value = value;
		}else {
			entryCollection.add(entry);
		}
	}
	
	public Object get(Object key) {
		if(key == null) {
			return null;
		}
		
		for(int index = 0; index < entryCollection.size(); index++) {
			Entry current = (Entry) entryCollection.get(index);
			
			if(current.key.equals(key)) {
				return current.value;
			}
		}
		
		return null;
	}
}
