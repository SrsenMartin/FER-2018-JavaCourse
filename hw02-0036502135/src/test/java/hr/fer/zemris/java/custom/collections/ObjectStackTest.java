package hr.fer.zemris.java.custom.collections;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ObjectStackTest {

	private ObjectStack stack;

	@Before
	public void init() {
		stack = new ObjectStack();
	}

	@Test
	public void isEmpty() {
		boolean expected = true;
		boolean actual = stack.isEmpty();

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void isNotEmpty() {
		boolean expected = false;
		stack.push(33);
		boolean actual = stack.isEmpty();

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void size() {
		int expected = 0;
		int actual = stack.size();
		Assert.assertEquals(expected, actual);

		stack.push(412);

		int expected2 = 1;
		int actual2 = stack.size();
		Assert.assertEquals(expected2, actual2);
	}

	@Test
	public void pushNormal() {
		stack.push(11);

		Object expected = 11;
		Object actual = stack.peek();

		Assert.assertEquals(expected, actual);
	}

	@Test(expected = IllegalArgumentException.class)
	public void pushNull() {
		stack.push(null);
	}

	@Test
	public void popNormal() {
		stack.push(100);
		Object expected = "oth";
		stack.push(expected);
		Object actual = stack.pop();

		Assert.assertEquals(expected, actual);

		Object expected2 = 100;
		Object actual2 = stack.peek();

		Assert.assertEquals(expected2, actual2);
	}

	@Test(expected = EmptyStackException.class)
	public void popEmpty() {
		stack.pop();
	}

	@Test
	public void peekNormal() {
		Object expected = 20;
		stack.push(20);
		Object actual = stack.peek();

		Assert.assertEquals(expected, actual);

		Object actual2 = stack.peek();

		Assert.assertEquals(expected, actual2);
	}

	@Test(expected = EmptyStackException.class)
	public void peekEmpty() {
		stack.peek();
	}

	@Test(expected = EmptyStackException.class)
	public void clear() {
		stack.push(1);
		stack.push("Str");

		stack.clear();

		stack.peek();
	}

}
