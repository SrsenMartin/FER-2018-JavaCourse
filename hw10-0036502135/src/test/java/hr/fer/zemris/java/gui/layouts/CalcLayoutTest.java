package hr.fer.zemris.java.gui.layouts;

import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.junit.Test;

import org.junit.Assert;

public class CalcLayoutTest {

	@Test (expected = CalcLayoutException.class)
	public void gapNegative() {
		new CalcLayout(-1);
	}
	
	@Test (expected = CalcLayoutException.class)
	public void add2ElementsAt11() {
		JPanel panel = new JPanel(new CalcLayout());
		panel.add(new JButton(), new RCPosition(1, 1));
		panel.add(new JButton(), new RCPosition(1, 1));
	}
	
	@Test (expected = CalcLayoutException.class)
	public void addDuplicateAtRandom() {
		JPanel panel = new JPanel(new CalcLayout());
		panel.add(new JButton(), new RCPosition(3, 5));
		panel.add(new JButton(), new RCPosition(3, 5));
	}
	
	@Test
	public void testScenario1() {
		JPanel p = new JPanel(new CalcLayout(2));
		JLabel l1 = new JLabel(""); l1.setPreferredSize(new Dimension(10, 30));
		JLabel l2 = new JLabel(""); l2.setPreferredSize(new Dimension(20, 15));
		p.add(l1, new RCPosition(1,1));
		p.add(l2, new RCPosition(3,3));
		Dimension dim = p.getPreferredSize();
		
		Assert.assertEquals(152, dim.width);
		Assert.assertEquals(158, dim.height);
	}
	
	 @Test
	 public void testScenario2() {
			JPanel p = new JPanel(new CalcLayout(2));
			JLabel l1 = new JLabel(""); l1.setPreferredSize(new Dimension(108,15));
			JLabel l2 = new JLabel(""); l2.setPreferredSize(new Dimension(16,30));
			p.add(l1, new RCPosition(1,1));
			p.add(l2, new RCPosition(3,3));
			Dimension dim = p.getPreferredSize();
			
			Assert.assertEquals(152, dim.width);
			Assert.assertEquals(158, dim.height);
	 }
}
