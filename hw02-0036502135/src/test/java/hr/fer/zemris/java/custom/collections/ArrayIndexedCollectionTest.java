package hr.fer.zemris.java.custom.collections;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;;

public class ArrayIndexedCollectionTest {

	private ArrayIndexedCollection coll;

	@Before
	public void init() {
		coll = new ArrayIndexedCollection();
		coll.add(-20);
		coll.add("OPJJ");
		coll.add("second string");
		coll.add(3.14);
		coll.add("third string");
		coll.add(100);
	}

	@Test(expected = IllegalArgumentException.class)
	public void initialCapacityConstructor() {
		new ArrayIndexedCollection(0);
	}

	@Test
	public void addAllConstructor() {
		ArrayIndexedCollection collection = new ArrayIndexedCollection(coll);

		Object expected = 3.14;
		Object actual = collection.get(3);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void addNormal() {
		ArrayIndexedCollection collection = new ArrayIndexedCollection();
		Object expected = -10;
		collection.add(expected);
		Object actual = collection.get(0);

		Assert.assertEquals(expected, actual);
	}

	@Test(expected = IllegalArgumentException.class)
	public void addNull() {
		ArrayIndexedCollection collection = new ArrayIndexedCollection();
		collection.add(null);
	}

	@Test
	public void addInitialSite1() {
		ArrayIndexedCollection collection = new ArrayIndexedCollection(1);
		Object expected = "Hee";
		collection.add(-40);
		collection.add(expected);
		Object actual = collection.get(1);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void isEmpty() {
		ArrayIndexedCollection collection = new ArrayIndexedCollection();
		boolean expected = true;
		boolean actual = collection.isEmpty();

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void isNotEmpty() {
		ArrayIndexedCollection collection = new ArrayIndexedCollection();
		collection.add(10);
		boolean expected = false;
		boolean actual = collection.isEmpty();

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void addAll() {
		ArrayIndexedCollection collection = new ArrayIndexedCollection();
		collection.addAll(coll);

		Object expected = "third string";
		Object actual = collection.get(4);
		Assert.assertEquals(expected, actual);

		Object expected2 = -20;
		Object actual2 = collection.get(0);
		Assert.assertEquals(expected2, actual2);

		Object expected3 = 100;
		Object actual3 = collection.get(5);
		Assert.assertEquals(expected3, actual3);
	}

	@Test(expected = IllegalArgumentException.class)
	public void addAllNull() {
		ArrayIndexedCollection collection = new ArrayIndexedCollection();
		collection.addAll(null);
	}

	@Test
	public void containsNormal() {
		boolean expected = true;

		boolean actual = coll.contains(3.14);
		Assert.assertEquals(expected, actual);

		boolean actual2 = coll.contains(-20);
		Assert.assertEquals(expected, actual2);

		boolean actual3 = coll.contains(100);
		Assert.assertEquals(expected, actual3);
	}

	@Test
	public void containsNull() {
		boolean expected = false;
		boolean actual = coll.contains(null);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void containsNot() {
		boolean expected = false;
		boolean actual = coll.contains("fds");

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void removeFirst() {
		int size = coll.size();
		Integer num = -20;

		boolean expected = true;
		boolean actual = coll.remove(num);
		Assert.assertEquals(expected, actual);

		Assert.assertEquals("OPJJ", coll.get(0));
		Assert.assertEquals(size - 1, coll.size());
	}

	@Test
	public void removeLast() {
		int size = coll.size();
		Integer num = 100;

		boolean expected = true;
		boolean actual = coll.remove(num);
		Assert.assertEquals(expected, actual);

		Assert.assertEquals("third string", coll.get(coll.size() - 1));
		Assert.assertEquals(size - 1, coll.size());
	}

	@Test
	public void removeAtMiddle() {
		int size = coll.size();

		boolean expected = true;
		boolean actual = coll.remove("second string");
		Assert.assertEquals(expected, actual);

		Assert.assertEquals("OPJJ", coll.get(1));
		Assert.assertEquals(3.14, coll.get(2));
		Assert.assertEquals(size - 1, coll.size());
	}

	@Test
	public void removeFalse() {
		boolean expected = false;
		boolean actual = coll.remove("fdsfds");

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void toArray() {
		Object[] arr = coll.toArray();
		Object expected = 100;
		Object actual = arr[5];

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void clear() {
		coll.clear();
		int expected = 0;
		int actual = coll.size();

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void getNormal() {
		Object expected = -20;
		Object actual = coll.get(0);

		Assert.assertEquals(expected, actual);
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void getOutOdBounds() {
		coll.get(coll.size());
	}

	@Test
	public void InsertAt0() {
		int size = coll.size();

		Object expected = "inserted";
		coll.insert(expected, 0);
		Object actual = coll.get(0);
		Assert.assertEquals(expected, actual);

		Object expected2 = -20;
		Object actual2 = coll.get(1);
		Assert.assertEquals(expected2, actual2);

		Object expectedLast = 100;
		Object actualLast = coll.get(size);
		Assert.assertEquals(expectedLast, actualLast);

		int expectedSize = size + 1;
		int actualSize = coll.size();
		Assert.assertEquals(expectedSize, actualSize);
	}

	@Test
	public void InsertAtLast() {
		int size = coll.size();

		Object expected = "inserted";
		coll.insert(expected, size);
		Object actual = coll.get(size);
		Assert.assertEquals(expected, actual);

		Object expectedLast = 100;
		Object actualLast = coll.get(size - 1);
		Assert.assertEquals(expectedLast, actualLast);

		int expectedSize = size + 1;
		int actualSize = coll.size();
		Assert.assertEquals(expectedSize, actualSize);
	}

	@Test
	public void InsertInMiddle() {
		int size = coll.size();

		Object expected = "inserted";
		coll.insert(expected, size - 1);
		Object actual = coll.get(size - 1);
		Assert.assertEquals(expected, actual);

		Object expected2 = 100;
		Object actual2 = coll.get(size);
		Assert.assertEquals(expected2, actual2);

		Object expectedLast = "third string";
		Object actualLast = coll.get(size - 2);
		Assert.assertEquals(expectedLast, actualLast);

		int expectedSize = size + 1;
		int actualSize = coll.size();
		Assert.assertEquals(expectedSize, actualSize);
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void insertOutOfBounds() {
		coll.insert("fdfd", coll.size() + 1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void insertNull() {
		coll.insert(null, 0);
	}

	@Test
	public void indexOfFirst() {
		int expected = 0;
		int actual = coll.indexOf(-20);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void indexOfLast() {
		int expected = coll.size() - 1;
		int actual = coll.indexOf(100);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void indexOfNotContained() {
		int expected = -1;
		int actual = coll.indexOf(321132);

		Assert.assertEquals(expected, actual);
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void removeOutOfBounds() {
		coll.remove(coll.size());
	}
}
