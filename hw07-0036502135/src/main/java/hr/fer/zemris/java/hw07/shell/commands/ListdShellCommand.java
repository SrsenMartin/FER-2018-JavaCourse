package hr.fer.zemris.java.hw07.shell.commands;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import hr.fer.zemris.java.hw07.shell.Environment;
import hr.fer.zemris.java.hw07.shell.ShellCommand;
import hr.fer.zemris.java.hw07.shell.ShellStatus;

/**
 * Class that implements ShellCommand interface.
 * Represents implementation for listd shell command.
 * Command takes no arguments.
 * Writes all directories from stack onto shell from
 * last added to first added.
 * 
 * @author Martin Sršen
 *
 */
public class ListdShellCommand implements ShellCommand {

	/**
	 * Constant that represents command name.
	 */
	private static final String COMMAND_NAME = "listd";
	
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
			env.writeln(getCommandName() + " command can't have arguments.");
			return ShellStatus.CONTINUE;
		}
		
		listDirectories(env);
		
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
		
		description.add("Command listd writes all directories currently on stack onto shell from last added to first.");
		description.add("listd command takes no arguments.");
		
		return description;
	}

	/**
	 * Method that executes actual listd command.
	 * Writes all directories from stack onto shell
	 * from last added to first.
	 * 
	 * @param env	Used to talk to shell.
	 */
	private void listDirectories(Environment env) {
		@SuppressWarnings("unchecked")
		Stack<Path> stack = (Stack<Path>) env.getSharedData("cdstack");
		if(stack == null || stack.isEmpty()) {
			env.writeln("There are no stored directories.");
			return;
		}
		
		for(int index = stack.size() - 1; index >= 0; --index) {
			env.writeln(stack.get(index).toString());
		}
	}
}
