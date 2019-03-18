package hr.fer.zemris.java.gui.layouts;

import org.junit.Test;

public class RCPositionTest {

	@Test (expected = CalcLayoutException.class)
	public void Row0Test() {
		new RCPosition(0, 2);
	}
	
	@Test (expected = CalcLayoutException.class)
	public void Row6Test() {
		new RCPosition(6, 2);
	}
	
	@Test (expected = CalcLayoutException.class)
	public void Column0Test() {
		new RCPosition(2, 0);
	}
	
	@Test (expected = CalcLayoutException.class)
	public void Column8Test() {
		new RCPosition(2, 8);
	}
	
	@Test (expected = CalcLayoutException.class)
	public void invalidFirstRowElement() {
		new RCPosition(1, 2);
	}
	
	@Test (expected = CalcLayoutException.class)
	public void invalidFirstRowElement2() {
		new RCPosition(1, 5);
	}
}
