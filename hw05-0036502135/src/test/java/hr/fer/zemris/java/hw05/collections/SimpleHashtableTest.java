package hr.fer.zemris.java.hw05.collections;

import org.junit.Before;
import org.junit.Test;

import java.util.ConcurrentModificationException;
import java.util.Iterator;

import org.junit.Assert;

public class SimpleHashtableTest {

	private SimpleHashtable<String, Integer> map;
	
	@Before
	public void init() {
		map = new SimpleHashtable<>();
	}
	
	@Test (expected = NullPointerException.class)
	public void putKeyNull() {
		map.put(null, 15);
	}
	
	@Test
	public void putNormal() {
		map.put("key1", 1);
		map.put("key2", 2);
		map.put("key3", null);
		
		Assert.assertEquals(Integer.valueOf(1), map.get("key1"));
		Assert.assertEquals(Integer.valueOf(2), map.get("key2"));
		Assert.assertEquals(null, map.get("key3"));
		Assert.assertEquals(3, map.size());
	}
	
	@Test
	public void putDuplicate() {
		map.put("key1", 1);
		map.put("key1", 12);
		
		Assert.assertEquals(Integer.valueOf(12), map.get("key1"));
		Assert.assertEquals(1, map.size());
	}
	
	@Test
	public void testResize() {
		map = new SimpleHashtable<>(1);
		
		map.put("key1", 1);
		map.put("key2", 2);
		map.put("key3", 3);
		
		Assert.assertEquals(Integer.valueOf(1), map.get("key1"));
		Assert.assertEquals(Integer.valueOf(2), map.get("key2"));
		Assert.assertEquals(Integer.valueOf(3), map.get("key3"));
		Assert.assertEquals(3, map.size());
	}
	
	@Test
	public void getNull() {
		Assert.assertEquals(null, map.get(null));
	}
	
	@Test
	public void getNonExisting() {
		Assert.assertEquals(null, map.get("key1"));
	}
	
	@Test
	public void getExisting() {
		map.put("key1", 1);
		map.put("key2", 2);
		map.put("key3", 3);
		
		Assert.assertEquals(Integer.valueOf(1), map.get("key1"));
		Assert.assertEquals(Integer.valueOf(2), map.get("key2"));
		Assert.assertEquals(Integer.valueOf(3), map.get("key3"));
	}
	
	@Test
	public void testSize() {
		Assert.assertEquals(0, map.size());
		Assert.assertEquals(true, map.isEmpty());
		
		map.put("key1", 1);
		map.put("key2", 2);
		map.put("key3", 3);
		
		Assert.assertEquals(3, map.size());
		Assert.assertEquals(false, map.isEmpty());
	}
	
	@Test
	public void containsKey() {
		map.put("key1", 1);
		map.put("key2", 2);
		map.put("key3", 3);
		
		Assert.assertEquals(false, map.containsKey(null));
		Assert.assertEquals(true, map.containsKey("key2"));
		Assert.assertEquals(false, map.containsKey("key5"));
	}
	
	@Test
	public void containsValue() {
		map.put("key1", 1);
		map.put("key2", 2);
		map.put("key3", null);
		
		Assert.assertEquals(true, map.containsValue(null));
		Assert.assertEquals(true, map.containsValue(2));
		Assert.assertEquals(false, map.containsValue(42));
	}
	
	@Test
	public void removeNull() {
		map.put("key1", 1);
		map.put("key2", 2);
		map.put("key3", null);
		
		map.remove(null);
		
		Assert.assertEquals(3, map.size());
	}
	
	@Test
	public void removeNotExisting() {
		map.put("key1", 1);
		map.put("key2", 2);
		map.put("key3", null);
		
		map.remove("key4");
		
		Assert.assertEquals(3, map.size());
	}
	
	@Test
	public void removeNormal() {
		map.put("key1", 1);
		map.put("key2", 2);
		map.put("key3", null);
		
		map.remove("key2");
		
		Assert.assertEquals(2, map.size());
		Assert.assertEquals(null, map.get("key2"));
	}
	
	@Test
	public void clear() {
		map.put("key1", 1);
		map.put("key2", 2);
		map.put("key3", null);
		
		map.clear();
		
		Assert.assertEquals(null, map.get("key1"));
		Assert.assertEquals(null, map.get("key2"));
		Assert.assertEquals(null, map.get("key3"));
		Assert.assertEquals(0, map.size());
	}
	
	@Test
	public void toStringTest() {
		map.put("key2", 2);
		map.put("key3", null);
		
		Assert.assertEquals("[key2=2, key3=null]", map.toString());
	}
	
	@Test
	public void testIteratorRemove() {
		map.put("Ivana", 2);
		map.put("Ante", 2);
		map.put("Jasna", 2);
		map.put("Kristina", 5);
		map.put("Ivana", 5);
		
		Iterator<SimpleHashtable.TableEntry<String,Integer>> iter = map.iterator();
		
		while(iter.hasNext()) {
			SimpleHashtable.TableEntry<String,Integer> pair = iter.next();
			
			if(pair.getKey().equals("Jasna")) {
				iter.remove();
			}
		}
		
		Assert.assertEquals(false, map.containsKey("Jasna"));
		Assert.assertEquals(3, map.size());
	}
	
	@Test (expected = IllegalStateException.class)
	public void oneItarationMultipleRemove() {
		map.put("Ivana", 2);
		map.put("Ante", 2);
		map.put("Jasna", 2);
		map.put("Kristina", 5);
		map.put("Ivana", 5);
		
		Iterator<SimpleHashtable.TableEntry<String,Integer>> iter = map.iterator();
		
		while(iter.hasNext()) {
			SimpleHashtable.TableEntry<String,Integer> pair = iter.next();
			
			if(pair.getKey().equals("Ivana")) {
				iter.remove();
				iter.remove();
			}
		}
	}
	
	@Test (expected = ConcurrentModificationException.class)
	public void iteratorOuterModification() {
		map.put("Ivana", 2);
		map.put("Ante", 2);
		map.put("Jasna", 2);
		map.put("Kristina", 5);
		map.put("Ivana", 5);
		
		Iterator<SimpleHashtable.TableEntry<String,Integer>> iter = map.iterator();
		
		while(iter.hasNext()) {
			SimpleHashtable.TableEntry<String,Integer> pair = iter.next();
			
			if(pair.getKey().equals("Ivana")) {
				map.remove("Ivana");
			}
		}
	}
	
	@Test
	public void iteratorNormalTest() {
		map.put("Ivana", 2);
		map.put("Ante", 2);
		map.put("Jasna", 2);
		map.put("Kristina", 5);
		map.put("Ivana", 5);
		
		Assert.assertEquals(4, map.size());
		
		Iterator<SimpleHashtable.TableEntry<String,Integer>> iter = map.iterator();
		while(iter.hasNext()) {
			iter.next();
			iter.remove();
		}

		Assert.assertEquals(0, map.size());
	}
}
