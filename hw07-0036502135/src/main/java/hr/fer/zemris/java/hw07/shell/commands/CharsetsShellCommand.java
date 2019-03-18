package hr.fer.zemris.java.hw07.shell.commands;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw07.shell.Environment;
import hr.fer.zemris.java.hw07.shell.ShellCommand;
import hr.fer.zemris.java.hw07.shell.ShellStatus;

/**
 * Class that implements ShellCommand interface.
 * Represents implementation for charsets shell command.
 * Command that takes no arguments.
 * Writes all java supported charsets onto output.
 * 
 * @author Martin Sršen
 *
 */
public class CharsetsShellCommand implements ShellCommand {

	/**
	 * Constant that represents command name.
	 */
	private static final String COMMAND_NAME = "charsets";
	
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
		if(arguments != null) {
			env.writeln("Command can't have arguments.");
			return ShellStatus.CONTINUE;
		}
		
		for(String charsetName: Charset.availableCharsets().keySet()) {
			env.writeln(charsetName);
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
		
		description.add("Command charsets takes no arguments and lists names of supported charsets for your Java platform.");
		description.add("A single charset name is written per line.");
		
		return description;
	}

}
