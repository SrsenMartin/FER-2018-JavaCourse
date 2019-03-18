package hr.fer.zemris.java.gui.prim;

import org.junit.Test;
import org.junit.Assert;

public class PrimListModelTest {

	@Test
	public void initializedContentTest() {
		PrimListModel<Integer> model = new PrimListModel<>();
		
		Assert.assertEquals(1, model.getSize());
		Assert.assertEquals(Integer.valueOf(1), model.getElementAt(0));
	}
	
	@Test
	public void nextCallTest() {
		PrimListModel<Integer> model = new PrimListModel<>();
		
		model.next();
		
		Assert.assertEquals(2, model.getSize());
		Assert.assertEquals(Integer.valueOf(2), model.getElementAt(1));
	}
	
	@Test
	public void nextMultipleTest() {
		PrimListModel<Integer> model = new PrimListModel<>();
		
		model.next();
		model.next();
		model.next();
		model.next();
		model.next();
		
		Assert.assertEquals(6, model.getSize());
		Assert.assertEquals(Integer.valueOf(1), model.getElementAt(0));
		Assert.assertEquals(Integer.valueOf(2), model.getElementAt(1));
		Assert.assertEquals(Integer.valueOf(3), model.getElementAt(2));
		Assert.assertEquals(Integer.valueOf(5), model.getElementAt(3));
		Assert.assertEquals(Integer.valueOf(7), model.getElementAt(4));
		Assert.assertEquals(Integer.valueOf(11), model.getElementAt(5));
	}
	
}
