package hr.fer.zemris.java.hw07.shell.commands;

import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw07.shell.Environment;
import hr.fer.zemris.java.hw07.shell.ShellCommand;
import hr.fer.zemris.java.hw07.shell.ShellStatus;
import hr.fer.zemris.java.hw07.shell.ArgumentSplitter;

/**
 * Class that implements ShellCommand interface.
 * Represents implementation for symbol shell command.
 * Command that takes string of arguments as input,
 * can accept 1 or 2 arguments.
 * First argument is one of 3 symbols.
 * Second argument is character used to change symbol.
 * If only 1 argument is given, command writes current character
 * for given symbol.
 * If 2 arguments are given, command changes character for symbol to given one.
 * 
 * @author Martin Sršen
 *
 */
public class SymbolShellCommand implements ShellCommand {

	/**
	 * Constant that represents command name.
	 */
	private static final String COMMAND_NAME = "symbol";
	
	/**
	 * Method that checks arguments, and if they are valid
	 * executes command, else returns to shell to take next input.
	 * 
	 * @param env	Used to talk to shell.
	 * @param arguments	Arguments used to execute command.
	 * @return	ShellStatus that determines shell status.
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		List<String> args = ArgumentSplitter.get1or2Argument(arguments);
		
		String argument1 = args.get(0).toUpperCase();
		String argument2 = args.get(1);
		
		if(isValidSymbolArgument(argument1)) {
			execute(env, argument1, argument2);
		}else {
			env.writeln("Invalid argument for symbol command: " + argument1);
		}
		
		return ShellStatus.CONTINUE;
	}

	/**
	 * Returns command name.
	 * 
	 * @return	command name.
	 */
	@Override
	public String getCommandName() {
		return COMMAND_NAME;
	}

	/**
	 * Returns command description.
	 * 
	 * @return	Command description.
	 */
	@Override
	public List<String> getCommandDescription() {
		List<String> description = new ArrayList<>();
		
		description.add("Command symbol takes one or two arguments. The first argument is prompt, morelines or multiline.");
		description.add("Second argument is new character that you want to put for given symbol.");
		description.add("If second argument is not given, current symbol is printed.");
		
		return description;
	}

	/**
	 * Method that does actual job of changing symbol character
	 *  or writing current one to output.
	 * 
	 * @param env	Used to talk to shell.
	 * @param argument1	One of 3 possible symbols.
	 * @param argument2	Character to set symbol character to.
	 */
	private void execute(Environment env, String argument1, String argument2) {
		char originalCharacter = '>';
		char newCharacter = '>';
		
		if(argument2 != null) {
			if(argument2.length() != 1) {
				env.writeln("Given new symbol character must be character.");
				return;
			}else {
				newCharacter = argument2.toCharArray()[0];
			}
		}
		
		switch(argument1) {
			case "PROMPT":
				originalCharacter = env.getPromptSymbol();
				
				if(argument2 != null) {
					env.setPromptSymbol(newCharacter);
				}
				break;
			case "MORELINES":
				originalCharacter = env.getMorelinesSymbol();
				
				if(argument2 != null) {
					env.setMorelinesSymbol(newCharacter);
				}
				break;
			case "MULTILINE":
				originalCharacter = env.getMultilineSymbol();
				
				if(argument2 != null) {
					env.setMultilineSymbol(newCharacter);
				}
				break;
		}
		
		if(argument2 == null) {
			env.writeln("Symbol for " + argument1 + " is '" + originalCharacter + "'");
		}else {
			env.writeln("Symbol for " + argument1 + " changed from '" + originalCharacter  + "' to '" + newCharacter + "'");
		}
	}
	
	/**
	 * Method that checks whether given argument is valid symbol.
	 * 
	 * @param argument	Symbol to check validation.
	 * @return	true if given argument is valid symbol,false otherwise.
	 */
	private boolean isValidSymbolArgument(String argument) {
		return argument.equals("PROMPT") ||
				argument.equals("MORELINES") ||
				argument.equals("MULTILINE");
	}
}
