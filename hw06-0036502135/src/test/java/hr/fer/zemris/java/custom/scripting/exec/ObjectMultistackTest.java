package hr.fer.zemris.java.custom.scripting.exec;

import org.junit.Test;
import java.util.EmptyStackException;
import org.junit.Assert;

public class ObjectMultistackTest {

	@Test (expected = NullPointerException.class)
	public void nameNull() {
		new ObjectMultistack().push(null, new ValueWrapper(null));
	}
	
	@Test (expected = NullPointerException.class)
	public void valueWrapperNull() {
		new ObjectMultistack().push("name", null);
	}
	
	@Test
	public void isEmptyTest() {
		ObjectMultistack multiStack = new ObjectMultistack();
		multiStack.push("lala", new ValueWrapper("1"));
		
		Assert.assertEquals(true, multiStack.isEmpty("empty"));
		Assert.assertEquals(false, multiStack.isEmpty("lala"));
	}
	
	@Test
	public void pushAsFirstKey() {
		ObjectMultistack multiStack = new ObjectMultistack();
		ValueWrapper add = new ValueWrapper(null);
		multiStack.push("name", add);
		
		Assert.assertEquals(multiStack.pop("name"), add);
		Assert.assertEquals(true, multiStack.isEmpty("name"));
	}
	
	@Test
	public void pushMoreAtSameKey() {
		ObjectMultistack multiStack = new ObjectMultistack();
		ValueWrapper add1 = new ValueWrapper(null);
		ValueWrapper add2 = new ValueWrapper("1");
		ValueWrapper add3 = new ValueWrapper("321");
		multiStack.push("name", add1);
		multiStack.push("name", add2);
		multiStack.push("name", add3);
		
		Assert.assertEquals(multiStack.pop("name"), add3);
		Assert.assertEquals(false, multiStack.isEmpty("name"));
		Assert.assertEquals(multiStack.pop("name"), add2);
		Assert.assertEquals(false, multiStack.isEmpty("name"));
		Assert.assertEquals(multiStack.pop("name"), add1);
		Assert.assertEquals(true, multiStack.isEmpty("name"));
	}
	
	@Test
	public void pushDifferentKey() {
		ObjectMultistack multiStack = new ObjectMultistack();
		ValueWrapper add1 = new ValueWrapper(null);
		ValueWrapper add2 = new ValueWrapper("1");
		ValueWrapper add3 = new ValueWrapper("321");
		multiStack.push("a", add1);
		multiStack.push("b", add2);
		multiStack.push("c", add3);
		
		Assert.assertEquals(multiStack.pop("a"), add1);
		Assert.assertEquals(true, multiStack.isEmpty("a"));
		Assert.assertEquals(multiStack.pop("c"), add3);
		Assert.assertEquals(true, multiStack.isEmpty("c"));
		Assert.assertEquals(multiStack.pop("b"), add2);
		Assert.assertEquals(true, multiStack.isEmpty("b"));
	}
	
	@Test  (expected = NullPointerException.class)
	public void popNull() {
		ObjectMultistack multiStack = new ObjectMultistack();
		multiStack.pop(null);
	}
	
	@Test (expected = EmptyStackException.class)
	public void popNotExisting() {
		ObjectMultistack multiStack = new ObjectMultistack();
		multiStack.pop("a");
	}
	
	@Test
	public void popNormal() {
		ObjectMultistack multiStack = new ObjectMultistack();
		ValueWrapper add1 = new ValueWrapper(null);
		ValueWrapper add2 = new ValueWrapper("1");
		ValueWrapper add3 = new ValueWrapper("321");
		multiStack.push("a", add1);
		multiStack.push("b", add2);
		multiStack.push("b", add3);
		
		Assert.assertEquals(multiStack.pop("a"), add1);
		Assert.assertEquals(true, multiStack.isEmpty("a"));
		Assert.assertEquals(multiStack.pop("b"), add3);
		Assert.assertEquals(false, multiStack.isEmpty("b"));
		Assert.assertEquals(multiStack.pop("b"), add2);
		Assert.assertEquals(true, multiStack.isEmpty("b"));
	}
	
	@Test (expected = NullPointerException.class)
	public void peekNull() {
		new ObjectMultistack().peek(null);
	}
	
	@Test (expected = EmptyStackException.class)
	public void peekNotExisting() {
		new ObjectMultistack().peek("x");
	}
	
	@Test
	public void peekNormal() {
		ObjectMultistack multiStack = new ObjectMultistack();
		ValueWrapper add1 = new ValueWrapper(null);
		ValueWrapper add2 = new ValueWrapper("1");
		ValueWrapper add3 = new ValueWrapper("321");
		multiStack.push("a", add1);
		multiStack.push("b", add2);
		multiStack.push("b", add3);
		
		Assert.assertEquals(multiStack.peek("a"), add1);
		Assert.assertEquals(false, multiStack.isEmpty("a"));
		Assert.assertEquals(multiStack.peek("b"), add3);
		Assert.assertEquals(false, multiStack.isEmpty("b"));
		Assert.assertEquals(multiStack.pop("b"), add3);
		Assert.assertEquals(false, multiStack.isEmpty("b"));
		Assert.assertEquals(multiStack.peek("b"), add2);
		Assert.assertEquals(false, multiStack.isEmpty("b"));
	}
}
