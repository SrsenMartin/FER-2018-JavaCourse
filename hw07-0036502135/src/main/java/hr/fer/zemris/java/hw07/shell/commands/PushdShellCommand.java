package hr.fer.zemris.java.hw07.shell.commands;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import hr.fer.zemris.java.hw07.shell.Environment;
import hr.fer.zemris.java.hw07.shell.ShellCommand;
import hr.fer.zemris.java.hw07.shell.ShellStatus;
import hr.fer.zemris.java.hw07.shell.ArgumentSplitter;

/**
 * Class that implements ShellCommand interface.
 * Represents implementation for pushd shell command.
 * Command takes single argument which represents path of
 * a new directory that will be set as current directory.
 * Old current directory is pushed on top of the stack
 * in sharedData map.
 * Path can be relative which will be added to current directory path.
 * 
 * @author Martin Sršen
 *
 */
public class PushdShellCommand implements ShellCommand {

	/**
	 * Constant that represents command name.
	 */
	private static final String COMMAND_NAME = "pushd";
	
	/**
	 * Method that checks arguments, and if they are valid
	 * executes command, else returns to shell to take next input.
	 * 
	 * @param env	Used to talk to shell.
	 * @param arguments	Arguments used to execute command.
	 * @return	ShellStatus that determines shell status.
	 * @throws IllegalArgumentException if invalid arguments were given.
	 * @throws InvalidPathException if path contains invalid characters.
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		String argument = ArgumentSplitter.get1Argument(arguments);
		
		Path newCurrent = env.getCurrentDirectory().resolve(argument);
		pushAndChange(env, newCurrent);
		
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
		
		description.add("Command pushes current directory onto stack and puts given path as new current directory.");
		description.add("pushd command takes single argument, path.");
		description.add("That path represents new current directory.");	
		description.add("New path can be relative path which will be add to current directory path.");
		
		return description;
	}

	/**
	 * Method that does adding old current directory on top of the stack and setting new given path as current directory.
	 * If stack is not initialized,it created new one and puts it into map.
	 * 
	 * @param env	Used to talk to shell.
	 * @param newCurrent	Path to directory that will be set to current directory.
	 */
	private void pushAndChange(Environment env, Path newCurrent) {
		Path current = env.getCurrentDirectory();

		env.setCurrentDirectory(newCurrent);

		@SuppressWarnings("unchecked")
		Stack<Path> stack = (Stack<Path>) env.getSharedData("cdstack");
		if (stack == null) {
			stack = new Stack<>();
			env.setSharedData("cdstack", stack);
		}

		stack.push(current);
	}
	
}
