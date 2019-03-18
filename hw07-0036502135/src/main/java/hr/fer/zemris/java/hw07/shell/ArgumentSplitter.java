package hr.fer.zemris.java.hw07.shell;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that contains methods that parse given arguments string
 * into list of arguments.
 * 
 * @author Martin Sršen
 *
 */
public class ArgumentSplitter {

	/**
	 * Nested class that contains current state for lexer
	 * that is used to parse arguments.
	 *
	 */
	private static class LexerState{
		/**
		 * String of arguments as char array.
		 */
		char[] elements;
		/**
		 * Current index in char array.
		 */
		int index;
		/**
		 * Current lexing mode.
		 */
		int mode;
		/**
		 * Determines whether escaping should be used or not.
		 */
		static boolean useEscape;
		
		/**
		 * Constructor that takes string of arguments
		 * and initializes char array.
		 * 
		 * @param arguments	String of arguments.
		 */
		public LexerState(String arguments) {
			elements = arguments.toCharArray();
		}
	}
	
	/**
	 * Method used to parse and check string of arguments for massrename command.
	 * 
	 * @param arguments	String of arguments.
	 * @return	list of arguments.
	 * @throws IllegalArgumentException	if invalid string is given.
	 */
	public static List<String> getMassrenameArguments(String arguments){
		LexerState.useEscape = false;
		
		List<String> args = getArguments(arguments);
		
		if(args == null || args.size() < 4 || args.size() > 5) {
			throw new IllegalArgumentException("Invalid number of arguments given.");
		}
		
		String cmd = args.get(2).toLowerCase();
		if((cmd.equals("filter") && args.size() != 4) ||
				(cmd.equals("groups") && args.size() != 4) ||
				(cmd.equals("show") && args.size() != 5) ||
				(cmd.equals("execute") && args.size() != 5)) {
			throw new IllegalArgumentException("Invalid number of arguments for subcommand given.");
		}
		
		return args;
	}
	
	/**
	 * Method used to parse and check string of arguments where 1 or 2 arguments
	 * are expected. Throws exception if invalid string is given.
	 * 
	 * @param arguments	String of arguments.
	 * @return	List of parsed arguments.
	 * @throws IllegalArgumentException if invalid string of arguments is given.
	 */
	public static List<String> get1or2Argument(String arguments){
		LexerState.useEscape = true;
		
		List<String> args = getArguments(arguments);
		
		if(args == null || args.size() > 2) {
			throw new IllegalArgumentException("Invalid number of arguments given.");
		}
		
		if(args.size() != 2) {
			args.add(null);
		}
		
		return args;
	}
	
	/**
	 * Method used to parse and check string of arguments where 1 argument
	 * is expected. Throws exception if invalid string is given.
	 * 
	 * @param arguments	String of arguments.
	 * @return	Expected parsed argument.
	 * @throws IllegalArgumentException if invalid string of arguments is given.
	 */
	public static String get1Argument(String arguments) {
		List<String> args = get1or2Argument(arguments);
		
		if(args.get(1) != null) {
			throw new IllegalArgumentException("Invalid number of arguments given.");
		}
		
		return args.get(0);
	}
	
	/**
	 * Method used to parse and check string of arguments where 2 arguments
	 * are expected. Throws exception if invalid string is given.
	 * 
	 * @param arguments	String of arguments.
	 * @return	List of parsed arguments.
	 * @throws IllegalArgumentException if invalid string of arguments is given.
	 */
	public static List<String> get2Arguments(String arguments) {
		List<String> args = get1or2Argument(arguments);
		
		if(args.get(1) == null) {
			throw new IllegalArgumentException("Invalid number of arguments given.");
		}
		
		return args;
	}
	
	/**
	 * Method that does lexing on string of arguments.
	 * Returns null if given arguments string is null 
	 * else returns list of parsed arguments.
	 * 
	 * @param arguments	String of arguments.
	 * @return	List of parsed arguments or null if given arguments string is null.
	 * @throws IllegalArgumentException if invalid string of arguments is given.
	 */
	private static List<String> getArguments(String arguments) {
		if(arguments == null) {
			return null;
		}
		
		LexerState state = new LexerState(arguments);
		
		List<String> args = new ArrayList<>();
		
		while(state.index < state.elements.length) {
			if(state.elements[state.index] == '"') {
				state.mode = 1;
			}
			
			try {
				args.add(getNextArgument(state));
			}catch(Exception e) {
				throw new IllegalArgumentException(e.getMessage());
			}
			
			removeSpaces(state);
		}
		
		return args;
	}
	
	/**
	 * Helper method that returns next argument.
	 * 
	 * @param state	Instance of LexerState that contains state of lexer.
	 * @return	next argument.
	 * @throws IllegalArgumentException if invalid argument is read.
	 */
	private static String getNextArgument(LexerState state) {
		StringBuilder sb = new StringBuilder();
		if(state.mode == 0) {		
			while (state.index < state.elements.length && state.elements[state.index] != '"' && state.elements[state.index] != ' ') {
				sb.append(state.elements[state.index++]);
			}
			
			if(state.index < state.elements.length && state.elements[state.index] == '"')	state.mode = 1;
			else	state.index++;
		}else {
			state.index++;
			while (state.index < state.elements.length && state.elements[state.index] != '"') {
				if(LexerState.useEscape) {
					checkEscape(state);
				}
				
				sb.append(state.elements[state.index++]);
			}
			
			if(state.index >= state.elements.length) {
				throw new IllegalArgumentException("Path name not closed with \"");
			}
			
			state.index++;
			state.mode = 0;
			
			if(state.index < state.elements.length && state.elements[state.index] != ' ') {
				throw new IllegalArgumentException("There can only be space after \" closer.");
			}
		}
		
		return sb.toString();
	}
	
	/**
	 * Method that checks whether next character is escape character.
	 * 
	 * @param state	Instance of LexerState that contains state of lexer.
	 * @return	true if next is escape, false otherwise.
	 */
	private static boolean checkEscape(LexerState state) {
		if (state.elements[state.index] != '\\') {
			return false;
		}
		
		if (state.index + 1 >= state.elements.length) {
			return false;
		}

		char next = state.elements[state.index + 1];
		
		if (next == '\\' || next == '"') {
			state.index++;
			return true;
		}
		
		return false;
	}
	
	/**
	 * Helper method used to remove spaces in char array.
	 * 
	 * @param state	Instance of LexerState that contains state of lexer.
	 */
	private static void removeSpaces(LexerState state) {
		while (state.index < state.elements.length) {
			char current = state.elements[state.index];

			if (current == ' ' || current == '\t') {
				state.index++;
				continue;
			}

			break;
		}
	}
}
