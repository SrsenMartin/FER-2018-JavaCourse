package hr.fer.zemris.java.hw07.shell.commands.namebuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that represents parser for given name expression.
 * Parser splits name on constant part and groups part.
 * Parser main purpose is to create list of NameBuilder objects
 * that will build name for new file.
 * 
 * @author Martin Sršen
 *
 */
public class NameBuilderParser {

	/**
	 * Given expression as char array.
	 */
	private char[] expressionSplitted;
	/**
	 * Current index in char array.
	 */
	private int index;
	
	/**
	 * Constructor tha takes expression as aguments
	 * and creates char array from it.
	 * 
	 * @param expression	Expression used to build new file names.
	 */
	public NameBuilderParser(String expression) {
		expressionSplitted = expression.toCharArray();
	}
	
	/**
	 * Method used to create a list of NameBuilders used to create
	 * new file name.
	 * After list is created, new NameBuilderMain object will be created and returned.
	 * 
	 * @return	NameBuilderObject that contains list of all created NameBuilders.
	 * @throws IllegalArgumentException if invalid expression is given.
	 */
	public NameBuilder getNameBuilder() {
		List<NameBuilder> builders = new ArrayList<>();
		
		while(index < expressionSplitted.length) {
			builders.add(getNextNameBuilder());
		}
		
		return new NameBuilderMain(builders);
	}
	
	/**
	 * Helper method that returns next NameBuilder
	 * generated from expression.
	 * 
	 * @return	NameBuilder generated from expression.
	 * @throws	IllegalArgumentException if invalid expression is given.
	 */
	private NameBuilder getNextNameBuilder() {
		if(expressionSplitted[index] == '$') {
			return getBuilderGroup();
		}
			
		return getConstantGroup();
	}
	
	/**
	 * Helper method used to create new NameBuilderGroup object and return it.
	 * 
	 * @return	new NameBuildeGroup object.
	 * @throws IllegalArgumentException if invalid expression is given.
	 */
	private NameBuilder getBuilderGroup() {
		index++;
		checkGroupOpener();
		
		removeSpaces();
		int groupNumber = getGroupNumber();
		
		removeSpaces();
		String width = getMinWidth();
		
		removeSpaces();
		checkGroupCloser();
		
		return new NameBuilderGroup(groupNumber, width);
	}
	
	/**
	 * Helper method used to create new NameBuilderUnchanged object and return it.
	 * 
	 * @return	new NameBuildeUnchanged object.
	 */
	private NameBuilder getConstantGroup() {
		StringBuilder sb = new StringBuilder();
		
		while(index < expressionSplitted.length && expressionSplitted[index] != '$') {
			sb.append(expressionSplitted[index++]);
		}
		
		return new NameBuilderUnchanged(sb.toString());
	}
	
	/**
	 * Helper method used to get groupNumber from expression.
	 * 
	 * @return	group number.
	 * @throws IllegalArgumentException if invalid expression is given.
	 */
	private int getGroupNumber() {
		String groupNum = getNextNumberString();
		
		return Integer.parseInt(groupNum);
	}
	
	/**
	 * Helper method used to get minimum width from expression.
	 * 
	 * @return	minimum width or null if not given.
	 * @throws IllegalArgumentException if invalid expression is given.
	 */
	private String getMinWidth() {
		if(index < expressionSplitted.length && expressionSplitted[index] == ',') {
			index++;
		}else {
			return null;
		}
		
		removeSpaces();
		
		return getNextNumberString();
	}
	
	/**
	 * Helper method that returns next number in expression as string.
	 * 
	 * @return	next number in expression as string.
	 * @throws IllegalArgumentException if invalid expression is given.
	 */
	private String getNextNumberString() {
		StringBuilder sb = new StringBuilder();
		
		while(index < expressionSplitted.length && Character.isDigit(expressionSplitted[index])) {
			sb.append(expressionSplitted[index++]);
		}
		
		if(sb.length() == 0) {
			throw new IllegalArgumentException("Expected number for group or width is missing.");
		}
		
		return sb.toString();
	}
	
	/**
	 * Helper method used to check if valid group opener is next.
	 * 
	 * @throws IllegalArgumentException if invalid expression is given.
	 */
	private void checkGroupOpener() {
		if(index >= expressionSplitted.length || expressionSplitted[index] != '{') {
			throw new IllegalArgumentException("After $ character is { expected.");
		}
		
		index++;
	}
	
	/**
	 * Helper method used to check if valid group closer is next.
	 * 
	 * @throws IllegalArgumentException if invalid expression is given.
	 */
	private void checkGroupCloser() {
		if(index >= expressionSplitted.length || expressionSplitted[index] != '}') {
			throw new IllegalArgumentException("Name builder group representation not closed.Exprected }");
		}
		
		index++;
	}
	
	/**
	 * Helper method used to remove spaces in char array.
	 */
	private void removeSpaces() {
		while (index < expressionSplitted.length) {
			char current = expressionSplitted[index];

			if (current == ' ' || current == '\t') {
				index++;
				continue;
			}

			break;
		}
	}
}
