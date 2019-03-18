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
 * Represents implementation for popd shell command.
 * Command takes no arguments.
 * Command removes last added directory path from
 * top of the stack in sharedData map and sets 
 * it as current directory.
 * 
 * @author Martin Sršen
 *
 */
public class PopdShellCommand implements ShellCommand {

	/**
	 * Constant that represents command name.
	 */
	private static final String COMMAND_NAME = "popd";
	
	/**
	 * Method that checks arguments, and if they are valid
	 * executes command, else returns to shell to take next input.
	 * 
	 * @param env	Used to talk to shell.
	 * @param arguments	Arguments used to execute command.
	 * @return	ShellStatus that determines shell status.
	 * @throws IllegalArgumentException if new current directory doesn't exist.
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		if(arguments != null) {
			env.writeln(getCommandName() + " command can't have arguments.");
			return ShellStatus.CONTINUE;
		}
		
		popAndChange(env);
		
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
		
		description.add("Command pops directory from top of the stack and sets it as current directory.");
		description.add("popd command takes no arguments.");
		
		return description;
	}

	/**
	 * Method that does removing from top of the stack and setting it as current directory.
	 * If stack is empty or not initialised than writes message and leaves method,
	 * else removes directory path from top of the stack and sets it as current directory.
	 * 
	 * @param env	Used to talk to shell.
	 */
	private void popAndChange(Environment env) {
		@SuppressWarnings("unchecked")
		Stack<Path> stack = (Stack<Path>) env.getSharedData("cdstack");
		if(stack == null || stack.isEmpty()) {
			env.writeln("There are no stored directories.");
			return;
		}
		
		Path stackDirectory = stack.pop();
		env.setCurrentDirectory(stackDirectory);
	}
}
